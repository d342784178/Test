package 工具使用.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import org.apache.lucene.index.Terms;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.cluster.node.tasks.get.GetTaskResponse;
import org.elasticsearch.action.admin.cluster.node.tasks.list.ListTasksResponse;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.*;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.tasks.TaskId;
import org.elasticsearch.tasks.TaskInfo;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.script.ScriptType.INLINE;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-06-21
 * Time: 09:46
 */

public class EsTest {

    private TransportClient client;

    @Before
    public void setup() throws Exception {
        // 设置集群名称
        //配置详情 https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.3/transport-client.html
        Settings settings = Settings.builder().put("cluster.name", "myapplication-elk")
                                    .put("client.transport.ignore_cluster_name", true).build();
        // 创建client   tcp端口 !不是http端口
        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
    }

    @Test
    public void index() throws Exception {
        //jsonbuilder方式
        IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
                                       .setSource(jsonBuilder()
                                               .startObject()
                                               .field("user", "kimchy")
                                               .field("postDate", new Date())
                                               .field("message", "trying out Elasticsearch")
                                               .endObject())
                                       .get();
        //json字符串方式
        String json = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        response = client.prepareIndex("twitter", "tweet")
                         .setSource(json, XContentType.JSON)
                         .get();
        //map方式
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", "kimchy");
        map.put("postDate", new Date());
        map.put("message", "trying out Elasticsearch");
        response = client.prepareIndex("twitter", "tweet")
                         .setSource(map)
                         .get();

        // byte[]数组方式
        ObjectMapper mapper    = new ObjectMapper(); // create once, reuse
        byte[]       jsonBytes = mapper.writeValueAsBytes(json);
        response = client.prepareIndex("twitter", "tweet")
                         .setSource(jsonBytes)
                         .get();
    }

    @Test
    public void get() throws Exception {
        GetResponse response = client.prepareGet("twitter", "tweet", "1").get();
    }

    @Test
    public void delete() throws Exception {
        DeleteResponse response = client.prepareDelete("twitter", "tweet", "1").get();
    }

    @Test
    public void deleteByQuery() throws Exception {
        //同步
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                                                                    .filter(QueryBuilders.matchQuery("gender", "male"))
                                                                    .source("persons")
                                                                    .get();
        long deleted = response.getDeleted();

        //异步callback
        DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                                    .filter(QueryBuilders.matchQuery("gender", "male"))
                                    .source("persons")
                                    .execute(new ActionListener<BulkByScrollResponse>() {
                                        @Override
                                        public void onResponse(BulkByScrollResponse response) {
                                            long deleted = response.getDeleted();
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            // Handle the exception
                                        }
                                    });

    }

    /**
     * 更新
     */
    @Test
    public void update() throws Exception {
        //全量更新 流式
        UpdateRequest updateRequest = new UpdateRequest("index", "type", "1")
                .doc(jsonBuilder()
                        .startObject()
                        .field("gender", "male")
                        .endObject());
        client.update(updateRequest).get();
        //全量更新 链式
        client.prepareUpdate("ttl", "doc", "1")
              .setDoc(jsonBuilder()
                      .startObject()
                      .field("gender", "male")
                      .endObject())
              .get();

        //局部更新
        client.prepareUpdate("ttl", "doc", "1")
              .setScript(new Script(INLINE, "ctx._source.gender = \"male\"", null, null))
              .get();


        //update or insert
        IndexRequest indexRequest = new IndexRequest("index", "type", "1")
                .source(jsonBuilder()
                        .startObject()
                        .field("name", "Joe Smith")
                        .field("gender", "male")
                        .endObject());
        updateRequest = new UpdateRequest("index", "type", "1")
                .doc(jsonBuilder()
                        .startObject()
                        .field("gender", "male")
                        .endObject())
                .upsert(indexRequest);
        client.update(updateRequest).get();
    }

    @Test
    public void updateByQuery() throws Exception {
        UpdateByQueryRequestBuilder updateAction = UpdateByQueryAction.INSTANCE.newRequestBuilder
                (client);
        updateAction.filter(QueryBuilders.matchQuery("gender", "male"))
                    .size(1000)
                    .script(new Script(ScriptType.INLINE, "ctx._source.awesome = 'absolutely'", "painless", Collections
                            .emptyMap()))
                    .abortOnVersionConflict(false)//防止因更新操作导致版本的冲突
                    .source()//对结果的操作
                    .addSort("cat", SortOrder.DESC);
        BulkByScrollResponse bulkByScrollResponse = updateAction.get();

        //查看所有update-by-query操作
        ListTasksResponse tasksList = client.admin().cluster().prepareListTasks()
                                            .setActions(UpdateByQueryAction.NAME).setDetailed(true).get();
        for (TaskInfo info : tasksList.getTasks()) {
            TaskId                  taskId = info.getTaskId();
            BulkByScrollTask.Status status = (BulkByScrollTask.Status) info.getStatus();
            // do stuff
        }

        //查看某个update-by-query操作
        GetTaskResponse get = client.admin().cluster().prepareGetTask("taskId").get();

        //取消所有update-by-query操作
        client.admin().cluster().prepareCancelTasks().setActions(UpdateByQueryAction.NAME).get().getTasks();
        //取消某个update-by-query操作
        client.admin().cluster().prepareCancelTasks().setTaskId(new TaskId("", 1L)).get().getTasks();

        //限流
        RethrottleAction.INSTANCE.newRequestBuilder(client)
                                 .setTaskId(new TaskId("", 1L))
                                 .setRequestsPerSecond(2.0f)
                                 .get();
    }

    /**
     * 批量获取
     */
    @Test
    public void multiGet() throws Exception {
        MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                                                       .add("twitter", "tweet", "1")
                                                       .add("twitter", "tweet", "2", "3", "4")
                                                       .add("another", "type", "foo")
                                                       .get();

        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                String json = response.getSourceAsString();
            }
        }
    }

    /**
     * 批量操作
     * 只允许一下几类操作
     * @see DocWriteRequest.OpType
     */
    @Test
    public void bulk() throws Exception {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        //加入index操作
        bulkRequest.add(client.prepareIndex("twitter", "tweet", "1")
                              .setSource(jsonBuilder()
                                      .startObject()
                                      .field("user", "kimchy")
                                      .field("postDate", new Date())
                                      .field("message", "trying out Elasticsearch")
                                      .endObject()
                              )
        );
        //加入delete操作
        bulkRequest.add(client.prepareDelete("twitter", "tweet", "2").request());

        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            for (BulkItemResponse bulkItemResponse : bulkResponse.getItems()) {
                DocWriteRequest.OpType opType = bulkItemResponse.getOpType();
            }
        }
    }

    /**
     * 批量操作监听
     */
    @Test
    public void bulkProcessor() throws Exception {
        //bulk监听器
        BulkProcessor bulkProcessor = BulkProcessor.builder(
                client,
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId,
                                           BulkRequest request) {
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          BulkResponse response) {
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          Throwable failure) {
                    }
                })
                                                   .setBulkActions(10000)//每10000个操作 执行一次
                                                   .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))//每5m 执行一次
                                                   .setFlushInterval(TimeValue.timeValueSeconds(5))//每5s 执行一次
                                                   .setConcurrentRequests(1)//并发数量
                                                   .setBackoffPolicy(//设置backoff策略
                                                           BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis
                                                                   (100), 3))
                                                   .build();

        // 添加操作
        bulkProcessor.add(client.prepareDelete("twitter", "tweet", "1").request());
        bulkProcessor.add(client.prepareDelete("twitter", "tweet", "2").request());
        // 强制刷入
        bulkProcessor.flush();
        // 关闭监听
        bulkProcessor.close();
        //等待关闭
        //bulkProcessor.awaitClose(10, TimeUnit.MINUTES);

        // Refresh your indices
        client.admin().indices().prepareRefresh().get();
        // Now you can start searching!
        client.prepareSearch().get();
    }


    /**
     * 重建索引
     */
    @Test
    public void reIndex() throws Exception {
        BulkByScrollResponse response = ReindexAction.INSTANCE.newRequestBuilder(client)
                                                              .destination("target_index")
                                                              .filter(QueryBuilders.matchQuery("category", "xzy"))
                                                              .get();
    }

    @Test
    public void search() throws Exception {
        SearchResponse response = client.prepareSearch("index1", "index2")
                                        .setTypes("type1", "type2")
                                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                                        .setQuery(QueryBuilders.termQuery("multi", "test"))                 // Query
                                        .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
                                        .setFrom(0).setSize(60).setExplain(true)
                                        .get();
    }

    @Test
    public void multiSearch() throws Exception {
        SearchRequestBuilder srb1 = client
                .prepareSearch().setQuery(QueryBuilders.queryStringQuery("elasticsearch")).setSize(1);
        SearchRequestBuilder srb2 = client
                .prepareSearch().setQuery(QueryBuilders.matchQuery("name", "kimchy")).setSize(1);
        MultiSearchResponse sr = client.prepareMultiSearch()
                                       .add(srb1)
                                       .add(srb2)
                                       .get();

        long nbHits = 0;
        for (MultiSearchResponse.Item item : sr.getResponses()) {
            SearchResponse response = item.getResponse();
            nbHits += response.getHits().getTotalHits();
        }
    }

    /**
     * 类似redis的scan
     */
    @Test
    public void scroll() throws Exception {
        QueryBuilder qb = QueryBuilders.termQuery("multi", "test");

        SearchResponse searchResponse = client.prepareSearch("index1")
                                              .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                                              .setScroll(new TimeValue(60000))//在指定时间内执行scroll操作
                                              .setQuery(qb)
                                              .setSize(100)//每次最大返回书
                                              .get();
        String scrollerId = searchResponse.getScrollId();
        Function<SearchResponse, List<String>> tranformFunc = new Function<SearchResponse, List<String>>() {
            @Override
            public List<String> apply(SearchResponse input) {
                List caller = Arrays.stream(input.getHits().getHits())
                                    .map(documentFields -> documentFields.getSourceAsMap().get("caller"))
                                    .collect(Collectors.toList());
                return caller.size() > 0 ? caller : null;
            }
        };
        List<String>                          result   = tranformFunc.apply(searchResponse);
        Scroller<List<String>>                scroller = new Scroller<>(scrollerId, result, 6000L, tranformFunc);
        Scroller.ScrollerHelper<List<String>> helper   = scroller.helper();
        do {
            scroller.getResult().forEach(System.out::println);
        }
        while ((scroller = helper.next(scroller, client)) != null);
    }


    /**
     * 聚合
     * @link https://blog.csdn.net/chengyuqiang/article/details/79351779
     */
    @Test
    public void aggregations() throws Exception {
        SearchResponse sr = client.prepareSearch()
                                  .setQuery(QueryBuilders.matchAllQuery())
                                  .addAggregation(
                                          AggregationBuilders.terms("agg1").field("field")
                                  )
                                  .addAggregation(
                                          AggregationBuilders.dateHistogram("agg2")
                                                             .field("birth")
                                                             .dateHistogramInterval(DateHistogramInterval.YEAR)
                                  )
                                  .get();

        Terms     agg1 = sr.getAggregations().get("agg1");
        Histogram agg2 = sr.getAggregations().get("agg2");
        //指标组合
        sr = client.prepareSearch()
                   .addAggregation(
                           AggregationBuilders.terms("by_country").field("country")
                                              .subAggregation(AggregationBuilders.dateHistogram("by_year")
                                                                                 .field("dateOfBirth")
                                                                                 .dateHistogramInterval
                                                                                         (DateHistogramInterval.YEAR)
                                                                                 .subAggregation(AggregationBuilders
                                                                                         .avg("avg_children")
                                                                                                                    .field("children"))
                                              )
                   )
                   .execute().actionGet();

        //准备工作
        AggregationBuilder agg = AggregationBuilders.stats("agg1").field("egg");
        sr = client.prepareSearch().addAggregation(agg).get();
        //获取指标
        Stats  stats = sr.getAggregations().get("agg");
        double min   = stats.getMin();
        double max   = stats.getMax();
        double avg   = stats.getAvg();
        double sum   = stats.getSum();
        long   count = stats.getCount();
    }

    /**
     * template查询
     */
    @Test
    public void searchTemplate() throws Exception {
        //保存脚本到es集群中
        client.admin().cluster()
              .preparePutStoredScript()
              .setId("template_gender")
              .setContent(new BytesArray(
                      "{\n" +
                              "    \"query\" : {\n" +
                              "        \"match\" : {\n" +
                              "            \"gender\" : \"{{param_gender}}\"\n" +
                              "        }\n" +
                              "    }\n" +
                              "}"), XContentType.valueOf("mustache"));

        //调用STORED脚本
        Map<String, Object> template_params = new HashMap<>();
        template_params.put("param_gender", "male");
        SearchResponse sr = new SearchTemplateRequestBuilder(client)
                .setScript("template_gender")//脚本名称
                .setScriptType(ScriptType.STORED)//SOTRED脚本
                .setScriptParams(template_params)//脚本参数
                .setRequest(new SearchRequest())//上下文
                .get()
                .getResponse();
        //调用INLINE脚本
        sr = new SearchTemplateRequestBuilder(client)
                .setScript("{\n" +
                        "        \"query\" : {\n" +
                        "            \"match\" : {\n" +
                        "                \"gender\" : \"{{param_gender}}\"\n" +
                        "            }\n" +
                        "        }\n" +
                        "}")
                .setScriptType(ScriptType.INLINE)//INLINE脚本
                .setScriptParams(template_params)
                .setRequest(new SearchRequest())
                .get()
                .getResponse();
    }


}
