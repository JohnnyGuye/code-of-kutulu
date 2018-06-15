package codeofkutulu.models;

import java.util.Observable;

import com.codingame.game.Referee;

import codeofkutulu.Constants;

public abstract class Unit extends Observable {
  public int id;
  public Vector2D pos;
  public int orientation;

  public Unit(int id) {
    this.id = id;
    this.pos = new Vector2D();
    this.orientation = Constants.DOWN;
  }

  public void move(int x, int y) {
    int ori = Vector2D.matchDirection(new Vector2D(x, y).sub(pos).normalize());
    setOrientation(ori);
    this.pos.set(x, y);
  }

  protected void setOrientation(int ori) {
    this.orientation = ori != Constants.STAY ? ori : this.orientation;
  }

  public void move(Cell cell) {
    this.move(cell.pos.x, cell.pos.y);
  }

  public boolean isOnCell(Cell cell) {
    return cell.isWalkable() && cell.pos.equals(pos);
  }

  public Cell getCurrentCell() {
    return Referee.playfield.getCell(this);
  }

  public void emitEvent(Object event) {
    this.setChanged();
    this.notifyObservers(event);
  }
}
