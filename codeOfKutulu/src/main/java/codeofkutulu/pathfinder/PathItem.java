package codeofkutulu.pathfinder;

import codeofkutulu.models.Cell;

public class PathItem {
  public int cumulativeLength = 0;
  int totalPrevisionalLength = 0;
  PathItem precedent = null;
  Cell pos;

  public Cell getPosition() {
    return pos;
  }

  public int length() {
    PathItem i = this;
    int count = 0;
    while (i != null) {
      count++;
      i = i.precedent;
    }
    return count;
  }
}