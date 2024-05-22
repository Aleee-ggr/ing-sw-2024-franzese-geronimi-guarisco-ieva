package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.threads.Status;
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
public class ClientHandler extends Thread {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Map<UUID, BlockingQueue<ThreadMessage>> threadMessages;

    /**
     * Constructs a new ClientHandler object with the specified socket connection.
     *
     * @param socket the socket connection to the client
     * @param threadMessages a map containing thread messages for each UUID
     * @throws IOException if an I/O error occurs while initializing the input and output streams
     */
    public ClientHandler(Socket socket, Map<UUID, BlockingQueue<ThreadMessage>> threadMessages) throws IOException {
        this.socket = socket;
        socket.setTcpNoDelay(true);
        this.input = new ObjectInputStream(socket.getInputStream());
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.threadMessages = threadMessages;
    }

    /**
     * Starts the client handler thread to handle communication with the client.
     * Reads objects from the client and processes them accordingly.
     */
    public void run() {
        try {
            while (!socket.isClosed()) {
                GenericRequestMessage message = (GenericRequestMessage) input.readObject();
                System.out.println("Message received: " + message);
                handleMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client disconnected");
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

    /**
     * Handles incoming messages from the client.
     * Dispatches the message to the appropriate server method for processing.
     *
     * @param message the message received from the client
     * @throws IOException if an I/O error occurs while sending responses
     */
    private void handleMessage(GenericRequestMessage message) throws IOException {
        output.flush();
        GenericResponseMessage responseMessage = null;
        switch (message) {

            case SocketClientCreateGameMessage socketClientCreateGameMessage -> {
                UUID id = Server.createGame(socketClientCreateGameMessage.getNumPlayers());
                responseMessage = new CreateGameResponseMessage(id);
            }

            case SocketClientJoinGameMessage socketClientJoinGameMessage -> {
                responseMessage = new JoinGameResponseMessage(
                        socketClientJoinGameMessage.getGameUUID(),
                        Server.joinGame(
                                socketClientJoinGameMessage.getGameUUID(),
                                message.getUsername()
                        )
                );
            }

            case SocketValidateCredentialsMessage socketValidateCredentialsMessage -> {
                responseMessage = new ValidateCredentialsResponseMessage(
                        socketValidateCredentialsMessage.getUsername(),
                        socketValidateCredentialsMessage.getPassword(),
                        Server.isValidPlayer(
                                socketValidateCredentialsMessage.getUsername(),
                                socketValidateCredentialsMessage.getPassword()
                        )
                );
            }

            case SocketClientWaitUpdateMessage socketClientWaitUpdateMessage -> {
                responseMessage = new WaitUpdateResponseMessage(
                        Server.waitUpdate(
                                socketClientWaitUpdateMessage.getGameUUID(),
                                socketClientWaitUpdateMessage.getUsername()
                        )
                );
            }

            //TODO: implementation of chat
            case SocketClientPostChatMessage socketClientPostChatMessage -> {
                return;
            }

            case SocketClientDrawCardMessage socketClientDrawCardMessage -> {
                responseMessage = new DrawCardResponseMessage(
                        Server.drawCardServer(
                                socketClientDrawCardMessage.getGameUUID(),
                                socketClientDrawCardMessage.getUsername(),
                                socketClientDrawCardMessage.getPosition()
                        )
                );
            }

            case SocketClientPlaceCardMessage socketClientPlaceCardMessage -> {
                responseMessage = new PlaceCardResponseMessage(
                        Server.placeCardServer(
                                socketClientPlaceCardMessage.getGameUUID(),
                                socketClientPlaceCardMessage.getUsername(),
                                socketClientPlaceCardMessage.getCoordinates(),
                                socketClientPlaceCardMessage.getCardId()
                        )
                );
            }

            case SocketClientPlaceStartingCardMessage socketClientPlaceStartingCardMessage -> {
                responseMessage = new PlaceStartingCardResponseMessage(
                        Server.setStartingCardServer(
                                socketClientPlaceStartingCardMessage.getGameUUID(),
                                socketClientPlaceStartingCardMessage.getUsername(),
                                socketClientPlaceStartingCardMessage.isFrontSideUp()
                        )
                );
            }

            case SocketClientChoosePersonalObjectiveMessage socketClientChoosePersonalObjectiveMessage -> {
                responseMessage = new ChoosePersonalObjectiveResponseMessage(
                        Server.choosePersonalObjectiveServer(
                                socketClientChoosePersonalObjectiveMessage.getGameUUID(),
                                socketClientChoosePersonalObjectiveMessage.getUsername(),
                                socketClientChoosePersonalObjectiveMessage.getObjectiveID()
                        )
                );
            }

            case SocketClientFetchAvailableGamesMessage socketClientFetchAvailableGamesMessage -> {
                responseMessage = new FetchAvailableGamesResponseMessage(
                        Server.getAvailableGamesServer(
                                socketClientFetchAvailableGamesMessage.getUsername()
                        )
                );
            }

            case SocketClientFetchGameStateMessage socketClientFetchGameStateMessage -> {
                responseMessage = new FetchGameStateResponseMessage(
                        Server.getGameStateServer(
                                socketClientFetchGameStateMessage.getGameUUID(),
                                socketClientFetchGameStateMessage.getUsername()
                        )
                );
            }

            case SocketClientGetPlayersMessage socketClientGetPlayersMessage -> {
                responseMessage = new GetPlayersResponseMessage(
                        Server.getPlayersServer(
                                socketClientGetPlayersMessage.getGameUUID(),
                                socketClientGetPlayersMessage.getUsername()
                        )
                );
            }

            case SocketClientGetCommonObjectivesMessage socketClientGetCommonObjectivesMessage -> {
                responseMessage = new GetCommonObjectivesResponseMessage(
                        Server.getCommonObjectivesServer(
                                socketClientGetCommonObjectivesMessage.getGameUUID(),
                                socketClientGetCommonObjectivesMessage.getUsername()
                        )
                );
            }

            case SocketClientGetVisibleCardsMessage socketClientGetVisibleCardsMessage -> {
                responseMessage = new GetVisibleCardsResponseMessage(
                        Server.getVisibleCardsServer(
                                socketClientGetVisibleCardsMessage.getGameUUID(),
                                socketClientGetVisibleCardsMessage.getUsername()
                        )
                );
            }

            case SocketClientGetBackSideDecksMessage socketClientGetBackSideDecksMessage -> {
                responseMessage = new GetBackSideDecksResponseMessage(
                        Server.getBackSideDecksServer(
                                socketClientGetBackSideDecksMessage.getGameUUID(),
                                socketClientGetBackSideDecksMessage.getUsername()
                        )
                );
            }

            case SocketClientGetScoreMapMessage socketClientGetScoreMapMessage -> {
                responseMessage = new GetScoreMapResponseMessage(
                        Server.getScoreMapServer(
                                socketClientGetScoreMapMessage.getGameUUID(),
                                socketClientGetScoreMapMessage.getUsername()
                        )
                );
            }

            case SocketClientGetPlayerResourcesMessage socketClientGetPlayerResourcesMessage -> {
                responseMessage = new GetPlayerResourcesResponseMessage(
                        Server.getPlayerResourcesServer(
                                socketClientGetPlayerResourcesMessage.getGameUUID(),
                                socketClientGetPlayerResourcesMessage.getUsername(),
                                socketClientGetPlayerResourcesMessage.getUsernameRequiredData()
                        ),
                        socketClientGetPlayerResourcesMessage.getUsernameRequiredData()
                );
            }

            case SocketClientGetPlayerBoard socketClientGetPlayerBoard -> {
                responseMessage = new GetPlayerBoardResponseMessage(
                        Server.getBoardServer(
                                socketClientGetPlayerBoard.getGameUUID(),
                                socketClientGetPlayerBoard.getUsername(),
                                socketClientGetPlayerBoard.getUsernameRequiredData()
                        ),
                        socketClientGetPlayerBoard.getUsernameRequiredData()
                );
            }

            case SocketClientGetPlacingOrderMessage socketClientGetPlacingOrderMessage -> {
                responseMessage = new GetPlacingOrderResponseMessage(
                        Server.getPlacingOrderServer(
                                socketClientGetPlacingOrderMessage.getGameUUID(),
                                socketClientGetPlacingOrderMessage.getUsername(),
                                socketClientGetPlacingOrderMessage.getUsernameRequiredData()
                        ),
                        socketClientGetPlacingOrderMessage.getUsernameRequiredData()
                );
            }

            case SocketClientGetValidPlacementsMessage socketClientGetValidPlacementsMessage -> {
                responseMessage = new GetValidPlacementsResponseMessage(
                        Server.getValidPlacementsServer(
                                socketClientGetValidPlacementsMessage.getGameUUID(),
                                socketClientGetValidPlacementsMessage.getUsername()
                        )
                );
            }

            case SocketClientGetHandMessage socketClientGetHandMessage -> {
                responseMessage = new GetHandResponseMessage(
                        Server.getHandServer(
                                socketClientGetHandMessage.getGameUUID(),
                                socketClientGetHandMessage.getUsername()
                        )
                );
            }

            case SocketClientGetHandColorMessage socketClientGetHandColorMessage -> {
                responseMessage = new GetHandColorResponseMessage(
                        Server.getHandColorServer(
                                socketClientGetHandColorMessage.getGameUUID(),
                                socketClientGetHandColorMessage.getUsername(),
                                socketClientGetHandColorMessage.getUsernameRequiredData()
                        ),
                        socketClientGetHandColorMessage.getUsernameRequiredData()
                );
            }

            case SocketClientGetStartingObjectivesMessage socketClientGetStartingObjectivesMessage -> {
                responseMessage = new GetStartingObjectivesResponseMessage(
                        Server.getStartingObjectivesServer(
                                socketClientGetStartingObjectivesMessage.getGameUUID(),
                                socketClientGetStartingObjectivesMessage.getUsername()
                        )
                );
            }

            case SocketClientGetStartingCardMessage socketClientGetStartingCardMessage -> {
                responseMessage = new GetStartingCardResponseMessage(
                        Server.getStartingCardServer(
                                socketClientGetStartingCardMessage.getGameUUID(),
                                socketClientGetStartingCardMessage.getUsername()
                        )
                );
            }

            default -> {
                throw new RuntimeException("Invalid message type");
            }

        }

        sendResponse(responseMessage);
        System.out.println("Message sent: " + responseMessage);
    }

    /**
     * Sends a response message back to the client.
     *
     * @param response the response message to be sent
     */
    private void sendResponse(GenericResponseMessage response) {
        try {
            output.writeObject(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
