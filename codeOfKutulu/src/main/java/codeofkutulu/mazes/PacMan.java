package codeofkutulu.mazes;

public class PacMan extends Maze {
  final static String[] map = { 
	  "###############",
      "#.....###.....#",
      "#.###.....###.#",
      "#.#...#.#...#.#",
      "#.#.#.#.#.#.#.#",
      "#....S...S....#",
      "##.####w####.##",
      "#....S...S....#",
      "#.#.#.#.#.#.#.#",
      "#.#...#.#...#.#",
      "#.###.....###.#",
      "#.....###.....#",
      "###############"
  };
  
  @Override
  public String[] getMap() { return map; } 
  
  public PacMan() {
	  super("Pac man");
  }
}
