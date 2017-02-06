package util.Http.okhttp;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

public class LogInterceptor implements Interceptor {

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request     request = chain.request();
        String      method  = request.method();
        HttpUrl     url     = request.url();
        RequestBody body    = request.body();
        System.out.println(method + ":" + url);
        System.out.println(body.toString());
        okhttp3.Response  response  = chain.proceed(chain.request());
        okhttp3.MediaType mediaType = response.body().contentType();
        String            content   = response.body().string();
        return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, content)).build();
    }
}
