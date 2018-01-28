package util.Http;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Desc: 传参数专用map
 * User: DLJ
 * Date: 2016-03-29
 * Time: 09:58
 */
public class RequestMap {
    private IHttpCallBack callback;//回调
    private LinkedHashMap<String, String>         headers   = new LinkedHashMap<>();
    private LinkedHashMap<String, String>         queryStrs = new LinkedHashMap<>();
    private LinkedHashMap<String, String>         forms     = new LinkedHashMap<>();
    private LinkedHashMap<String, UploadFileInfo> files     = new LinkedHashMap<>();
    private String json;
    private String url;

    public RequestMap(String url) {
        this.url = url;
    }

    public RequestMap() {
    }

    public void putUrl(String url) {
        this.url = url;
    }

    public Object putHeader(String key, String value) {
        if (value == null) { return null; } else { return headers.put(key, value); }
    }

    public Object putQuery(String key, String value) {
        if (value == null) { return null; } else { return queryStrs.put(key, value); }
    }

    public Object putForm(String key, String value) {
        if (value == null) { return null; } else { return forms.put(key, value); }
    }

    public Object putFile(String mimetype, String key, File file) {
        return files.put(key, new UploadFileInfo(file, mimetype));
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        sb.append(this.getUrl());
        sb.append("?");
        for (Map.Entry<String, String> entry : this.getQueryStrs()
                .entrySet()) {
            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        sb.append("\n");
        for (Map.Entry<String, String> entry : this.getHeaders()
                .entrySet()) {
            sb.append("所传的参数为")
                    .append("[header]")
                    .append(entry.getKey())
                    .append(":")
                    .append(entry.getValue())
                    .append("\n");
        }
        for (Map.Entry<String, String> entry : this.getForms()
                .entrySet()) {
            sb.append("所传的参数为")
                    .append("[form]")
                    .append(entry.getKey())
                    .append(":")
                    .append(entry.getValue())
                    .append("\n");
        }
        for (Map.Entry<String, UploadFileInfo> entry : this.getFiles()
                .entrySet()) {
            sb.append("所传的参数为")
                    .append("[header]")
                    .append(entry.getKey())
                    .append(":")
                    .append(((entry.getValue().file.getAbsoluteFile())))
                    .append("\n");
        }
        return sb.toString();
    }

    public String getUrl() {
        return url;
    }

    public LinkedHashMap<String, String> getQueryStrs() {
        return queryStrs;
    }

    public LinkedHashMap<String, String> getHeaders() {
        return headers;
    }

    public LinkedHashMap<String, String> getForms() {
        return forms;
    }

    public LinkedHashMap<String, UploadFileInfo> getFiles() {
        return files;
    }

    public RequestMap setCallback(IHttpCallBack callback) {
        this.callback = callback;
        return this;
    }

    public static class UploadFileInfo {
        public File   file;
        public String mimetype;

        public UploadFileInfo(File file, String mimetype) {
            this.file = file;
            this.mimetype = mimetype;
        }
    }
}
