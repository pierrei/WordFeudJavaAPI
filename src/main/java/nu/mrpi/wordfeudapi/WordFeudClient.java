package nu.mrpi.wordfeudapi;

import nu.mrpi.util.SHA1;
import nu.mrpi.wordfeudapi.domain.*;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Pierre Ingmansson
 */
public class WordFeudClient {


    enum RuleSet {
        American,
        Norwegian,
        Dutch,
        Danish,
        Swedish,
        English,
        Spanish,
        French;
    }


    enum BoardType {
        Normal,
        Random;
    }
    private String sessionId;


    private User loggedInUser = null;
    private HttpClient httpClient;
    private static final Pattern SESSION_ID_COOKIE_PATTERN = Pattern.compile("sessionid=(.*?);");

    public WordFeudClient() {
        httpClient = new DefaultHttpClient();
    }

    public void useSessionId(String sessionId) {
        this.sessionId = sessionId;
        this.loggedInUser = new User();
        loggedInUser.setSessionId(sessionId);
    }

    public User logon(String email, String password) {
        String path = "/user/login/email/";
        String data = "{\"email\":\"" + email + "\",\"password\":\"" + encodePassword(password) + "\"}";

        JSONObject json = callAPI(path, data);

        try {
            loggedInUser = User.fromJson(json.getString("content"));
            loggedInUser.setSessionId(sessionId);

            return loggedInUser;
        } catch (JSONException e) {
            throw new RuntimeException("Could not deserialize JSON", e);
        }
    }

    /**
     * Accept an invite.
     *
     * @param inviteID Invite ID
     */
    public String acceptInvite(int inviteID) {
        // 'access_denied'
        String path = "/invite/" + inviteID + "/accept/";

        return callAPI(path).toString();
    }

    /**
     * Reject an invite.
     *
     * @param inviteID Invite ID
     */
    public String rejectInvite(int inviteID)
    {
        // 'access_denied'
        String path = "/invite/" + inviteID + "/reject/";

        return callAPI(path).toString();
    }

    /**
     * Gets notifications!
     *
     * @return The notifications
     */
    public String getNotifications()
    {
        String path = "/user/notifications/";

        return callAPI(path).toString();
    }

    public Game[] getGames() {
        String path = "/user/games/";

        JSONObject json = callAPI(path);
        try {
            return Game.fromJsonArray(json.getJSONObject("content").getString("games"), loggedInUser);
        } catch (JSONException e) {
            throw new RuntimeException("Could not deserialize JSON", e);
        }
    }

    public Game getGame(int gameId) {
        String path = "/game/" + gameId + "/";

        JSONObject json = callAPI(path);
        try {
            return Game.fromJson(json.getJSONObject("content").getString("game"), loggedInUser);
        } catch (JSONException e) {
            throw new RuntimeException("Could not deserialize JSON", e);
        }
    }

    public String getBoard(int boardId) {
        String path = "/board/" + boardId + "/";

        return callAPI(path).toString();
    }

    public Status getStatus() {
        String path = "/user/status/";

        JSONObject json = callAPI(path);
        try {
            return Status.fromJson(json.getString("content"));
        } catch (JSONException e) {
            throw new RuntimeException("Could not deserialize JSON", e);
        }
    }

    /**
     * Place a solution for the given game
     * @param game The game to place solution for
     * @param solution The solution to place
     * @return The WordFeud API response
     */
    public String place(Game game, Solution solution) {
        return place(game.getId(), game.getRuleset(), solution.getTiles(), solution.getWord().toCharArray());
    }

    /**
     * Place a word on the board.
     * @param gameID The ID of the game to place the word on
     * @param ruleset The ruleset the game is using
     * @param tiles The tiles to place (only the tiles to be placed = tiles from the users rack)
     * @param word The whole word to place (including tiles already on the board)
     * @return The WordFeud API response
     */
    public String place(int gameID, int ruleset, Tile[] tiles, char[] word) {
        String path = "/game/" + gameID + "/move/";

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("move", Tile.convert(tiles));
        parameters.put("ruleset", ruleset);
        parameters.put("word", word);

        return callAPI(path, toJSON(parameters)).toString();
    }

    public String createAccount(String username, String email, String password) {
        String path = "/user/create/";
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("username", username);
        parameters.put("email", email);
        parameters.put("password", encodePassword(password));

        return callAPI(path, toJSON(parameters)).toString();
    }

    private String toJSON(HashMap<String, ?> parameters) {
        return new JSONObject(parameters).toString();
    }

    private String encodePassword(String password) {
        try {
            return SHA1.sha1(password + "JarJarBinks9");
        } catch (Exception e) {
            throw new RuntimeException("Error when encoding password", e);
        }
    }

    private JSONObject callAPI(String path) {
        return callAPI(path, "");
    }

    private JSONObject callAPI(String path, String data) {
        try {
            HttpPost post = new HttpPost("http://game05.wordfeud.com/wf" + path);
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Accept", "application/json");
            HttpEntity entity = new StringEntity(data, "UTF-8");
            post.setEntity(entity);

            if (sessionId != null) {
                post.addHeader("Cookie", "sessiond=" + sessionId);
            }

            HttpResponse response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() == 200) {
                Header[] cookies = response.getHeaders("Set-Cookie");
                if (cookies != null && cookies.length > 0) {
                    sessionId = extractSessionIdFromCookie(cookies[0]);
                }
                String responseString = IOUtils.toString(response.getEntity().getContent());
                JSONObject jsonObject = new JSONObject(responseString);

                if (!"success".equals(jsonObject.getString("status"))) {
                    throw new WordFeudException("Error when calling API: " + jsonObject.getJSONObject("content").getString("type"));
                }

                return jsonObject;
            } else {
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

    private String extractSessionIdFromCookie(Header cookie) {
        String cookieValue = cookie.getValue();
        Matcher matcher = SESSION_ID_COOKIE_PATTERN.matcher(cookieValue);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
