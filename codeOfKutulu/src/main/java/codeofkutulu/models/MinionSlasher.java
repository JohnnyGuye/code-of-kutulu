package codeofkutulu.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.codingame.game.Referee;

import codeofkutulu.Constants;

public class MinionSlasher extends Minion {

  int stunCountdown = 0;
  Cell targetPositionBeforePreparation = null;

  private MinionSlasher(int id) {
    super(id);

    this.spawnCountdown = Constants.SLASHER_SPAWN_TIME;
  }

  public MinionSlasher(int id, PlayerUnit target) {
    this(id);

    this.pos.set(target.pos);
    this.target = target;
    this.targetPositionBeforePreparation = target.getCurrentCell();
  }

  @Override
  public int getType() {
    return Constants.SLASHER_TYPE;
  }

  @Override
  public int getCountdown() {
    switch (state) {
    case STALKING:
      return stunCountdown;
    case STUNNED:
      return stunCountdown;
    default:
      return super.getCountdown();
    }
  }

  @Override
  protected void move() {
    this.chaseClosestPlayers();
  }

  @Override
  public void update() {
    switch (state) {
    case SPAWNING:
      spawnCountdown--;
      if (spawnCountdown == 0) {

        state = MinionState.RUSH;
      }
      break;
    case STALKING:
      stunCountdown--;
      acquireTarget();
      if (stunCountdown <= 0) {
        this.state = MinionState.RUSH;
      }
      break;
    case RUSH:
      this.rush();
      state = MinionState.STUNNED;
      stunCountdown = Constants.SLASHER_RUSH_STUN;
      break;
    case STUNNED:
      stunCountdown--;
      if (stunCountdown == 0) {
        this.state = MinionState.WANDERING;
      }
      break;
    case RECALLING_FAILURE:
    case RECALLING_SUCCESS:
      break;
    case WANDERING:
    default:
      if (this.acquireTarget()) {
        stunCountdown = Constants.SLASHER_RUSH_PREP;
        this.state = MinionState.STALKING;
      } else {
        move();
      }
    }
  }

  /**
   * Manage to acquire a new target if it's in the slasher's line of sight When
   * acquired he goes into rush mode
   */
  boolean acquireTarget() {
    if (target != null && target.isDead()) {
      target = null;
      targetPositionBeforePreparation = null;
    }
    PlayerUnit previousTarget = target;
    if (target != null && isPlayerInLoS(target)) {
      target = previousTarget;
      targetPositionBeforePreparation = target.getCurrentCell();
      return true;
    }
    
    List<PlayerUnit> targets = acquireClosestTargets();
    if (targets.isEmpty()) {
      target = null;
      return false;
    } else if (targets.size() == 1) {
      target = targets.get(0);
      targetPositionBeforePreparation = target.getCurrentCell();
      return true;
    } else {
      // can't decide which one to target !
      target = null;
      targetPositionBeforePreparation = null;
      return false;
    }
  }

  private boolean isPlayerInLoS(PlayerUnit player) {
    for (int i = 0; i < Constants.MAX_DIRS; i++) {
      for (Cell cell = Referee.playfield.getCell(pos); cell.isWalkable(); cell = cell.cellFromHere(Constants.vdir[i])) {
        if (player.isOnCell(cell)) return true;
      }
    }
    return false;
  }
  
  private List<PlayerUnit> acquireClosestTargets() {
    int minDepth = Referee.maze.dim.tchebychevDistance();
    List<PlayerUnit> targets = new ArrayList<>();
    
    // Check each direction one after an other
    for (int i = 0; i < Constants.MAX_DIRS; i++) {
      for (Cell cell = Referee.playfield.getCell(pos); cell.isWalkable(); cell = cell.cellFromHere(Constants.vdir[i])) {

        List<PlayerUnit> possibleTargets = Referee.playersOnCell(cell);

        if (possibleTargets.isEmpty())
          continue;

        int depth = cell.pos.tchebychevDistance(pos);
        if (depth < minDepth) {
          targets.clear();
          targets.addAll(possibleTargets);
          minDepth = depth;
        } else if (depth == minDepth) {
          targets.addAll(possibleTargets);
        }
        break;
      }
    }
    targets = targets.stream().distinct().collect(Collectors.toList());
    return targets;
  }

  private void rush() {
    if (acquireTarget()) {
      move(target.getCurrentCell());
    } else if (targetPositionBeforePreparation != null) {
      move(targetPositionBeforePreparation);
    }
    this.frightenPlayers();

    resetTarget();
  }

  private void resetTarget() {
    this.target = null;
    this.targetPositionBeforePreparation = null;
  }

  /**
   * Spook players on it's own cell
   * 
   * @return
   */
  @Override
  protected boolean frightenPlayers() {
    return spookPlayersInCell(getCurrentCell());
  }

  @Override
  public MinionType type() {
    return MinionType.SLASHER;
  }
}
