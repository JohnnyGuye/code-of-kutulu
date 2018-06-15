package codeofkutulu.mazes;

/**
 * Hypersonic-ish map
 * 
 * @author johnny
 *
 */
public class Hypersonic extends Maze {
	final static String[] map = {
			"###############",
			"#.............#", 
			"#.#.#.#.#.#.#.#", 
			"#..w.......w..#", 
			"#.#.#.#.#.#.#.#", 
			"#....S...S....#", 
			"#.#.#.#.#.#.#.#",
			"#....S...S....#", 
			"#.#.#.#.#.#.#.#", 
			"#..w.......w..#", 
			"#.#.#.#.#.#.#.#", 
			"#.............#",
			"###############"
			};

	@Override
	public String[] getMap() {
		return map;
	}

	public Hypersonic() {
		super("Hypersonic");
	}
}
