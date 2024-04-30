package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.threads.ThreadMessage;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.messages.requests.*;
import it.polimi.ingsw.network.messages.responses.*;

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
                    GenericRequestMessage message = (GenericRequestMessage) input.readObject();
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

    private void handleMessage(GenericRequestMessage message) throws IOException {

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
            Server.sendMessage(((SocketClientDrawCardMessage) message).getGameUUID(), threadMessage);
            return;
        }

        if (message instanceof SocketValidateCredentialsMessage) {
            boolean isValid = Server.isValidPlayer(
                    message.getUsername(),
                    ((SocketValidateCredentialsMessage) message).getPassword()
            );
        }

        if (message instanceof SocketClientGetHandColorMessage) {
            GetHandColorResponseMessage response = new GetHandColorResponseMessage(Server.getHandColor(((SocketClientGetHandColorMessage) message).getGameUUID(), message.getUsername(), ((SocketClientGetHandColorMessage) message).getUsernameRequiredData()), ((SocketClientGetHandColorMessage) message).getUsernameRequiredData());
            sendResponse(response);
        }

        if (message instanceof SocketClientGetBackSideDecksMessage) {
            GetBackSideDecksResponseMessage response = new GetBackSideDecksResponseMessage(Server.getBackSideDecks(((SocketClientGetBackSideDecksMessage) message).getGameUUID(), message.getUsername()));
            sendResponse(response);
        }

        if (message instanceof SocketClientGetCommonObjectivesMessage) {
            GetCommonObjectivesResponseMessage response = new GetCommonObjectivesResponseMessage(Server.getCommonObjectives(((SocketClientGetCommonObjectivesMessage) message).getGameUUID(), message.getUsername()));
            sendResponse(response);
        }

        if (message instanceof SocketClientGetHandMessage) {
            GetHandResponseMessage response = new GetHandResponseMessage(Server.getHand(((SocketClientGetHandMessage) message).getGameUUID(), message.getUsername()));
            sendResponse(response);
        }

        if (message instanceof SocketClientGetPlayerBoard) {
            GetPlayerBoardResponseMessage response = new GetPlayerBoardResponseMessage(Server.getBoard(((SocketClientGetPlayerBoard) message).getGameUUID(), message.getUsername(), ((SocketClientGetPlayerBoard) message).getUsernameRequiredData()), ((SocketClientGetPlayerBoard) message).getUsernameRequiredData());
            sendResponse(response);
        }

        if (message instanceof SocketClientGetPlayerResourcesMessage) {
            GetPlayerResourcesResponseMessage response = new GetPlayerResourcesResponseMessage(Server.getPlayerResources(((SocketClientGetPlayerResourcesMessage) message).getGameUUID(), message.getUsername(), ((SocketClientGetPlayerResourcesMessage) message).getUsernameRequiredData()), ((SocketClientGetPlayerResourcesMessage) message).getUsernameRequiredData());
            sendResponse(response);
        }

        if (message instanceof SocketClientGetValidPlacementsMessage) {
            GetValidPlacementsResponseMessage response = new GetValidPlacementsResponseMessage(Server.getValidPlacements(((SocketClientGetValidPlacementsMessage) message).getGameUUID(), message.getUsername()));
            sendResponse(response);
        }

        if (message instanceof SocketClientGetVisibleCardsMessage) {
            GetVisibleCardsResponseMessage response = new GetVisibleCardsResponseMessage(Server.getVisibleCards(((SocketClientGetVisibleCardsMessage) message).getGameUUID(), message.getUsername()));
            sendResponse(response);
        }

        if (message instanceof SocketClientGetStartingObjectivesMessage) {
            GetStartingObjectivesResponseMessage response = new GetStartingObjectivesResponseMessage(Server.getStartingObjectives(((SocketClientGetStartingObjectivesMessage) message).getGameUUID(), message.getUsername()));
            sendResponse(response);
        }

        if (message instanceof SocketClientChooseStartingObjective) {
            ChooseStartingObjectiveResponseMessage response = new ChooseStartingObjectiveResponseMessage(Server.choosePersonalObjective(((SocketClientChooseStartingObjective) message).getGameUUID(), message.getUsername(), ((SocketClientChooseStartingObjective) message).getObjectiveID()));
            sendResponse(response);
        }
    }

    private void sendResponse(GenericResponseMessage response) throws IOException {
        try {
            output.writeObject(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
