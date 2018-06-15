package codeofkutulu.pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.codingame.game.Referee;

import codeofkutulu.Constants;
import codeofkutulu.models.Cell;
import codeofkutulu.models.Playfield;
import codeofkutulu.models.Vector2D;

/**
 * PATH : A*
 *
 */
public class AStar {
  Map<Cell, PathItem> closedList = new HashMap<>();
  List<PathItem> openList = new ArrayList<>();
  List<PathItem> path = new ArrayList<>();

  Playfield playfield;
  Cell from;
  Cell target;
  int dirOffset;
  private Function<Cell, Integer> weightFunction;

  public AStar(Playfield playfield, Cell from, Cell target, int dirOffset, Function<Cell, Integer> weightFunction) {
    this.playfield = playfield;
    this.from = from;
    this.target = target;
    this.dirOffset = dirOffset;
    this.weightFunction = weightFunction;
  }

  public List<PathItem> find() {
    PathItem item = getPathItemLinkedList();
    path.clear();
    if (item != null) {
      calculatePath(item);
    }
    return path;
  }

  void calculatePath(PathItem item) {
    PathItem i = item;
    while (i != null) {
      path.add(0, i);
      i = i.precedent;
    }
  }

  PathItem getPathItemLinkedList() {
    PathItem root = new PathItem();
    root.pos = this.from;
    openList.add(root);

    while (openList.size() > 0) {
      PathItem visiting = openList.remove(0); // imagine it's the best
      Cell cell = visiting.pos;

      if (cell == target) {
        return visiting;
      }
      if (closedList.containsKey(cell)) {
        continue;
      }
      closedList.put(cell, visiting);
      Cell nextCell;

      for (int i = 0; i < Constants.MAX_DIRS; i++) {
        int index = (i + dirOffset) % Constants.MAX_DIRS;
        nextCell = playfield.getCell(Vector2D.add(Constants.vdir[index], cell.pos));
        if (nextCell.isWalkable())
          addToOpenList(visiting, cell, nextCell);
      }

      // sort with distances
      Collections.sort(openList, new Comparator<PathItem>() {
        @Override
        public int compare(PathItem o1, PathItem o2) {
          return Integer.compare(o1.totalPrevisionalLength, o2.totalPrevisionalLength);
        }
      });
    }
    return null; // not found !
  }

  void addToOpenList(PathItem visiting, Cell fromCell, Cell toCell) {
    if (closedList.containsKey(toCell)) {
      return;
    }
    PathItem pi = new PathItem();
    pi.pos = toCell;
    pi.cumulativeLength = visiting.cumulativeLength + weightFunction.apply(toCell);
    int manh = fromCell.pos.manhattanDistance(toCell.pos);
    pi.totalPrevisionalLength = pi.cumulativeLength + manh;
    pi.precedent = visiting;
    openList.add(pi);
  }
}
/** End of PATH */