package codeofkutulu.models;

import codeofkutulu.Constants;
import codeofkutulu.models.events.SpawnEvent;

public class MinionWanderer extends Minion {

  public MinionWanderer(int id) {
    super(id);

    this.spawnCountdown = Constants.WANDERER_SPAWN_TIME;
  }

  @Override
  public int getType() {
    return Constants.WANDERER_TYPE;
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
        state = MinionState.WANDERING;
        lifeTimeCountdown = Constants.WANDERER_LIFE_TIME;
      } else {
        getCurrentCell().emitEvent(new SpawnEvent());
      }
      break;
    case RECALLING_FAILURE:
    case RECALLING_SUCCESS:
      break;
    default:
      move();
      if (frightenPlayers())
        recallSuccess();// did my job, recall me master
      else {
        lifeTimeCountdown--;
        if (lifeTimeCountdown == 0)
          recallFailure(); // sorry master, didn't succeed in time
      }
    }
  }

  /**
   * (Basic) Line of sight of algorithm
   * 
   * @return
   */
  @SuppressWarnings("unused")
@Override
  protected boolean frightenPlayers() {
    boolean playerSpooked = false;

    playerSpooked |= spookPlayersInCell(getCurrentCell());

//    if (Constants.WANDERER_FRIGHTEN_RADIUS > 0) {
//      for (int i = 0; i < Constants.MAX_DIRS; i++) {
//        for (int delta = 1; delta <= Constants.WANDERER_FRIGHTEN_RADIUS; delta++) {
//          Cell cell = Referee.playfield.getCell(Vector2D.mult(Constants.vdir[i], delta).add(pos));
//          if (!cell.isWalkable())
//            break;
//          playerSpooked |= spookPlayersInCell(cell);
//        }
//      }
//    }
    return playerSpooked;
  }

  @Override
  public MinionType type() {
    return MinionType.WANDERER;
  }
}
