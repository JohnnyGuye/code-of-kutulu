package com.codingame.game;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import codeofkutulu.Constants;
import codeofkutulu.mazes.Hypersonic;
import codeofkutulu.models.PlayerUnit;
import codeofkutulu.models.Playfield;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class PlayerActionTest {

  private PlayerUnit player;

  @Before
  public void setup() {
    Constants.random = new Random();
    
    Referee.livingPlayers.clear();
    Referee.maze = new Hypersonic();
    Referee.playfield = new Playfield(Referee.maze);
    
    League league = League.get(3);
    league.activateRules();
    
    player = new PlayerUnit(0);
    player.move(Referee.playfield.getCell(5,5));
  }
  
  @Test
  @Parameters({
    "WAIT | WAIT |",
    "wait | WAIT |",
    "wait theMessage| WAIT | theMessage",
    "UP | UP |",
    "UP upMessage| UP |upMessage",
    "DOWN downMessage| DOWN |downMessage",
    "RIGHT rightMessage| RIGHT |rightMessage",
    "LEFT leftMessage| LEFT | leftMessage",
    "PLAN planMessage with spaces| PLAN |planMessage with spaces",
    "LIGHT lightMess| LIGHT | lightMess",
    "YELL yellMEssage | YELL | yellMEssage",
  })
  public void checkInput(String input, PlayerAction.Action expectedAction, String expectedMessage) throws Exception {
    PlayerUnit unit = new PlayerUnit(0);
    
    PlayerAction action = new PlayerAction(unit, input);
   
    assertThat(action.action, is (expectedAction));
    assertThat(action.message, is (expectedMessage));
  }
  
  @Test
  public void move_invalidCoords__WAIT() throws Exception {
    PlayerAction action = new PlayerAction(player, "MOVE 0 0");
    
    assertThat(action.message, is(""));
    assertThat(action.action, is(PlayerAction.Action.WAIT));
  }
  
  @Test
  public void move_myCoordinate__WAIT() throws Exception {
    PlayerAction action = new PlayerAction(player, "MOVE 5 5");
    
    assertThat(action.message, is(""));
    assertThat(action.action, is(PlayerAction.Action.WAIT));
  }

  @Test
  public void move_right() throws Exception {
    PlayerAction action = new PlayerAction(player, "MOVE 11 5");
    
    assertThat(action.action, is(PlayerAction.Action.RIGHT));
  }

  @Test
  public void move_left() throws Exception {
    PlayerAction action = new PlayerAction(player, "MOVE 1 5");
    
    assertThat(action.action, is(PlayerAction.Action.LEFT));
  }

  @Test
  public void move_up() throws Exception {
    PlayerAction action = new PlayerAction(player, "MOVE 5 1");
    
    assertThat(action.action, is(PlayerAction.Action.UP));
  }

  @Test
  public void move_down() throws Exception {
    PlayerAction action = new PlayerAction(player, "MOVE 5 11");
    
    assertThat(action.action, is(PlayerAction.Action.DOWN));
  }
}
