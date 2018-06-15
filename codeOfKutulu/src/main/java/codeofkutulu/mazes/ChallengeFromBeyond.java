package codeofkutulu.mazes;

public class ChallengeFromBeyond extends Maze {

	final static String[] map = {
		"###############",
		"#w.....###...w#",
		"#.#.##.###.##.#",
		"#.#.....#.....#",
		"#...###...#.#.#",
		"###.....#.#.#.#",
		"####.##S#.#...#",
		"###...S.S...###",
		"#...#.#S##.####",
		"#.#.#.#.....###",
		"#.#.#...###...#",
		"#.....#.....#.#",
		"#.##.###.##.#.#",
		"#w...###.....w#",
		"###############"
	};

	@Override
	public String[] getMap(){ return map; }

	public ChallengeFromBeyond() {
		super("The challenge from beyond");
	}
}
