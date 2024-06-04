package it.polimi.ingsw.model;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.helpers.exceptions.model.ExistingUsernameException;
import it.polimi.ingsw.helpers.exceptions.model.TooManyPlayersException;
import it.polimi.ingsw.model.objectives.Objective;
import it.polimi.ingsw.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameTest {
    private Game game;
    private final List <Player> players = new ArrayList<>();

    @Before
    public void beforeEach() {
         game = new Game(4);
  ;  }
    @Test
    public void addPlayer_NewUsernames_FullGame() throws ExistingUsernameException, TooManyPlayersException {
        for(int i = 0; i < GameConsts.maxPlayersNum; i++){
            game.addPlayer("user"+i);
        }
        assertNotNull(game.getPlayers());
        assertEquals(GameConsts.maxPlayersNum ,game.getPlayers().size());
    }

    @Test (expected = TooManyPlayersException.class)
    public void addPlayer_FullGame_ThrowException() throws ExistingUsernameException, TooManyPlayersException {
        for(int i = 0; i < GameConsts.maxPlayersNum+1; i++){
            game.addPlayer("user"+i);
        }
    }

    @Test
    public void getNumPlayer_NoPlayers() {
        assertEquals(0, game.getNumPlayers());
    }

    @Test
    public void getCardByID_testIds() {
        for (int id = 1; id < GameConsts.totalPlayableCards; id++) {
            assertEquals(id, Game.getCardByID(id).getId());
        }
    }

    @Test
    public void manageObjectives_test() {
        game.manageObjectives();
        int objCount = 0;
        for (Objective obj : game.getGameBoard().getGlobalObjectives()) {
            objCount++;
            assertNotNull(obj);
        }

        assertEquals(2, objCount);
    }
}