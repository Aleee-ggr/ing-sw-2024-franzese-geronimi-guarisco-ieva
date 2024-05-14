package it.polimi.ingsw.controller;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.controller.threads.Status;
import it.polimi.ingsw.controller.threads.ThreadMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

        Set<Integer> objectives = new HashSet<>(2 * GameConsts.objectiesToChooseFrom);

        for (String username : usernames) {
            UUID msgUUID = UUID.randomUUID();
            controller.getStartingObjectives(username, msgUUID);
            ThreadMessage msg = msgQueue.take();
            assertEquals(Status.OK, msg.status());
            objectives.addAll(Arrays.stream(msg.args()).map(Integer::parseInt).toList());
        }
        assertEquals(2 * GameConsts.objectiesToChooseFrom, objectives.size());
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


    private void fillGame(String[] usernames) throws InterruptedException {
        for (String username : usernames) {
            UUID msgUUID = UUID.randomUUID();
            controller.join(username, msgUUID);
            msgQueue.take();
        }
    }
}
