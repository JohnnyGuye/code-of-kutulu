package codeofkutulu.models.effects;

import codeofkutulu.Constants;
import codeofkutulu.models.Cell;
import codeofkutulu.models.PlayerUnit;

public class YellEffect extends TimerEffect {
  private PlayerUnit receiver;

  public YellEffect(PlayerUnit caster, PlayerUnit receiver) {
    super(caster);
    setDuration(Constants.PLAYER_INITIAL_STUCK_ROUNDS);
    this.receiver = receiver;
  }
  
  @Override
  public int getType() {
    return Constants.EFFECT_YELL_TYPE;
  }

  @Override
  public Cell getCurrentCell() {
    return Cell.INVALID_CELL;
  }

  @Override
  protected void doRealEffect() {
    // do nothing, it is handled with commands
  }

  @Override
  protected String getParam2() {
    return ""+receiver.getId();
  }

  public PlayerUnit getReceiver() {
    return receiver;
  }
}
