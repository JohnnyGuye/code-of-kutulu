package codeofkutulu.mazes;

public class MinimalSpawningThree extends Maze {

	final static String[] map = {
		"#################",
		"#U.....U##......#",
		"#.###.###..#.##.#",
		"#.###..##.##.##.#",
		"#.####..#...S...#",
		"#...###.#.##.##.#",
		"#.#..##.#.##.#..#",
		"#U##.....w.....##",
		"#######.#.#######",
		"##.....w.w.....##",
		"#..#.##.#.##.#..#",
		"#.##.##.#.##.##.#",
		"#...S...#...S...#",
		"#.##.##.#.##.##.#",
		"#.##.#..#..#.##.#",
		"#......###......#",
		"#################"
	};

	@Override
	public String[] getMap(){ return map; }

	public MinimalSpawningThree() {
		super("Minimal spawning three");
	}
}