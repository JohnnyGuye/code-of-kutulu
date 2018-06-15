package codeofkutulu.view.nodes;

import static codeofkutulu.Constants.CELL_SIZE;

import com.codingame.gameengine.module.entities.GraphicEntityModule;

import codeofkutulu.Constants;
import codeofkutulu.models.Cell;
import codeofkutulu.models.CellType;
import codeofkutulu.view.TileFactory;

public class WallNode extends CellNode {
  
  public WallNode(GraphicEntityModule entityManager, Cell model) {
    super(entityManager, model);
  }

  @Override
  protected void createView() {
    this.background = entityManager.createSprite()
        .setImage(TileFactory.getWallSpriteName(computeProximity()))
        .setBaseHeight(CELL_SIZE)
        .setBaseWidth(CELL_SIZE)
        .setAlpha(1.0)
        .setTint(0xA0A080)
        .setZIndex(-1);
    
  }
  @Override
  public void updateView() {
  }
  
  private CellProximityStyle computeProximity() {
    CellProximityStyle proximityStyle = CellProximityStyle.SQUARE;
    
    CellType stay = model.type;
    CellType up = model.nextCell(Constants.UP).type;
    CellType down = model.nextCell(Constants.DOWN).type;
    CellType right = model.nextCell(Constants.RIGHT).type;
    CellType left = model.nextCell(Constants.LEFT).type;

    if (stay == up && stay == down && stay == right && stay == left)
      proximityStyle = CellProximityStyle.SQUARE;
    else if (stay != up && stay != down && stay != right && stay != left)
      proximityStyle = CellProximityStyle.SOLO;

    else if (stay == up && stay == down && stay == right && stay != left)
      proximityStyle = CellProximityStyle.T_LEFT;
    else if (stay != up && stay != down && stay != right && stay == left)
      proximityStyle = CellProximityStyle.LEFT;

    else if (stay == up && stay == down && stay != right && stay == left)
      proximityStyle = CellProximityStyle.T_RIGHT;
    else if (stay != up && stay != down && stay == right && stay != left)
      proximityStyle = CellProximityStyle.RIGHT;

    else if (stay == up && stay != down && stay == right && stay == left)
      proximityStyle = CellProximityStyle.T_DOWN;
    else if (stay != up && stay == down && stay != right && stay != left)
      proximityStyle = CellProximityStyle.DOWN;

    else if (stay != up && stay == down && stay == right && stay == left)
      proximityStyle = CellProximityStyle.T_UP;
    else if (stay == up && stay != down && stay != right && stay != left)
      proximityStyle = CellProximityStyle.UP;

    else if (stay == up && stay == down && stay != right && stay != left)
      proximityStyle = CellProximityStyle.VERTICAL;
    else if (stay != up && stay != down && stay == right && stay == left)
      proximityStyle = CellProximityStyle.HORIZONTAL;

    else if (stay == up && stay != down && stay != right && stay == left)
      proximityStyle = CellProximityStyle.CORNER_DOWN_RIGHT;
    else if (stay == up && stay != down && stay == right && stay != left)
      proximityStyle = CellProximityStyle.CORNER_DOWN_LEFT;

    else if (stay != up && stay == down && stay != right && stay == left)
      proximityStyle = CellProximityStyle.CORNER_UP_RIGHT;
    else if (stay != up && stay == down && stay == right && stay != left)
      proximityStyle = CellProximityStyle.CORNER_UP_LEFT;
    
    return proximityStyle;
  }
}
