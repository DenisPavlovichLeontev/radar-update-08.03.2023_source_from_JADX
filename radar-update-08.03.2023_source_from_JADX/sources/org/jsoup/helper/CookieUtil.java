package org.jsoup.helper;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.internal.StringUtil;

class CookieUtil {
    private static final String Cookie2Name = "Cookie2";
    private static final String CookieName = "Cookie";
    private static final Map<String, List<String>> EmptyRequestHeaders = Collections.unmodifiableMap(new HashMap());
    private static final String Sep = "; ";

    CookieUtil() {
    }

    static void applyCookiesToRequest(HttpConnection.Request request, HttpURLConnection httpURLConnection) throws IOException {
        HashSet hashSet;
        HashSet hashSet2;
        LinkedHashSet<String> requestCookieSet = requestCookieSet(request);
        HashSet hashSet3 = null;
        for (Map.Entry next : request.cookieManager().get(asUri(request.url), EmptyRequestHeaders).entrySet()) {
            List list = (List) next.getValue();
            if (!(list == null || list.size() == 0)) {
                String str = (String) next.getKey();
                if (CookieName.equals(str)) {
                    hashSet = hashSet3;
                    hashSet2 = requestCookieSet;
                } else if (Cookie2Name.equals(str)) {
                    hashSet2 = new HashSet();
                    hashSet = hashSet2;
                }
                hashSet2.addAll(list);
                hashSet3 = hashSet;
            }
        }
        if (requestCookieSet.size() > 0) {
            httpURLConnection.addRequestProperty(CookieName, StringUtil.join((Collection<?>) requestCookieSet, Sep));
        }
        if (hashSet3 != null && hashSet3.size() > 0) {
            httpURLConnection.addRequestProperty(Cookie2Name, StringUtil.join((Collection<?>) hashSet3, Sep));
        }
    }

    private static LinkedHashSet<String> requestCookieSet(Connection.Request request) {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();
        for (Map.Entry next : request.cookies().entrySet()) {
            linkedHashSet.add(((String) next.getKey()) + SimpleComparison.EQUAL_TO_OPERATION + ((String) next.getValue()));
        }
        return linkedHashSet;
    }

    static URI asUri(URL url) throws IOException {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            MalformedURLException malformedURLException = new MalformedURLException(e.getMessage());
            malformedURLException.initCause(e);
            throw malformedURLException;
        }
    }

    static void storeCookies(HttpConnection.Request request, URL url, Map<String, List<String>> map) throws IOException {
        request.cookieManager().put(asUri(url), map);
    }
}
