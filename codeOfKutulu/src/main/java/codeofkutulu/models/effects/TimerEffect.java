package codeofkutulu.models.effects;

import codeofkutulu.models.PlayerUnit;

public abstract class TimerEffect extends Effect {

  public TimerEffect(PlayerUnit player) {
    super(player);
  }

  protected void setDuration(int duration) {
    this.tick = duration;
  }

  public int getDuration() {
    return tick;
  }

  @Override
  public void doEffect() {
    super.doEffect();
    tick--;
  }
}
