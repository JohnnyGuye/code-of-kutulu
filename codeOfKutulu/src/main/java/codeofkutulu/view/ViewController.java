package codeofkutulu.view;

import java.util.ArrayList;
import java.util.List;

import com.codingame.game.Player;
import com.codingame.game.Referee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;

import codeofkutulu.Constants;
import codeofkutulu.models.Cell;
import codeofkutulu.models.Minion;
import codeofkutulu.tooltipModule.TooltipModule;
import codeofkutulu.view.nodes.AbstractNode;
import codeofkutulu.view.nodes.MapNode;
import codeofkutulu.view.nodes.ScoreNode;

public class ViewController {

	private List<AbstractNode> viewNodes = new ArrayList<>();
	private GraphicEntityModule entityManager;
	private MultiplayerGameManager<Player> gameManager;
	
	private MapNode map;

	public static int colors[] = new int[4];
	private TooltipModule tooltipModule;

	public ViewController(GraphicEntityModule entityManager, MultiplayerGameManager<Player> gameManager, TooltipModule tooltipModule) {
	    this.entityManager = entityManager;
	    this.gameManager = gameManager;
	    this.tooltipModule = tooltipModule;
	    
	    this.map = new MapNode(entityManager);
	    
	    initView();
	}

	public MapNode getMap() {
	    return map;
	}
	
	private void initView() {
		initColors();
		entityManager.createSprite().setImage("background.jpg").setScale(1).setAnchorX(0).setAnchorY(0).setZIndex(-1);
	}

	private void initColors() {
		for (int i = 0; i < gameManager.getPlayerCount(); i++) {
			colors[i] = gameManager.getPlayer(i).getColorToken();
		}
	}
	
	public void update() {
	  map.updateToolTip(tooltipModule);
	  
    for(AbstractNode node : viewNodes ) {
      node.updateView();
      node.updateToolTip(tooltipModule);
      node.teardown();
    }
    cleanDeadNodes();
	}

  private void cleanDeadNodes() {
    viewNodes.stream().filter(node -> node.isToBeRemoved()).forEach(node -> node.removeTooltip(tooltipModule));
    viewNodes.removeIf(node -> node.isToBeRemoved());
  }

	private void add(AbstractNode node) {
		viewNodes.add(node);
	}

	public void createCellView(Cell cell) {
		this.add(map.createCellNode(cell));
	}
	
	public void createPlayerView(Player player) {
		this.add( new ScoreNode(entityManager, player) );
		this.add(map.createPlayerNode(player));
	}

	public void createMinionView(Minion minion) {
		this.add( map.createMinionNode(minion) );
	}

}
