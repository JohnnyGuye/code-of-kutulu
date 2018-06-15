package codeofkutulu.view.nodes;

import static codeofkutulu.Constants.CELL_SIZE;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.codingame.game.Player;
import com.codingame.game.Referee;
import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;

import codeofkutulu.Constants;
import codeofkutulu.models.Cell;
import codeofkutulu.models.Minion;
import codeofkutulu.tooltipModule.TooltipModule;

public class MapNode extends AbstractNode {

  Group group;
  List<CellNode> cellNodes;
  List<PlayerNode> playerNodes;
  List<MinionNode> minionNodes;

  public MapNode(GraphicEntityModule entityManager) {
    super(entityManager);

    cellNodes = new LinkedList<CellNode>();
    playerNodes = new LinkedList<PlayerNode>();
    minionNodes = new LinkedList<MinionNode>();

    this.group = this.entityManager.createGroup()
        .setX(left())
        .setY(0)
        .setVisible(true)
        .setZIndex(1)
        .setScale(getBestFitScale());
    
    createMapName();
    createTentacles();
  }

  private void createMapName() {
    Text text = entityManager.createText(Referee.maze.name)
    .setFontSize(30)
    .setX(Constants.CELL_SIZE * Referee.maze.dim.x / 2)
    .setY(10)
    .setFillColor(0xF0EFEF)
    .setAnchorX(.5)
    .setVisible(true)
    .setRotation(-.01)
    .setStrokeColor(0x601010)
    .setStrokeThickness(10)
    .setZIndex(2);   
    
    this.group.add(text);
  }

  private void createTentacles() {
    Sprite tentacle = entityManager.createSprite()
        .setImage("kutulu_tentacle.png")
        .setX(getBaseWidth())
        .setY(getBaseHeight())
        .setBaseHeight(CELL_SIZE * 4)
        .setBaseWidth(CELL_SIZE * 4)
        .setScaleX(-1)
        .setScaleY(-1)
        .setZIndex(10);
    
    this.group.add(tentacle);
  }

  private int getBaseHeight() {
    return Constants.CELL_SIZE * Referee.maze.dim.y;
  }

  private int getBaseWidth() {
    return Constants.CELL_SIZE * Referee.maze.dim.x;
  }

  private double getBestFitScale() {
    return Math.min(
        (1080.0 / Referee.maze.dim.y) / (double)Constants.CELL_SIZE,
        (1350.0 / Referee.maze.dim.x) / (double)Constants.CELL_SIZE
        );
  }
  
  private int left() {
	  return (int) (Constants.SCREEN_DIM.x - Referee.maze.dim.x * Constants.CELL_SIZE * getBestFitScale());
  }
  
  @Override
  public void updateView() {
    // nothing to update in walls
  }

  public CellNode createCellNode(Cell cell) {
    CellNode cn = CellNode.build(entityManager, cell);
    this.group.add(cn.getPrimaryNode().setZIndex(0));
    this.cellNodes.add(cn);
    return cn;
  }

  public PlayerNode createPlayerNode(Player player) {
    PlayerNode pn = new PlayerNode(entityManager, player, player.getPlayerUnit());
    this.group.add(pn.getPrimaryNode().setZIndex(2));
    this.playerNodes.add(pn);
    return pn;
  }

  public MinionNode createMinionNode(Minion minion) {
    MinionNode mn;
    switch (minion.type()) {
    case SLASHER:
      mn = new SlasherMinionNode(entityManager, minion);
      break;
    case WANDERER:
      mn = new WandererMinionNode(entityManager, minion);
      break;
    default:
      mn = new MinionNode(entityManager, minion);
    }
    this.group.add(mn.getPrimaryNode().setZIndex(1));
    entityManager.commitEntityState(0, this.getPrimaryNode());
    this.minionNodes.add(mn);
    return mn;
  }

  @Override
  public Entity<?> getPrimaryNode() {
    return group;
  }

  @Override
  public void updateToolTip(TooltipModule tooltipModule) {
    super.updateToolTip(tooltipModule);
    
    Map<String, Object> params = tooltipModule.getParams(getPrimaryNode().getId());
    params.put("entity", "map");
    params.put("startx", left());
    params.put("starty", 0);
    params.put("size", ""+Constants.CELL_SIZE);
    params.put("scale", ""+getBestFitScale());
  }
}
