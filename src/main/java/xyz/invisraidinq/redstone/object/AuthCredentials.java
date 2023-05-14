package xyz.invisraidinq.redstone.object;

public class AuthCredentials {
    private final String username;
    private final String password;

    private AuthCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static AuthCredentials of(String username, String password) {
        return new AuthCredentials(username, password);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
