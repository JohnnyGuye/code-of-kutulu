package codeofkutulu.models;

import java.util.Observable;

import com.codingame.game.Referee;

import codeofkutulu.Constants;
import codeofkutulu.models.events.RepulseEvent;

public class Cell extends Observable {
	
  public static final Cell INVALID_CELL = new Cell(-1, -1) {
	  
	@Override
	public boolean isInBoundaries() {
	  return false;
	}
	
	@Override
	public boolean isInvalidCell() {
			return true;
	}
  };
  
  public Vector2D pos 		= new Vector2D();
  public CellType type		= CellType.NONE;
  boolean spawnPoint 		= false;
  
  private boolean minionRepulse;
  
  public Cell() {
	  this(0,0);
  }
  
  public Cell(int x, int y) {
    this.pos.set(x, y);
  }

  public void setAsSpawnPoint() {
    spawnPoint = true;
  }
  
  public boolean isSpawnPoint() {
    return spawnPoint;
  }
  
  public void evolveInShelter() {
	  this.type = CellType.SHELTER;
  }
  
  public boolean isShelter() {
	  return this.type == CellType.SHELTER;
  }

  public boolean hasMinionRepulse() { return minionRepulse; }
  
  /**
   * If it's not a wall, and in the grid, it's walkable
   * @return
   */
  public boolean isWalkable() {
    return type != CellType.WALL && isInBoundaries();
  }
  
  /**
   * Is the cell a wall ?
   * @return
   */
  public boolean isWall() {
    return type == CellType.WALL;
  }
  
  /**
   * Is the cell in the grid ?
   * @return
   */
  public boolean isInBoundaries() {
	  return Referee.playfield.isInBoundaries(this);
  }
  
  public boolean isInvalidCell() {
	  return false;
  }

  public void emitEvent(Object event) {
    this.setChanged();
    this.notifyObservers(event);
  }
  
  public int getStandardPathLength() {
    return 1;
  }
  
  /*
   * minions can be repulse by certain cells
   */
  public int getMinionPathLength() {
    return getStandardPathLength() + Constants.EFFECT_REPULSE_WEIGHT * (minionRepulse ? 1 : 0);
  }

  public void resetEffects() {
    minionRepulse = false;
  }
  
  public void addMinionRepulseEffect() {
    minionRepulse = true;
    this.emitEvent(new RepulseEvent());
  }
  
  /**
   * Get a cell at the specified distance delta from this cell
   * @param delta
   * @return
   */
  public Cell cellFromHere(Vector2D delta) {
	  return Referee.playfield.getCell(Vector2D.add(pos, delta));
  }
  
  public Cell nextCell(int dir) {
	  
	  Vector2D nextPos;

	  switch(dir) {
	  case Constants.UP:
		  nextPos = Vector2D.add(pos, Vector2D.UP);
		  break;
	  case Constants.RIGHT:
		  nextPos = Vector2D.add(pos, Vector2D.RIGHT);
		  break;
	  case Constants.DOWN:
		  nextPos = Vector2D.add(pos, Vector2D.DOWN);
		  break;
	  case Constants.LEFT:
		  nextPos = Vector2D.add(pos, Vector2D.LEFT);
		  break;
	  default:
		  nextPos = new Vector2D(pos);
	  }
	  
	  return Referee.playfield.getCell(nextPos);
  }
  
  public String toString() {
	  return "Cell: " + pos + " " + type;
  }
}
