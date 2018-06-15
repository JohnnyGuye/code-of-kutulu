package codeofkutulu.view.nodes;

import static codeofkutulu.Constants.CELL_SIZE;

import java.util.Map;

import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.GraphicEntityModule;

import codeofkutulu.Constants;
import codeofkutulu.models.Minion;
import codeofkutulu.models.MinionState;
import codeofkutulu.models.PlayerUnit;
import codeofkutulu.tooltipModule.TooltipModule;

public class WandererMinionNode extends MinionNode {
	
	public WandererMinionNode(GraphicEntityModule entityManager, Minion minion) {
		super(entityManager, minion);
	
		this.animation.setZIndex(2);
		
//		this.sight.setRadius((Constants.WANDERER_FRIGHTEN_RADIUS)*CELL_SIZE + CELL_SIZE / 2);
	}
	
	@Override
	public void updateView() {

		super.updateView();
		
    if (minion.getState() == MinionState.SPAWNING) {
      this.animation.setAnimation("spawn");
    } else {
      this.animation.setAnimation(Constants.directions[minion.orientation]);
    }
    this.animation.setVisible(true);
		
//		if (minion.isSpawning()) {
//			this.sight.setVisible(false);
//		}
		
		if (minion.isBeingRecalled()) {
			// small animation, make disappear
			this.animation.setAlpha(0.0, Curve.LINEAR);
			this.animation.setScaleX(0, Curve.LINEAR);
			this.animation.setScaleY(this.animation.getScaleY() * 2, Curve.EASE_IN_AND_OUT );
//			sight.setAlpha(0.0);
			
			// Shall we do something on recall ?
			
			setToBeRemoved(true);
		} else if(minion.getTarget() != null) {
			this.animation.setMaskTint(this.getTrackedPlayerColor(), Curve.EASE_IN_AND_OUT);
		}
	}
	
	@Override
	public void updateToolTip(TooltipModule tooltipModule) {
		super.updateToolTip(tooltipModule);
		
		Map<String, Object> params = tooltipModule.getParams(getTooltipEntityId());
		params.put("life", minion.lifeTimeCountdown);
	    PlayerUnit target = minion.getTarget();
		params.put("target", target != null ? target.id : "");
	}

}
