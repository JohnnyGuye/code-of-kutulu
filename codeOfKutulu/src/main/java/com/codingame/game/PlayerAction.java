package com.codingame.game;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import codeofkutulu.Rule;
import codeofkutulu.models.Cell;
import codeofkutulu.models.PlayerUnit;
import codeofkutulu.models.Vector2D;
import codeofkutulu.pathfinder.PathFinder;
import codeofkutulu.pathfinder.PathFinder.PathFinderResult;

public class PlayerAction {
  public enum Action {
    WAIT (0, 0),
    UP   (0, -1),
    DOWN (0, 1),
    LEFT (-1, 0),
    RIGHT(1, 0),
    PLAN (0, 0), 
    LIGHT(0, 0),
    YELL (0 , 0),
    ;
    
    public final Vector2D move = new Vector2D();
  
    Action(int dx, int dy) {
      this.move.set( dx, dy );
      
    }

    public String read(String output) {
      return output.substring(this.toString().length());
    }
  }

  public static final PlayerAction WAIT = new PlayerAction(Action.WAIT);

  Action action;
  public String message;
  
  private PlayerAction(Action action) {
    this.action = action;
    this.message = "";
  }
  
  public PlayerAction(PlayerUnit player, String line) throws Exception {
    Pattern PLAYER_MOVE_PATTERN = Pattern.compile("^MOVE (?<x>-?\\d+) (?<y>-?\\d+)(?:\\s+(?<message>.+))?\\s*$", Pattern.CASE_INSENSITIVE);
    Pattern PLAYER_OTHER_INPUT_PATTERN = Pattern.compile("^(?<action>\\w+)(?:\\s+(?<message>.+))?\\s*$", Pattern.CASE_INSENSITIVE);
    
    String message;
    String output;
    
    player.resetPerformingInvalidMove();
    Matcher moveMatcher = PLAYER_MOVE_PATTERN.matcher(line);
    if (moveMatcher.find()) {
      int x = Integer.parseInt(moveMatcher.group("x"));
      int y = Integer.parseInt(moveMatcher.group("y"));
      output = coordinatesToAction(player, x, y).name();
      if ("WAIT".equals(output)) {
          player.setPerformingInvalidMove(true);
      }
      message = moveMatcher.group("message");
    } else {
      Matcher matcher = PLAYER_OTHER_INPUT_PATTERN.matcher(line);
      matcher.find();
      output = matcher.group("action").toUpperCase().trim();
      message = matcher.group("message");
    }
    
    this.action = PlayerAction.Action.valueOf(output);
    
    if ("PLAN".equals(this.action.name()) && !Rule.HAS_PLAN) {
      this.action = Action.WAIT;
    } else if ("LIGHT".equals(this.action.name()) && !Rule.HAS_LIGHT) {
      this.action = Action.WAIT;
    } else if ("YELL".equals(this.action.name()) && !Rule.HAS_YELL) {
      this.action = Action.WAIT;
    }
    
    this.message = message != null ? message : "";
  }
  
  private Action coordinatesToAction(PlayerUnit player, int x, int y) {
    Cell targetCell = Referee.playfield.getCell(x, y);
    if (targetCell == Cell.INVALID_CELL || !targetCell.isWalkable()) {
      return Action.WAIT;
    }
    
    PathFinderResult findPath = new PathFinder()
        .from(player.getCurrentCell())
        .to(targetCell)
        .findPath();

    if (findPath == PathFinderResult.NO_PATH || !findPath.hasNextCell()) {
      return Action.WAIT;
    } else {
      Cell nextCell = findPath.getNextCell();
      if (nextCell == player.getCurrentCell().cellFromHere(new Vector2D(1, 0))) return Action.RIGHT;
      if (nextCell == player.getCurrentCell().cellFromHere(new Vector2D(-1, 0))) return Action.LEFT;
      if (nextCell == player.getCurrentCell().cellFromHere(new Vector2D(0, -1))) return Action.UP;
      if (nextCell == player.getCurrentCell().cellFromHere(new Vector2D(0, 1))) return Action.DOWN;
    }

    return Action.WAIT;
  }

  public boolean isCastEffect() {
    return action == PlayerAction.Action.PLAN || action == PlayerAction.Action.LIGHT;
  }

  public boolean isYell() {
    return action == Action.YELL;
  }

}
