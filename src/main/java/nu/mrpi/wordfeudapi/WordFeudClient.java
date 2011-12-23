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
        French
    }

    enum BoardType {
        Normal,
        Random
    }

    private String sessionId;

    private User loggedInUser = null;
    private final HttpClient httpClient;
    private static final Pattern SESSION_ID_COOKIE_PATTERN = Pattern.compile("sessionid=(.*?);");

    public WordFeudClient() {
        httpClient = new DefaultHttpClient();
    }

    public void useSessionId(final String sessionId) {
        this.sessionId = sessionId;
        this.loggedInUser = new User();
        loggedInUser.setSessionId(sessionId);
    }

    public User logon(final String email, final String password) {
        final String path = "/user/login/email/";
        final String data = "{\"email\":\"" + email + "\",\"password\":\"" + encodePassword(password) + "\"}";

        final JSONObject json = callAPI(path, data);

        try {
            loggedInUser = User.fromJson(json.getString("content"));
            loggedInUser.setSessionId(sessionId);

            return loggedInUser;
        } catch (JSONException e) {
            throw new RuntimeException("Could not deserialize JSON", e);
        }
    }

    /**
     * Accept an invite
     *
     * @param inviteID The invite ID
     * @return The WordFeud API response
     */
    public String acceptInvite(final int inviteID) {
        // 'access_denied'
        final String path = "/invite/" + inviteID + "/accept/";

        return callAPI(path).toString();
    }

    /**
     * Reject an invite
     *
     * @param inviteId The invite ID
     * @return The WordFeud API response
     */
    public String rejectInvite(final int inviteId) {
        final String path = "/invite/" + inviteId + "/reject/";

        return callAPI(path).toString();
    }

    /**
     * Get the pending notifications of the current user
     *
     * @return The WordFeud API response
     */
    public String getNotifications() {
        final String path = "/user/notifications/";

        return callAPI(path).toString();
    }

    public Game[] getGames() {
        final String path = "/user/games/";

        final JSONObject json = callAPI(path);
        try {
            return Game.fromJsonArray(json.getJSONObject("content").getString("games"), loggedInUser);
        } catch (JSONException e) {
            throw new RuntimeException("Could not deserialize JSON", e);
        }
    }

    public Game getGame(final int gameId) {
        final String path = "/game/" + gameId + "/";

        final JSONObject json = callAPI(path);
        try {
            return Game.fromJson(json.getJSONObject("content").getString("game"), loggedInUser);
        } catch (JSONException e) {
            throw new RuntimeException("Could not deserialize JSON", e);
        }
    }


    /**
     * Get the board for a given game
     *
     * @param game The game to find the board for
     * @return The board
     */
    public Board getBoard(final Game game) {
        return getBoard(game.getBoard());
    }

    /**
     * Get a specific board
     *
     * @param boardId The id of the board to get
     * @return The WordFeud API response
     */
    public Board getBoard(final int boardId) {
        final String path = "/board/" + boardId + "/";

        final JSONObject json = callAPI(path);
        try {
            return Board.fromJson(json.getString("content"));
        } catch (JSONException e) {
            throw new RuntimeException("Could not deserialize JSON", e);
        }
    }

    /**
     * Get the status of the current user
     *
     * @return The status
     */
    public Status getStatus() {
        final String path = "/user/status/";

        final JSONObject json = callAPI(path);
        try {
            return Status.fromJson(json.getString("content"));
        } catch (JSONException e) {
            throw new RuntimeException("Could not deserialize JSON", e);
        }
    }

    /**
     * Place a solution for the given game
     *
     * @param game     The game to place solution for
     * @param solution The solution to place
     * @return The placement result
     */
    public PlaceResult place(final Game game, final Solution solution) {
        return place(game.getId(), game.getRuleset(), solution.getTiles(), solution.getWord().toCharArray());
    }

    /**
     * Place a word on the board.
     *
     * @param gameId  The ID of the game to place the word on
     * @param ruleset The ruleset the game is using
     * @param tiles   The tiles to place (only the tiles to be placed = tiles from the users rack)
     * @param word    The whole word to place (including tiles already on the board)
     * @return The placement result
     */
    public PlaceResult place(final int gameId, final int ruleset, final Tile[] tiles, final char[] word) {
        final String path = "/game/" + gameId + "/move/";

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("move", Tile.convert(tiles));
        parameters.put("ruleset", ruleset);
        parameters.put("word", word);

        final JSONObject json = callAPI(path, toJSON(parameters));
        try {
            return PlaceResult.fromJson(json.getString("content"));
        } catch (JSONException e) {
            throw new RuntimeException("Could not deserialize JSON", e);
        }
    }

    /**
     * Pass a game
     *
     * @param game The game to pass
     * @return The WordFeud API response
     */
    public String pass(final Game game) {
        return pass(game.getId());
    }

    /**
     * Pass a game
     *
     * @param gameId The id of the game
     * @return The WordFeud API response
     */
    public String pass(final int gameId) {
        final String path = "/game/" + gameId + "/pass/";

        return callAPI(path).toString();
    }

    /**
     * Swap letters in given game
     *
     * @param game             The game to swap tiles for
     * @param duplicateLetters The letters to swap
     * @return The WordFeud API response
     */
    public String swap(final Game game, final char[] duplicateLetters) {
        return swap(game.getId(), game.getRuleset(), duplicateLetters);
    }

    /**
     * Swap letters in given game
     *
     * @param gameId  The id of the game
     * @param ruleset The ruleset of the game
     * @param letters The letters to swap
     * @return The WordFeud API response
     */
    public String swap(final int gameId, final int ruleset, final char[] letters) {
        final String path = "/game/" + gameId + "/swap/";

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("swap", letters);
        parameters.put("ruleset", ruleset);

        final JSONObject json = callAPI(path, toJSON(parameters));
        return json.toString();
    }


    /**
     * Send a chat message to a game
     *
     * @param game    The game to send chat on
     * @param message The message to send
     * @return The WordFeud API response
     */
    public String chat(final Game game, final String message) {
        return chat(game.getId(), message);
    }

    /**
     * Send a chat message to a game
     *
     * @param gameId  The game ID of the game to send chat on
     * @param message The message to send
     * @return The WordFeud API response
     */
    public String chat(final int gameId, final String message) {
        final String path = "/game/" + gameId + "/chat/send/";

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("message", message);

        final JSONObject json = callAPI(path, toJSON(parameters));
        return json.toString();
    }

    /**
     * Create a new account
     *
     * @param username The username of the new user
     * @param email    The email of the new user
     * @param password The password of the new user
     * @return The WordFeud API response
     */
    public String createAccount(final String username, final String email, final String password) {
        final String path = "/user/create/";
        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("username", username);
        parameters.put("email", email);
        parameters.put("password", encodePassword(password));

        return callAPI(path, toJSON(parameters)).toString();
    }

    private String toJSON(final HashMap<String, ?> parameters) {
        return new JSONObject(parameters).toString();
    }

    private String encodePassword(final String password) {
        try {
            return SHA1.sha1(password + "JarJarBinks9");
        } catch (Exception e) {
            throw new RuntimeException("Error when encoding password", e);
        }
    }

    private JSONObject callAPI(final String path) {
        return callAPI(path, "");
    }

    private JSONObject callAPI(final String path, final String data) {
        try {
            final HttpPost post = new HttpPost("http://game05.wordfeud.com/wf" + path);
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Accept", "application/json");
            final HttpEntity entity = new StringEntity(data, "UTF-8");
            post.setEntity(entity);

            if (sessionId != null) {
                post.addHeader("Cookie", "sessiond=" + sessionId);
            }

            final HttpResponse response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() == 200) {
                final Header[] cookies = response.getHeaders("Set-Cookie");
                if (cookies != null && cookies.length > 0) {
                    sessionId = extractSessionIdFromCookie(cookies[0]);
                }
                final String responseString = IOUtils.toString(response.getEntity().getContent());
                final JSONObject jsonObject = new JSONObject(responseString);

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

    private String extractSessionIdFromCookie(final Header cookie) {
        final String cookieValue = cookie.getValue();
        final Matcher matcher = SESSION_ID_COOKIE_PATTERN.matcher(cookieValue);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
