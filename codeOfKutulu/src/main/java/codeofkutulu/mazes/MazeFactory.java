package codeofkutulu.mazes;

public class MazeFactory {
	private static final Maze Hypersonic = new Hypersonic();
	private static final Maze OriginOfSymmetry = new OriginOfSymmetry();
	private static final Maze PacMan = new PacMan();
	private static final Maze ShelterMe = new ShelterMe();
	private static final Maze SlasherHell = new SlasherHell();
	private static final Maze TheRandomMap = new TheRandomMap();
	private static final Maze Typhoon = new Typhoon();
	private static final Maze FourOfAKind = new FourOfAKind();
	private static final Maze OneOnOne = new OneOnOne();
	private static final Maze TooSmallForUsTwo = new TooSmallForUsTwo();
	private static final Maze MinimalSpawningThree = new MinimalSpawningThree();
	private static final Maze Oasis = new Oasis();
	private static final Maze Cross = new Cross();
	private static final Maze ShelterInPeril = new ShelterInPeril();
	private static final Maze Corridors = new Corridors();
	private static final Maze Roommates = new Roommates();
	private static final Maze ChallengeFromBeyond = new ChallengeFromBeyond();
	private static final Maze Cog = new Cog();
	private static final Maze HillClimbing = new HillClimbing();
	private static final Maze Pixelated = new Pixelated();

  private static MazeFactory mazeFactory;

	private static final Maze[] woodmaps = {
            PacMan,
            Hypersonic,
            Oasis,
            Corridors,
            OriginOfSymmetry,
            FourOfAKind
	};

	private final static Maze[] bronzeMaps = {
			FourOfAKind,
			Hypersonic,
			OriginOfSymmetry,
			ShelterMe,
			SlasherHell,
			Typhoon,
			Oasis,
			Cross,
			ShelterInPeril,
			Corridors,
			Roommates,
			PacMan,
			ChallengeFromBeyond,
			Cog,
			HillClimbing,
			Pixelated
	};

	public static Maze[] allMaps = woodmaps;

	public MazeFactory() {
	}

  public void activateBronzeMaps() {
    allMaps = bronzeMaps;
  }

	public Maze pickRandomMaze(int randomNumber) {
	  randomNumber = Math.abs(randomNumber);
		return allMaps[randomNumber % allMaps.length ];
	}

  public Maze getMaze(String name) {
    for (Maze maze : allMaps) {
      if (maze.getName().equals(name)) {
        return maze;
      }
    }
    return Maze.NO_MAZE;
  }

  public static MazeFactory getInstance() {
    if (mazeFactory == null) {
      synchronized (MazeFactory.class) {
        if (mazeFactory == null) {
          mazeFactory = new MazeFactory();
        }
      }
    }
    return mazeFactory;
  }


}
