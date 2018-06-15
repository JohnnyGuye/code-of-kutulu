package codeofkutulu.view.nodes;

import static codeofkutulu.Constants.CELL_SIZE;

import java.util.Map;

import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.SpriteSheetLoader;
import com.codingame.gameengine.module.entities.Text;

import codeofkutulu.Constants;
import codeofkutulu.models.Minion;
import codeofkutulu.models.MinionState;
import codeofkutulu.tooltipModule.TooltipModule;
import codeofkutulu.view.AnimationFactory;
import codeofkutulu.view.AnimationWithMask;

public class SlasherMinionNode extends MinionNode {

  private Text statusMark;

  public SlasherMinionNode(GraphicEntityModule entityManager, Minion minion) {
    super(entityManager, minion);
    this.animation.setBaseName("Slasher");
    this.statusMark = this.entityManager.createText("")
        .setAnchorX(.5).setAnchorY(1)
        .setX(CELL_SIZE / 2)
        .setFillColor(0xFFFFFF);

//    this.sight.setRadius(CELL_SIZE / 2);

    addToGroup(statusMark);
  }

  @Override
  public void updateView() {
    super.updateView();

    this.animation.setVisible(true);
    if (minion.getState() == MinionState.SPAWNING) {
      this.animation.setAnimation("spawn");
    } else {
      this.animation.setAnimation(Constants.directions[minion.orientation]);
      if (this.minion.getTarget() != null) {
        this.animation.setMaskTint(this.getTrackedPlayerColor(), Curve.EASE_IN_AND_OUT);
      } else {
        this.animation.setMaskTint(0xFFFFFF);
      }
    }
    
    this.animation.setVisible(true);
    
    switch (minion.getState()) {
    case SPAWNING:
      break;
    case PREPARING_RUSH:
      this.statusMark.setText("!");
      break;
    case RUSH:
      this.statusMark.setText("Fwuush!");
      break;
    case STUNNED:
      this.statusMark.setText("....".substring((int) ((3 * minion.getCountdown() + 0.5) / Constants.SLASHER_RUSH_STUN)));
      break;
    default:
      this.statusMark.setText("");
      this.animation.setVisible(true);
    }
  }

  @Override
  public void updateToolTip(TooltipModule tooltipModule) {
    super.updateToolTip(tooltipModule);

    Map<String, Object> params = tooltipModule.getParams(getTooltipEntityId());
    params.put("state", minion.getState());
  }

}
