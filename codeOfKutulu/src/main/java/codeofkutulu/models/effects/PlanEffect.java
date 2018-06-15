package codeofkutulu.models.effects;

import java.util.HashSet;
import java.util.Set;

import com.codingame.game.Referee;

import codeofkutulu.Constants;
import codeofkutulu.models.Cell;
import codeofkutulu.models.PlayerUnit;
import codeofkutulu.models.events.PlanEvent;

public class PlanEffect extends TimerEffect {
  public PlanEffect(PlayerUnit player) {
    super(player);
    setDuration(Constants.EFFECT_PLAN_MAX_DURATION);
  }

  @Override
  public int getType() {
    return Constants.EFFECT_PLAN_TYPE;
  }
  
  @Override
  protected void doRealEffect() {
    
    caster.heal(Constants.EFFECT_PLAN_HEAL);
    
    doBFSEffect(Referee.playfield.getCell(caster), 
        Constants.EFFECT_PLAN_HEAL_RADIUS, 
        (Cell cell) -> { cell.emitEvent(new PlanEvent());
      
      for (PlayerUnit player : Referee.livingPlayers) {
        if (player != caster && player.isOnCell(cell)) {
          player.heal(Constants.EFFECT_PLAN_HEAL);
          caster.heal(Constants.EFFECT_PLAN_HEAL);
        }
      }
    });
  }
}
