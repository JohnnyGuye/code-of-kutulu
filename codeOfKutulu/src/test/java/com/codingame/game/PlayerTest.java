package com.codingame.game;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import codeofkutulu.models.PlayerUnit;

public class PlayerTest {

  private Player player;
  
  @Before
  public void setup() {
    player = new Player();
  }
  
  @Test
  public void messageNullIsTransformedInEmptyString() throws Exception {
    player.setDisplayMessage(null);
    assertThat(player.getDisplayMessage(), is(""));
  }

  @Test
  public void messageIsTruncatedTo26chars() throws Exception {
    player.setDisplayMessage("12345678901234567890123456789");
    assertThat(player.getDisplayMessage(), is("12345678901234567890 (...)"));
  }

  @Test
  public void messageNominalWithSpaces() throws Exception {
    player.setDisplayMessage("my text");
    assertThat(player.getDisplayMessage(), is("my text"));
  }
  
  @Test
  public void waitingOnlyOneInputLine() throws Exception {
    assertThat(player.getExpectedOutputLines(), is (1));
  }
  
  @Test
  public void playerCreatePlayerUnitWithSameId() throws Exception {
    PlayerUnit playerUnit = player.createUnit();
    assertThat(player.getIndex(), is(playerUnit.id));
  }
}
