package codeofkutulu.view.nodes;

import static codeofkutulu.Constants.CELL_SIZE;

import java.util.Observer;

import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;

import codeofkutulu.models.Cell;
import codeofkutulu.models.CellType;

public abstract class CellNode extends AbstractNode implements Observer {

  protected static final int PADDING = 0;

  Cell model;
  Group group;
  Sprite background;

  protected CellNode(GraphicEntityModule entityManager, Cell model) {
    super(entityManager);
    this.model = model;
    this.model.addObserver(this);

    this.group = this.entityManager.createGroup();
    this.group.setX(model.pos.x * CELL_SIZE).setY(model.pos.y * CELL_SIZE);
    createView();
    this.group.add(background);
  }
  
  public static CellNode build(GraphicEntityModule entityManager, Cell model) {
    if (model.type == CellType.WALL) {
      return new WallNode(entityManager, model);
    } else {
      return new GroundNode(entityManager, model);
    }
  }

  @Override
  public Entity<?> getPrimaryNode() {
    return group;
  }

  protected abstract void createView();
  
  @Override
  int getTooltipEntityId() {
    return group.getId();
  }

}