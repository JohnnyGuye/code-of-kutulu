package com.codingame.game;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import codeofkutulu.mazes.ShelterInPeril;
import codeofkutulu.models.PlayerUnit;
import codeofkutulu.models.Playfield;
import codeofkutulu.models.effects.PlanEffect;
import codeofkutulu.models.effects.ShelterEffect;
import codeofkutulu.models.effects.YellEffect;

public class RefereeTest {

  public static void clearEffects() {
    Referee.effects.clear();
  }
  public static void updateEffects() {
    Referee.doEffects();
  }

  @Before
  public void setup() {
    Referee.livingPlayers.clear();
    Referee.maze = new ShelterInPeril();
    Referee.playfield = new Playfield(Referee.maze);
    
    Referee.effects.clear();
    Referee.livingPlayers.clear();
  }
  
  
  @Test
  public void shelters_do_not_spawn_at_turn_0() throws Exception {
    Referee referee = new Referee();
    referee.turn = 0;
    
    referee.spawnShelters();
    
    assertThat(Referee.effects.size(), is(0));
  }

  @Test
  public void shelters_spawn_at_turn_50() throws Exception {
    Referee referee = new Referee();
    referee.turn = 50;
    
    referee.spawnShelters();
    
    assertThat(Referee.effects.size(), is(4));
  }

  @Test
  public void shelters_old_shelter_effects_are_dimissed() throws Exception {
    Referee referee = new Referee();
    referee.turn = 50;
    ShelterEffect oldShelter = new ShelterEffect(Referee.playfield.getCell(1, 1));

    referee.effects.add(oldShelter);
    referee.spawnShelters();
    
    assertThat(Referee.effects, not(hasItem(oldShelter)));
    assertThat(Referee.effects.size(), is(4));
  }

  @Test
  public void deadPlayersPlansAreRemoved() throws Exception {
    Referee referee = new Referee();
    Player player = new Player();
    PlayerUnit p1 = player.createUnit();;
    p1.move(1, 1);
    p1.sanity = 200;
    Referee.livingPlayers.add(p1);
    
    PlanEffect effect = new PlanEffect(p1);
    Referee.effects.add(effect);
    
    referee.removeThePlayer(p1);
    
    assertThat(Referee.effects.size(), is(0));
  }

  @Test
  public void deadPlayersYellsAre_NOT_Removed() throws Exception {
    Referee referee = new Referee();
    Player player = new Player();
    PlayerUnit p1 = player.createUnit();;
    p1.move(1, 1);
    p1.sanity = 200;
    Referee.livingPlayers.add(p1);
    
    YellEffect effect = new YellEffect(p1, null);
    Referee.effects.add(effect);
    
    referee.removeThePlayer(p1);
    
    assertThat(Referee.effects.size(), is(1));
  }
}
