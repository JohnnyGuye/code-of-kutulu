package codeofkutulu.models.effects;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

import codeofkutulu.models.PlayerUnit;

public class YellEffectTest {

  @Test
  public void output() throws Exception {
    PlayerUnit p1 = new PlayerUnit(47);
    PlayerUnit p2 = new PlayerUnit(73);
    
    YellEffect yell = new YellEffect(p1,p2);
    
    assertThat(yell.toOutput(), is("EFFECT_YELL -1 -1 -1 2 47 73"));
    
    yell.doEffect();
    assertThat(yell.toOutput(), is("EFFECT_YELL -1 -1 -1 1 47 73"));
    
  }
}
