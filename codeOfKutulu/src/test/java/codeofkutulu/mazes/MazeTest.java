package codeofkutulu.mazes;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class MazeTest {

  @Test
  public void sanitizeLinesForPlayer_removePlayerSpawnPoints() throws Exception {
    Maze maze = new OnlyOneCellMaze();
    String output = maze.getLineForPlayer(0);
    
    assertThat(output, is("."));
  }

  @Test
  public void sanitizeLinesForPlayer_sharpWallToArobase() throws Exception {
    Maze maze = new Hypersonic();
    String output = maze.getLineForPlayer(0);
    
    assertThat(output, is("###############"));
  }

}
