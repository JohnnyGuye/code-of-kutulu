package codeofkutulu.mazes;

/**
 * Designed to be a map where the main threat are slashers
 * @author johnny
 *
 */
public class SlasherHell extends Maze {

	final static String[] map = {
		"######################",
		"#.......######.......#",
		"#.###.#........#.###.#",
		"#.#S#.##########.#S#.#",
		"#.#.#.###w##w###.#.#.#",
		"#....................#",
		"#.#.#.##########.#.#.#",
		"#.#.#..U......U..#.#.#",
		"#.#.###.######.###.#.#",
		"#.#.###.######.###.#.#",
		"#.#.#..U......U..#.#.#",
		"#.#.#.##########.#.#.#",
		"#....................#",
		"#.#.#.###w##w###.#.#.#",
		"#.#S#.##########.#S#.#",
		"#.###.#........#.###.#",
		"#.......######.......#",
		"######################"
	};

	@Override
	public String[] getMap(){ return map; }

	public SlasherHell() {
		super("Slasher's hell");
	}
}