package com.codingame.game;

import codeofkutulu.Constants;
import codeofkutulu.Rule;
import codeofkutulu.mazes.MazeFactory;

public enum League {
  WOOD_3(1) {
    @Override
    public void activateRules() {
      Rule.HAS_WANDERER = true;
      Rule.HAS_ISOLATED_FEAR = true;
    }
  },
  WOOD_2(2) {
    @Override
    public void activateRules() {
      WOOD_3.activateRules();
      Rule.HAS_PLAN = true;
      Rule.HAS_LIGHT = true;
    }
  },
  WOOD_1(3) {
    @Override
    public void activateRules() {
      WOOD_2.activateRules();
      Rule.HAS_SLASHER = true;
      Rule.HAS_YELL = true;
      Rule.HAS_SHELTER = true;
      Constants.calculateParametrizedConstants();
    }
  },
  BRONZE(4) {
    @Override
    public void activateRules() {
      WOOD_1.activateRules();
      MazeFactory.getInstance().activateBronzeMaps();
    }
  },
  SILVER(5) {
    @Override
    public void activateRules() {
      BRONZE.activateRules();
    }
  },
  GOLD(6) {
    @Override
    public void activateRules() {
      SILVER.activateRules();
    }
  },
  LEGEND(7) {
    @Override
    public void activateRules() {
      GOLD.activateRules();
    }
  };
  
  int level;
  public abstract void activateRules();

  League(int level) {
    this.level = level;
  }

  public static League get(int level) {
    for (League league : League.values()) {
      if (league.level == level) return league;
    }
    return League.LEGEND;
  }
}
