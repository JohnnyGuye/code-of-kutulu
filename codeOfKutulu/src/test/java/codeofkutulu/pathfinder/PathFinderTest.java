package codeofkutulu.pathfinder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import codeofkutulu.Constants;
import codeofkutulu.mazes.FourOfAKind;
import codeofkutulu.models.Cell;
import codeofkutulu.models.Playfield;
import codeofkutulu.models.Vector2D;
import com.codingame.game.Referee;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PathFinderTest {

  @Before
  public void setup() {
    Referee.livingPlayers.clear();
	  Referee.maze = new FourOfAKind();
    Referee.playfield = new Playfield(Referee.maze);
  }

  @Test
  public void pathfinderGetsCloserToTarget() throws Exception {
	// compare AStar results with BFS to verify, that the pathfinder
	// returns a field closer to the target than the initial one
	
	// parse board into 
	String[] map = Referee.maze.getMap();
	int width = Referee.maze.dim.x;
	int height = Referee.maze.dim.y;
    boolean[][] passable = new boolean[width][height];
	List<Vector2D> free = new ArrayList<>();
	for (int y = 0; y < height; y++) {
	  for (int x = 0; x < width; x++) {
		if (Referee.maze.isEmpty(map[y].charAt(x))) {
		  passable[x][y] = true;
		  free.add(new Vector2D(x, y));
		}
	  }
	}
	
	PathFinder pathfinder = new PathFinder().withWeightFunction(Cell::getMinionPathLength);
	for (Vector2D end : free) {
	  int[][] dist = BFS(passable, end, width, height);
	  for (Vector2D start : free) {
		if (start == end || dist[start.x][start.y] == -1) continue;
		PathFinder.PathFinderResult findPath = pathfinder
          .from(Referee.playfield.getCell(start.x, start.y))
          .to(Referee.playfield.getCell(end.x, end.y))
          .findPath();
		Cell next = findPath.getNextCell();
		assertThat(dist[next.pos.x][next.pos.y], is(dist[start.x][start.y] - 1));
	  }
	}
  }
  
  static int[][] BFS(boolean[][] passable, Vector2D start, int width, int height) {
	int[][] result = new int[width][height];
	for (int x = 0; x < width; x++) {
	  for (int y = 0; y < height; y++) result[x][y] = -1;
	}
	result[start.x][start.y] = 0;
	Queue<Vector2D> queue = new LinkedList<>();
	queue.add(start);
	while (queue.size() > 0) {
	  Vector2D p = queue.poll();
	  for (Vector2D dir : Constants.vdir) {
		Vector2D q = Vector2D.add(p, dir);
		if (q.x < 0 || q.x >= width || q.y < 0 || q.y >= height) continue;
		if (!passable[q.x][q.y] || result[q.x][q.y] != -1) continue;
		result[q.x][q.y] = 1 + result[p.x][p.y];
		queue.add(q);
	  }
	}
	return result;
  }
}
