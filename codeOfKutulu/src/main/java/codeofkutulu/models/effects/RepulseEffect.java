package codeofkutulu.models.effects;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import codeofkutulu.Constants;
import codeofkutulu.models.Cell;
import codeofkutulu.models.PlayerUnit;

public class RepulseEffect extends TimerEffect {
  public RepulseEffect(PlayerUnit player) {
    super(player);
    setDuration(Constants.EFFECT_REPULSE_MAX_DURATION);
  }

  @Override
  public int getType() {
    return Constants.EFFECT_REPULSE_TYPE;
  }
  
  @Override
  protected void doRealEffect() {
    doBFSEffect(getCurrentCell(),
        Constants.EFFECT_REPULSE_RADIUS, 
        (Cell cell) -> cell.addMinionRepulseEffect());

  }
}
