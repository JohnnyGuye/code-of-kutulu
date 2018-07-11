package codeofkutulu;

import java.util.Random;

import com.codingame.game.Player;

import codeofkutulu.models.Vector2D;

public class Constants {

  public static final String FILE_SEPARATOR = System.getProperty("file.separator");

  public static final int MAX_PLAYERS = 4;
  public static final int MAX_TURNS = 200;

  public static final int MAX_DIRS = 4;
  public static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3, STAY = 4;
  public static final String[] directions = { "up", "right", "down", "left", "wait" };
  public static final Vector2D vdir[] = { Vector2D.UP, Vector2D.RIGHT, Vector2D.DOWN, Vector2D.LEFT };

  public static int SPREAD_MADNESS_PER_TURN_AMOUNT = 3; // [3..5]
  public static int SPREAD_MADNESS_PLAYER_PROX_PER_TURN_AMOUNT = 1; // [1..3]
  public static final int SPREAD_KUTULU_MADNESS_PER_TURN_AMOUNT = 50;
  public static final int IM_NOT_ALONE_RANGE = 2;

  public static final int PLAYER_TYPE = 0;
  public static final int WANDERER_TYPE = 1;
  public static final int SLASHER_TYPE = 2;
  public static final int EFFECT_SHELTER_TYPE = 3;
  public static final int EFFECT_PLAN_TYPE = 4;
  public static final int EFFECT_REPULSE_TYPE = 5;
  public static final int EFFECT_YELL_TYPE = 6;
  public static final String[] unittypes = { "EXPLORER", "WANDERER", "SLASHER", "EFFECT_SHELTER", "EFFECT_PLAN", "EFFECT_LIGHT", "EFFECT_YELL" };
  
  public static int WANDERER_SPAWN_TIME = 3; // [3..6]
  public static final int WANDERER_SPAWN_INTERVAL = 5;
  public static int WANDERER_LIFE_TIME = 40; // [20..40]
//  public static final int WANDERER_FRIGHTEN_RADIUS = 0;

  public static final int SLASHER_SPAWN_TIME = 6;
  public static final int SLASHER_RUSH_PREP = 2;
  public static final int SLASHER_RUSH_STUN = 6;
  public static final int SLASHER_SANITY_SPAWN = 200;

  public static final int MINION_SANITY_LOST = 20;
  public static final boolean MINION_SEARCH_CYCLE_DIRECTIONS = true;
  public static final boolean MINION_SEARCH_CYCLE_PLAYERS = true;

  public static final int PLAYER_MAX_SANITY = 250;
  public static final int PLAYER_MAX_PLAN_EFFECT = 2;
  public static final int PLAYER_MAX_LIGHT_EFFECT = 3;

  public static final int EFFECT_PLAN_MAX_DURATION = 4;
  public static final int EFFECT_PLAN_HEAL_RADIUS = 2;
  public static final int EFFECT_PLAN_HEAL = 3;

  public static final int SHELTER_SPAWN_INTERVAL = 50;
  public static final int EFFECT_MAX_SHELTER_ENERGY = 10;
  public static final int EFFECT_SHELTER_HEAL = 5;

  public static final int EFFECT_REPULSE_MAX_DURATION = 2;
  public static final int EFFECT_REPULSE_RADIUS = 5;
  public static final int EFFECT_REPULSE_WEIGHT = 4;

  // Visual constant
  public static final int CELL_SIZE = 64;
  public static final Vector2D SCREEN_DIM = new Vector2D(1920, 1080);

  public static final int PLAYER_INITIAL_STUCK_ROUNDS = 2;

  public static final int EFFECT_YELL_RADIUS = 1;


  public static Random random; // not so constant constant


  public static void calculateParametrizedConstants() {
    SPREAD_MADNESS_PER_TURN_AMOUNT =  randomBetween(3,6);;
    SPREAD_MADNESS_PLAYER_PROX_PER_TURN_AMOUNT = randomBetween(1,3);
    WANDERER_SPAWN_TIME = randomBetween(3, 6);
    WANDERER_LIFE_TIME = randomBetween(30,40);
  }

  private static int randomBetween(int min, int max) {
    if (max <= min) return max;
    return min + random.nextInt(max-min+1);
  }

  public static void sendParametrizedConstants(Player player) {
    StringBuilder sb = new StringBuilder();
    sb.append(SPREAD_MADNESS_PER_TURN_AMOUNT).append(" ");
    sb.append(SPREAD_MADNESS_PLAYER_PROX_PER_TURN_AMOUNT).append(" ");
    sb.append(WANDERER_SPAWN_TIME).append(" ");
    sb.append(WANDERER_LIFE_TIME).append(" ");
    
    player.sendInputLine(sb.toString().trim());
  }


}
