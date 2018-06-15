package codeofkutulu.view.nodes;

import static codeofkutulu.Constants.CELL_SIZE;

import java.util.Map;
import java.util.Observable;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;

import codeofkutulu.Constants;
import codeofkutulu.Rule;
import codeofkutulu.models.PlayerUnit;
import codeofkutulu.models.events.SpookEvent;
import codeofkutulu.tooltipModule.TooltipModule;
import codeofkutulu.view.AnimationWithMask;
import codeofkutulu.view.TileFactory;

public class PlayerNode extends AbstractNode {
  private Player cokPlayer;
  private PlayerUnit model;

  private Group group;
  private Sprite rip;

  AnimationWithMask animation;
  
  boolean spooked;

  public PlayerNode(GraphicEntityModule entityManager, Player cokPlayer, PlayerUnit player) {
    super(entityManager);
    this.cokPlayer = cokPlayer;
    this.model = player;
    player.addObserver(this);
    createView();
  }

  @Override
  public void updateView() {
    this.group.setX(model.pos.x * CELL_SIZE + (int)(4 * (cokPlayer.getIndex()-1.5)))
        .setY(model.pos.y * CELL_SIZE - CELL_SIZE / 4);

    
    if (model.isDead()) {
      animation.setVisible(false);
      rip.setVisible(true);
      group.setZIndex(1);
      
      setToBeRemoved(true);
    } else {
      if (model.isStuck()) {
        this.animation.setAnimation("stun");
      } else {
        this.animation.setAnimation(Constants.directions[model.orientation]);
      }
      this.animation.setTint(spooked ? 0x222222 : 0xFFFFFF);
    }
  }

  @Override
  public void teardown() {
    spooked = false;
  }

  private void createView() {
    rip = entityManager.createSprite()
        .setImage(TileFactory.getTomb())
        .setAlpha(.8)
        .setZIndex(0)
        .setTint(cokPlayer.getColorToken())
        .setBaseWidth(CELL_SIZE).setBaseHeight(CELL_SIZE).setVisible(false);

    animation = new AnimationWithMask(entityManager, "Player");
    animation.setScale(1.6);
    animation.setMaskTint(cokPlayer.getColorToken());
    this.group = this.entityManager.createGroup(animation.animation, animation.mask, rip);
  }

  @Override
  public void update(Observable player, Object event) {
    super.update(player, event);
    if (event instanceof SpookEvent) {
      spooked = true;
    }
  }

  @Override
  int getTooltipEntityId() {
    return group.getId();
  }

  @Override
  public void updateToolTip(TooltipModule tooltipModule) {
    if (model.isDead()) {
      tooltipModule.removeEntity(getTooltipEntityId());
      return;
    }
    Map<String, Object> params = tooltipModule.getParams(getTooltipEntityId());
    params.put("entity", "player");
    params.put("id", model.id);
    params.put("sanity", model.getSanity());
    if (Rule.HAS_ISOLATED_FEAR) {
      params.put("isolated", model.isIsolated());
    }
    StringBuilder items = new StringBuilder();
    if (Rule.HAS_PLAN) {
      items.append("plans: ").append(model.getRemainingPlans());
    }
    if (Rule.HAS_LIGHT) {
      if (items.length() > 0) items.append(" / ");
      items.append("lights: ").append(model.getRemainingLights());
    }
    params.put("items",  items.toString());
  }

  @Override
  public Entity<?> getPrimaryNode() {
    return group;
  }
}
