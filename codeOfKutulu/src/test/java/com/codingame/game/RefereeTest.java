package com.codingame.game;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import codeofkutulu.mazes.ShelterInPeril;
import codeofkutulu.models.Playfield;
import codeofkutulu.models.effects.ShelterEffect;

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
  
}
