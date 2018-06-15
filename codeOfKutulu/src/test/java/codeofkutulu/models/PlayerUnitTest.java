package codeofkutulu.models;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.codingame.game.Referee;
import com.codingame.game.RefereeTest;

import codeofkutulu.Constants;
import codeofkutulu.Rule;
import codeofkutulu.mazes.Hypersonic;
import codeofkutulu.models.effects.Effect;
import codeofkutulu.models.effects.PlanEffect;
import codeofkutulu.models.effects.RepulseEffect;

public class PlayerUnitTest {

  private PlayerUnit player;

  @Before
  public void setup() {
    Rule.HAS_ISOLATED_FEAR = true; // activate the rule

    player = new PlayerUnit(0);
    Referee.livingPlayers.clear();
    Referee.livingPlayers.add(player);
    Referee.playfield = new Playfield(new Hypersonic());
    
    RefereeTest.clearEffects();
  }
  
  @Test
  public void whenSanityIsZeroPlayerIsDead() throws Exception {
    player.sanity = 0;
    assertThat(player.isDead(), is(true));
  }
  
  @Test
  public void whenSanityIsGreaterThanZeroPlayerIsNotDead() throws Exception {
    player.sanity = 1;
    assertThat(player.isDead(), is(false));
  }
  
  @Test
  public void isOnCell_theSame() throws Exception {
    Cell cell = new Cell(4, 7);
    player.move(4, 7);

    assertThat(player.isOnCell(cell), is(true));
  }

  @Test
  public void isOnCell_theSameButInvalid() throws Exception {
    Cell cell = new Cell(4, 7) {
      @Override
      public boolean isWalkable() {
        return false;
      }
    };
    player.move(4, 7);

    assertThat(player.isOnCell(cell), is(false));
  }
  
  @Test
  public void spookToDeath() throws Exception {
    player.sanity = 10;
    player.spook(11);
    
    assertThat(player.isDead(), is(true));
  }
  
  @Test
  public void healPlayer() throws Exception {
    player.sanity = 80;
    player.heal(20);
    
    assertThat(player.sanity, is(100));
  }

  @Test
  public void healPlayerToMax() throws Exception {
    player.sanity = 70;
    player.heal(Constants.PLAYER_MAX_SANITY);
    
    assertThat(player.sanity, is(Constants.PLAYER_MAX_SANITY));
  }
  
  @Test
  public void planAlone_HEAL_3() throws Exception {
    player.sanity = 70;
    player.move(9,9);
    
    Effect plan = new PlanEffect(player);
    plan.doEffect();
    
    assertThat(player.sanity, is(73));
  }


  @Test
  public void planWithOtherPlayerTooFar_HEAL_3() throws Exception {
    player.sanity = 70;
    player.move(9,9);
    
    PlayerUnit player2 = new PlayerUnit(1);
    player2.sanity = 60;
    player2.move(9 + Constants.EFFECT_PLAN_HEAL_RADIUS + 1,9);
    Referee.livingPlayers.add(player2);
    
    Effect plan = new PlanEffect(player);
    plan.doEffect();
    
    assertThat(player.sanity, is(73));
    assertThat(player2.sanity, is(60));
  }

  @Test
  public void planWithOtherPlayerAtDistance_HEAL_6() throws Exception {
    player.sanity = 70;
    player.move(9,9);
    
    PlayerUnit player2 = new PlayerUnit(1);
    player2.sanity = 60;
    player2.move(9 + Constants.EFFECT_PLAN_HEAL_RADIUS,9);
    Referee.livingPlayers.add(player2);
    
    Effect plan = new PlanEffect(player);
    plan.doEffect();
    
    assertThat(player.sanity, is(76));
    assertThat(player2.sanity, is(63)); // and one for the other
  }
  
  @Test
  public void castPlanEffect_cantCastIfNoneRemaining() throws Exception {
    player.remainingPlans = 0;
    
    Effect effect = player.castPlanEffect();
    
    assertThat(effect, is(nullValue()));
    assertThat(player.remainingPlans, is(0));
  }

  @Test
  public void castPlanEffectRemoveOnePlan() throws Exception {
    player.remainingPlans = 7;
    
    Effect effect = player.castPlanEffect();
    
    assertThat(effect, is(instanceOf(PlanEffect.class)));
    assertThat(player.remainingPlans, is(6));
  }
  
  @Test
  public void cantCastLightEffectIfNoLightsLeft() throws Exception {
    player.remainingLight= 0;
    
    Effect effect = player.castLightEffect();
    
    assertThat(effect, is(nullValue()));
    assertThat(player.remainingLight, is(0));
  }

  @Test
  public void castLightEffectRemoveOneLight() throws Exception {
    player.remainingLight= 14;
    
    Effect effect = player.castLightEffect();
    
    assertThat(effect, is(instanceOf(RepulseEffect.class)));
    assertThat(player.remainingLight, is(13));
  }

  @Test
  public void sanityTo0WhenDead() throws Exception {
    player.sanity = 20;
    player.kill();
    
    assertThat(player.sanity, is(0));
  }
  

  @Test
  public void updateIsolation_whenAloneButRuleIsOFF() throws Exception {
    Rule.HAS_ISOLATED_FEAR = false;
    List<PlayerUnit> players = Arrays.asList(player);
    player.updateIsolation(players);
    assertThat(player.isIsolated(), is(false));
  }
  
  @Test
  public void updateIsolation_whenAlone() throws Exception {
    List<PlayerUnit> players = Arrays.asList(player);
    player.updateIsolation(players);
    assertThat(player.isIsolated(), is(true));
  }
  
  @Test
  public void updateIsolation_whenOneTooFar() throws Exception {
    PlayerUnit tooFar = new PlayerUnit(1);

    player.move(0,0);
    tooFar.move(3,0);

    List<PlayerUnit> players = Arrays.asList(player, tooFar);
    player.updateIsolation(players);
    assertThat(player.isIsolated(), is(true));
  }

  @Test
  public void updateIsolation_whenOneInRange() throws Exception {
    PlayerUnit tooFar = new PlayerUnit(1);

    player.move(0,0);
    tooFar.move(1,0);

    List<PlayerUnit> players = Arrays.asList(player, tooFar);
    player.updateIsolation(players);
    assertThat(player.isIsolated(), is(false));
  }

  @Test
  public void spreadMadness_whenIsolated() throws Exception {
    player.sanity = 100;
    player.isIsolated = true;
    
    player.spreadMadness();
    
    assertThat(player.sanity, is(100-Constants.SPREAD_MADNESS_PER_TURN_AMOUNT));
  }

  @Test
  public void spreadMadness_whenWithFriends() throws Exception {
    player.sanity = 100;
    player.isIsolated = false;
    
    player.spreadMadness();
    
    assertThat(player.sanity, is(100-Constants.SPREAD_MADNESS_PLAYER_PROX_PER_TURN_AMOUNT));
  }
  
  @Test
  public void outputCompatibleWithStub() throws Exception {
    /**
     * IF THIS TEST IS KO : 
     * 
     * It mean output have change, you need to CHECK THE STUB AND the BOTS code!
     */
    player.id = 10;
    player.sanity = 99;
    player.pos.x = 14;
    player.pos.y = 17;
    player.remainingPlans = 3;
    player.remainingLight = 5;
    
    assertThat(player.toOutput(), is("EXPLORER 10 14 17 99 3 5"));
  }
  
  @Test
  public void canNOTAutoYell() throws Exception {
    player.yell(player);
    
    assertThat(player.isStuck(), is(false));
  }
  
  @Test
  public void yellOutOfRange() throws Exception {
    PlayerUnit other = new PlayerUnit(2);
    other.move(11, 15);
    player.yell(other);
    
    assertThat(other.isStuck(), is(false));
  }
  
  @Test
  public void yellInRange() throws Exception {
    PlayerUnit other = new PlayerUnit(2);
    other.move(player.getCurrentCell());
    player.yell(other);
    
    assertThat(other.isStuck(), is(true));
  }

  @Test
  public void canOnlyYellOnce() throws Exception {
    PlayerUnit other = new PlayerUnit(2);
    other.move(player.getCurrentCell());
    player.yell(other);
    
    assertThat(player.yell(other), is(false));
  }
  
  @Test
  public void yellStuck2Turns() throws Exception {
    // Note : test based on Constants.PLAYER_INITIAL_STUCK_ROUNDS
    PlayerUnit other = new PlayerUnit(2);
    other.move(player.getCurrentCell());
    player.yell(other);
    
    assertThat(other.isStuck(), is(true));
    
    RefereeTest.updateEffects();
    assertThat(other.isStuck(), is(true));
    
    RefereeTest.updateEffects();
    assertThat(other.isStuck(), is(false));
    
    // decrease one more time to be sure ...
    RefereeTest.updateEffects();
    assertThat(other.isStuck(), is(false));
  }
  
  
}
