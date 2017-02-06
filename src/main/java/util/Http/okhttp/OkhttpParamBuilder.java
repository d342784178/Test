package util.Http.okhttp;


import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import util.Http.IParamBuilder;
import util.Http.RequestMap;

public class OkhttpParamBuilder extends IParamBuilder.DefaultParamBuilder<RequestBody> {

    public OkhttpParamBuilder(RequestMap requestMap1) {
        super(requestMap1);
    }

    public RequestBody build() {
        if (json != null) {
            return RequestBody.create(MediaType.parse("application/json"), json);
        } else if (files.size() > 0 && form.size() > 0) {
            MultipartBody.Builder multiBuilder = new MultipartBody.Builder();
            for (String key : form.keySet()) {
                String value = form.get(key);
                multiBuilder.addFormDataPart(key, value);
            }
            for (String key : files.keySet()) {
                RequestMap.UploadFileInfo uploadFile     = files.get(key);
                MediaType                 MEDIA_TYPE_PNG = MediaType.parse(uploadFile.mimetype);
                multiBuilder.addFormDataPart(key, uploadFile.file.getName(), RequestBody
                        .create(MEDIA_TYPE_PNG, uploadFile.file));
            }
            return multiBuilder.build();
        } else if (files.size() > 0) {
            MultipartBody.Builder multiBuilder = new MultipartBody.Builder();
            for (String key : files.keySet()) {
                RequestMap.UploadFileInfo uploadFile = files.get(key);
                MediaType                 mediaType  = MediaType.parse(uploadFile.mimetype);
                multiBuilder.addFormDataPart(key, uploadFile.file.getName(), RequestBody
                        .create(mediaType, uploadFile.file));
            }
            return multiBuilder.build();
        } else if (form.size() > 0) {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : form.keySet()) {
                String value = form.get(key);
                builder.add(key, value);
            }
            return builder.build();
        }
        throw new IllegalArgumentException("requestBody is null");
    }
}
