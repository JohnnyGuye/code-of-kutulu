package codeofkutulu.models.effects;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.codingame.game.Referee;

import codeofkutulu.mazes.ShelterMe;
import codeofkutulu.models.PlayerUnit;
import codeofkutulu.models.Playfield;

public class PlanEffectTest {

  @Before
  public void setup() {
    Referee.livingPlayers.clear();
  }
  
  @Test
  public void plan_when_alone() {
    Referee.playfield = new Playfield(new ShelterMe());
    PlayerUnit player = new PlayerUnit(0);
    player.move(7,13);
    player.sanity = 100;
    PlanEffect effect = new PlanEffect(player);
    
    effect.doEffect();

    assertThat(player.sanity, is(100+3));
  }

  @Test
  public void plan_when_anotherPlayer() {
    Referee.playfield = new Playfield(new ShelterMe());
    
    PlayerUnit player = new PlayerUnit(0);
    player.move(7,13);
    player.sanity = 100;
    Referee.livingPlayers.add(player);

    
    PlayerUnit other = new PlayerUnit(1);
    other.move(7,13);
    other.sanity = 200;
    Referee.livingPlayers.add(other);
    
    PlanEffect effect = new PlanEffect(player);
    
    effect.doEffect();

    assertThat(player.sanity, is(100+3+3));
    assertThat(other.sanity, is(200+3));
  }

}
