package com.codingame.game;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import codeofkutulu.Constants;
import codeofkutulu.Rule;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class LeagueTest {

  @Before
  public void resetRules() {
    Constants.random = new Random();
    
    Rule.HAS_WANDERER = false;
    Rule.HAS_SLASHER = false;
    Rule.HAS_SHELTER = false;
    Rule.HAS_PLAN = false;
    Rule.HAS_LIGHT = false;
    Rule.HAS_YELL = false;
    Rule.HAS_ISOLATED_FEAR = false;
  }
  
  @Test
  public void checkWood3Rules() {
    League.WOOD_3.activateRules();
    
    assertThat(Rule.HAS_WANDERER,      is(true));
    assertThat(Rule.HAS_ISOLATED_FEAR, is(true));
    
    assertThat(Rule.HAS_SLASHER,       is(false));
    assertThat(Rule.HAS_SHELTER,       is(false));
    assertThat(Rule.HAS_PLAN,          is(false));
    assertThat(Rule.HAS_LIGHT,         is(false));
    assertThat(Rule.HAS_YELL,          is(false));
  }
  
  @Test
  public void checkWood2Rules() {
    League.WOOD_2.activateRules();
    
    assertThat(Rule.HAS_WANDERER,      is(true));
    assertThat(Rule.HAS_ISOLATED_FEAR, is(true));
    assertThat(Rule.HAS_PLAN,          is(true));
    assertThat(Rule.HAS_LIGHT,         is(true));

    assertThat(Rule.HAS_SLASHER,       is(false));
    assertThat(Rule.HAS_YELL,          is(false));
    assertThat(Rule.HAS_SHELTER,       is(false));
  }
  @Test
  public void checkWood1Rules() {
    League.WOOD_1.activateRules();

    assertThat(Rule.HAS_WANDERER,      is(true));
    assertThat(Rule.HAS_SHELTER,       is(true));
    assertThat(Rule.HAS_ISOLATED_FEAR, is(true));
    assertThat(Rule.HAS_PLAN,          is(true));
    assertThat(Rule.HAS_LIGHT,         is(true));
    assertThat(Rule.HAS_YELL,          is(true));
    assertThat(Rule.HAS_SLASHER,       is(true));
  }
  @Test
  public void checkBronzeRules() {
    League.BRONZE.activateRules();

    assertThat(Rule.HAS_WANDERER,      is(true));
    assertThat(Rule.HAS_SLASHER,       is(true));
    assertThat(Rule.HAS_SHELTER,       is(true));
    assertThat(Rule.HAS_PLAN,          is(true));
    assertThat(Rule.HAS_LIGHT,         is(true));
    assertThat(Rule.HAS_YELL,          is(true));
    assertThat(Rule.HAS_ISOLATED_FEAR, is(true));
  }

  @Test
  public void checkSilverRules() {
    League.SILVER.activateRules();

    assertThat(Rule.HAS_WANDERER,      is(true));
    assertThat(Rule.HAS_SLASHER,       is(true));
    assertThat(Rule.HAS_SHELTER,       is(true));
    assertThat(Rule.HAS_PLAN,          is(true));
    assertThat(Rule.HAS_LIGHT,         is(true));
    assertThat(Rule.HAS_YELL,          is(true));
    assertThat(Rule.HAS_ISOLATED_FEAR, is(true));
  }

  @Test
  public void checkGoldRules() {
    League.GOLD.activateRules();

    assertThat(Rule.HAS_WANDERER,      is(true));
    assertThat(Rule.HAS_SLASHER,       is(true));
    assertThat(Rule.HAS_SHELTER,       is(true));
    assertThat(Rule.HAS_PLAN,          is(true));
    assertThat(Rule.HAS_LIGHT,         is(true));
    assertThat(Rule.HAS_YELL,          is(true));
    assertThat(Rule.HAS_ISOLATED_FEAR, is(true));
  }

  @Test
  public void AllRulesActivatedInLegend() throws Exception {
    League.LEGEND.activateRules();

    assertThat(Rule.HAS_WANDERER,      is(true));
    assertThat(Rule.HAS_SLASHER,       is(true));
    assertThat(Rule.HAS_SHELTER,       is(true));
    assertThat(Rule.HAS_PLAN,          is(true));
    assertThat(Rule.HAS_LIGHT,         is(true));
    assertThat(Rule.HAS_YELL,          is(true));
    assertThat(Rule.HAS_ISOLATED_FEAR, is(true));
  }
  
  @Test
  @Parameters({
    "1|WOOD_3",
    "2|WOOD_2",
    "3|WOOD_1",
    "4|BRONZE",
    "5|SILVER",
    "6|GOLD",
    "7|LEGEND",
    // invalid -> LEGEND
    "0|LEGEND",
    "17|LEGEND",
  })
  public void levelsMatchLeagues(int level, League expectedLeague) throws Exception {
    assertThat(League.get(level), is (expectedLeague));
  }

}
