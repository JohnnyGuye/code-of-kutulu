package codeofkutulu.view;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.google.inject.Inject;

import codeofkutulu.view.nodes.CellProximityStyle;

public class TileFactory {

  @Inject
  GraphicEntityModule graphicEntityModule;

  private static TileFactory instance;

  private static String[] spriteNames;
  
  public static String getWallSpriteName(CellProximityStyle cellStyle) {
    return spriteNames[cellStyle.tileIndex];
  }

  public static TileFactory getInstance() {
    if (instance != null) return instance;
    
    instance=  new TileFactory();
    return instance;
  }

  public void init(GraphicEntityModule graphicEntityModule) {
    this.graphicEntityModule = graphicEntityModule;
    
    spriteNames = instance.graphicEntityModule.createSpriteSheetLoader()
        .setSourceImage("tiles.png")
        .setName("TILE_")
        .setWidth(48)
        .setHeight(48)
        .setImageCount(8*8)
        .setImagesPerRow(8)
        .setOrigCol(0)
        .setOrigRow(0)
        .load();
  }

  public static String getGroundImageName(int offset) {
    return spriteNames[24+offset];
  }

  public static String getSpawnPoint() {
    return spriteNames[8*4+4];
  }
  public static String getActiveSpawnPoint() {
    return spriteNames[8*4+5];
  }
  
  public static String getShelterOffSpriteName() {
    return spriteNames[8*4+6];
  }
  
  public static String getShelterOnSpriteName() {
    return spriteNames[8*4+7];
  }
  public static String getYellSpriteName() {
    return spriteNames[8*7+0];
  }
  
  public static String getLightSpriteName() {
    return spriteNames[8*7+1];
  }
  
  public static String getPlanSpriteName() {
    return spriteNames[8*7+2];
  }
  public static String getTomb() {
    return spriteNames[8*7+3];
  }

  public static String getIsolationSpriteName() {
    return spriteNames[8*7+4];
  }

}
