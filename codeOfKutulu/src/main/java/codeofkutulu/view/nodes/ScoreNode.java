package codeofkutulu.view.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.codingame.game.Player;
import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Rectangle;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;

import codeofkutulu.Constants;
import codeofkutulu.Rule;
import codeofkutulu.tooltipModule.TooltipModule;
import codeofkutulu.view.TileFactory;
import codeofkutulu.view.ViewController;

public class ScoreNode extends AbstractNode {
  public static final int GLOB_MARGIN_TOP = 20;
  public static final int GLOB_MARGIN_LEFT = 20;
  public static final int HEIGHT = (Constants.SCREEN_DIM.y - GLOB_MARGIN_TOP) / 4;
  public static final int MARGIN_Y = 5;

  public static final int NICKNAME_HEIGHT = HEIGHT / 3;

  public static final int SANITY_WIDTH = 300;
  public static final int SANITY_HEIGHT = 40;

  public static final int AVATAR_SIZE = NICKNAME_HEIGHT + SANITY_HEIGHT;
  public static final int SANITY_LEFT = AVATAR_SIZE + 2 * MARGIN_Y;

  private Group scoreBoard;
  private Player player;

  private Group sanityGroup;
  private Rectangle sanityView, sanityViewFill;
  private Text sanityText;
  private Sprite sanityBackground;

  private Sprite isolatedStatus;
  private Sprite avatarView;
  private Text name;
  private Text message;
  private Sprite planSprite;
  private Text planText;
  private Sprite lightSprite;
  private Text lightText;
  
  private Rectangle sanityTextMask;
  private Text 		sanityForegroundText;
  
  private Sprite yells[] = new Sprite[3];

  public ScoreNode(GraphicEntityModule entityManager, Player player) {
    super(entityManager);
    this.player = player;

    createViewNodes();
  }

  @Override
  public void updateView() {
    if (!player.isActive()) {
      avatarView.setTint(0x222222);
    }
    
    this.message.setText(player.getDisplayMessage());

    this.sanityViewFill.setWidth(this.sanityView.getWidth());
    this.sanityView.setWidth(SANITY_WIDTH * player.getPlayerUnit().getSanity() / Constants.PLAYER_MAX_SANITY);
    int sanity = this.player.getPlayerUnit().getSanity();
    this.sanityText.setText(sanity > 0 ? ""+sanity : "✝");
    
    this.sanityTextMask.setWidth(this.sanityView.getWidth());
    this.sanityForegroundText.setText(this.sanityText.getText());
    
    if (Rule.HAS_ISOLATED_FEAR) {
      this.isolatedStatus.setVisible(player.getPlayerUnit().isIsolated());
    }
    if (Rule.HAS_PLAN) {
      this.planText.setText(String.valueOf(player.getPlayerUnit().getRemainingPlans()));
    }
    if (Rule.HAS_LIGHT) {
      this.lightText.setText(String.valueOf(player.getPlayerUnit().getRemainingLights()));
    }
    if (Rule.HAS_YELL) {
      updateYellNodes();
    }
  }

  private void createViewNodes() {
    buildMetaInformationViewNodes();
    buildSanityViewNodes();
    buildStatusViewNodes();
    buildItemsViewNodes();
    buildYellsNodes();
    createViewGroup();
  }

  private void buildYellsNodes() {
    int index = 0;
    for (int i=0;i<4;i++) {
      if (i != player.getIndex()) {
        yells[index++] = entityManager.createSprite()
                            .setImage(TileFactory.getYellSpriteName())
                            .setX(SANITY_LEFT + MARGIN_Y + 2 * 64 + index * (32+2))
                            .setY(NICKNAME_HEIGHT + SANITY_HEIGHT + 16)
                            .setBaseWidth(32)
                            .setBaseHeight(32)
                            .setTint(ViewController.colors[i])
                            .setAlpha(0.0)
                            ;
      }
    }
  }

  private void updateYellNodes() {
    int index = 0;
    for (int i=0;i<4;i++) {
      if (i != player.getIndex()) {
        if (player.getPlayerUnit().hasBeenStuckedBy(i)) {
          yells[index].setAlpha(1.0);
        }
        index++;
      }
    }
  }
  
  private void createViewGroup() {
    List<Entity<?>> entities = new ArrayList<>();
        
    entities.addAll(Arrays.asList(avatarView, name, message, sanityGroup));
    if (Rule.HAS_PLAN) entities.addAll(Arrays.asList(planSprite, planText));
    if (Rule.HAS_LIGHT) entities.addAll(Arrays.asList(lightSprite, lightText));
    if (Rule.HAS_ISOLATED_FEAR) entities.add(isolatedStatus);
    if (Rule.HAS_YELL) entities.addAll(Arrays.asList(yells[0], yells[1], yells[2]));
    
    this.scoreBoard = entityManager
        .createGroup(entities.toArray(new Entity[0]))
        .setX(GLOB_MARGIN_LEFT)
        .setY(GLOB_MARGIN_TOP + player.getIndex() * HEIGHT);
  }

  private void buildItemsViewNodes() {
    if (Rule.HAS_PLAN) {
      createPlanEntities();
    }
  
    if (Rule.HAS_LIGHT) {
      createLightEntities();
    }
  }

  private void createLightEntities() {
    this.lightSprite = entityManager.createSprite()
        .setImage(TileFactory.getLightSpriteName())
        .setX(SANITY_LEFT + MARGIN_Y + 64)
        .setY(NICKNAME_HEIGHT + SANITY_HEIGHT + 16)
        .setBaseHeight(SANITY_HEIGHT)
        .setBaseWidth(SANITY_HEIGHT);

    this.lightText = entityManager.createText("")
        .setX(lightSprite.getX() + 4 + SANITY_HEIGHT)
        .setY(lightSprite.getY())
        .setScale(1.2)
        .setFillColor(0xFFFFFF);
  }

  private void createPlanEntities() {
    this.planSprite = entityManager.createSprite()
        .setImage(TileFactory.getPlanSpriteName())
        .setX(SANITY_LEFT + MARGIN_Y)
        .setY(NICKNAME_HEIGHT + SANITY_HEIGHT + 16)
        .setBaseHeight(SANITY_HEIGHT)
        .setBaseWidth(SANITY_HEIGHT);

    this.planText = entityManager.createText("")
        .setX(planSprite.getX() + 4 + SANITY_HEIGHT )
        .setY(planSprite.getY())
        .setScale(1.2)
        .setFillColor(0xFFFFFF);
  }

  private void buildStatusViewNodes() {
    if (Rule.HAS_ISOLATED_FEAR) {
      this.isolatedStatus = entityManager.createSprite()
        .setImage(TileFactory.getIsolationSpriteName())
        .setBaseHeight(SANITY_HEIGHT)
        .setBaseWidth(SANITY_HEIGHT)
        .setZIndex(20)
        .setX(SANITY_LEFT + SANITY_WIDTH + 2 * MARGIN_Y)
        .setY(NICKNAME_HEIGHT);
    }
  }

  private void buildSanityViewNodes() {
    this.sanityBackground = entityManager.createSprite()
        .setImage("sanity_background.png")
        .setBaseWidth(SANITY_WIDTH)
        .setBaseHeight(SANITY_HEIGHT)
        .setAlpha(0.5)
        .setZIndex(2)
        .setVisible(true);

    this.sanityView = entityManager.createRectangle()
        .setWidth(SANITY_WIDTH)
        .setHeight(SANITY_HEIGHT)
        .setFillColor(player.getColorToken())
        .setZIndex(1);

    this.sanityViewFill = entityManager.createRectangle()
        .setWidth(SANITY_WIDTH)
        .setHeight(SANITY_HEIGHT)
        .setFillColor(0xD0D0D0)
        .setAlpha(0.5)
        .setZIndex(0);

    this.sanityText = entityManager.createText("")
        .setX(SANITY_WIDTH / 2)
        .setScale(1.2)
        .setAnchorX(.5)
        .setFontFamily("Arial Black")
        .setStrokeThickness(1)
        .setStrokeColor(0x000000)
        .setFillColor(player.getColorToken())
        .setTint(0xFFFFFF)
        .setZIndex(3);
    
    this.sanityTextMask = entityManager.createRectangle()
    		.setWidth(this.sanityView.getWidth())
    		.setHeight(this.sanityView.getHeight());
    
    this.sanityForegroundText = entityManager.createText("")
        .setX(sanityText.getX())
        .setScale(sanityText.getScaleX())
        .setAnchorX(sanityText.getAnchorX())
        .setFontFamily(sanityText.getFontFamily())
        .setStrokeThickness(1)
        .setMask(this.sanityTextMask)
        .setStrokeColor(this.player.getColorToken())
        .setFillColor(0x000000)
        .setZIndex(4);
    
    this.sanityGroup = entityManager.createGroup(sanityBackground, sanityViewFill, sanityView, sanityText, sanityTextMask, sanityForegroundText)
        .setX(SANITY_LEFT)
        .setY(NICKNAME_HEIGHT);
  }

  private void buildMetaInformationViewNodes() {
    this.avatarView = entityManager.createSprite()
        .setBaseWidth(AVATAR_SIZE)
        .setBaseHeight(AVATAR_SIZE)
        .setZIndex(20)
        .setImage(player.getAvatarToken());

    this.name = entityManager.createText(player.getNicknameToken())
        .setX(SANITY_LEFT)
        .setFillColor(player.getColorToken())
        .setScale(1.5)
        .setFontFamily("Arial Black");

    this.message = entityManager.createText("")
        .setX(SANITY_LEFT)
        .setY(2 * this.name.getFontSize())
        .setFillColor(player.getColorToken())
        .setScale(1.0);

  }

  @Override
  public Entity<?> getPrimaryNode() {
    return scoreBoard;
  }
  
  @Override
  public void updateToolTip(TooltipModule tooltipModule) {
    super.updateToolTip(tooltipModule);
    
    if (Rule.HAS_ISOLATED_FEAR) {
      Map<String, Object> params = tooltipModule.getParams(isolatedStatus.getId());
      params.put("entity", "message");
      params.put("message", "Isolated");
    }
  }
}
