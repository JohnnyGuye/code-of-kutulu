package codeofkutulu.mazes;

/**
 * Small maze to test replacement of player spawn points
 *
 */
public class OnlyOneCellMaze extends Maze {
  final static String[] map = { 
      "S"
  };  
  
  @Override
  public String[] getMap() 		{ 	return map; }
  
  public OnlyOneCellMaze() {
	  super("Smallest Test");
  }

}
