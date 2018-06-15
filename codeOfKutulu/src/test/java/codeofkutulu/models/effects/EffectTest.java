package codeofkutulu.models.effects;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import codeofkutulu.models.Cell;

public class EffectTest {

  @Test
  public void testOutput() throws Exception {
    /**
     * If THIS TEST IS KO, CHECK THE STUB
     */
    Effect myEffect = new Effect(null) {
      public int getType() {
        return 4;
        }
      protected void doRealEffect() {
      }
      @Override
      public Cell getCurrentCell() {
        return new Cell(42, 53);
      }
    };
    myEffect.tick = 99;
    
    assertThat(myEffect.toOutput(), is("EFFECT_PLAN -1 42 53 99 -1 -1"));
  }
}
