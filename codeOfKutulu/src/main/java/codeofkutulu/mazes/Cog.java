package codeofkutulu.mazes;

public class Cog extends Maze {

	final static String[] map = {
		"###############",
		"######.U.######",
		"###....#....###",
		"##..##.#.##..##",
		"##.##.....##.##",
		"##.#..#.#..#.##",
		"#....##.##....#",
		"#U##...w...##U#",
		"#....##.##....#",
		"##.#..#.#..#.##",
		"##.##.....##.##",
		"##..##.#.##..##",
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
