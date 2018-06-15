package codeofkutulu.mazes;

public class OneOnOne extends Maze {

	final static String[] map = {
		"####################",
		"#...#############w##",
		"#.#.#############.##",
		"#.#....##.........##",
		"#.#.##.#..#.##.#####",
		"#.#.##.#.##.##.#####",
		"#.#...U...#.......##",
		"#.####.##.#######.##",
		"#..###.##.###...#.##",
		"##........S##.#.#.##",
		"##.#.#.##S........##",
		"##.#...###.##.###..#",
		"##.#######.##.####.#",
		"##.......#...U...#.#",
		"#####.##.##.#.##.#.#",
		"#####.##.#..#.##.#.#",
		"##.........##....#.#",
		"##.#############.#.#",
		"##w#############...#",
		"####################"
	};

	@Override
	public String[] getMap(){ return map; }

	public OneOnOne() {
		super("One on one");
	}
}