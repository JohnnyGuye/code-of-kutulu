package codeofkutulu.view.nodes;

public enum CellProximityStyle {
  SOLO(0), 
  LEFT(1), 
  RIGHT(2), 
  UP(3), 
  DOWN(4), 
  HORIZONTAL(5), 
  VERTICAL(6), 
  CORNER_UP_LEFT(7), 
  CORNER_UP_RIGHT(1*8+0), 
  CORNER_DOWN_LEFT(1*8+1), 
  CORNER_DOWN_RIGHT(1*8+2), 
  T_UP(1*8+3), 
  T_RIGHT(1*8+4), 
  T_DOWN(1*8+5), 
  T_LEFT(1*8+6), 
  SQUARE(1*8+7);
  
  public final int tileIndex;
  private CellProximityStyle(int tileIndex) {
    this.tileIndex = tileIndex;
  }
}
