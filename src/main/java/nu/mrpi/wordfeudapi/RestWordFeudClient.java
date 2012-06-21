package nu.mrpi.wordfeudapi;

import java.io.*;
import java.util.HashMap;

import nu.mrpi.util.SHA1;
import nu.mrpi.wordfeudapi.domain.Board;
import nu.mrpi.wordfeudapi.domain.BoardType;
import nu.mrpi.wordfeudapi.domain.Game;
import nu.mrpi.wordfeudapi.domain.Notifications;
import nu.mrpi.wordfeudapi.domain.PlaceResult;
import nu.mrpi.wordfeudapi.domain.RuleSet;
import nu.mrpi.wordfeudapi.domain.Status;
import nu.mrpi.wordfeudapi.domain.SwapResult;
import nu.mrpi.wordfeudapi.domain.Tile;
import nu.mrpi.wordfeudapi.domain.TileMove;
import nu.mrpi.wordfeudapi.domain.User;
import nu.mrpi.wordfeudapi.exception.WordFeudException;
import nu.mrpi.wordfeudapi.exception.WordFeudLoginRequiredException;
import nu.mrpi.wordfeudapi.http.ApacheHttpClientCommunicator;
import nu.mrpi.wordfeudapi.http.HttpCommunicator;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import sun.misc.BASE64Encoder;

/**
 * @author Pierre Ingmansson
 */
public class RestWordFeudClient implements WordFeudClient {
    private User loggedInUser = null;

    private HttpCommunicator wordFeudHttpCommunicator;

    public RestWordFeudClient(HttpCommunicator wordFeudHttpCommunicator) {
        this.wordFeudHttpCommunicator = wordFeudHttpCommunicator;
    }

    public RestWordFeudClient() {
        this(new ApacheHttpClientCommunicator());
    }

    @Override
    public void useSessionId(final String sessionId) {
        wordFeudHttpCommunicator.setSessionId(sessionId);
        this.loggedInUser = new User();
        loggedInUser.setSessionId(sessionId);
    }

    @Override
    public User logon(final String email, final String password) {
        final String path = "/user/login/email/";

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("email", email);
        parameters.put("password", encodePassword(password));

        final JSONObject json = callAPI(path, toJSON(parameters));

        try {
            loggedInUser = User.fromJson(json.getString("content"));
            loggedInUser.setSessionId(wordFeudHttpCommunicator.getSessionId());

            return loggedInUser;
        } catch (JSONException e) {
            throw new RuntimeException("Could not deserialize JSON", e);
        }
    }

    /**
     * Invite somebody to a game
     * 
     * @param username
     *            The user to invite
     * @param ruleset
     *            The ruleset to use for the game
     * @param boardType
     *            The board type
     * @return The WordFeud API response
     */
    @Override
    public String invite(final String username, final RuleSet ruleset, final BoardType boardType) {
        final String path = "/invite/new/";

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("invitee", username);
        parameters.put("ruleset", ruleset.getApiIntRepresentation());
        parameters.put("board_type", boardType.getApiStringRepresentation());

        return callAPI(path, toJSON(parameters)).toString();
        // Errors can be: 'duplicate_invite', 'invalid_ruleset', 'invalid_board_type', 'user_not_found'
    }

    /**
     * Accept an invite
     * 
     * @param inviteId
     *            The invite ID
     * @return The id of the game that just started
     */
    @Override
    public int acceptInvite(final int inviteId) {
        // 'access_denied'
        final String path = "/invite/" + inviteId + "/accept/";

        try {
            return callAPI(path).getJSONObject("content").getInt("id");
        } catch (JSONException e) {
            throw new RuntimeException("Could not deserialize JSON", e);
        }
    }

    /**
     * Reject an invite
     * 
     * @param inviteId
     *            The invite ID
     * @return The WordFeud API response
     */
    @Override
    public String rejectInvite(final int inviteId) {
        final String path = "/invite/" + inviteId + "/reject/";

        return callAPI(path).toString();
    }

    /**
     * Get the pending notifications of the current user
     * 
     * @return The WordFeud API response
     */
    @Override
    public Notifications getNotifications() {
        final String path = "/user/notifications/";

        final JSONObject json = callAPI(path);
        try {
            return Notifications.fromJson(json.getString("content"));
        } catch (JSONException e) {
            throw new RuntimeException("Could not deserialize JSON", e);
        }
    }

    @Override
    public Game[] getGames() {
        final String path = "/user/games/";

        final JSONObject json = callAPI(path);
        try {
            return Game.fromJsonArray(json.getJSONObject("content").getString("games"), loggedInUser);
        } catch (JSONException e) {
            throw new RuntimeException("Could not deserialize JSON", e);
        }
    }

    @Override
    public Game getGame(final long gameId) {
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
     * @param game
     *            The game to find the board for
     * @return The board
     */
    @Override
    public Board getBoard(final Game game) {
        return getBoard(game.getBoard());
    }

    /**
     * Get a specific board
     * 
     * @param boardId
     *            The id of the board to get
     * @return The WordFeud API response
     */
    @Override
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
    @Override
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
     * Make a move for the given game
     * 
     * @param game
     *            The game to make a move for
     * @param tileMove
     *            The move to make
     * @return The placement result
     */
    @Override
    public PlaceResult makeMove(final Game game, final TileMove tileMove) {
        return place(game.getId(), game.getRuleset(), tileMove.getTiles(), tileMove.getWord().toCharArray());
    }

    /**
     * Place a word on the board.
     * 
     * @param gameId
     *            The ID of the game to place the word on
     * @param ruleset
     *            The ruleset the game is using
     * @param tiles
     *            The tiles to place (only the tiles to be placed = tiles from the users rack)
     * @param word
     *            The whole word to place (including tiles already on the board)
     * @return The placement result
     */
    @Override
    public PlaceResult place(final long gameId, final RuleSet ruleset, final Tile[] tiles, final char[] word) {
        final String path = "/game/" + gameId + "/move/";

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("move", Tile.convert(tiles));
        parameters.put("ruleset", ruleset.getApiIntRepresentation());
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
     * @param game
     *            The game to pass
     * @return The WordFeud API response
     */
    @Override
    public String pass(final Game game) {
        return pass(game.getId());
    }

    /**
     * Pass a game
     * 
     * @param gameId
     *            The id of the game
     * @return The WordFeud API response
     */
    @Override
    public String pass(final long gameId) {
        final String path = "/game/" + gameId + "/pass/";

        return callAPI(path).toString();
    }

    /**
     * Swap letters in given game
     * 
     * @param game
     *            The game to swap tiles for
     * @param tiles
     *            The letters to swap
     * @return The result of the swap
     */
    @Override
    public SwapResult swap(final Game game, final char[] tiles) {
        return swap(game.getId(), tiles);
    }

    /**
     * Swap tiles in given game
     * 
     * @param gameId
     *            The id of the game
     * @param tiles
     *            The tiles to swap
     * @return The result of the swap
     */
    @Override
    public SwapResult swap(final long gameId, final char[] tiles) {
        final String path = "/game/" + gameId + "/swap/";

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("tiles", tiles);

        final JSONObject json = callAPI(path, toJSON(parameters));
        try {
            return SwapResult.fromJson(json.getString("content"));
        } catch (JSONException e) {
            throw new RuntimeException("Could not deserialize JSON", e);
        }
    }

    /**
     * Send a chat message to a game
     * 
     * @param game
     *            The game to send chat on
     * @param message
     *            The message to send
     * @return The WordFeud API response
     */
    @Override
    public String chat(final Game game, final String message) {
        return chat(game.getId(), message);
    }

    /**
     * Send a chat message to a game
     * 
     * @param gameId
     *            The game ID of the game to send chat on
     * @param message
     *            The message to send
     * @return The WordFeud API response
     */
    @Override
    public String chat(final long gameId, final String message) {
        final String path = "/game/" + gameId + "/chat/send/";

        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("message", message);

        final JSONObject json = callAPI(path, toJSON(parameters));
        return json.toString();
    }

    /**
     * Get all chat messages from a specific game
     * 
     * @param game
     *            The game to fetch chat messages from
     * @return The WordFeud API response
     */
    @Override
    public String getChatMessages(final Game game) {
        return getChatMessages(game.getId());
    }

    /**
     * Get all chat messages from a specific game
     * 
     * @param gameId
     *            The game ID
     * @return The WordFeud API response
     */
    @Override
    public String getChatMessages(final long gameId) {
        final String path = "/game/" + gameId + "/chat/";

        final JSONObject json = callAPI(path);
        return json.toString();
    }

    /**
     * Upload a new avatar of the currently logged in user
     * @param file The JPEG file (I think it has to be 60x60 as well..)
     * @return The WordFeud API response
     * @throws IOException If something bad happened reading the file
     */
    @Override
    public String uploadAvatar(File file) throws IOException {
        return uploadAvatar(readImage(file));
    }

    /**
     * Upload a new avatar of the currently logged in user
     * 
     * @param imageData
     *            The image data
     * @return The WordFeud API response
     */
    @Override
    public String uploadAvatar(byte[] imageData) {
        final String path = "/user/avatar/upload/";

        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("image_data", new BASE64Encoder().encode(imageData));

        return callAPI(path, toJSON(parameters)).toString();
    }

    /**
     * Create a new account
     * 
     * @param username
     *            The username of the new user
     * @param email
     *            The email of the new user
     * @param password
     *            The password of the new user
     * @return The WordFeud API response
     */
    @Override
    public String createAccount(final String username, final String email, final String password) {
        final String path = "/user/create/";
        final HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("username", username);
        parameters.put("email", email);
        parameters.put("password", encodePassword(password));

        return callAPI(path, toJSON(parameters)).toString();
    }

    private JSONObject callAPI(final String path) {
        return callAPI(path, "");
    }

    private JSONObject callAPI(final String path, final String data) {
        JSONObject response = wordFeudHttpCommunicator.call(path, data);

        try {
            String status = response.getString("status");

            if (!"success".equals(status)) {
                String type = response.getJSONObject("content").getString("type");
                if ("login_required".equals(type)) {
                    throw new WordFeudLoginRequiredException("Login is required", this);
                }
                throw new WordFeudException("Error when calling API: " + type);
            }
        } catch (JSONException e) {
            throw new RuntimeException("Could not parse JSON");
        }

        return response;
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

    private static byte[] readImage(File file) throws IOException {
        InputStream is = null;
        try {
            is = new FileInputStream(file);

            return IOUtils.toByteArray(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
