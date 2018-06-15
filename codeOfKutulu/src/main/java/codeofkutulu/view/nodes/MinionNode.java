package codeofkutulu.view.nodes;

import static codeofkutulu.Constants.CELL_SIZE;

import java.util.Map;

import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;

import codeofkutulu.models.Minion;
import codeofkutulu.tooltipModule.TooltipModule;
import codeofkutulu.view.AnimationWithMask;
import codeofkutulu.view.ViewController;

public class MinionNode extends AbstractNode {

	public static final int PADDING = (int)(0.0 * CELL_SIZE);
	Minion minion;

	Group baseNode;
	AnimationWithMask animation;

	public MinionNode(GraphicEntityModule entityManager, Minion minion) {
		super(entityManager);

		this.minion = minion;

		this.animation = new AnimationWithMask(entityManager, "Wanderer");
		this.animation.setScale(1.8);
		this.animation.setMaskAlpha(1.0);

		baseNode = entityManager.createGroup(animation.animation, animation.mask/*, sight*/);
		
		this.setPosition();
		
		entityManager.commitEntityState(0, baseNode);
	}

	protected void addToGroup(Entity<?> entity) {
		baseNode.add(entity);
	}

	protected void removeFromGroup(Entity<?> entity) {
		baseNode.remove(entity);
		entity.setVisible(false);
	}

	private void setPosition() {
		baseNode.setX(minion.pos.x * CELL_SIZE + PADDING)
		.setY(minion.pos.y * CELL_SIZE + PADDING - CELL_SIZE / 4);
	}

	@Override
	public void updateView() {
		this.setPosition();
	}

	protected int getTrackedPlayerColor() {
		return minion.getTarget() != null ? ViewController.colors[minion.getTarget().id] : 0xFFFFFF;
	}

	@Override
	int getTooltipEntityId() {
		return animation.getId();
	}

	@Override
	public void updateToolTip(TooltipModule tooltipModule) {

		Map<String, Object> params = tooltipModule.getParams(getTooltipEntityId());
		params.put("entity", "minion");
		params.put("id", minion.getId());
		params.put("type", minion.type().toString());

		if (minion.isSpawning()) {
			params.put("spawn", minion.spawnCountdown);
		} else {
			params.remove("spawn");
		}
	}

	@Override
	public Entity<?> getPrimaryNode() {
		return baseNode;
	}
}
