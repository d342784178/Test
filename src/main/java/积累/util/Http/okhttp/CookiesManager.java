package 积累.util.Http.okhttp;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Desc:
 * User: DLJ
 * Date: 2016-08-29
 * Time: 14:50
 */
public class CookiesManager implements CookieJar {

    private final CookiesStore cookiesStore;

    public CookiesManager() {
        cookiesStore = new CookiesStore();
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookiesStore.add(item.domain(), item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        String domain = getDomain(url);
        List<Cookie> cookies = cookiesStore.get(domain);
        return cookies == null ? new ArrayList<Cookie>() : cookies;
    }

    private String getDomain(HttpUrl url) {
        String host = url.host();
        if (!StringUtils.isEmpty(host)) {
            int i = host.indexOf(".");
            if (i != -1) { return host.substring(i + 1, host.length()); } else { return "";}
        } else {
            return "";
        }
    }

    class CookiesStore extends HashMap<String, List<Cookie>> {
        public void add(String key, Cookie cookie) {
            if (containsKey(key)) {
                get(key).add(cookie);
            } else {
                ArrayList<Cookie> cookies = new ArrayList<>();
                cookies.add(cookie);
                put(key, cookies);
            }
        }


    }
}
