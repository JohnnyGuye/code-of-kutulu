package codeofkutulu.view;

import static codeofkutulu.Constants.CELL_SIZE;

import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.SpriteAnimation;

public class AnimationWithMask {
  private String baseName;
  public SpriteAnimation animation;
  public SpriteAnimation mask;
  private GraphicEntityModule entityManager;
  private int maskColor = 0xFFFFFF;
  
  public AnimationWithMask(GraphicEntityModule entityManager, String baseName) {
    this.entityManager = entityManager;
    this.baseName = baseName;

    createAnimations();
  }

  public void setScale(double scale) {
    this.animation.setScale(scale);
    this.mask.setScale(scale);
  }
  
  private void createAnimations() {
    this.animation = this.entityManager.createSpriteAnimation()
    .setAnchorX(.5)
    .setX(CELL_SIZE / 2)
    .setY(0)
    .setDuration(500)
    .setVisible(true)
    .setZIndex(5);
    
    this.mask= this.entityManager.createSpriteAnimation()
    .setAnchorX(.5)
    .setX(CELL_SIZE / 2)
    .setY(0)
    .setDuration(500)
    .setVisible(true)
    .setZIndex(5)
    .setAlpha(1.0);
  }

  public void setVisible(boolean b) {
    animation.setVisible(b);
    mask.setVisible(b);
  }

  public void setMaskAlpha(double value) {
    mask.setAlpha(value);
  }
  
  public void setAnimation(String index) {
    this.animation.setImages(AnimationFactory.getInstance().getAnimation(baseName+"_" + index).getImages());
    this.mask.setImages(AnimationFactory.getInstance().getAnimation(baseName+"_Mask_"+index).getImages());
    
    
    this.animation.reset();
    this.mask.reset();
    this.entityManager.commitEntityState(0, animation, mask);
  }

  public void setTint(int color) {
    this.animation.setTint(color);
  }
  
  public void setMaskTint(int color, Curve curve) {
    this.mask.setTint(color, curve);
  }

  
  public void setMaskTint(int color) {
    this.maskColor  = color;
    this.mask.setTint(maskColor);
  }

  public int getId() {
    return animation.getId();
  }

  public void setZIndex(int zIndex) {
    animation.setZIndex(zIndex);
    mask.setZIndex(zIndex);
    
  }

  public void setAlpha(double d, Curve curve) {
    animation.setAlpha(d, curve);
    mask.setAlpha(d, curve);
  }

  public void setScaleX(int scale, Curve curve) {
    animation.setScaleX(scale, curve);
    mask.setScaleX(scale, curve);
  }

  public double getScaleY() {
    return animation.getScaleY();
  }

  public void setScaleY(double scale, Curve curve) {
    animation.setScaleY(scale, curve);
    mask.setScaleY(scale, curve);
  }

  public void setBaseName(String basename) {
    this.baseName = basename;
  }

}
