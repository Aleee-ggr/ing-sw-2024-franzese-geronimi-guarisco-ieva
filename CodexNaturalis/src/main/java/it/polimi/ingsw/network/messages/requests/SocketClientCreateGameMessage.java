package it.polimi.ingsw.network.messages.requests;

/**
 * Class representing a message from a client to create a new game in the network communication via sockets.
 * This class extends the `GenericRequestMessage` class, inheriting the username field.
 * It is used when a client wants to create a new game.
 *
 * @author Samuele Franzese
 */
public class SocketClientCreateGameMessage extends GenericRequestMessage {
    private final int numPlayers;
    private final String gameName;

    /**
     * Constructor for the SocketClientCreateGameMessage class.
     *
     * @param numPlayers The number of players for the game.
     */
    public SocketClientCreateGameMessage(int numPlayers, String gameName) {
        this.numPlayers = numPlayers;
        this.gameName = gameName;
    }

    /**
     * Returns the number of players for the game to be created.
     *
     * @return The number of players.
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    public String getGameName() {
        return gameName;
    }
}
