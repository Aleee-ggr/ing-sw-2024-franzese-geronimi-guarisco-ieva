package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.threads.message.ThreadMessage;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.SocketClientCreateGameMessage;
import it.polimi.ingsw.network.messages.SocketClientJoinGameMessage;
import it.polimi.ingsw.network.messages.SocketClientLeaveGameMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

/**
 * The ClientHandler class manages the communication between a client and a server using sockets.
 * It provides methods to send and receive objects over the network.
 * @author Samuele Franzese
 */
public class ClientHandler extends Server implements Runnable {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Map<UUID, BlockingQueue<ThreadMessage>> threadMessages;

    /**
     * Constructs a new ClientHandler object with the specified socket connection.
     *
     * @param socket the socket connection to the client
     * @throws IOException if an I/O error occurs while initializing the input and output streams
     */
    public ClientHandler(Socket socket, Map<UUID, BlockingQueue<ThreadMessage>> threadMessages) throws IOException {
        this.socket = socket;
        this.input = new ObjectInputStream(socket.getInputStream());
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.threadMessages = threadMessages;
    }

    public void run() {
        while (!this.socket.isClosed()){
            try {
                Message message = (Message) input.readObject();
                if (message instanceof SocketClientCreateGameMessage) {
                    createGame(((SocketClientCreateGameMessage) message).getNumPlayers());
                } else if (message instanceof SocketClientJoinGameMessage) {
                    String threadMessage = ThreadMessage.join.formatted(message.getUsername());
                    sendMessage(((SocketClientJoinGameMessage) message).getGameUUID(), threadMessage, message.getUsername());
                } else if (message instanceof SocketClientLeaveGameMessage) {
                } else {
                }

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
