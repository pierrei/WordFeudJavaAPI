package nu.mrpi.wordfeudapi.http;

import org.json.JSONObject;

/**
 * @author Pierre Ingmansson
 */
public interface HttpCommunicator {
    void setSessionId(String sessionId);

    String getSessionId();

    JSONObject call(String path, String data);
}
