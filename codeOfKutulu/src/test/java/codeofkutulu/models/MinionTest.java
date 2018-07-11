package codeofkutulu.models;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.codingame.game.Referee;

import codeofkutulu.Constants;
import codeofkutulu.mazes.Hypersonic;

public class MinionTest {
  
  private PlayerUnit player;
  private Minion minion;
  private MinionWanderer wminion;
  
  @Before
  public void setup() {
    Constants.random = new Random();
    player = new PlayerUnit(0);
    minion = new Minion(4) {
      @Override
      protected void move() {
        // DONT MOVE
      }

      @Override
      public int getType() {
        return -1;
      }
    };

    wminion = new MinionWanderer(5);
    
    // fix the random of minion
    minion.dirOffset = 0;

    Referee.livingPlayers.clear();
    Referee.livingPlayers.add(player);
    Referee.minions.add(minion);
    Referee.minions.add(wminion);
    
    Referee.playfield = new Playfield(new Hypersonic());
  }
  
  
  @Test
  public void outputCompatibleWithStub() throws Exception {
    /**
     * IF THIS TEST IS KO : 
     * 
     * It mean output have change, you need to CHECK THE STUB AND the BOTS code!
     */
    minion = new MinionWanderer(10);
    minion.spawnCountdown= 99;
    minion.pos.x = 14;
    minion.pos.y = 17;
    minion.target = null;
    minion.state = MinionState.SPAWNING;
    
    assertThat(minion.toOutput(), is("WANDERER 10 14 17 99 0 -1"));
  }
  @Test
  public void spookPlayerInCell_Over() throws Exception {
    player.move(3, 8);
    wminion.move(3, 8);
    
    Cell cell = Referee.playfield.getCell(3, 8);
    
    assertThat(wminion.spookPlayersInCell(cell), is(true));
  }
  
  @Test
  public void spookPlayerInCell_neighbor() throws Exception {
    player.move(3, 8);
    wminion.move(3, 7);
    
    Cell cell = Referee.playfield.getCell(minion);
    
    assertThat(wminion.spookPlayersInCell(cell), is(false));
  }
  
  @Test
  public void frighten_neighbor_horizontal() throws Exception {
    player.move(3, 8);
    wminion.move(2, 8);
    
//    assertThat(wminion.frightenPlayers(), is(true && Constants.WANDERER_FRIGHTEN_RADIUS > 0));
    assertThat(wminion.frightenPlayers(), is(false));
  }
  @Test
  public void frighten_neighbor_vertical() throws Exception {
    player.move(3, 8);
    wminion.move(3, 7);
    
//    assertThat(wminion.frightenPlayers(), is(true && Constants.WANDERER_FRIGHTEN_RADIUS > 0));
    assertThat(wminion.frightenPlayers(), is(false));
  }
  
  @Test
  public void recalling_whenFailed() throws Exception {
    minion.recallFailure();
    wminion.recallFailure();
    
    assertThat(wminion.isBeingRecalled(), is(true));
    assertThat(minion.isBeingRecalled(), is(true));
  }
  @Test
  public void recalling_whenSuccess() throws Exception {
    minion.recallSuccess();
    wminion.recallSuccess();
    
    assertThat(wminion.isBeingRecalled(), is(true));
    assertThat(minion.isBeingRecalled(), is(true));
  }
  
  @Test
  public void whenWanderingIsFnished_beRecalled() throws Exception {
    minion.state = MinionState.WANDERING;
    minion.lifeTimeCountdown = 1;
    minion.update();
    
    assertThat(minion.isBeingRecalled(), is(true));
  }
  
  @Test
  public void chaseChangeChoosesATarget() throws Exception {
    player.move(1, 1);
    
    PlayerUnit p2 = new PlayerUnit(1);
    p2.move(6,1);
    
    Referee.livingPlayers.add(p2);
    minion.move(1, 1);
    minion.target = null;
    
    minion.chaseClosestPlayers();
    
    assertThat(minion.target, is(player));
  }
  
  @Test
  public void chase_keepTargetIfSameDistance() throws Exception {
    player.move(0, 0);
    
    PlayerUnit p2 = new PlayerUnit(1);
    p2.move(4,0);
    
    Referee.livingPlayers.add(p2);
    minion.move(2, 0);
    minion.target = player;
    
    minion.chaseClosestPlayers();
    
    assertThat(minion.target, is(player));
  }
  
  @Test
  public void chase_keepTargetIfSameDistanceEvenIfP2() throws Exception {
    player.move(0, 0);
    
    PlayerUnit p2 = new PlayerUnit(1);
    p2.move(4,0);
    
    Referee.livingPlayers.add(p2);
    minion.move(2, 0);
    minion.target = p2;
    
    minion.chaseClosestPlayers();
    
    assertThat(minion.target, is(p2));
  }

  @Test
  public void chaseChangeTargetIfCloser() throws Exception {
    player.move(0, 0);
    
    PlayerUnit p2 = new PlayerUnit(1);
    p2.move(6,0);
    
    Referee.livingPlayers.add(p2);
    minion.move(2, 0);
    minion.target = player;
    
    minion.chaseClosestPlayers();
    
    assertThat(minion.target, is(player));
  }
}
