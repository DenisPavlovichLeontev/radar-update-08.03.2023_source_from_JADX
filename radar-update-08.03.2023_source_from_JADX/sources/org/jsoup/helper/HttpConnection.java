package org.jsoup.helper;

import com.google.android.gms.common.internal.ImagesContract;
import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.IDN;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import kotlin.UByte;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.text.Typography;
import mil.nga.geopackage.schema.constraints.DataColumnConstraints;
import org.json.HTTP;
import org.jsoup.Connection;
import org.jsoup.UncheckedIOException;
import org.jsoup.internal.ConstrainableInputStream;
import org.jsoup.internal.Normalizer;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TokenQueue;
import org.mapsforge.core.model.Tag;
import org.osgeo.proj4j.units.AngleFormat;
import org.osmdroid.tileprovider.modules.DatabaseFileArchive;

public class HttpConnection implements Connection {
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String DEFAULT_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36";
    private static final String DefaultUploadType = "application/octet-stream";
    public static final String FORM_URL_ENCODED = "application/x-www-form-urlencoded";
    private static final int HTTP_TEMP_REDIR = 307;
    /* access modifiers changed from: private */
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static final String USER_AGENT = "User-Agent";
    /* access modifiers changed from: private */
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    private Request req;
    @Nullable
    private Connection.Response res;

    public static Connection connect(String str) {
        HttpConnection httpConnection = new HttpConnection();
        httpConnection.url(str);
        return httpConnection;
    }

    public static Connection connect(URL url) {
        HttpConnection httpConnection = new HttpConnection();
        httpConnection.url(url);
        return httpConnection;
    }

    public HttpConnection() {
        this.req = new Request();
    }

    HttpConnection(Request request) {
        this.req = new Request(request);
    }

    private static String encodeUrl(String str) {
        try {
            return encodeUrl(new URL(str)).toExternalForm();
        } catch (Exception unused) {
            return str;
        }
    }

    static URL encodeUrl(URL url) {
        URL punyUrl = punyUrl(url);
        try {
            return new URL(new URI(punyUrl.toExternalForm().replace(" ", "%20")).toASCIIString());
        } catch (MalformedURLException | URISyntaxException unused) {
            return punyUrl;
        }
    }

    /* access modifiers changed from: private */
    public static URL punyUrl(URL url) {
        if (StringUtil.isAscii(url.getHost())) {
            return url;
        }
        try {
            return new URL(url.getProtocol(), IDN.toASCII(url.getHost()), url.getPort(), url.getFile());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /* access modifiers changed from: private */
    public static String encodeMimeName(String str) {
        return str.replace(AngleFormat.STR_SEC_SYMBOL, "%22");
    }

    public Connection newRequest() {
        return new HttpConnection(this.req);
    }

    private HttpConnection(Request request, Response response) {
        this.req = request;
        this.res = response;
    }

    public Connection url(URL url) {
        this.req.url(url);
        return this;
    }

    public Connection url(String str) {
        Validate.notEmptyParam(str, ImagesContract.URL);
        try {
            this.req.url(new URL(encodeUrl(str)));
            return this;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(String.format("The supplied URL, '%s', is malformed. Make sure it is an absolute URL, and starts with 'http://' or 'https://'. See https://jsoup.org/cookbook/extracting-data/working-with-urls", new Object[]{str}), e);
        }
    }

    public Connection proxy(@Nullable Proxy proxy) {
        this.req.proxy(proxy);
        return this;
    }

    public Connection proxy(String str, int i) {
        this.req.proxy(str, i);
        return this;
    }

    public Connection userAgent(String str) {
        Validate.notNullParam(str, "userAgent");
        this.req.header(USER_AGENT, str);
        return this;
    }

    public Connection timeout(int i) {
        this.req.timeout(i);
        return this;
    }

    public Connection maxBodySize(int i) {
        this.req.maxBodySize(i);
        return this;
    }

    public Connection followRedirects(boolean z) {
        this.req.followRedirects(z);
        return this;
    }

    public Connection referrer(String str) {
        Validate.notNullParam(str, "referrer");
        this.req.header("Referer", str);
        return this;
    }

    public Connection method(Connection.Method method) {
        this.req.method(method);
        return this;
    }

    public Connection ignoreHttpErrors(boolean z) {
        this.req.ignoreHttpErrors(z);
        return this;
    }

    public Connection ignoreContentType(boolean z) {
        this.req.ignoreContentType(z);
        return this;
    }

    public Connection data(String str, String str2) {
        this.req.data((Connection.KeyVal) KeyVal.create(str, str2));
        return this;
    }

    public Connection sslSocketFactory(SSLSocketFactory sSLSocketFactory) {
        this.req.sslSocketFactory(sSLSocketFactory);
        return this;
    }

    public Connection data(String str, String str2, InputStream inputStream) {
        this.req.data((Connection.KeyVal) KeyVal.create(str, str2, inputStream));
        return this;
    }

    public Connection data(String str, String str2, InputStream inputStream, String str3) {
        this.req.data(KeyVal.create(str, str2, inputStream).contentType(str3));
        return this;
    }

    public Connection data(Map<String, String> map) {
        Validate.notNullParam(map, "data");
        for (Map.Entry next : map.entrySet()) {
            this.req.data((Connection.KeyVal) KeyVal.create((String) next.getKey(), (String) next.getValue()));
        }
        return this;
    }

    public Connection data(String... strArr) {
        Validate.notNullParam(strArr, "keyvals");
        Validate.isTrue(strArr.length % 2 == 0, "Must supply an even number of key value pairs");
        for (int i = 0; i < strArr.length; i += 2) {
            String str = strArr[i];
            String str2 = strArr[i + 1];
            Validate.notEmpty(str, "Data key must not be empty");
            Validate.notNull(str2, "Data value must not be null");
            this.req.data((Connection.KeyVal) KeyVal.create(str, str2));
        }
        return this;
    }

    public Connection data(Collection<Connection.KeyVal> collection) {
        Validate.notNullParam(collection, "data");
        for (Connection.KeyVal data : collection) {
            this.req.data(data);
        }
        return this;
    }

    public Connection.KeyVal data(String str) {
        Validate.notEmptyParam(str, DatabaseFileArchive.COLUMN_KEY);
        for (Connection.KeyVal next : request().data()) {
            if (next.key().equals(str)) {
                return next;
            }
        }
        return null;
    }

    public Connection requestBody(String str) {
        this.req.requestBody(str);
        return this;
    }

    public Connection header(String str, String str2) {
        this.req.header(str, str2);
        return this;
    }

    public Connection headers(Map<String, String> map) {
        Validate.notNullParam(map, "headers");
        for (Map.Entry next : map.entrySet()) {
            this.req.header((String) next.getKey(), (String) next.getValue());
        }
        return this;
    }

    public Connection cookie(String str, String str2) {
        this.req.cookie(str, str2);
        return this;
    }

    public Connection cookies(Map<String, String> map) {
        Validate.notNullParam(map, "cookies");
        for (Map.Entry next : map.entrySet()) {
            this.req.cookie((String) next.getKey(), (String) next.getValue());
        }
        return this;
    }

    public Connection cookieStore(CookieStore cookieStore) {
        CookieManager unused = this.req.cookieManager = new CookieManager(cookieStore, (CookiePolicy) null);
        return this;
    }

    public CookieStore cookieStore() {
        return this.req.cookieManager.getCookieStore();
    }

    public Connection parser(Parser parser) {
        this.req.parser(parser);
        return this;
    }

    public Document get() throws IOException {
        this.req.method(Connection.Method.GET);
        execute();
        Validate.notNull(this.res);
        return this.res.parse();
    }

    public Document post() throws IOException {
        this.req.method(Connection.Method.POST);
        execute();
        Validate.notNull(this.res);
        return this.res.parse();
    }

    public Connection.Response execute() throws IOException {
        Response execute = Response.execute(this.req);
        this.res = execute;
        return execute;
    }

    public Connection.Request request() {
        return this.req;
    }

    public Connection request(Connection.Request request) {
        this.req = (Request) request;
        return this;
    }

    public Connection.Response response() {
        Connection.Response response = this.res;
        if (response != null) {
            return response;
        }
        throw new IllegalArgumentException("You must execute the request before getting a response.");
    }

    public Connection response(Connection.Response response) {
        this.res = response;
        return this;
    }

    public Connection postDataCharset(String str) {
        this.req.postDataCharset(str);
        return this;
    }

    private static abstract class Base<T extends Connection.Base<T>> implements Connection.Base<T> {
        private static final URL UnsetUrl;
        Map<String, String> cookies;
        Map<String, List<String>> headers;
        Connection.Method method;
        URL url;

        static {
            try {
                UnsetUrl = new URL("http://undefined/");
            } catch (MalformedURLException e) {
                throw new IllegalStateException(e);
            }
        }

        private Base() {
            this.url = UnsetUrl;
            this.method = Connection.Method.GET;
            this.headers = new LinkedHashMap();
            this.cookies = new LinkedHashMap();
        }

        private Base(Base<T> base) {
            this.url = UnsetUrl;
            this.method = Connection.Method.GET;
            this.url = base.url;
            this.method = base.method;
            this.headers = new LinkedHashMap();
            for (Map.Entry next : base.headers.entrySet()) {
                this.headers.put((String) next.getKey(), new ArrayList((Collection) next.getValue()));
            }
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            this.cookies = linkedHashMap;
            linkedHashMap.putAll(base.cookies);
        }

        public URL url() {
            URL url2 = this.url;
            if (url2 != UnsetUrl) {
                return url2;
            }
            throw new IllegalArgumentException("URL not set. Make sure to call #url(...) before executing the request.");
        }

        public T url(URL url2) {
            Validate.notNullParam(url2, ImagesContract.URL);
            this.url = HttpConnection.punyUrl(url2);
            return this;
        }

        public Connection.Method method() {
            return this.method;
        }

        public T method(Connection.Method method2) {
            Validate.notNullParam(method2, "method");
            this.method = method2;
            return this;
        }

        public String header(String str) {
            Validate.notNullParam(str, "name");
            List<String> headersCaseInsensitive = getHeadersCaseInsensitive(str);
            if (headersCaseInsensitive.size() > 0) {
                return StringUtil.join((Collection<?>) headersCaseInsensitive, ", ");
            }
            return null;
        }

        public T addHeader(String str, String str2) {
            Validate.notEmptyParam(str, "name");
            if (str2 == null) {
                str2 = "";
            }
            List headers2 = headers(str);
            if (headers2.isEmpty()) {
                headers2 = new ArrayList();
                this.headers.put(str, headers2);
            }
            headers2.add(fixHeaderEncoding(str2));
            return this;
        }

        public List<String> headers(String str) {
            Validate.notEmptyParam(str, "name");
            return getHeadersCaseInsensitive(str);
        }

        private static String fixHeaderEncoding(String str) {
            byte[] bytes = str.getBytes(HttpConnection.ISO_8859_1);
            if (!looksLikeUtf8(bytes)) {
                return str;
            }
            return new String(bytes, HttpConnection.UTF_8);
        }

        private static boolean looksLikeUtf8(byte[] bArr) {
            int i;
            int i2 = 3;
            if (!(bArr.length >= 3 && (bArr[0] & UByte.MAX_VALUE) == 239 && (bArr[1] & UByte.MAX_VALUE) == 187 && (bArr[2] & UByte.MAX_VALUE) == 191)) {
                i2 = 0;
            }
            int length = bArr.length;
            while (i2 < length) {
                byte b = bArr[i2];
                if ((b & ByteCompanionObject.MIN_VALUE) != 0) {
                    if ((b & 224) == 192) {
                        i = i2 + 1;
                    } else if ((b & 240) == 224) {
                        i = i2 + 2;
                    } else if ((b & 248) != 240) {
                        return false;
                    } else {
                        i = i2 + 3;
                    }
                    if (i >= bArr.length) {
                        return false;
                    }
                    while (i2 < i) {
                        i2++;
                        if ((bArr[i2] & 192) != 128) {
                            return false;
                        }
                    }
                    continue;
                }
                i2++;
            }
            return true;
        }

        public T header(String str, String str2) {
            Validate.notEmptyParam(str, "name");
            removeHeader(str);
            addHeader(str, str2);
            return this;
        }

        public boolean hasHeader(String str) {
            Validate.notEmptyParam(str, "name");
            return !getHeadersCaseInsensitive(str).isEmpty();
        }

        public boolean hasHeaderWithValue(String str, String str2) {
            Validate.notEmpty(str);
            Validate.notEmpty(str2);
            for (String equalsIgnoreCase : headers(str)) {
                if (str2.equalsIgnoreCase(equalsIgnoreCase)) {
                    return true;
                }
            }
            return false;
        }

        public T removeHeader(String str) {
            Validate.notEmptyParam(str, "name");
            Map.Entry<String, List<String>> scanHeaders = scanHeaders(str);
            if (scanHeaders != null) {
                this.headers.remove(scanHeaders.getKey());
            }
            return this;
        }

        public Map<String, String> headers() {
            LinkedHashMap linkedHashMap = new LinkedHashMap(this.headers.size());
            for (Map.Entry next : this.headers.entrySet()) {
                String str = (String) next.getKey();
                List list = (List) next.getValue();
                if (list.size() > 0) {
                    linkedHashMap.put(str, (String) list.get(0));
                }
            }
            return linkedHashMap;
        }

        public Map<String, List<String>> multiHeaders() {
            return this.headers;
        }

        private List<String> getHeadersCaseInsensitive(String str) {
            Validate.notNull(str);
            for (Map.Entry next : this.headers.entrySet()) {
                if (str.equalsIgnoreCase((String) next.getKey())) {
                    return (List) next.getValue();
                }
            }
            return Collections.emptyList();
        }

        @Nullable
        private Map.Entry<String, List<String>> scanHeaders(String str) {
            String lowerCase = Normalizer.lowerCase(str);
            for (Map.Entry<String, List<String>> next : this.headers.entrySet()) {
                if (Normalizer.lowerCase(next.getKey()).equals(lowerCase)) {
                    return next;
                }
            }
            return null;
        }

        public String cookie(String str) {
            Validate.notEmptyParam(str, "name");
            return this.cookies.get(str);
        }

        public T cookie(String str, String str2) {
            Validate.notEmptyParam(str, "name");
            Validate.notNullParam(str2, DataColumnConstraints.COLUMN_VALUE);
            this.cookies.put(str, str2);
            return this;
        }

        public boolean hasCookie(String str) {
            Validate.notEmptyParam(str, "name");
            return this.cookies.containsKey(str);
        }

        public T removeCookie(String str) {
            Validate.notEmptyParam(str, "name");
            this.cookies.remove(str);
            return this;
        }

        public Map<String, String> cookies() {
            return this.cookies;
        }
    }

    public static class Request extends Base<Connection.Request> implements Connection.Request {
        @Nullable
        private String body;
        /* access modifiers changed from: private */
        public CookieManager cookieManager;
        private final Collection<Connection.KeyVal> data;
        /* access modifiers changed from: private */
        public volatile boolean executing;
        private boolean followRedirects;
        private boolean ignoreContentType;
        private boolean ignoreHttpErrors;
        private int maxBodySizeBytes;
        private Parser parser;
        /* access modifiers changed from: private */
        public boolean parserDefined;
        private String postDataCharset;
        @Nullable
        private Proxy proxy;
        @Nullable
        private SSLSocketFactory sslSocketFactory;
        private int timeoutMilliseconds;

        public /* bridge */ /* synthetic */ Connection.Base addHeader(String str, String str2) {
            return super.addHeader(str, str2);
        }

        public /* bridge */ /* synthetic */ String cookie(String str) {
            return super.cookie(str);
        }

        public /* bridge */ /* synthetic */ Connection.Base cookie(String str, String str2) {
            return super.cookie(str, str2);
        }

        public /* bridge */ /* synthetic */ Map cookies() {
            return super.cookies();
        }

        public /* bridge */ /* synthetic */ boolean hasCookie(String str) {
            return super.hasCookie(str);
        }

        public /* bridge */ /* synthetic */ boolean hasHeader(String str) {
            return super.hasHeader(str);
        }

        public /* bridge */ /* synthetic */ boolean hasHeaderWithValue(String str, String str2) {
            return super.hasHeaderWithValue(str, str2);
        }

        public /* bridge */ /* synthetic */ String header(String str) {
            return super.header(str);
        }

        public /* bridge */ /* synthetic */ Connection.Base header(String str, String str2) {
            return super.header(str, str2);
        }

        public /* bridge */ /* synthetic */ List headers(String str) {
            return super.headers(str);
        }

        public /* bridge */ /* synthetic */ Map headers() {
            return super.headers();
        }

        public /* bridge */ /* synthetic */ Connection.Base method(Connection.Method method) {
            return super.method(method);
        }

        public /* bridge */ /* synthetic */ Connection.Method method() {
            return super.method();
        }

        public /* bridge */ /* synthetic */ Map multiHeaders() {
            return super.multiHeaders();
        }

        public /* bridge */ /* synthetic */ Connection.Base removeCookie(String str) {
            return super.removeCookie(str);
        }

        public /* bridge */ /* synthetic */ Connection.Base removeHeader(String str) {
            return super.removeHeader(str);
        }

        public /* bridge */ /* synthetic */ URL url() {
            return super.url();
        }

        public /* bridge */ /* synthetic */ Connection.Base url(URL url) {
            return super.url(url);
        }

        static {
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        }

        Request() {
            super();
            this.body = null;
            this.ignoreHttpErrors = false;
            this.ignoreContentType = false;
            this.parserDefined = false;
            this.postDataCharset = DataUtil.defaultCharsetName;
            this.executing = false;
            this.timeoutMilliseconds = 30000;
            this.maxBodySizeBytes = 2097152;
            this.followRedirects = true;
            this.data = new ArrayList();
            this.method = Connection.Method.GET;
            addHeader("Accept-Encoding", "gzip");
            addHeader(HttpConnection.USER_AGENT, HttpConnection.DEFAULT_UA);
            this.parser = Parser.htmlParser();
            this.cookieManager = new CookieManager();
        }

        Request(Request request) {
            super(request);
            this.body = null;
            this.ignoreHttpErrors = false;
            this.ignoreContentType = false;
            this.parserDefined = false;
            this.postDataCharset = DataUtil.defaultCharsetName;
            this.executing = false;
            this.proxy = request.proxy;
            this.postDataCharset = request.postDataCharset;
            this.timeoutMilliseconds = request.timeoutMilliseconds;
            this.maxBodySizeBytes = request.maxBodySizeBytes;
            this.followRedirects = request.followRedirects;
            ArrayList arrayList = new ArrayList();
            this.data = arrayList;
            arrayList.addAll(request.data());
            this.body = request.body;
            this.ignoreHttpErrors = request.ignoreHttpErrors;
            this.ignoreContentType = request.ignoreContentType;
            this.parser = request.parser.newInstance();
            this.parserDefined = request.parserDefined;
            this.sslSocketFactory = request.sslSocketFactory;
            this.cookieManager = request.cookieManager;
            this.executing = false;
        }

        public Proxy proxy() {
            return this.proxy;
        }

        public Request proxy(@Nullable Proxy proxy2) {
            this.proxy = proxy2;
            return this;
        }

        public Request proxy(String str, int i) {
            this.proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(str, i));
            return this;
        }

        public int timeout() {
            return this.timeoutMilliseconds;
        }

        public Request timeout(int i) {
            Validate.isTrue(i >= 0, "Timeout milliseconds must be 0 (infinite) or greater");
            this.timeoutMilliseconds = i;
            return this;
        }

        public int maxBodySize() {
            return this.maxBodySizeBytes;
        }

        public Connection.Request maxBodySize(int i) {
            Validate.isTrue(i >= 0, "maxSize must be 0 (unlimited) or larger");
            this.maxBodySizeBytes = i;
            return this;
        }

        public boolean followRedirects() {
            return this.followRedirects;
        }

        public Connection.Request followRedirects(boolean z) {
            this.followRedirects = z;
            return this;
        }

        public boolean ignoreHttpErrors() {
            return this.ignoreHttpErrors;
        }

        public SSLSocketFactory sslSocketFactory() {
            return this.sslSocketFactory;
        }

        public void sslSocketFactory(SSLSocketFactory sSLSocketFactory) {
            this.sslSocketFactory = sSLSocketFactory;
        }

        public Connection.Request ignoreHttpErrors(boolean z) {
            this.ignoreHttpErrors = z;
            return this;
        }

        public boolean ignoreContentType() {
            return this.ignoreContentType;
        }

        public Connection.Request ignoreContentType(boolean z) {
            this.ignoreContentType = z;
            return this;
        }

        public Request data(Connection.KeyVal keyVal) {
            Validate.notNullParam(keyVal, "keyval");
            this.data.add(keyVal);
            return this;
        }

        public Collection<Connection.KeyVal> data() {
            return this.data;
        }

        public Connection.Request requestBody(@Nullable String str) {
            this.body = str;
            return this;
        }

        public String requestBody() {
            return this.body;
        }

        public Request parser(Parser parser2) {
            this.parser = parser2;
            this.parserDefined = true;
            return this;
        }

        public Parser parser() {
            return this.parser;
        }

        public Connection.Request postDataCharset(String str) {
            Validate.notNullParam(str, "charset");
            if (Charset.isSupported(str)) {
                this.postDataCharset = str;
                return this;
            }
            throw new IllegalCharsetNameException(str);
        }

        public String postDataCharset() {
            return this.postDataCharset;
        }

        /* access modifiers changed from: package-private */
        public CookieManager cookieManager() {
            return this.cookieManager;
        }
    }

    public static class Response extends Base<Connection.Response> implements Connection.Response {
        private static final String LOCATION = "Location";
        private static final int MAX_REDIRECTS = 20;
        private static final Pattern xmlContentTypeRxp = Pattern.compile("(application|text)/\\w*\\+?xml.*");
        @Nullable
        private InputStream bodyStream;
        @Nullable
        private ByteBuffer byteData;
        @Nullable
        private String charset;
        @Nullable
        private HttpURLConnection conn;
        @Nullable
        private final String contentType;
        private boolean executed;
        private boolean inputStreamRead;
        private int numRedirects;
        private final Request req;
        private final int statusCode;
        private final String statusMessage;

        public /* bridge */ /* synthetic */ Connection.Base addHeader(String str, String str2) {
            return super.addHeader(str, str2);
        }

        public /* bridge */ /* synthetic */ String cookie(String str) {
            return super.cookie(str);
        }

        public /* bridge */ /* synthetic */ Connection.Base cookie(String str, String str2) {
            return super.cookie(str, str2);
        }

        public /* bridge */ /* synthetic */ Map cookies() {
            return super.cookies();
        }

        public /* bridge */ /* synthetic */ boolean hasCookie(String str) {
            return super.hasCookie(str);
        }

        public /* bridge */ /* synthetic */ boolean hasHeader(String str) {
            return super.hasHeader(str);
        }

        public /* bridge */ /* synthetic */ boolean hasHeaderWithValue(String str, String str2) {
            return super.hasHeaderWithValue(str, str2);
        }

        public /* bridge */ /* synthetic */ String header(String str) {
            return super.header(str);
        }

        public /* bridge */ /* synthetic */ Connection.Base header(String str, String str2) {
            return super.header(str, str2);
        }

        public /* bridge */ /* synthetic */ List headers(String str) {
            return super.headers(str);
        }

        public /* bridge */ /* synthetic */ Map headers() {
            return super.headers();
        }

        public /* bridge */ /* synthetic */ Connection.Base method(Connection.Method method) {
            return super.method(method);
        }

        public /* bridge */ /* synthetic */ Connection.Method method() {
            return super.method();
        }

        public /* bridge */ /* synthetic */ Map multiHeaders() {
            return super.multiHeaders();
        }

        public /* bridge */ /* synthetic */ Connection.Base removeCookie(String str) {
            return super.removeCookie(str);
        }

        public /* bridge */ /* synthetic */ Connection.Base removeHeader(String str) {
            return super.removeHeader(str);
        }

        public /* bridge */ /* synthetic */ URL url() {
            return super.url();
        }

        public /* bridge */ /* synthetic */ Connection.Base url(URL url) {
            return super.url(url);
        }

        Response() {
            super();
            this.executed = false;
            this.inputStreamRead = false;
            this.numRedirects = 0;
            this.statusCode = 400;
            this.statusMessage = "Request not made";
            this.req = new Request();
            this.contentType = null;
        }

        static Response execute(Request request) throws IOException {
            return execute(request, (Response) null);
        }

        /* JADX WARNING: Removed duplicated region for block: B:28:0x0092 A[Catch:{ IOException -> 0x009f, all -> 0x009d, IOException -> 0x01f9 }] */
        /* JADX WARNING: Removed duplicated region for block: B:48:0x00bf A[Catch:{ IOException -> 0x01f4 }] */
        /* JADX WARNING: Removed duplicated region for block: B:59:0x010f  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static org.jsoup.helper.HttpConnection.Response execute(org.jsoup.helper.HttpConnection.Request r8, @javax.annotation.Nullable org.jsoup.helper.HttpConnection.Response r9) throws java.io.IOException {
            /*
                monitor-enter(r8)
                boolean r0 = r8.executing     // Catch:{ all -> 0x0204 }
                java.lang.String r1 = "Multiple threads were detected trying to execute the same request concurrently. Make sure to use Connection#newRequest() and do not share an executing request between threads."
                org.jsoup.helper.Validate.isFalse(r0, r1)     // Catch:{ all -> 0x0204 }
                r0 = 1
                boolean unused = r8.executing = r0     // Catch:{ all -> 0x0204 }
                monitor-exit(r8)     // Catch:{ all -> 0x0204 }
                java.lang.String r1 = "req"
                org.jsoup.helper.Validate.notNullParam(r8, r1)
                java.net.URL r1 = r8.url()
                java.lang.String r2 = "URL must be specified to connect"
                org.jsoup.helper.Validate.notNull(r1, r2)
                java.lang.String r1 = r1.getProtocol()
                java.lang.String r2 = "http"
                boolean r2 = r1.equals(r2)
                if (r2 != 0) goto L_0x003a
                java.lang.String r2 = "https"
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L_0x0032
                goto L_0x003a
            L_0x0032:
                java.net.MalformedURLException r8 = new java.net.MalformedURLException
                java.lang.String r9 = "Only http & https protocols supported"
                r8.<init>(r9)
                throw r8
            L_0x003a:
                org.jsoup.Connection$Method r1 = r8.method()
                boolean r1 = r1.hasBody()
                java.lang.String r2 = r8.requestBody()
                r3 = 0
                if (r2 == 0) goto L_0x004b
                r2 = r0
                goto L_0x004c
            L_0x004b:
                r2 = r3
            L_0x004c:
                if (r1 != 0) goto L_0x0066
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "Cannot set a request body for HTTP method "
                r4.append(r5)
                org.jsoup.Connection$Method r5 = r8.method()
                r4.append(r5)
                java.lang.String r4 = r4.toString()
                org.jsoup.helper.Validate.isFalse(r2, r4)
            L_0x0066:
                java.util.Collection r4 = r8.data()
                int r4 = r4.size()
                r5 = 0
                if (r4 <= 0) goto L_0x0079
                if (r1 == 0) goto L_0x0075
                if (r2 == 0) goto L_0x0079
            L_0x0075:
                serialiseRequestUrl(r8)
                goto L_0x0080
            L_0x0079:
                if (r1 == 0) goto L_0x0080
                java.lang.String r1 = setOutputContentType(r8)
                goto L_0x0081
            L_0x0080:
                r1 = r5
            L_0x0081:
                long r6 = java.lang.System.nanoTime()
                java.net.HttpURLConnection r2 = createConnection(r8)
                r2.connect()     // Catch:{ IOException -> 0x01f9 }
                boolean r4 = r2.getDoOutput()     // Catch:{ IOException -> 0x01f9 }
                if (r4 == 0) goto L_0x00a8
                java.io.OutputStream r4 = r2.getOutputStream()     // Catch:{ IOException -> 0x01f9 }
                writePost(r8, r4, r1)     // Catch:{ IOException -> 0x009f }
                r4.close()     // Catch:{ IOException -> 0x01f9 }
                goto L_0x00a8
            L_0x009d:
                r9 = move-exception
                goto L_0x00a4
            L_0x009f:
                r9 = move-exception
                r2.disconnect()     // Catch:{ all -> 0x009d }
                throw r9     // Catch:{ all -> 0x009d }
            L_0x00a4:
                r4.close()     // Catch:{ IOException -> 0x01f9 }
                throw r9     // Catch:{ IOException -> 0x01f9 }
            L_0x00a8:
                int r1 = r2.getResponseCode()     // Catch:{ IOException -> 0x01f9 }
                org.jsoup.helper.HttpConnection$Response r4 = new org.jsoup.helper.HttpConnection$Response     // Catch:{ IOException -> 0x01f9 }
                r4.<init>(r2, r8, r9)     // Catch:{ IOException -> 0x01f9 }
                java.lang.String r9 = "Location"
                boolean r9 = r4.hasHeader(r9)     // Catch:{ IOException -> 0x01f4 }
                if (r9 == 0) goto L_0x010f
                boolean r9 = r8.followRedirects()     // Catch:{ IOException -> 0x01f4 }
                if (r9 == 0) goto L_0x010f
                r9 = 307(0x133, float:4.3E-43)
                if (r1 == r9) goto L_0x00d7
                org.jsoup.Connection$Method r9 = org.jsoup.Connection.Method.GET     // Catch:{ IOException -> 0x01f4 }
                r8.method(r9)     // Catch:{ IOException -> 0x01f4 }
                java.util.Collection r9 = r8.data()     // Catch:{ IOException -> 0x01f4 }
                r9.clear()     // Catch:{ IOException -> 0x01f4 }
                r8.requestBody(r5)     // Catch:{ IOException -> 0x01f4 }
                java.lang.String r9 = "Content-Type"
                r8.removeHeader(r9)     // Catch:{ IOException -> 0x01f4 }
            L_0x00d7:
                java.lang.String r9 = "Location"
                java.lang.String r9 = r4.header(r9)     // Catch:{ IOException -> 0x01f4 }
                org.jsoup.helper.Validate.notNull(r9)     // Catch:{ IOException -> 0x01f4 }
                java.lang.String r0 = "http:/"
                boolean r0 = r9.startsWith(r0)     // Catch:{ IOException -> 0x01f4 }
                if (r0 == 0) goto L_0x00f5
                r0 = 6
                char r1 = r9.charAt(r0)     // Catch:{ IOException -> 0x01f4 }
                r2 = 47
                if (r1 == r2) goto L_0x00f5
                java.lang.String r9 = r9.substring(r0)     // Catch:{ IOException -> 0x01f4 }
            L_0x00f5:
                java.net.URL r0 = r8.url()     // Catch:{ IOException -> 0x01f4 }
                java.net.URL r9 = org.jsoup.internal.StringUtil.resolve((java.net.URL) r0, (java.lang.String) r9)     // Catch:{ IOException -> 0x01f4 }
                java.net.URL r9 = org.jsoup.helper.HttpConnection.encodeUrl((java.net.URL) r9)     // Catch:{ IOException -> 0x01f4 }
                r8.url(r9)     // Catch:{ IOException -> 0x01f4 }
                boolean unused = r8.executing = r3     // Catch:{ IOException -> 0x01f4 }
                org.jsoup.helper.HttpConnection$Response r9 = execute(r8, r4)     // Catch:{ IOException -> 0x01f4 }
                boolean unused = r8.executing = r3
                return r9
            L_0x010f:
                r9 = 200(0xc8, float:2.8E-43)
                if (r1 < r9) goto L_0x0117
                r9 = 400(0x190, float:5.6E-43)
                if (r1 < r9) goto L_0x011d
            L_0x0117:
                boolean r9 = r8.ignoreHttpErrors()     // Catch:{ IOException -> 0x01f4 }
                if (r9 == 0) goto L_0x01e4
            L_0x011d:
                java.lang.String r9 = r4.contentType()     // Catch:{ IOException -> 0x01f4 }
                if (r9 == 0) goto L_0x014e
                boolean r1 = r8.ignoreContentType()     // Catch:{ IOException -> 0x01f4 }
                if (r1 != 0) goto L_0x014e
                java.lang.String r1 = "text/"
                boolean r1 = r9.startsWith(r1)     // Catch:{ IOException -> 0x01f4 }
                if (r1 != 0) goto L_0x014e
                java.util.regex.Pattern r1 = xmlContentTypeRxp     // Catch:{ IOException -> 0x01f4 }
                java.util.regex.Matcher r1 = r1.matcher(r9)     // Catch:{ IOException -> 0x01f4 }
                boolean r1 = r1.matches()     // Catch:{ IOException -> 0x01f4 }
                if (r1 == 0) goto L_0x013e
                goto L_0x014e
            L_0x013e:
                org.jsoup.UnsupportedMimeTypeException r0 = new org.jsoup.UnsupportedMimeTypeException     // Catch:{ IOException -> 0x01f4 }
                java.lang.String r1 = "Unhandled content type. Must be text/*, application/xml, or application/*+xml"
                java.net.URL r2 = r8.url()     // Catch:{ IOException -> 0x01f4 }
                java.lang.String r2 = r2.toString()     // Catch:{ IOException -> 0x01f4 }
                r0.<init>(r1, r9, r2)     // Catch:{ IOException -> 0x01f4 }
                throw r0     // Catch:{ IOException -> 0x01f4 }
            L_0x014e:
                if (r9 == 0) goto L_0x0169
                java.util.regex.Pattern r1 = xmlContentTypeRxp     // Catch:{ IOException -> 0x01f4 }
                java.util.regex.Matcher r9 = r1.matcher(r9)     // Catch:{ IOException -> 0x01f4 }
                boolean r9 = r9.matches()     // Catch:{ IOException -> 0x01f4 }
                if (r9 == 0) goto L_0x0169
                boolean r9 = r8.parserDefined     // Catch:{ IOException -> 0x01f4 }
                if (r9 != 0) goto L_0x0169
                org.jsoup.parser.Parser r9 = org.jsoup.parser.Parser.xmlParser()     // Catch:{ IOException -> 0x01f4 }
                r8.parser((org.jsoup.parser.Parser) r9)     // Catch:{ IOException -> 0x01f4 }
            L_0x0169:
                java.lang.String r9 = r4.contentType     // Catch:{ IOException -> 0x01f4 }
                java.lang.String r9 = org.jsoup.helper.DataUtil.getCharsetFromContentType(r9)     // Catch:{ IOException -> 0x01f4 }
                r4.charset = r9     // Catch:{ IOException -> 0x01f4 }
                int r9 = r2.getContentLength()     // Catch:{ IOException -> 0x01f4 }
                if (r9 == 0) goto L_0x01d8
                org.jsoup.Connection$Method r9 = r8.method()     // Catch:{ IOException -> 0x01f4 }
                org.jsoup.Connection$Method r1 = org.jsoup.Connection.Method.HEAD     // Catch:{ IOException -> 0x01f4 }
                if (r9 == r1) goto L_0x01d8
                java.io.InputStream r9 = r2.getErrorStream()     // Catch:{ IOException -> 0x01f4 }
                if (r9 == 0) goto L_0x018a
                java.io.InputStream r9 = r2.getErrorStream()     // Catch:{ IOException -> 0x01f4 }
                goto L_0x018e
            L_0x018a:
                java.io.InputStream r9 = r2.getInputStream()     // Catch:{ IOException -> 0x01f4 }
            L_0x018e:
                r4.bodyStream = r9     // Catch:{ IOException -> 0x01f4 }
                org.jsoup.helper.Validate.notNull(r9)     // Catch:{ IOException -> 0x01f4 }
                java.lang.String r9 = "Content-Encoding"
                java.lang.String r1 = "gzip"
                boolean r9 = r4.hasHeaderWithValue(r9, r1)     // Catch:{ IOException -> 0x01f4 }
                if (r9 == 0) goto L_0x01a7
                java.util.zip.GZIPInputStream r9 = new java.util.zip.GZIPInputStream     // Catch:{ IOException -> 0x01f4 }
                java.io.InputStream r1 = r4.bodyStream     // Catch:{ IOException -> 0x01f4 }
                r9.<init>(r1)     // Catch:{ IOException -> 0x01f4 }
                r4.bodyStream = r9     // Catch:{ IOException -> 0x01f4 }
                goto L_0x01bf
            L_0x01a7:
                java.lang.String r9 = "Content-Encoding"
                java.lang.String r1 = "deflate"
                boolean r9 = r4.hasHeaderWithValue(r9, r1)     // Catch:{ IOException -> 0x01f4 }
                if (r9 == 0) goto L_0x01bf
                java.util.zip.InflaterInputStream r9 = new java.util.zip.InflaterInputStream     // Catch:{ IOException -> 0x01f4 }
                java.io.InputStream r1 = r4.bodyStream     // Catch:{ IOException -> 0x01f4 }
                java.util.zip.Inflater r2 = new java.util.zip.Inflater     // Catch:{ IOException -> 0x01f4 }
                r2.<init>(r0)     // Catch:{ IOException -> 0x01f4 }
                r9.<init>(r1, r2)     // Catch:{ IOException -> 0x01f4 }
                r4.bodyStream = r9     // Catch:{ IOException -> 0x01f4 }
            L_0x01bf:
                java.io.InputStream r9 = r4.bodyStream     // Catch:{ IOException -> 0x01f4 }
                r1 = 32768(0x8000, float:4.5918E-41)
                int r2 = r8.maxBodySize()     // Catch:{ IOException -> 0x01f4 }
                org.jsoup.internal.ConstrainableInputStream r9 = org.jsoup.internal.ConstrainableInputStream.wrap(r9, r1, r2)     // Catch:{ IOException -> 0x01f4 }
                int r1 = r8.timeout()     // Catch:{ IOException -> 0x01f4 }
                long r1 = (long) r1     // Catch:{ IOException -> 0x01f4 }
                org.jsoup.internal.ConstrainableInputStream r9 = r9.timeout(r6, r1)     // Catch:{ IOException -> 0x01f4 }
                r4.bodyStream = r9     // Catch:{ IOException -> 0x01f4 }
                goto L_0x01de
            L_0x01d8:
                java.nio.ByteBuffer r9 = org.jsoup.helper.DataUtil.emptyByteBuffer()     // Catch:{ IOException -> 0x01f4 }
                r4.byteData = r9     // Catch:{ IOException -> 0x01f4 }
            L_0x01de:
                boolean unused = r8.executing = r3
                r4.executed = r0
                return r4
            L_0x01e4:
                org.jsoup.HttpStatusException r9 = new org.jsoup.HttpStatusException     // Catch:{ IOException -> 0x01f4 }
                java.lang.String r0 = "HTTP error fetching URL"
                java.net.URL r2 = r8.url()     // Catch:{ IOException -> 0x01f4 }
                java.lang.String r2 = r2.toString()     // Catch:{ IOException -> 0x01f4 }
                r9.<init>(r0, r1, r2)     // Catch:{ IOException -> 0x01f4 }
                throw r9     // Catch:{ IOException -> 0x01f4 }
            L_0x01f4:
                r9 = move-exception
                r5 = r4
                goto L_0x01fa
            L_0x01f7:
                r9 = move-exception
                goto L_0x0200
            L_0x01f9:
                r9 = move-exception
            L_0x01fa:
                if (r5 == 0) goto L_0x01ff
                r5.safeClose()     // Catch:{ all -> 0x01f7 }
            L_0x01ff:
                throw r9     // Catch:{ all -> 0x01f7 }
            L_0x0200:
                boolean unused = r8.executing = r3
                throw r9
            L_0x0204:
                r9 = move-exception
                monitor-exit(r8)     // Catch:{ all -> 0x0204 }
                throw r9
            */
            throw new UnsupportedOperationException("Method not decompiled: org.jsoup.helper.HttpConnection.Response.execute(org.jsoup.helper.HttpConnection$Request, org.jsoup.helper.HttpConnection$Response):org.jsoup.helper.HttpConnection$Response");
        }

        public int statusCode() {
            return this.statusCode;
        }

        public String statusMessage() {
            return this.statusMessage;
        }

        public String charset() {
            return this.charset;
        }

        public Response charset(String str) {
            this.charset = str;
            return this;
        }

        public String contentType() {
            return this.contentType;
        }

        public Document parse() throws IOException {
            Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before parsing response");
            if (this.byteData != null) {
                this.bodyStream = new ByteArrayInputStream(this.byteData.array());
                this.inputStreamRead = false;
            }
            Validate.isFalse(this.inputStreamRead, "Input stream already read and parsed, cannot re-read.");
            Document parseInputStream = DataUtil.parseInputStream(this.bodyStream, this.charset, this.url.toExternalForm(), this.req.parser());
            parseInputStream.connection(new HttpConnection(this.req, this));
            this.charset = parseInputStream.outputSettings().charset().name();
            this.inputStreamRead = true;
            safeClose();
            return parseInputStream;
        }

        private void prepareByteData() {
            Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before getting response body");
            if (this.bodyStream != null && this.byteData == null) {
                Validate.isFalse(this.inputStreamRead, "Request has already been read (with .parse())");
                try {
                    this.byteData = DataUtil.readToByteBuffer(this.bodyStream, this.req.maxBodySize());
                    this.inputStreamRead = true;
                    safeClose();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                } catch (Throwable th) {
                    this.inputStreamRead = true;
                    safeClose();
                    throw th;
                }
            }
        }

        public String body() {
            prepareByteData();
            Validate.notNull(this.byteData);
            String str = this.charset;
            String charBuffer = (str == null ? DataUtil.UTF_8 : Charset.forName(str)).decode(this.byteData).toString();
            this.byteData.rewind();
            return charBuffer;
        }

        public byte[] bodyAsBytes() {
            prepareByteData();
            Validate.notNull(this.byteData);
            return this.byteData.array();
        }

        public Connection.Response bufferUp() {
            prepareByteData();
            return this;
        }

        public BufferedInputStream bodyStream() {
            Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before getting response body");
            Validate.isFalse(this.inputStreamRead, "Request has already been read");
            this.inputStreamRead = true;
            return ConstrainableInputStream.wrap(this.bodyStream, 32768, this.req.maxBodySize());
        }

        private static HttpURLConnection createConnection(Request request) throws IOException {
            URLConnection uRLConnection;
            Proxy proxy = request.proxy();
            if (proxy == null) {
                uRLConnection = request.url().openConnection();
            } else {
                uRLConnection = request.url().openConnection(proxy);
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) uRLConnection;
            httpURLConnection.setRequestMethod(request.method().name());
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setConnectTimeout(request.timeout());
            httpURLConnection.setReadTimeout(request.timeout() / 2);
            if (request.sslSocketFactory() != null && (httpURLConnection instanceof HttpsURLConnection)) {
                ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(request.sslSocketFactory());
            }
            if (request.method().hasBody()) {
                httpURLConnection.setDoOutput(true);
            }
            CookieUtil.applyCookiesToRequest(request, httpURLConnection);
            for (Map.Entry entry : request.multiHeaders().entrySet()) {
                for (String addRequestProperty : (List) entry.getValue()) {
                    httpURLConnection.addRequestProperty((String) entry.getKey(), addRequestProperty);
                }
            }
            return httpURLConnection;
        }

        private void safeClose() {
            InputStream inputStream = this.bodyStream;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException unused) {
                } catch (Throwable th) {
                    this.bodyStream = null;
                    throw th;
                }
                this.bodyStream = null;
            }
            HttpURLConnection httpURLConnection = this.conn;
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
                this.conn = null;
            }
        }

        private Response(HttpURLConnection httpURLConnection, Request request, @Nullable Response response) throws IOException {
            super();
            this.executed = false;
            this.inputStreamRead = false;
            this.numRedirects = 0;
            this.conn = httpURLConnection;
            this.req = request;
            this.method = Connection.Method.valueOf(httpURLConnection.getRequestMethod());
            this.url = httpURLConnection.getURL();
            this.statusCode = httpURLConnection.getResponseCode();
            this.statusMessage = httpURLConnection.getResponseMessage();
            this.contentType = httpURLConnection.getContentType();
            LinkedHashMap<String, List<String>> createHeaderMap = createHeaderMap(httpURLConnection);
            processResponseHeaders(createHeaderMap);
            CookieUtil.storeCookies(request, this.url, createHeaderMap);
            if (response != null) {
                for (Map.Entry entry : response.cookies().entrySet()) {
                    if (!hasCookie((String) entry.getKey())) {
                        cookie((String) entry.getKey(), (String) entry.getValue());
                    }
                }
                response.safeClose();
                int i = response.numRedirects + 1;
                this.numRedirects = i;
                if (i >= 20) {
                    throw new IOException(String.format("Too many redirects occurred trying to load URL %s", new Object[]{response.url()}));
                }
            }
        }

        private static LinkedHashMap<String, List<String>> createHeaderMap(HttpURLConnection httpURLConnection) {
            LinkedHashMap<String, List<String>> linkedHashMap = new LinkedHashMap<>();
            int i = 0;
            while (true) {
                String headerFieldKey = httpURLConnection.getHeaderFieldKey(i);
                String headerField = httpURLConnection.getHeaderField(i);
                if (headerFieldKey == null && headerField == null) {
                    return linkedHashMap;
                }
                i++;
                if (!(headerFieldKey == null || headerField == null)) {
                    if (linkedHashMap.containsKey(headerFieldKey)) {
                        linkedHashMap.get(headerFieldKey).add(headerField);
                    } else {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(headerField);
                        linkedHashMap.put(headerFieldKey, arrayList);
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void processResponseHeaders(Map<String, List<String>> map) {
            for (Map.Entry next : map.entrySet()) {
                String str = (String) next.getKey();
                if (str != null) {
                    List<String> list = (List) next.getValue();
                    if (str.equalsIgnoreCase("Set-Cookie")) {
                        for (String str2 : list) {
                            if (str2 != null) {
                                TokenQueue tokenQueue = new TokenQueue(str2);
                                String trim = tokenQueue.chompTo(SimpleComparison.EQUAL_TO_OPERATION).trim();
                                String trim2 = tokenQueue.consumeTo(";").trim();
                                if (trim.length() > 0 && !this.cookies.containsKey(trim)) {
                                    cookie(trim, trim2);
                                }
                            }
                        }
                    }
                    for (String addHeader : list) {
                        addHeader(str, addHeader);
                    }
                }
            }
        }

        @Nullable
        private static String setOutputContentType(Connection.Request request) {
            String header = request.header(HttpConnection.CONTENT_TYPE);
            if (header != null) {
                if (header.contains(HttpConnection.MULTIPART_FORM_DATA) && !header.contains("boundary")) {
                    String mimeBoundary = DataUtil.mimeBoundary();
                    request.header(HttpConnection.CONTENT_TYPE, "multipart/form-data; boundary=" + mimeBoundary);
                    return mimeBoundary;
                }
            } else if (HttpConnection.needsMultipart(request)) {
                String mimeBoundary2 = DataUtil.mimeBoundary();
                request.header(HttpConnection.CONTENT_TYPE, "multipart/form-data; boundary=" + mimeBoundary2);
                return mimeBoundary2;
            } else {
                request.header(HttpConnection.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=" + request.postDataCharset());
            }
            return null;
        }

        private static void writePost(Connection.Request request, OutputStream outputStream, @Nullable String str) throws IOException {
            Collection<Connection.KeyVal> data = request.data();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName(request.postDataCharset())));
            if (str != null) {
                for (Connection.KeyVal next : data) {
                    bufferedWriter.write("--");
                    bufferedWriter.write(str);
                    bufferedWriter.write(HTTP.CRLF);
                    bufferedWriter.write("Content-Disposition: form-data; name=\"");
                    bufferedWriter.write(HttpConnection.encodeMimeName(next.key()));
                    bufferedWriter.write(AngleFormat.STR_SEC_SYMBOL);
                    InputStream inputStream = next.inputStream();
                    if (inputStream != null) {
                        bufferedWriter.write("; filename=\"");
                        bufferedWriter.write(HttpConnection.encodeMimeName(next.value()));
                        bufferedWriter.write("\"\r\nContent-Type: ");
                        String contentType2 = next.contentType();
                        if (contentType2 == null) {
                            contentType2 = HttpConnection.DefaultUploadType;
                        }
                        bufferedWriter.write(contentType2);
                        bufferedWriter.write("\r\n\r\n");
                        bufferedWriter.flush();
                        DataUtil.crossStreams(inputStream, outputStream);
                        outputStream.flush();
                    } else {
                        bufferedWriter.write("\r\n\r\n");
                        bufferedWriter.write(next.value());
                    }
                    bufferedWriter.write(HTTP.CRLF);
                }
                bufferedWriter.write("--");
                bufferedWriter.write(str);
                bufferedWriter.write("--");
            } else {
                String requestBody = request.requestBody();
                if (requestBody != null) {
                    bufferedWriter.write(requestBody);
                } else {
                    boolean z = true;
                    for (Connection.KeyVal next2 : data) {
                        if (!z) {
                            bufferedWriter.append(Typography.amp);
                        } else {
                            z = false;
                        }
                        bufferedWriter.write(URLEncoder.encode(next2.key(), request.postDataCharset()));
                        bufferedWriter.write(61);
                        bufferedWriter.write(URLEncoder.encode(next2.value(), request.postDataCharset()));
                    }
                }
            }
            bufferedWriter.close();
        }

        private static void serialiseRequestUrl(Connection.Request request) throws IOException {
            boolean z;
            URL url = request.url();
            StringBuilder borrowBuilder = StringUtil.borrowBuilder();
            borrowBuilder.append(url.getProtocol());
            borrowBuilder.append("://");
            borrowBuilder.append(url.getAuthority());
            borrowBuilder.append(url.getPath());
            borrowBuilder.append("?");
            if (url.getQuery() != null) {
                borrowBuilder.append(url.getQuery());
                z = false;
            } else {
                z = true;
            }
            for (Connection.KeyVal next : request.data()) {
                Validate.isFalse(next.hasInputStream(), "InputStream data not supported in URL query string.");
                if (!z) {
                    borrowBuilder.append(Typography.amp);
                } else {
                    z = false;
                }
                borrowBuilder.append(URLEncoder.encode(next.key(), DataUtil.defaultCharsetName));
                borrowBuilder.append(Tag.KEY_VALUE_SEPARATOR);
                borrowBuilder.append(URLEncoder.encode(next.value(), DataUtil.defaultCharsetName));
            }
            request.url(new URL(StringUtil.releaseBuilder(borrowBuilder)));
            request.data().clear();
        }
    }

    /* access modifiers changed from: private */
    public static boolean needsMultipart(Connection.Request request) {
        for (Connection.KeyVal hasInputStream : request.data()) {
            if (hasInputStream.hasInputStream()) {
                return true;
            }
        }
        return false;
    }

    public static class KeyVal implements Connection.KeyVal {
        @Nullable
        private String contentType;
        private String key;
        @Nullable
        private InputStream stream;
        private String value;

        public static KeyVal create(String str, String str2) {
            return new KeyVal(str, str2);
        }

        public static KeyVal create(String str, String str2, InputStream inputStream) {
            return new KeyVal(str, str2).inputStream(inputStream);
        }

        private KeyVal(String str, String str2) {
            Validate.notEmptyParam(str, DatabaseFileArchive.COLUMN_KEY);
            Validate.notNullParam(str2, DataColumnConstraints.COLUMN_VALUE);
            this.key = str;
            this.value = str2;
        }

        public KeyVal key(String str) {
            Validate.notEmptyParam(str, DatabaseFileArchive.COLUMN_KEY);
            this.key = str;
            return this;
        }

        public String key() {
            return this.key;
        }

        public KeyVal value(String str) {
            Validate.notNullParam(str, DataColumnConstraints.COLUMN_VALUE);
            this.value = str;
            return this;
        }

        public String value() {
            return this.value;
        }

        public KeyVal inputStream(InputStream inputStream) {
            Validate.notNullParam(this.value, "inputStream");
            this.stream = inputStream;
            return this;
        }

        public InputStream inputStream() {
            return this.stream;
        }

        public boolean hasInputStream() {
            return this.stream != null;
        }

        public Connection.KeyVal contentType(String str) {
            Validate.notEmpty(str);
            this.contentType = str;
            return this;
        }

        public String contentType() {
            return this.contentType;
        }

        public String toString() {
            return this.key + SimpleComparison.EQUAL_TO_OPERATION + this.value;
        }
    }
}
