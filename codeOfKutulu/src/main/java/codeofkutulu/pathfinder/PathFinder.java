package codeofkutulu.pathfinder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.codingame.game.Referee;

import codeofkutulu.models.Cell;

/*
 * Find the best path from a cell to another cell
 * currently : using Astar algorithm
 */
public class PathFinder {
  public static class PathFinderResult {
    public static final PathFinderResult NO_PATH = new PathFinderResult();
    public List<Cell> path = new ArrayList<>();
    public int weightedLength = -1;

    public boolean hasNextCell() {
      return path.size() > 1;
    }

    public Cell getNextCell() {
      return path.get(1);
    }

    public boolean hasNoPath() {
      return weightedLength == -1;
    }
  }

  Cell from = null;
  Cell to = null;
  int directionOffest = 0;
  private Function<Cell, Integer> weightFunction = Cell::getStandardPathLength;

  public PathFinder from(Cell cell) {
    from = cell;
    return this;
  }

  public PathFinder to(Cell cell) {
    to = cell;
    return this;
  }

  public PathFinder withOffset(int offset) {
    this.directionOffest = offset;
    return this;
  }

  public PathFinder withWeightFunction(Function<Cell, Integer> weightFunction) {
    this.weightFunction = weightFunction;
    return this;
  }

  public PathFinderResult findPath() {
    if (from == null || to == null) {
      return new PathFinderResult();
    }
    List<PathItem> pathItems = new AStar(Referee.playfield, from, to, directionOffest, weightFunction).find();
    if (pathItems.isEmpty()) {
      return PathFinderResult.NO_PATH;
    } else {
      PathFinderResult pfr = new PathFinderResult();
      pfr.path = pathItems.stream()
          .map(item -> item.getPosition())
          .collect(Collectors.toList());
      pfr.weightedLength = pathItems.get(pathItems.size() - 1).cumulativeLength;
      return pfr;
    }
  }
}
