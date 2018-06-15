package codeofkutulu.mazes;

import java.util.LinkedList;
import java.util.List;

import codeofkutulu.Rule;
import codeofkutulu.models.Vector2D;

public abstract class Maze {

	public static final char WALL 			= '#';
	public static final char EMPTY 			= '.';
	public static final char WANDERER_SPAWN 	= 'w';
	public static final char OTHER_SPAWN 	= 's';
	public static final char PLAYER_SPAWN 	= 'S';
	public static final char SHELTER 		= 'U';
  public static final Maze NO_MAZE = new Maze() {
    @Override
    public String[] getMap() {
      return new String[] {"#"};
    }
  };

	abstract public String[] getMap();

	public Vector2D dim;
	public String name;

	Maze() {
		this("Abstract");
	}

	Maze(String name) {
		this.name = name;
		computeDims(getMap());
	}
	
	public String getName() {
	  return name;
	}
	
	private void computeDims(String[] map) {
		dim = new Vector2D(map.length > 0 ? map[0].length() : 0, map.length);
	}

	private Vector2D[] getMatchesTo(char code) {
		List<Vector2D> starts = new LinkedList<Vector2D>();
		for (int i = 0; i < this.dim.y; i++) {
			for (int j = 0; j < this.dim.x; j++) {
				if (this.getMap()[i].charAt(j) == code)
					starts.add(new Vector2D(j, i));
			}
		}
		return starts.stream().toArray(Vector2D[]::new);
	}

	private Vector2D[] playerSpawns = null;

	public Vector2D[] getStartPos() {
		if (playerSpawns == null)	playerSpawns = getMatchesTo(PLAYER_SPAWN);
		return playerSpawns;
	}

	private Vector2D[] shelters = null;

	public Vector2D[] getShelters() {
		if (shelters == null)	shelters = getMatchesTo(SHELTER);
		return shelters;
	}

	private Vector2D[] wandererSpawns = null;

	public Vector2D[] getWandererSpawns() {
		if (wandererSpawns == null)	wandererSpawns = getMatchesTo(WANDERER_SPAWN);
		return wandererSpawns;
	}

	public boolean isEmpty(char encodedCellChar) {
		return encodedCellChar != Maze.WALL;
	}

    public String getLineForPlayer(int id) {
      String result = getMap()[id].replace(Maze.PLAYER_SPAWN, Maze.EMPTY);
	  if (!Rule.HAS_SHELTER) result = result.replace(Maze.SHELTER, Maze.EMPTY);
	  return result;
  }

}
