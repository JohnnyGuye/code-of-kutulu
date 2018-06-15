package codeofkutulu.models.effects;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Random;

import codeofkutulu.mazes.ShelterMe;
import org.junit.Before;
import org.junit.Test;

import com.codingame.game.Referee;

import codeofkutulu.Constants;
import codeofkutulu.mazes.Hypersonic;
import codeofkutulu.models.Cell;
import codeofkutulu.models.Minion;
import codeofkutulu.models.PlayerUnit;
import codeofkutulu.models.Playfield;
import codeofkutulu.pathfinder.PathFinder;
import codeofkutulu.pathfinder.PathFinder.PathFinderResult;

public class RepulseEffectTest {
  private PlayerUnit player;

  @Before
  public void setup() {
    player = new PlayerUnit(0);
    player.move(1,1);
    Constants.random = new Random();
    Referee.livingPlayers.clear();
    Referee.livingPlayers.add(player);
    Referee.playfield = new Playfield(new Hypersonic());
  }
  
  @Test
  public void noRepulseEffect_normalDistance() throws Exception {
    
    PathFinderResult pfr = new PathFinder().withWeightFunction(Cell::getMinionPathLength)
        .from(Referee.playfield.getCell(3, 1))
        .to(Referee.playfield.getCell(1, 1))
        .findPath();
    
    assertThat(pfr.weightedLength, is(2));
  }
  
  @Test
  public void repulseEffectAddWeightedDistanceToMinionPathLength() throws Exception {
    RepulseEffect effect = new RepulseEffect(player);
    effect.tick = 10;
    effect.doEffect();
    
    PathFinderResult pfr = new PathFinder().withWeightFunction(Cell::getMinionPathLength)
        .from(Referee.playfield.getCell(3, 3))
        .to(Referee.playfield.getCell(1, 3))
        .findPath();
    
    assertThat(pfr.weightedLength, is(10));
  }

  @Test
  public void findAllFields() {
    Referee.playfield = new Playfield(new ShelterMe());
    player = new PlayerUnit(0);
    player.move(7,13);
    RepulseEffect effect = new RepulseEffect(player);
    effect.doEffect();
    assertThat(Referee.playfield.getCell(10, 11).hasMinionRepulse(), is(true));
    assertThat(Referee.playfield.getCell(9, 11).hasMinionRepulse(), is(true));
  }
}
