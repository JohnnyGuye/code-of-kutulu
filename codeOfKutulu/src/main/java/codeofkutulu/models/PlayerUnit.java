package codeofkutulu.models;

import java.util.ArrayList;
import java.util.List;

import com.codingame.game.PlayerAction;
import com.codingame.game.Referee;

import codeofkutulu.Constants;
import codeofkutulu.Rule;
import codeofkutulu.models.effects.Effect;
import codeofkutulu.models.effects.PlanEffect;
import codeofkutulu.models.effects.RepulseEffect;
import codeofkutulu.models.effects.YellEffect;
import codeofkutulu.models.events.SpookEvent;

public class PlayerUnit extends Unit {

  boolean isIsolated = true;
  public int sanity = Constants.PLAYER_MAX_SANITY;
  int remainingPlans = Constants.PLAYER_MAX_PLAN_EFFECT;
  int remainingLight = Constants.PLAYER_MAX_LIGHT_EFFECT;
  private List<Integer> yellsBy = new ArrayList<>();
  private boolean performingInvalidMove = false;

  public PlayerAction wantedAction = PlayerAction.WAIT;

  public PlayerUnit(int id) {
    super(id);
  }

  @Override
  protected void setOrientation(int ori) {
    this.orientation = ori;
  }
  
  public boolean yell(PlayerUnit opponent) {
    if (opponent == this) return false;
    if (opponent.pos.manhattanDistance(this.pos) <= Constants.EFFECT_YELL_RADIUS) {
      return opponent.stuck(this);
    }
    return false;
  }

  private boolean stuck(PlayerUnit player) {
    if (hasBeenStuckedBy(player.id)) return false;
    yellsBy.add(player.id);

    YellEffect effect = new YellEffect(player, this);
    Referee.effects.add(effect);

    return true;
  }

  public boolean hasBeenStuckedBy(int id) {
    return yellsBy.contains(new Integer(id));
  }
  
  public boolean isDead() {
    return sanity <= 0;
  }

  public void spook(int amount) {
    emitEvent(new SpookEvent());
    madden(amount);
  }

  public void madden(int amount) {
    sanity -= amount;
    if (sanity <= 0) {
      kill();
    }
  }

  public void kill() {
    sanity = 0;
  }

  public int getSanity() {
    return sanity;
  }

  public void heal(int points) {
    sanity = Math.min(Constants.PLAYER_MAX_SANITY, sanity + points);
  }

  public Effect castPlanEffect() {
    if (remainingPlans <= 0)
      return null;
    remainingPlans--;
    return new PlanEffect(this);
  }

  public Effect castLightEffect() {
    if (remainingLight <= 0)
      return null;
    remainingLight--;
    return new RepulseEffect(this);
  }

  public int getRemainingPlans() {
    return remainingPlans;
  }

  public int getRemainingLights() {
    return remainingLight;
  }

  public String toOutput() {
    StringBuffer output = new StringBuffer()
        .append(Constants.unittypes[Constants.PLAYER_TYPE]).append(" ") // type
        .append(id).append(" ") // id
        .append(pos.x).append(" ") // x 
        .append(pos.y).append(" ") // y
        .append(sanity).append(" ") // life
        .append(remainingPlans).append(" ") // param1
        .append(remainingLight).append(" ") // param2 
    ;
    
    return output.toString().trim();
  }

  public boolean isIsolated() {
    return isIsolated;
  }

  @SuppressWarnings("unused")
  public void updateIsolation(List<PlayerUnit> players) {
    this.isIsolated = Rule.HAS_ISOLATED_FEAR;
    if (Constants.IM_NOT_ALONE_RANGE == 0)
      return;

    for (PlayerUnit other : players) {
      if (other == this) {
        continue;
      }
      this.isIsolated &= other.pos.manhattanDistance(this.pos) > Constants.IM_NOT_ALONE_RANGE;
    }
  }

  public void spreadMadness() {
    this.madden(this.isIsolated ? Constants.SPREAD_MADNESS_PER_TURN_AMOUNT
        : Constants.SPREAD_MADNESS_PLAYER_PROX_PER_TURN_AMOUNT);
  }

  public boolean isStuck() {
    return Referee.effects.stream()
          .filter(effect -> effect.getType() == Constants.EFFECT_YELL_TYPE)
          .filter(effect -> ((YellEffect)effect).getReceiver() == this)
          .count() > 0;
  }

  public int getId() {
    return id;
  }

  public boolean isPerformingInvalidMove() {
    return performingInvalidMove;
  }

  public void setPerformingInvalidMove(boolean performingInvalidMove) {
    this.performingInvalidMove = performingInvalidMove;
  }

  public void resetPerformingInvalidMove() {
    setPerformingInvalidMove(false);
  }
}
