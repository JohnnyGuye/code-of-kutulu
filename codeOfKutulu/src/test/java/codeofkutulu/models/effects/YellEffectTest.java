package codeofkutulu.models.effects;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import codeofkutulu.models.PlayerUnit;

public class YellEffectTest {

  PlayerUnit p1, p2;
  YellEffect yell;

  @Before
  public void setup() {
    p1 = new PlayerUnit(47);
    p2 = new PlayerUnit(73);
    yell = new YellEffect(p1, p2);
  }

  @Test
  public void output() throws Exception {
    assertThat(yell.toOutput(), is("EFFECT_YELL -1 -1 -1 2 47 73"));

    yell.doEffect();
    assertThat(yell.toOutput(), is("EFFECT_YELL -1 -1 -1 1 47 73"));

  }

  @Test
  public void yellTheDeads() throws Exception {
    p2.kill();
    assertTrue(yell.toOutput() == null);
  }
}
