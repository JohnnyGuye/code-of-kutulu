package codeofkutulu.view.nodes;

import java.util.Observable;
import java.util.Observer;

import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;

import codeofkutulu.tooltipModule.TooltipModule;

public abstract class AbstractNode implements Observer {
  protected GraphicEntityModule entityManager;
  private boolean toBeRemoved = false;
  
  public AbstractNode(GraphicEntityModule entityManager) {
    this.entityManager = entityManager;
  }

  public abstract void updateView();
  public void teardown() {
  }
  public boolean isToBeRemoved() {
    return toBeRemoved;
  }

  public void setToBeRemoved(boolean toRemoved) {
    this.toBeRemoved = toRemoved;
  }
  
  public abstract Entity<?> getPrimaryNode();
  
  @Override
  public void update(Observable o, Object arg) {
  }

  public void updateToolTip(TooltipModule tooltipModule) {
  }

  public void removeTooltip(TooltipModule tooltipModule) {
    tooltipModule.removeEntity(getTooltipEntityId());
  }

  int getTooltipEntityId() {
    return -1; // no tooltip
  }

}
