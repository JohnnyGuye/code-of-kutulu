package codeofkutulu.view.nodes;

import static codeofkutulu.Constants.CELL_SIZE;

import java.util.Map;
import java.util.Observable;

import com.codingame.game.Referee;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Rectangle;

import codeofkutulu.Rule;
import codeofkutulu.models.Cell;
import codeofkutulu.models.events.PlanEvent;
import codeofkutulu.models.events.RepulseEvent;
import codeofkutulu.models.events.ShelterEvent;
import codeofkutulu.models.events.SpawnEvent;
import codeofkutulu.models.events.SpookEvent;
import codeofkutulu.tooltipModule.TooltipModule;
import codeofkutulu.view.TileFactory;

public class GroundNode extends CellNode {
  private static final int DARK_GREEN_TINT = 0x225522;

  Rectangle effectRectangle;
  int plan = 0;
  int spook = 0;
  int shelter = 0;
  int spawn = 0;
  int repulse = 0;
  
  public GroundNode(GraphicEntityModule entityManager, Cell model) {
    super(entityManager, model);
  }

  @Override
  protected void createView() {
    this.background = entityManager.createSprite()
        .setImage(TileFactory.getGroundImageName(posBasedPseudoRandom(12)))
        .setBaseHeight(CELL_SIZE)
        .setBaseWidth(CELL_SIZE)
        .setAlpha(1.0)
        .setZIndex(-1);

    this.effectRectangle = entityManager.createRectangle()
        .setAlpha(0.0)
        .setX(PADDING)
        .setY(PADDING)
        .setWidth(CELL_SIZE - PADDING * 2)
        .setHeight(CELL_SIZE - PADDING * 2)
        .setZIndex(5);

    switch (model.type) {
    case SHELTER:
      break;
    default:
      if (Rule.HAS_WANDERER && model.isSpawnPoint()) {
        this.background.setImage(TileFactory.getSpawnPoint());
      }

    }

    this.group.add(effectRectangle);
  }
  
  
  @Override
  public void updateView() {
    if (Referee.isKutuluComing()) {
      background.setTint(DARK_GREEN_TINT);
    }

    resetEffects();
    updateShelterEffect();
    updateSpawnEffect();
    updatePlayerEffects();
  }
  
  private void resetEffects() {
    effectRectangle.setAlpha(0.0);
  }

  private void updatePlayerEffects() {
    if (plan > 0) {
      effectRectangle.setFillColor(0x0000FF).setAlpha(Math.min(1.0, 0.2 * plan));
    } else if (repulse > 0) {
      effectRectangle.setFillColor(0xFFFF00).setAlpha(Math.min(1.0, 0.3 * repulse));
    }
  }

  private void updateShelterEffect() {
    if (Rule.HAS_SHELTER && model.isShelter()) {
      if (shelter == 0) {
        this.background.setImage(TileFactory.getShelterOffSpriteName());
      } else {
        this.background.setImage(TileFactory.getShelterOnSpriteName());
      }
    }
  }

  private void updateSpawnEffect() {
    if (model.isSpawnPoint()) {
      if (spawn == 0) {
        this.background.setImage(TileFactory.getSpawnPoint());
      } else {
        this.background.setImage(TileFactory.getActiveSpawnPoint());
      }
    }
  }

  
  private int posBasedPseudoRandom(int range) {
    return ((model.pos.x + model.pos.y) % range);
  }
  
  @Override
  public void updateToolTip(TooltipModule tooltipModule) {
    if (Rule.HAS_SHELTER && model.isShelter()) {
      Map<String, Object> params = tooltipModule.getParams(getTooltipEntityId());
      params.put("entity", "shelter");
      params.put("energy", shelter);
    }
  }
  
  @Override
  public void teardown() {
    resetFlags();
  }

  private void resetFlags() {
    plan = 0;
    spook = 0;
    shelter = 0;
    spawn = 0;
    repulse = 0;
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg instanceof ShelterEvent) {
      ShelterEvent event = (ShelterEvent) arg;
      shelter += event.energy;
    }
    if (arg instanceof PlanEvent) {
      plan++;
    }
    if (arg instanceof SpookEvent) {
      spook++;
    }
    if (arg instanceof RepulseEvent) {
      repulse++;
    }
    if (arg instanceof SpawnEvent) {
      spawn++;
    }
  }
}
