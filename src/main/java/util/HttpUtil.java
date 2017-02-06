package util;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import util.Http.RequestMap;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 项目：fs_liquidator_platform_openapi
 * 包名：com.fshows.liquidator.platform.openapi.common.utils
 * 功能：
 * 时间：2016-08-23
 * 作者：呱牛
 */
public class HttpUtil {
    private static int           SocketTimeout            = 10000;//10秒
    private static int           ConnectTimeout           = 10000;//10秒
    private static int           ConnectionRequestTimeout = 10000;//10秒
    private static Boolean       SetTimeOut               = true;
    private static RequestConfig requestConfig            =
            RequestConfig.custom().setSocketTimeout(SocketTimeout).setConnectTimeout(ConnectTimeout)
                    .setConnectionRequestTimeout(ConnectionRequestTimeout).build();

    private static CloseableHttpClient getHttpClient() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder =
                RegistryBuilder.<ConnectionSocketFactory>create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);
        //指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //信任任何链接
            TrustStrategy anyTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            };
            SSLContext sslContext =
                    SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslSF =
                    new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        //设置连接管理器
        PoolingHttpClientConnectionManager connManager =
                new PoolingHttpClientConnectionManager(registry);
        //		connManager.setDefaultConnectionConfig(connConfig);
        //		connManager.setDefaultSocketConfig(socketConfig);
        //构建客户端
        return HttpClientBuilder.create().setConnectionManager(connManager).build();
    }

    public static String get(RequestMap map) throws Exception {
        return execute(false, map);
    }

    public static String post(RequestMap map) throws Exception {
        return execute(true, map);
    }

    private static String execute(boolean post, RequestMap map) throws IOException {
        String              responseBody = null;
        CloseableHttpClient httpClient   = getHttpClient();
        try {
            //请求数据
            CloseableHttpResponse response = httpClient.execute(getMethod(post, map));
            int                   status   = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            httpClient.close();
        }
        return responseBody;
    }

    private static HttpRequestBase getMethod(boolean post, RequestMap map) {
        //添加queryStr
        HttpRequestBase               httpRequestBase;
        LinkedHashMap<String, String> queryStrs = map.getQueryStrs();
        if (post) {
            httpRequestBase = new HttpPost(map.getUrl() + getQueryStr(queryStrs));
        } else {
            httpRequestBase = new HttpGet(map.getUrl() + getQueryStr(queryStrs));
        }

        //添加header
        for (Map.Entry<String, String> entry : map.getHeaders().entrySet()) {
            httpRequestBase.addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
        }
        if (httpRequestBase instanceof HttpEntityEnclosingRequestBase) {
            //添加form
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            for (final Map.Entry<String, String> entry : map.getForms().entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            ((HttpEntityEnclosingRequestBase) httpRequestBase).setEntity(new UrlEncodedFormEntity(nameValuePairs, Consts
                    .UTF_8));
        }

        if (SetTimeOut) {
            httpRequestBase.setConfig(requestConfig);
        }
        return httpRequestBase;
    }

    /**
     * 获取url后的queryString
     * @param queries
     */
    private static String getQueryStr(Map<String, String> queries) {
        StringBuilder sb = new StringBuilder("");
        if (queries != null && queries.keySet().size() > 0) {
            boolean  firstFlag = true;
            Iterator iterator  = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + (String) entry.getKey() + "=" + (String) entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + (String) entry.getKey() + "=" + (String) entry.getValue());
                }
            }
        }
        return sb.toString();
    }
}
