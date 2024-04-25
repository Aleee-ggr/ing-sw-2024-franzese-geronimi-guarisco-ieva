package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.threads.ThreadMessage;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.messages.*;

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
public class ClientHandler implements Runnable {
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
            try {
                while (!this.socket.isClosed()) {
                    Message message = (Message) input.readObject();
                    handleMessage(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                try {
                    input.close();
                    output.close();
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing connection" + e.getMessage());
                }
            }

    }

    private void handleMessage(Message message) {

        if (message instanceof SocketClientCreateGameMessage) {
            Server.createGame(((SocketClientCreateGameMessage) message).getNumPlayers());
            return;
        }
        if (message instanceof SocketClientJoinGameMessage) {
            ThreadMessage threadMessage = ThreadMessage.join(message.getUsername());
            return;
        }
        if (message instanceof SocketClientPlaceCardMessage) {
            ThreadMessage threadMessage = ThreadMessage.placeCard(
                    message.getUsername(),
                    ((SocketClientPlaceCardMessage) message).getCoordinates(),
                    ((SocketClientPlaceCardMessage) message).getCardId()
            );
            return;
        }
        if (message instanceof SocketClientDrawCardMessage) {
            ThreadMessage threadMessage = ThreadMessage.draw(
                    message.getUsername(),
                    ((SocketClientDrawCardMessage) message).getPosition()
            );
            return;
        }
        if (message instanceof SocketValidateCredentialsMessage) {
            boolean isValid = Server.isValidPlayer(
                    message.getUsername(),
                    ((SocketValidateCredentialsMessage) message).getPassword()
            );
        }
    }
}
