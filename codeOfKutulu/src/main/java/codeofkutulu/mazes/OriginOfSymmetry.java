package codeofkutulu.mazes;

public class OriginOfSymmetry extends Maze {

	final static String[] map = {
		"####################",
		"##................##",
		"#.w###.######.###w.#",
		"#.#....#....#....#.#",
		"#.#.##.#.##.#.##.#.#",
		"#...##...##...##...#",
		"#.#...U#....#U...#.#",
		"#.###.##S##S##.###.#",
		"#.###.##S##S##.###.#",
		"#.#...U#....#U...#.#",
		"#...##...##...##...#",
		"#.#.##.#.##.#.##.#.#",
		"#.#....#....#....#.#",
		"#.w###.######.###w.#",
		"##................##",
		"####################"
	};

	@Override
	public String[] getMap(){ return map; }

	public OriginOfSymmetry() {
		super("Origin of symmetry");
	}
}