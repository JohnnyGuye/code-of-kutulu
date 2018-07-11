package codeofkutulu.models;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.codingame.game.Referee;

import codeofkutulu.Constants;
import codeofkutulu.mazes.Hypersonic;

public class MinionSlasherTest {

  private PlayerUnit player;

  @Before
  public void setup() {
    player = new PlayerUnit(0);
    player.move(1,1);
    Referee.livingPlayers.clear();
    Referee.livingPlayers.add(player);
    
    Hypersonic maze = new Hypersonic();
    Referee.playfield = new Playfield(maze);
    Referee.maze = maze; 
    Constants.random = new Random();
  }
  
  @Test
  public void idIsSet() throws Exception {
    MinionSlasher slasher = new MinionSlasher(10, player);
    assertThat(slasher.getId(), is(10));
  }
  
  @Test
  public void slasherNeed_6_turns_to_spawn() throws Exception {
    MinionSlasher slasher = new MinionSlasher(0, player);
    assertThat(slasher.getspawnCountdown(), is(6));
  }
  
  @Test
  public void slasherAppearsOnPlayerPosition() throws Exception {
    MinionSlasher slasher = new MinionSlasher(1, player);
    assertThat(slasher.pos.x, is(1));
    assertThat(slasher.pos.y, is(1));
  }
  @Test
  public void slasherFirstTargetIsPlayer() throws Exception {
    MinionSlasher slasher = new MinionSlasher(1, player);
    
    assertThat(slasher.target, is(player));
  }
  
  @Test
  public void slasherIsOfTypeSLASHER() throws Exception {
    MinionSlasher slasher = new MinionSlasher(1, player);
    assertThat(slasher.type(), is(MinionType.SLASHER));
  }
  
  @Test
  public void cooldown_is_stun_when_PREPARING_RUSH() throws Exception {
    MinionSlasher slasher = new MinionSlasher(1, player);
    slasher.state = MinionState.STALKING;
    slasher.stunCountdown = 17;
    
    assertThat(slasher.getCountdown(), is(17));
  }
  
  @Test
  public void slasherUpdateItsTargetWhenOneMovingPlayer() throws Exception {
    player.move(13,2);

    MinionSlasher slasher = new MinionSlasher(1, player);
    slasher.state = MinionState.RUSH;
    slasher.stunCountdown = 1;
    slasher.move(13, 7);
    slasher.target = player;
    slasher.targetPositionBeforePreparation = Referee.playfield.getCell(13, 3);

    
    boolean result = slasher.acquireTarget();
    
    assertThat(result, is(true));
    assertThat(slasher.target, is(player));
    assertThat(slasher.targetPositionBeforePreparation, is(player.getCurrentCell()));
  }
  
  @Test
  public void slasherUpdateItsTargetWhenTwoMovingPlayer() throws Exception {
    // player & second where in 13,3. player move to left, second move to up
    // slasher was targeting player (the one not in LOS anymore
    // slasher should now retarget second with its updated position
    player.move(12,3);

    PlayerUnit second = new PlayerUnit(1);
    second.move(13, 2);
    Referee.livingPlayers.add(second);
    
    MinionSlasher slasher = new MinionSlasher(1, player);
    slasher.state = MinionState.RUSH;
    slasher.stunCountdown = 1;
    slasher.move(13, 7);
    slasher.target = player;
    slasher.targetPositionBeforePreparation = Referee.playfield.getCell(13, 3);

    
    boolean result = slasher.acquireTarget();
    
    assertThat(result, is(true));
    assertThat(slasher.target, is(second));
    assertThat(slasher.targetPositionBeforePreparation, is(second.getCurrentCell()));
  }
  
  @Test
  public void twoPlayerAtSameDistance_chooseOldTarget() throws Exception {
    player.move(13,2);

    PlayerUnit second = new PlayerUnit(1);
    second.move(13,6);
    Referee.livingPlayers.add(second);
    
    MinionSlasher slasher = new MinionSlasher(1, player);
    slasher.state = MinionState.RUSH;
    slasher.stunCountdown = 1;
    slasher.move(13, 4);
    slasher.target = player;
    slasher.targetPositionBeforePreparation = player.getCurrentCell();

    boolean result = slasher.acquireTarget();
    
    assertThat(result, is(true));
    assertThat(slasher.target, is(player));
  }
  
  @Test
  public void twoPlayerStillInLosButOldTargetFarther_chooseOldTarget() throws Exception {
    player.move(13,1);

    PlayerUnit second = new PlayerUnit(1);
    second.move(13,6);
    Referee.livingPlayers.add(second);
    
    MinionSlasher slasher = new MinionSlasher(1, player);
    slasher.state = MinionState.RUSH;
    slasher.stunCountdown = 1;
    slasher.move(13, 4);
    slasher.target = player;
    slasher.targetPositionBeforePreparation = player.getCurrentCell();

    boolean result = slasher.acquireTarget();
    
    assertThat(result, is(true));
    assertThat(slasher.target, is(player));
  }
  
  @Test
  public void twoPlayerStillInLosButOldTargetNotInClosestTarget_goToStun() throws Exception {
    player.move(12,1);

    PlayerUnit second = new PlayerUnit(1);
    second.move(13,6);
    Referee.livingPlayers.add(second);
    
    PlayerUnit third = new PlayerUnit(2);
    third.move(13,6);
    Referee.livingPlayers.add(third);

    MinionSlasher slasher = new MinionSlasher(1, player);
    slasher.state = MinionState.RUSH;
    slasher.stunCountdown = 1;
    slasher.move(13, 4);
    slasher.target = player;
    slasher.targetPositionBeforePreparation = player.getCurrentCell();

    boolean result = slasher.acquireTarget();
    
    assertThat(result, is(false));
    assertThat(slasher.target, is(nullValue()));
  }
  
  @Test
  public void acquireTargetWhenNull() throws Exception {
    player.move(13,2);

    MinionSlasher slasher = new MinionSlasher(1, player);
    slasher.state = MinionState.RUSH;
    slasher.stunCountdown = 1;
    slasher.move(13, 7);
    slasher.target = null;
    slasher.targetPositionBeforePreparation = Referee.playfield.getCell(13, 3);

    
    boolean result = slasher.acquireTarget();
    
    assertThat(result, is(true));
    assertThat(slasher.target, is(player));
    assertThat(slasher.targetPositionBeforePreparation, is(player.getCurrentCell()));
  }

}
