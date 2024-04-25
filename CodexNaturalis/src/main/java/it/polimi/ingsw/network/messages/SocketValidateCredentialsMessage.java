package it.polimi.ingsw.network.messages;

public class SocketValidateCredentialsMessage extends Message {
    private final String password;

    /**
     *
     * @param username the username of the player
     * @param password the password of the player
     */
    public SocketValidateCredentialsMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }
}
