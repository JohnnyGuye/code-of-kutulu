package codeofkutulu.mazes;

public class Cog extends Maze {

	final static String[] map = {
      "###############",
      "######.U.######",
      "###....#....###",
      "##.S##.#.##S.##",
      "##.##.....##.##",
      "##.#..#.#..#.##",
      "#....##.##....#",
      "#U##...w...##U#",
      "#....##.##....#",
      "##.#..#.#..#.##",
      "##.##.....##.##",
      "##.S##.#.##S.##",
      "###....#....###",
      "######.U.######",
      "###############"
	};

	@Override
	public String[] getMap(){ return map; }

	public Cog() {
		super("Cog");
	}
}
