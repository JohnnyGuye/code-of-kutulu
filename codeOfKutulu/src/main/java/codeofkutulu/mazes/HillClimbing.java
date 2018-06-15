package codeofkutulu.mazes;

public class HillClimbing extends Maze {

	final static String[] map = {
		"#################",
		"#S.............S#",
		"#.######.######.#",
		"#.##.........##.#",
		"#.#.w###.###w.#.#",
		"#.#.##.....##.#.#",
		"#.#.#..#.#..#.#.#",
		"#.#.#.##U##.#.#.#",
		"#......U.U......#",
		"#.#.#.##U##.#.#.#",
		"#.#.#..#.#..#.#.#",
		"#.#.##.....##.#.#",
		"#.#.w###.###w.#.#",
		"#.##.........##.#",
		"#.######.######.#",
		"#S.............S#",
		"#################"
	};

	@Override
	public String[] getMap(){ return map; }

	public HillClimbing() {
		super("Hill climbing");
	}
}
