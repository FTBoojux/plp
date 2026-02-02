package org.example.json;


public class User {
    private String username;
    private String userId;
    private Detail detail;

    public User(String username, String userId, Detail detail) {
        this.username = username;
        this.userId = userId;
        this.detail = detail;
    }
    public User(){

    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }

    public Detail getDetail() {
        return detail;
    }

    @Override
    public String toString() {
        return "User[" +
               "username=" + username + ", " +
               "userId=" + userId + ", " +
               "detail=" + detail + ']';
    }
}
