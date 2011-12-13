package nu.mrpi.wordfeudapi.domain;

import com.google.gson.Gson;

/**
 * @author Pierre Ingmansson (pierre@ingmansson.se)
 */
public class User{
    private String username;
    private int id;
    private String email;
    private String sessionId;

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public static User fromJson(String jsonObject) {
        return new Gson().fromJson(jsonObject, User.class);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
