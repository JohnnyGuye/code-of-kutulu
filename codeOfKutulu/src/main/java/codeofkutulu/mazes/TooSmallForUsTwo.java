package codeofkutulu.mazes;

public class TooSmallForUsTwo extends Maze {

	final static String[] map = {
		"###############",
		"#.....#U#.....#",
		"#.#w###.###w#.#",
		"#..##.....##..#",
		"#.##..#.#..##.#",
		"#.#..##.##..#.#",
		"#.#.###.###.#.#",
		"#.............#",
		"#.#.###.###.#.#",
		"#.#S#.#.#.#S#.#",
		"#.###.#.#.###.#",
		"#.............#",
		"###.###.###.###",
		"#....##.##....#",
		"#.##.......##.#",
		"###############"
	};

	@Override
	public String[] getMap(){ return map; }

	public TooSmallForUsTwo() {
		super("Too small for us two");
	}
}