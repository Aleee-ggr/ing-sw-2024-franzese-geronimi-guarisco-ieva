package it.polimi.ingsw.model;

import it.polimi.ingsw.GameConsts;
import it.polimi.ingsw.model.exceptions.ExistingUsernameException;
import it.polimi.ingsw.model.exceptions.TooManyPlayersException;
import it.polimi.ingsw.model.player.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameTest {
    private final Game game = new Game(null);
    private final List <Player> players = new ArrayList<>();
    @Test
    public void NormalAdd() throws ExistingUsernameException, TooManyPlayersException {
        for(int i = 0; i < GameConsts.maxPlayersNum; i++){
            game.addPlayer("user"+i);
        }
        assertNotNull(game.getPlayers());
        assertEquals(GameConsts.maxPlayersNum ,game.getPlayers().size());
    }

    @Test (expected = ExistingUsernameException.class)
    public void DuplicatePlayer() throws ExistingUsernameException, TooManyPlayersException {
        game.addPlayer("user");
        game.addPlayer("user");
    }

    @Test (expected = TooManyPlayersException.class)
    public void TooManyPlayers() throws ExistingUsernameException, TooManyPlayersException {
        for(int i = 0; i < GameConsts.maxPlayersNum+1; i++){
            game.addPlayer("user"+i);
        }
    }
}