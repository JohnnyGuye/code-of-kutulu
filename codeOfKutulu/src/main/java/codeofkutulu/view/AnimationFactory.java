package codeofkutulu.view;

import java.util.HashMap;
import java.util.Map;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.SpriteAnimation;
import com.codingame.gameengine.module.entities.SpriteSheetLoader;
import com.google.inject.Inject;

public class AnimationFactory {

  public static final String WANDERER_SPAWN = "Wanderer_spawn";
  public static final String WANDERER_UP = "Wanderer_up";
  public static final String WANDERER_RIGHT = "Wanderer_right";
  public static final String WANDERER_LEFT = "Wanderer_left";
  public static final String WANDERER_DOWN = "Wanderer_down";
  
  public static final String WANDERER_Mask_SPAWN = "Wanderer_Mask_spawn";
  public static final String WANDERER_Mask_UP = "Wanderer_Mask_up";
  public static final String WANDERER_Mask_RIGHT = "Wanderer_Mask_right";
  public static final String WANDERER_Mask_LEFT = "Wanderer_Mask_left";
  public static final String WANDERER_Mask_DOWN = "Wanderer_Mask_down";

  public static final String SLASHER_SPAWN = "Slasher_spawn";
  public static final String SLASHER_UP = "Slasher_up";
  public static final String SLASHER_RIGHT = "Slasher_right";
  public static final String SLASHER_LEFT = "Slasher_left";
  public static final String SLASHER_DOWN = "Slasher_down";

  public static final String SLASHER_Mask_SPAWN = "Slasher_Mask_spawn";
  public static final String SLASHER_Mask_UP = "Slasher_Mask_up";
  public static final String SLASHER_Mask_RIGHT = "Slasher_Mask_right";
  public static final String SLASHER_Mask_LEFT = "Slasher_Mask_left";
  public static final String SLASHER_Mask_DOWN = "Slasher_Mask_down";
  
  public static final String PLAYER_UP    = "Player_up";
  public static final String PLAYER_RIGHT = "Player_right";
  public static final String PLAYER_LEFT  = "Player_left";
  public static final String PLAYER_DOWN  = "Player_down";
  public static final String PLAYER_WAIT  = "Player_wait";
  public static final String PLAYER_STUN  = "Player_stun";

  public static final String PLAYER_Mask_UP    = "Player_Mask_up";
  public static final String PLAYER_Mask_RIGHT = "Player_Mask_right";
  public static final String PLAYER_Mask_LEFT  = "Player_Mask_left";
  public static final String PLAYER_Mask_DOWN  = "Player_Mask_down";
  public static final String PLAYER_Mask_WAIT  = "Player_Mask_wait";
  public static final String PLAYER_Mask_STUN  = "Player_Mask_stun";

  @Inject
  GraphicEntityModule graphicEntityModule;

  private Map<String, SpriteSheetLoader> spriteSheelLoaders;
  private Map<String, SpriteAnimation> spriteAnimations;

  private static AnimationFactory instance = new AnimationFactory();

  private AnimationFactory() {
    spriteSheelLoaders = new HashMap<>();
    spriteAnimations = new HashMap<>();
  }

  public static AnimationFactory getInstance() {
    return instance;
  }

  public void init(GraphicEntityModule graphicEntityModule) {
    this.graphicEntityModule = graphicEntityModule;
    createAnimations();
  }

  private SpriteSheetLoader createSSL(String source, String name, int width, int height, int count, int imagePerRow) {
    SpriteSheetLoader ssl = this.graphicEntityModule.createSpriteSheetLoader()
        .setSourceImage(source)
        .setName(name)
        .setWidth(width)
        .setHeight(height)
        .setImageCount(count)
        .setImagesPerRow(imagePerRow)
        .setOrigCol(0)
        .setOrigRow(0);
    spriteSheelLoaders.put(name, ssl);
    return ssl;
  }

  private SpriteAnimation createAnimation(String name, SpriteSheetLoader ssl, int row) {
    return createAnimation(name, ssl, row, 0);
  }

  private SpriteAnimation createAnimation(String name, SpriteSheetLoader ssl, int row, int col) {
    SpriteAnimation sa = this.graphicEntityModule.createSpriteAnimation()
        .setImages(ssl.setOrigCol(col).setOrigRow(row).load());
    spriteAnimations.put(name, sa);
    return sa;
  }

  private void createAnimations() {
    createAnimation(SLASHER_SPAWN, createSSL("flame_animation.png", "Flames_1", 48, 48, 8, 8), 1);
    createAnimation(SLASHER_DOWN, createSSL("slasher_scythe.png", "Slasher_0", 50, 48, 4, 4), 0);
    createAnimation(SLASHER_LEFT, createSSL("slasher_scythe.png", "Slasher_1", 50, 48, 4, 4), 1);
    createAnimation(SLASHER_RIGHT, createSSL("slasher_scythe.png", "Slasher_2", 50, 48, 4, 4), 2);
    createAnimation(SLASHER_UP, createSSL("slasher_scythe.png", "Slasher_3", 50, 48, 4, 4), 3);

    createAnimation(SLASHER_Mask_SPAWN, createSSL("flame_animation.png", "Flames_Mask_1", 48, 48, 8, 8), 1);
    createAnimation(SLASHER_Mask_DOWN, createSSL("slasher_scythe-tint.png", "Slasher_Mask_0", 50, 48, 4, 4), 0);
    createAnimation(SLASHER_Mask_LEFT, createSSL("slasher_scythe-tint.png", "Slasher_Mask_1", 50, 48, 4, 4), 1);
    createAnimation(SLASHER_Mask_RIGHT, createSSL("slasher_scythe-tint.png", "Slasher_Mask_2", 50, 48, 4, 4), 2);
    createAnimation(SLASHER_Mask_UP, createSSL("slasher_scythe-tint.png", "Slasher_Mask_3", 50, 48, 4, 4), 3);

    createAnimation(WANDERER_SPAWN, createSSL("flame_animation.png", "Flames_0", 48, 48, 8, 8), 0);
    createAnimation(WANDERER_DOWN, createSSL("wanderer.png", "Wanderer_0", 39, 48, 4, 4), 0);
    createAnimation(WANDERER_LEFT, createSSL("wanderer.png", "Wanderer_1", 39, 48, 4, 4), 1);
    createAnimation(WANDERER_RIGHT, createSSL("wanderer.png", "Wanderer_2", 39, 48, 4, 4), 2);
    createAnimation(WANDERER_UP, createSSL("wanderer.png", "Wanderer_3", 39, 48, 4, 4), 3);
    
    createAnimation(WANDERER_Mask_SPAWN, createSSL("flame_animation.png", "Flames_0", 48, 48, 8, 8), 0);
    createAnimation(WANDERER_Mask_DOWN, createSSL("wanderer-tint.png", "Wanderer_Mask_0", 39, 48, 4, 4), 0);
    createAnimation(WANDERER_Mask_LEFT, createSSL("wanderer-tint.png", "Wanderer_Mask_1", 39, 48, 4, 4), 1);
    createAnimation(WANDERER_Mask_RIGHT, createSSL("wanderer-tint.png", "Wanderer_Mask_2", 39, 48, 4, 4), 2);
    createAnimation(WANDERER_Mask_UP, createSSL("wanderer-tint.png", "Wanderer_Mask_3", 39, 48, 4, 4), 3);
    
    createAnimation(PLAYER_DOWN,  createSSL("player.png", "Player_0", 32, 48, 4, 4), 0);
    createAnimation(PLAYER_LEFT,  createSSL("player.png", "Player_1", 32, 48, 4, 4), 1);
    createAnimation(PLAYER_RIGHT, createSSL("player.png", "Player_2", 32, 48, 4, 4), 2);
    createAnimation(PLAYER_UP,    createSSL("player.png", "Player_3", 32, 48, 4, 4), 3);
    createAnimation(PLAYER_WAIT,  createSSL("player.png", "Player_WAIT", 32, 48, 4, 4), 4);
    createAnimation(PLAYER_STUN,  createSSL("player.png", "Player_STUN", 32, 48, 4, 4), 5);

    createAnimation(PLAYER_Mask_DOWN,  createSSL("player-tint.png", "Player_Mask_0", 32, 48, 4, 4), 0);
    createAnimation(PLAYER_Mask_LEFT,  createSSL("player-tint.png", "Player_Mask_1", 32, 48, 4, 4), 1);
    createAnimation(PLAYER_Mask_RIGHT, createSSL("player-tint.png", "Player_Mask_2", 32, 48, 4, 4), 2);
    createAnimation(PLAYER_Mask_UP,    createSSL("player-tint.png", "Player_Mask_3", 32, 48, 4, 4), 3);
    createAnimation(PLAYER_Mask_WAIT,  createSSL("player-tint.png", "Player_Mask_WAIT", 32, 48, 4, 4), 4);
    createAnimation(PLAYER_Mask_STUN,  createSSL("player-tint.png", "Player_Mask_STUN", 32, 48, 4, 5), 5);
    
  }

  public SpriteAnimation getAnimation(String animationName) {
    return spriteAnimations.get(animationName);
  }
}
