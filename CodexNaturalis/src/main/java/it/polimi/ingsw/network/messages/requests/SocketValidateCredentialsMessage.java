package it.polimi.ingsw.network.messages.requests;

/**
 * Represents a message from a client to validate a player's credentials
 * via socket network communication.
 * This class extends the GenericRequestMessage class and contains the username and password of the player
 * whose credentials are being validated.
 */
public class SocketValidateCredentialsMessage extends GenericRequestMessage {
    private final String password;

    /**
     * Constructs a new SocketValidateCredentialsMessage with the specified username and password.
     *
     * @param username The username of the player.
     * @param password The password of the player.
     */
    public SocketValidateCredentialsMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the password of the player whose credentials are being validated.
     * @return The password of the player.
     */
    public String getPassword() {
        return this.password;
    }
}
