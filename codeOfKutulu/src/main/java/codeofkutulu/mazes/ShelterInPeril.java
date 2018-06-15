package codeofkutulu.mazes;

public class ShelterInPeril extends Maze {
	final static String[] map = {
			"#############",
			"#...........#",
			"#.#.##.##.#.#",
			"#..S##.##S..#",
			"#.###...###.#",
			"#.##.wUw.##.#",
			"#....U#U....#",
			"#.##.wUw.##.#",
			"#.###...###.#",
			"#..S##.##S..#",
			"#.#.##.##.#.#",
			"#...........#",
			"#############",

	};

	@Override
	public String[] getMap() {
		return map;
	}

	public ShelterInPeril() {
		super("Shelter in Peril");
	}
}
