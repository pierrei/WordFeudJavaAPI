package nu.mrpi.wordfeudapi;

import nu.mrpi.wordfeudapi.exception.WordFeudLoginRequiredException;
import nu.mrpi.wordfeudapi.exception.WordFeudException;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nu.mrpi.util.MathUtil.random;

/**
 * @author Pierre Ingmansson
 */
public class WordFeudHttpCommunicator {
    public static final String CONTENT_TYPE_JSON = "application/json";

    public static final String SESSION_COOKIE_NAME = "sessionid";

    private static final Pattern SESSION_ID_COOKIE_PATTERN = Pattern.compile(SESSION_COOKIE_NAME + "=(.*?);");

    private final HttpClient httpClient;

    private String sessionId;

    public WordFeudHttpCommunicator() {
        httpClient = createHttpClient();
    }

    private HttpClient createHttpClient() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(
                new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

        ClientConnectionManager cm = new ThreadSafeClientConnManager(schemeRegistry);
        HttpClient httpClient = new DefaultHttpClient(cm);

        final String proxyHost = System.getProperty("proxy.host");
        final String proxyPort = System.getProperty("proxy.port");
        if (proxyHost != null) {
            final HttpHost proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }

        return httpClient;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public JSONObject call(final String path) {
        return call(path, "");
    }

    public  JSONObject call(final String path, final String data) {
        try {
            final HttpPost post = createPost(path, data);

            if (sessionId != null) {
                post.addHeader("Cookie", SESSION_COOKIE_NAME + "=" + sessionId);
            }

            final HttpResponse response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return handleResponse(response);
            } else {
                EntityUtils.consume(response.getEntity());
                throw new WordFeudException("Got unexpected HTTP " + response.getStatusLine().getStatusCode() + ": " + response.toString());
            }

        } catch (IOException e) {
            throw new RuntimeException("Error when contacting WordFeud API", e);
        } catch (JSONException e) {
            throw new RuntimeException("Could not parse JSON", e);
        } finally {
            httpClient.getConnectionManager().closeExpiredConnections();
        }
    }

    private JSONObject handleResponse(final HttpResponse response) throws IOException, JSONException {
        checkCookieHeader(response);

        final JSONObject jsonObject = extractJsonFromResponse(response);

        String status = jsonObject.getString("status");

        if (!"success".equals(status)) {
            String type = jsonObject.getJSONObject("content").getString("type");
            if ("login_required".equals(type)) {
                throw new WordFeudLoginRequiredException("Login is required");
            }
            throw new WordFeudException("Error when calling API: " + type);
        }

        return jsonObject;
    }

    private JSONObject extractJsonFromResponse(final HttpResponse response) throws IOException, JSONException {
        final String responseString = EntityUtils.toString(response.getEntity());
        return new JSONObject(responseString);
    }

    private void checkCookieHeader(final HttpResponse response) {
        final Header[] cookies = response.getHeaders("Set-Cookie");
        if (cookies != null && cookies.length > 0) {
            sessionId = extractSessionIdFromCookie(cookies[0]);
        }
    }

    private String extractSessionIdFromCookie(final Header cookie) {
        final String cookieValue = cookie.getValue();
        final Matcher matcher = SESSION_ID_COOKIE_PATTERN.matcher(cookieValue);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private HttpPost createPost(final String path, final String data) throws UnsupportedEncodingException {
        final HttpPost post = new HttpPost("http://" + calculateHostName() + "/wf" + path);
        post.addHeader("Content-Type", CONTENT_TYPE_JSON);
        post.addHeader("Accept", CONTENT_TYPE_JSON);
        final HttpEntity entity = new StringEntity(data, "UTF-8");
        post.setEntity(entity);
        return post;
    }

    private String calculateHostName() {
        return "game0" + random(1, 7) + ".wordfeud.com";
    }
}
