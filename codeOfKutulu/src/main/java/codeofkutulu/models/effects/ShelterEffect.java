package codeofkutulu.models.effects;

import com.codingame.game.Referee;

import codeofkutulu.Constants;
import codeofkutulu.models.Cell;
import codeofkutulu.models.PlayerUnit;
import codeofkutulu.models.events.ShelterEvent;

public class ShelterEffect extends Effect {
  Cell cell;

  public ShelterEffect(Cell cell) {
    super(null);
    this.cell = cell;
    refill();
  }

  public void refill() {
    tick = Constants.EFFECT_MAX_SHELTER_ENERGY;
  }

  @Override
  public int getType() {
    return Constants.EFFECT_SHELTER_TYPE;
  }

  @Override
  public Cell getCurrentCell() {
    return cell;
  }
  
  @Override
  protected void doRealEffect() {
    cell.emitEvent(new ShelterEvent(tick));
    int totalHeal = 0;
    for (PlayerUnit p: Referee.livingPlayers) {
      if (p.getCurrentCell()== cell) {
        p.heal(Constants.EFFECT_SHELTER_HEAL);
        totalHeal+=1;
      }
    }
    tick = Math.max(0, tick-totalHeal);
  }
  
  @Override
  public String toOutput() {
    return super.toOutput();
  }
}
