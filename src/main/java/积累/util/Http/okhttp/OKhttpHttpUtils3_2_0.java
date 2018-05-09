package 积累.util.Http.okhttp;


import okhttp3.*;
import 积累.util.Http.IHttpCallBack;
import 积累.util.Http.IHttpUtils;
import 积累.util.Http.Pair;
import 积累.util.Http.RequestMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Desc: okhttp 门面类
 * User: DLJ
 * Date: 16/8/28
 */
public class OKhttpHttpUtils3_2_0 extends IHttpUtils.AbstractHttpUtils {
    private static OKhttpHttpUtils3_2_0 instance;

    protected OkHttpClient client;
    protected String       targetPath;

    protected OKhttpHttpUtils3_2_0(boolean isDebug) {
        super(isDebug);
        init();
    }

    public void init() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(5, TimeUnit
                .SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS);
        client = builder.build();
    }

    public static OKhttpHttpUtils3_2_0 getInstance(boolean isDebug) {
        if (instance == null) {
            instance = new OKhttpHttpUtils3_2_0(isDebug);
        }
        return instance;
    }


    @Override
    public void post(RequestMap params, IHttpCallBack<String>
            callBack) {
        if (params != null) {
            OkhttpParamBuilder okhttpParamBuilder = new OkhttpParamBuilder(params);
            params.setCallback(callBack);
            client.newCall(new Request.Builder().headers(Headers.of(params.getHeaders()))
                    .url(params.getUrl() + getQueryStr(params.getQueryStrs()))
                    .post(okhttpParamBuilder.build())
                    .build())
                    .enqueue(generateCallBack(params, callBack));
        }
    }

    @Override
    public void put(RequestMap params, IHttpCallBack<String> callBack) {
        if (params != null) {
            OkhttpParamBuilder okhttpParamBuilder = new OkhttpParamBuilder(params);
            params.setCallback(callBack);
            client.newCall(new Request.Builder().headers(Headers.of(params.getHeaders()))
                    .url(params.getUrl() + getQueryStr(params.getQueryStrs()))
                    .put(okhttpParamBuilder.build())
                    .build())
                    .enqueue(generateCallBack(params, callBack));
        }
    }

    @Override
    public <T> Callback generateCallBack(final RequestMap params, final IHttpCallBack<T> callBack) {
        return new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                callBack.onFailure(callBack.genetraResponseBean());
                printLog(params, e.getCause() + ":" + e.getMessage());

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String result = null;
                final IHttpCallBack.ResponseBean responceBean
                        = callBack.genetraResponseBean();
                try {
                    result = response.body()
                            .string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                printLog(params, result);
                final String finalResult = result;
                responceBean.pair = Pair.of(callBack.tag, finalResult);
                callBack.onComplate(responceBean);
            }
        };
    }

    @Override
    public void get(RequestMap params, IHttpCallBack<String>
            callBack) {
        Request request = new Request.Builder().headers(Headers.of(params.getHeaders()))
                .url(params.getUrl() + getQueryStr(params.getQueryStrs()))
                .build();
        client.newCall(request)
                .enqueue(generateCallBack(params, callBack));
    }

    @Override
    public void download(String targetPath, final RequestMap params, final IHttpCallBack<File> callBack) {
        this.targetPath = targetPath;
        Request request = new Request.Builder().url(params.getUrl() + getQueryStr(params.getQueryStrs()))
                .build();
        client.newCall(request)
                .enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        callBack.onFailure(callBack.genetraResponseBean());
                        printLog(params, e.getCause() + ":" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream inputStream = response.body()
                                .byteStream();
                        FileOutputStream fileOutputStream = new FileOutputStream(new File
                                ("/sdcard/logo" + ".jpg"));
                        byte[] buffer = new byte[2048];
                        int    len    = 0;
                        while ((len = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                        fileOutputStream.flush();

                        IHttpCallBack.ResponseBean
                                responceBean = callBack.genetraResponseBean();
                        responceBean.pair = Pair.of(callBack.tag, "success");
                        callBack.onComplate(responceBean);
                        printLog(params, "success");

                    }
                });
    }
}
