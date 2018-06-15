package com.codingame.game;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.codingame.gameengine.runner.MultiplayerGameRunner;

import codeofkutulu.models.Minion;
import codeofkutulu.models.PlayerUnit;
import codeofkutulu.models.effects.Effect;

/**
 * 'IT' test, running in the maven test:test phase breaks codingame upload :(
 * 
 * @author nmahoude
 *
 */
public class RefereeIT {

    @Before
    public void setup() {
        Referee.NEXT_ID = 0; // clean up static members from last run
    }

    @Test
    public void gameIsReproducable() throws Exception {
        // run a match once, save the minions on the map
        Properties properties = new Properties();
        properties.setProperty("seed", "123456");
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
        gameRunner.setGameParameters(properties);
        java.lang.Class defaultBot = runner.tests.waiter.Player.class;
        gameRunner.addAgent(defaultBot);
        gameRunner.addAgent(defaultBot);
        gameRunner.addAgent(defaultBot);
        gameRunner.addAgent(defaultBot);
        try {
            cleanStaticMembers();
            gameRunner.start(-1); // invalid port, as we don't want to start a webserver
        } catch (RuntimeException ex) {
            assertThat(ex.getCause().getMessage(), is("port out of range:-1"));
        }
        int testIndex = Referee.NEXT_ID;
        List<Minion> minions = Referee.minions;
        List<PlayerUnit> players = Referee.livingPlayers;
        List<Effect> effects = Referee.effects;

        // run the same match a few more times and make sure, that the minions are
        // at the same positions
        for (int i = 0; i < 1; i++) {
            gameRunner = new MultiplayerGameRunner();
            gameRunner.setGameParameters(properties);

            cleanStaticMembers();
            gameRunner.addAgent(defaultBot);
            gameRunner.addAgent(defaultBot);
            gameRunner.addAgent(defaultBot);
            gameRunner.addAgent(defaultBot);
            try {
                gameRunner.start(-1);
            } catch (RuntimeException ex) {
                assertThat(ex.getCause().getMessage(), is("port out of range:-1"));
            }

            assertThat(testIndex, is(Referee.NEXT_ID));
            assertThat(minions.size(), is(Referee.minions.size()));
            assertThat(players.size(), is(Referee.livingPlayers.size()));
            assertThat(effects.size(), is(Referee.effects.size()));
            for (int minion = 0; minion < minions.size(); minion++) {
                assertThat(minions.get(minion).getCurrentCell(), is(Referee.minions.get(minion).getCurrentCell()));
            }
        }
    }

    private void cleanStaticMembers() {
        Referee.NEXT_ID = 0; // clean up static members from last run
        Referee.minions = new ArrayList<>();
        Referee.livingPlayers = new ArrayList<>();
        Referee.effects = new ArrayList<>();
    }
}
