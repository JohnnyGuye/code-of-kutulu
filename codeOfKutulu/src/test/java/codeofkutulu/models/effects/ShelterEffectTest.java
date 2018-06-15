package codeofkutulu.models.effects;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import codeofkutulu.Constants;
import org.junit.Before;
import org.junit.Test;

import com.codingame.game.Referee;

import codeofkutulu.mazes.Hypersonic;
import codeofkutulu.models.Cell;
import codeofkutulu.models.PlayerUnit;
import codeofkutulu.models.Playfield;
import codeofkutulu.models.Vector2D;

public class ShelterEffectTest {

  @Before
  public void setup() {
    Referee.livingPlayers.clear();
    Referee.playfield = new Playfield(new Hypersonic());
  }
  
  @Test
  public void shelter_nobodyInDoesntDecrease() throws Exception {
    Cell shelter = Referee.playfield.getCell(10, 10);
    
    ShelterEffect se = new ShelterEffect(shelter);
    se.tick = 10;
    
    se.doEffect();
    
    assertThat(se.tick, is(10));
  }
  
  @Test
  public void shelter_OneLivingPlayerInsideButShelterDepleted() throws Exception {
    Cell shelter = Referee.playfield.getCell(10, 10);
    
    ShelterEffect se = new ShelterEffect(shelter);
    se.tick = 0;
    
    PlayerUnit player = new PlayerUnit(0);
    player.sanity = 30;
    Referee.livingPlayers.add(player);
    player.move(shelter);
    
    se.doEffect();
    
    assertThat(se.tick, is(0));
    assertThat(player.sanity, is(30));
  }
  
  @Test
  public void shelter_OneLivingPlayerJustNear() throws Exception {
    Cell shelter = Referee.playfield.getCell(9, 9);
    
    ShelterEffect se = new ShelterEffect(shelter);
    se.tick = 5;
    
    PlayerUnit player = new PlayerUnit(0);
    player.sanity = 30;
    Referee.livingPlayers.add(player);
    player.move(shelter.cellFromHere(new Vector2D(1,0)));
    
    se.doEffect();
    
    assertThat(se.tick, is(5));
    assertThat(player.sanity, is(30));
  }
  
  @Test
  public void shelter_OneLivingPlayerInsideDecreaseBy10() throws Exception {
    Cell shelter = Referee.playfield.getCell(10, 10);
    
    ShelterEffect se = new ShelterEffect(shelter);
    se.tick = 5;
    
    PlayerUnit player = new PlayerUnit(0);
    player.sanity = 30;
    Referee.livingPlayers.add(player);
    player.move(shelter);
    
    se.doEffect();
    
    assertThat(se.tick, is(4));
    assertThat(player.sanity, is(30+ Constants.EFFECT_SHELTER_HEAL));
  }
  
  @Test
  public void shelter_OneLivingPlayerMaxHealthReached() throws Exception {
    Cell shelter = Referee.playfield.getCell(10, 10);
    
    ShelterEffect se = new ShelterEffect(shelter);
    se.tick = 5;
    
    PlayerUnit player = new PlayerUnit(0);
    player.sanity = Constants.PLAYER_MAX_SANITY - 1;
    Referee.livingPlayers.add(player);
    player.move(shelter);
    
    se.doEffect();
    
    assertThat(se.tick, is(4));
    assertThat(player.sanity, is(Constants.PLAYER_MAX_SANITY));
  }
  
  @Test
  public void shelter_2LivingPlayerInsideDecreaseBy20() throws Exception {
    Cell shelter = Referee.playfield.getCell(10, 10);
    ShelterEffect se = new ShelterEffect(shelter);
    se.tick = 37;
    
    PlayerUnit player = new PlayerUnit(0);
    player.sanity = 71;
    Referee.livingPlayers.add(player);
    player.move(shelter);

    PlayerUnit player2 = new PlayerUnit(1);
    player2.sanity = 36;
    Referee.livingPlayers.add(player2);
    player2.move(shelter);
    
    se.doEffect();
    
    assertThat(se.tick, is(37 - 1 - 1));
    assertThat(player.sanity, is(71+Constants.EFFECT_SHELTER_HEAL));
    assertThat(player2.sanity, is(36+Constants.EFFECT_SHELTER_HEAL));
  }
}
