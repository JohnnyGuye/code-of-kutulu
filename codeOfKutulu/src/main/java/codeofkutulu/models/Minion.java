package codeofkutulu.models;

import static codeofkutulu.Constants.MINION_SEARCH_CYCLE_DIRECTIONS;
import static codeofkutulu.Constants.WANDERER_SPAWN_TIME;

import java.util.List;

import com.codingame.game.Referee;

import codeofkutulu.Constants;
import codeofkutulu.models.events.SpookEvent;
import codeofkutulu.pathfinder.PathFinder;
import codeofkutulu.pathfinder.PathFinder.PathFinderResult;

public abstract class Minion extends Unit {
  private static final PathFinder MINION_PATH_FINDER = new PathFinder().withWeightFunction(Cell::getMinionPathLength);

  int dirOffset; // used to change direction to search on A*

  protected MinionState state = MinionState.SPAWNING;

  public int spawnCountdown = Constants.WANDERER_SPAWN_TIME; // time before
                                                             // invocation
  public int lifeTimeCountdown = Constants.WANDERER_LIFE_TIME;
  PlayerUnit target;

  public PlayerUnit getTarget() {
    return target;
  }

  public int getspawnCountdown() {
    return spawnCountdown;
  }

  public Minion(int id) {
    super(id);

    this.state = MinionState.SPAWNING;
    this.spawnCountdown = WANDERER_SPAWN_TIME;
    this.dirOffset = MINION_SEARCH_CYCLE_DIRECTIONS ? Constants.random.nextInt(Constants.MAX_DIRS) : 0;
  }

  public void update() {
    switch (state) {
    case SPAWNING:
      spawnCountdown--;
      if (spawnCountdown == 0) {
        state = MinionState.WANDERING;
        lifeTimeCountdown = Constants.WANDERER_LIFE_TIME;
      }
      break;
    case RECALLING_FAILURE:
    case RECALLING_SUCCESS:
      break;
    default:
      move();
      lifeTimeCountdown--;
      if (lifeTimeCountdown == 0)
        recallFailure(); // Gosh...I'm such a failure
    }
  }

  protected abstract void move();

  /**
   * get random order to test targets, but try previous target first
   */
  private static int[] fisherYatesShuffle(PlayerUnit target) {
    int[] shuffle = new int[Referee.livingPlayers.size()];
    for (int i = 0; i < shuffle.length; i++) shuffle[i] = i;

    int start = 0;
    if (target != null && Referee.livingPlayers.contains(target)) {
      int index = Referee.livingPlayers.indexOf(target);
      shuffle[index] = 0;
      shuffle[0] = index;
      start = 1;
    }
    for (int i = start; i < shuffle.length - 1; i++) {
      int rnd = Constants.random.nextInt(shuffle.length - i) + i;
      int tmp = shuffle[i];
      shuffle[i] = shuffle[rnd];
      shuffle[rnd] = tmp;
    }
    return shuffle;
  }

  /**
   * chase closest player If 2 or more players are at same distance, the minion
   * will keep its target
   */
  protected void chaseClosestPlayers() {
    MINION_PATH_FINDER.withOffset(Referee.turn);
    PathFinderResult bestPath = null;
    PlayerUnit newTarget = null;
    int[] targetOrder = fisherYatesShuffle(target);

    for (int index : targetOrder) {
      PlayerUnit player = Referee.livingPlayers.get(index);
      PathFinderResult findPath = MINION_PATH_FINDER
          .from(this.getCurrentCell())
          .to(player.getCurrentCell())
          .findPath();

      if (findPath == PathFinderResult.NO_PATH)
        continue; // no path to player, ignore
      if (bestPath == null // no current best path
          || findPath.weightedLength < bestPath.weightedLength // OR better distance
          || (findPath.weightedLength == bestPath.weightedLength && player == target) // keep target if same distance
      ) {
        newTarget = player;
        bestPath = findPath;
      }
    }
    if (bestPath != null) {
      target = newTarget;
      if (bestPath.hasNextCell()) {
        move(bestPath.getNextCell());
      }
    }
  }

  /**
   * Pick one player and chase him
   */
  // private void chaseTarget() {
  //
  // // Wait master, you picked a dead target for me :/
  // if(!Referee.livingPlayers.contains(target)) {
  // recallFailure();
  // return;
  // }
  //
  // PathFinderResult findPath = MINION_PATH_FINDER
  // .from(this.getCurrentCell())
  // .to(target.getCurrentCell())
  // .findPath();
  //
  // if (!findPath.hasNextCell()) return;
  //
  // move(findPath.getNextCell());
  // }

  /**
   * Basically return false. What did you expect ?
   * 
   * @return
   */
  protected boolean frightenPlayers() {
    return false;
  }

  /**
   * Spook the players in that particular sell
   * 
   * @param cell
   * @return
   */
  protected boolean spookPlayersInCell(Cell cell) {
    cell.emitEvent(new SpookEvent());

    List<PlayerUnit> frightenPlayers = Referee.playfield.getPlayersInCell(cell);
    frightenPlayers.forEach(player -> player.spook(Constants.MINION_SANITY_LOST));

    return !frightenPlayers.isEmpty();
  }

  public boolean isSpawning() {
    return state == MinionState.SPAWNING;
  }

  public boolean isStunned() {
    return state == MinionState.STUNNED;
  }

  public boolean isRushing() {
    return state == MinionState.RUSH;
  }

  public boolean isPreparingRush() {
    return state == MinionState.STALKING;
  }

  protected void recallSuccess() {
    state = MinionState.RECALLING_SUCCESS;
  }

  protected void recallFailure() {
    state = MinionState.RECALLING_FAILURE;
  }

  public boolean isBeingRecalled() {
    return state == MinionState.RECALLING_FAILURE || isBeingRecalledAfterSuccess();
  }

  public boolean isBeingRecalledAfterSuccess() {
    return state == MinionState.RECALLING_SUCCESS;
  }

  protected void randomChase() {
    boolean ok = false;
    do {
      int dir = Constants.random.nextInt(Constants.MAX_DIRS);
      Vector2D newPos = Vector2D.add(pos, Constants.vdir[dir]);

      Cell cell = Referee.playfield.getCell(newPos);
      if (cell.isWalkable()) {
        ok = true;
        pos.set(newPos);
      }
    } while (!ok);
  }

  public String toOutput() {
    StringBuffer output = new StringBuffer()
        .append(Constants.unittypes[getType()]).append(" ")
        .append(id).append(" ")
        .append(pos.x).append(" ")
        .append(pos.y).append(" ")
        .append(getCountdown()).append(" ")
        .append(state.getId()).append(" ") // param1
        .append(target != null && !target.isDead() ? target.getId() : -1).append(" ") // param2
        ;
    
    return output.toString().trim();
  }

  public abstract int getType();

  public Object getId() {
    return id;
  }

  public MinionType type() {
    return MinionType.DUMMYNION;
  }

  public int getCountdown() {
    switch (state) {
    case SPAWNING:
      return spawnCountdown;
    default:
      return lifeTimeCountdown;
    }
  }

  public MinionState getState() {
    return state;
  }
}
