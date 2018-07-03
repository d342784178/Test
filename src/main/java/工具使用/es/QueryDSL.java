package 工具使用.es;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.network.InetAddresses;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.join.query.JoinQueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.net.InetSocketAddress;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.exponentialDecayFunction;
import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.randomFunction;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-06-21
 * Time: 11:19
 */
public class QueryDSL {
    /**
     * 查询所有
     * @exception Exception
     */
    @Test
    public void matchAll() throws Exception {
        matchAllQuery();
    }

    @Test
    public void fullTextQuery() throws Exception {
        //精确匹配(相等)
        matchQuery("name", "value");
        //多值匹配
        multiMatchQuery("name", "value1", "value2");
        //模糊匹配(包含)
        commonTermsQuery("name",
                "kimchy");
        queryStringQuery("+kimchy -elasticsearch");
        //类似queryString 区别在于错误语法不抛异常
        simpleQueryStringQuery("+kimchy -elasticsearch");

    }

    @Test
    public void termLevelQuery() throws Exception {
        //包含查询
        termQuery("name", "value");
        //包含查询 or关系
        termsQuery("name", "value1", "value2");
        //范围查询
        rangeQuery("name").from(10).to(20);
        //存在查询
        existsQuery("name");
        //前缀查询
        prefixQuery("name", "prefix");
        //通配符查询
        wildcardQuery("user", "k?mch*");
        //正则查询
        regexpQuery("name.first", "s.*y");
        //近似查询 如:value=123 匹配 value=133
        fuzzyQuery("name", "like");
        //类型查询
        typeQuery("type");
        idsQuery("type1", "type2").addIds("id1", "id2");

    }

    /**
     * @link https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.3/java-compound-queries
     * .html#java-query-dsl-function-score-query
     */
    @Test
    public void compoundQuery() throws Exception {

        //TODO 不知道啥用
        constantScoreQuery(
                termQuery("name", "kimchy"))
                .boost(2.0f);
        //符合所有条件
        boolQuery()
                .must(termQuery("content", "test1"))
                .must(termQuery("content", "test4"))
                .mustNot(termQuery("content", "test2"))
                .should(termQuery("content", "test3"))
                .filter(termQuery("content", "test5"));
        //符合一个条件即可
        disMaxQuery()
                .add(termQuery("name", "kimchy"))
                .add(termQuery("name", "elasticsearch"))
                .boost(1.2f)
                .tieBreaker(0.7f);

        //TODO 不知道啥用
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] functions = {
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        matchQuery("name", "kimchy"),
                        randomFunction()),
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        exponentialDecayFunction("age", 0L, 1L))
        };
        functionScoreQuery(functions);

        //TODO 不知道啥用
        boostingQuery(
                termQuery("name", "kimchy"),
                termQuery("name", "dadoonet"))
                .negativeBoost(0.2f);

    }

    @Test
    public void joinQuery() throws Exception {
        //TODO nestJoinQuery
        nestedQuery(
                "obj1",
                boolQuery()
                        .must(matchQuery("obj1.name", "blue"))
                        .must(rangeQuery("obj1.count").gt(5)),
                ScoreMode.Avg);
        //TODO parentOrChildQuery
        Settings        settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client   = new PreBuiltTransportClient(settings);//使用PreBuiltTransportClient
        client.addTransportAddress(new TransportAddress(new InetSocketAddress(InetAddresses.forString("127.0.0.1"),
                9300)));
        JoinQueryBuilders.hasChildQuery(
                "blog_tag",
                termQuery("tag", "something"),
                ScoreMode.None);
        JoinQueryBuilders.hasParentQuery(
                "blog",
                termQuery("tag", "something"),
                false);

    }

    /**
     * @link https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.3/java-geo-queries.html
     */
    @Test
    public void geoQuery() throws Exception {

    }

    /**
     * @link https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.3/java-specialized-queries.html
     */
    @Test
    public void specialQuery() throws Exception {

    }

    /**
     * @link https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.3/java-span-queries.html
     */
    @Test
    public void spanQuery() throws Exception {

    }

}
