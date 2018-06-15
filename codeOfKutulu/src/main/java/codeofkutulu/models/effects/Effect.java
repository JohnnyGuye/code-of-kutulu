package codeofkutulu.models.effects;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import codeofkutulu.Constants;
import codeofkutulu.models.Cell;
import codeofkutulu.models.PlayerUnit;

public abstract class Effect {
  protected PlayerUnit caster;
  protected int tick = 0;

  public Effect(PlayerUnit caster) {
    this.caster = caster;
  }

  public abstract int getType();

  public void stop() {
    tick = 0;
  }
  public boolean isFinished() {
    return tick <= 0;
  }

  public void doEffect() {
    if (isFinished()) return;
    doRealEffect();
  }

  protected abstract void doRealEffect();

  public Cell getCurrentCell() {
    return caster.getCurrentCell();
  }
  
  public String toOutput() {
    Cell cell = getCurrentCell();
    return new StringBuffer()
          .append(Constants.unittypes[getType()]).append(" ") // type
          .append("-1").append(" ") // index
          .append(cell.pos.x).append(" ") // x 
          .append(cell.pos.y).append(" ") // y
          .append(tick).append(" ") // life
          .append(getParam1()).append(" ") // param1
          .append(getParam2()).append(" ") // param2
          .toString().trim();
  }

  protected String getParam1() {
    return caster != null ? ""+caster.getId(): "-1";
  }

  protected String getParam2() {
    return "-1";
  }

  public PlayerUnit getCaster() {
    return caster;
  }
  
  protected void doBFSEffect(Cell start, int depth, Consumer<Cell> functionToApply ) {
    Set<Cell> visited = new HashSet<Cell>();
    Set<Cell> toVisit = new HashSet<>();
    toVisit.add(start);
    
    for (int i=0;i<=depth;i++) {
      Set<Cell> nextCellsToVisit = new HashSet<>();
      for(Cell cell : toVisit) {
        functionToApply.accept(cell);
        visited.add(cell);
        
        for (int dir=0;dir<4;dir++) {
          Cell next = cell.nextCell(dir);
          if (!next.isWalkable()) continue;
          if (visited.contains(next)) continue;
          if (toVisit.contains(next)) continue;
          nextCellsToVisit.add(next);
        }
      }
      toVisit = nextCellsToVisit;
    }
  }

}
