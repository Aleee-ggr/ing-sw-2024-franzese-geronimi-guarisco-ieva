package it.polimi.ingsw.controller;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.controller.threads.Status;
import it.polimi.ingsw.controller.threads.ThreadMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Coordinates;
import it.polimi.ingsw.model.cards.Corner;
import it.polimi.ingsw.model.cards.StartingCard;
import it.polimi.ingsw.model.enums.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static org.junit.Assert.*;

public class ControllerTest {
    private Controller controller;
    private final BlockingQueue<ThreadMessage> msgQueue = new LinkedBlockingQueue<>();

    @Before
    public void setUp() {
        msgQueue.clear();
        controller = new Controller(msgQueue,2);
    }

    @Test
    public void testJoinSuccessful() throws InterruptedException {
        UUID msgUUID = UUID.randomUUID();
        String username = "p1";
        controller.join(username, msgUUID);

        ThreadMessage msg = msgQueue.take();
        assertEquals(ThreadMessage.okResponse(username, msgUUID), msg);
    }

    @Test
    public void testJoinFailedUsernameAlreadyExists() throws InterruptedException {
        UUID msgUUID = UUID.randomUUID();
        String username = "p1";
        controller.join(username, msgUUID);
        msgQueue.take();

        msgUUID = UUID.randomUUID();
        controller.join(username, msgUUID);
        ThreadMessage msg = msgQueue.take();
        assertEquals(Status.ERROR, msg.status());
    }

    @Test
    public void testJoinFailedGameFull() throws InterruptedException {
        String[] validUsernames = {"p1", "p2"};
        String[] excessUsernames = {"p3", "p4", "p5"};

        for (String username : validUsernames) {
            UUID msgUUID = UUID.randomUUID();
            controller.join(username, msgUUID);
            msgQueue.take();
        }

        for (String username : excessUsernames) {
            UUID msgUUID = UUID.randomUUID();
            controller.join(username, msgUUID);
            ThreadMessage msg = msgQueue.take();
            assertEquals(Status.ERROR, msg.status());
        }
    }

    @Test
    public void testGetStartingObjectives() throws InterruptedException {
        String[] usernames = {"p1", "ignored"};

        for (String username : usernames) {
            UUID msgUUID = UUID.randomUUID();
            controller.join(username, msgUUID);
            msgQueue.take();
        }

        UUID msgUUID = UUID.randomUUID();
        controller.getStartingObjectives(usernames[0], msgUUID);
        ThreadMessage msg = msgQueue.take();
        assertEquals(Status.OK, msg.status());
        for (Integer value : Arrays.stream(msg.args()).map(Integer::parseInt).toList()) {
            assertTrue(87 <= value && value <= 102);
        }
    }

    @Test
    public void testStartingObjectivesUnique() throws InterruptedException {
        String[] usernames = {"p1", "p2"};
        fillGame(usernames);

        Set<Integer> objectives = new HashSet<>(2 * GameConsts.objectivesToChooseFrom);

        for (String username : usernames) {
            UUID msgUUID = UUID.randomUUID();
            controller.getStartingObjectives(username, msgUUID);
            ThreadMessage msg = msgQueue.take();
            assertEquals(Status.OK, msg.status());
            objectives.addAll(Arrays.stream(msg.args()).map(Integer::parseInt).toList());
        }
        assertEquals(2 * GameConsts.objectivesToChooseFrom, objectives.size());
    }

    @Test
    public void testDrawTwiceDifferentCard() throws InterruptedException {
        String[] usernames = {"p1", "p2"};
        fillGame(usernames);
        Set<Integer> drawnCards = new HashSet<>();
        int cardCount = 30;
        for (int i = 0; i < cardCount; i++) {
            UUID msgUUID = UUID.randomUUID();
            controller.draw(usernames[0], i % 6, msgUUID);
            ThreadMessage msg = msgQueue.take();
            drawnCards.add(Integer.parseInt(msg.args()[0]));
        }

        assertEquals(cardCount, drawnCards.size());
    }

    @Test
    public void testGetStartingCard() throws InterruptedException {
        String[] usernames = {"p1", "p2"};
        fillGame(usernames);

        UUID msgUUID = UUID.randomUUID();
        controller.getStartingCards(usernames[0], msgUUID);
        ThreadMessage msg = msgQueue.take();
        int id = abs(Integer.parseInt(msg.args()[0]));
        assertTrue(81 <= id && id <= 86);
    }

    @Test
    public void testPlaceStartingCard() throws InterruptedException {
        String[] usernames = {"p1", "p2"};
        fillGame(usernames);

        UUID msgUUID = UUID.randomUUID();
        controller.getStartingCards(usernames[0], msgUUID);
        msgQueue.take();

        msgUUID = UUID.randomUUID();
        controller.placeStartingCard(usernames[0], true, msgUUID);
        ThreadMessage msg = msgQueue.take();
        assertEquals(Status.OK, msg.status());
    }

    @Test
    public void testGetBoardStartingCardOnly() throws InterruptedException {
        String[] usernames = {"p1", "p2"};
        fillGame(usernames);

        int expectedId;
        UUID msgUUID = UUID.randomUUID();
        controller.getStartingCards(usernames[0], msgUUID);
        ThreadMessage msg = msgQueue.take();
        expectedId = abs(Integer.parseInt(msg.args()[0]));

        msgUUID = UUID.randomUUID();
        controller.placeStartingCard(usernames[0], false, msgUUID);
        msgQueue.take();

        msgUUID = UUID.randomUUID();
        controller.getBoard(usernames[0], usernames[0], msgUUID);
        msg = msgQueue.take();
        String[] data = msg.args()[0].split(",");
        int x = Integer.parseInt(data[0]);
        int y = Integer.parseInt(data[1]);
        int cardId = Integer.parseInt(data[2]);

        assertEquals(0, x);
        assertEquals(0, y);
        assertEquals(expectedId, abs(cardId));
    }

    @Test
    public void testGetBoardSharedDataStartingCardOnly() throws InterruptedException {
        String[] usernames = {"p1", "p2"};
        fillGame(usernames);

        UUID msgUUID = UUID.randomUUID();
        controller.getStartingCards(usernames[0], msgUUID);
        msgQueue.take();

        controller.placeStartingCard(usernames[0], false, msgUUID);
        msgQueue.take();

        controller.getBoard(usernames[0], usernames[0], msgUUID);
        ThreadMessage msg = msgQueue.take();
        String[] data_p1 = msg.args()[0].split(",");

        controller.getBoard(usernames[1], usernames[0], msgUUID);
        msg = msgQueue.take();
        String[] data_p2 = msg.args()[0].split(",");

        assertArrayEquals(data_p1, data_p2);
    }

    @Test
    public void testPlacingOrderStartingCardOnly() throws InterruptedException {
        String[] usernames = {"p1", "p2"};
        fillGame(usernames);

        int expectedId;
        UUID msgUUID = UUID.randomUUID();
        controller.getStartingCards(usernames[0], msgUUID);
        ThreadMessage msg = msgQueue.take();
        expectedId = abs(Integer.parseInt(msg.args()[0]));

        controller.placeStartingCard(usernames[0], true, msgUUID);
        msgQueue.take();

        controller.getPlacingOrder(usernames[0], usernames[0], msgUUID);

        msg = msgQueue.take();
        assertEquals(Integer.toString(expectedId), msg.args()[0]);
    }

    @Test
    public void testPlacingOrderStartingCardOnlyBothPlayers() throws InterruptedException {
        String[] usernames = {"p1", "p2"};
        fillGame(usernames);

        UUID msgUUID = UUID.randomUUID();
        for (String username : usernames) {
            controller.getStartingCards(username, msgUUID);
            msgQueue.take();

            controller.placeStartingCard(username, true, msgUUID);
            msgQueue.take();

            controller.getPlacingOrder(usernames[0], usernames[0], msgUUID);
            ThreadMessage msg = msgQueue.take();
            assertEquals(1, msg.args().length);
        }
    }

    @Test
    public void testValidPlacements() throws InterruptedException {
        String[] usernames = {"p1", "p2"};
        fillGame(usernames);
        UUID msgUUID = UUID.randomUUID();

        controller.getStartingCards(usernames[0], msgUUID);
        int id = Integer.parseInt(msgQueue.take().args()[0]);

        StartingCard card = (StartingCard) Game.getCardByID(id);

        controller.placeStartingCard(usernames[0], true, msgUUID);
        msgQueue.take();

        controller.getValidPlacements(usernames[0], msgUUID);
        ThreadMessage msg = msgQueue.take();

        Set<Coordinates> validCoordinates = new HashSet<>(4);
        List<Resource> res = Arrays.stream(card.getFrontCorners())
                .map(Corner::getCornerResource)
                .collect(Collectors.toCollection(ArrayList::new));

        if (res.removeFirst() != Resource.NONCOVERABLE) {
            validCoordinates.add(new Coordinates(-1, 0));
        }
        if (res.removeFirst() != Resource.NONCOVERABLE) {
            validCoordinates.add(new Coordinates(0, 1));
        }
        if (res.removeFirst() != Resource.NONCOVERABLE) {
            validCoordinates.add(new Coordinates(0, -1));
        }
        if (res.removeFirst() != Resource.NONCOVERABLE) {
            validCoordinates.add(new Coordinates(1, 0));
        }

        for (String data : msg.args()) {
            String[] split = data.split(",");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            assertTrue(validCoordinates.contains(new Coordinates(x, y)));
        }
    }
    private void fillGame(String[] usernames) throws InterruptedException {
        for (String username : usernames) {
            UUID msgUUID = UUID.randomUUID();
            controller.join(username, msgUUID);
            msgQueue.take();
        }
    }
}
