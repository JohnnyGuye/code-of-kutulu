package com.codingame.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.view.EndScreenModule;
import com.google.inject.Inject;

import codeofkutulu.Constants;
import codeofkutulu.Rule;
import codeofkutulu.mazes.Maze;
import codeofkutulu.mazes.MazeFactory;
import codeofkutulu.models.Cell;
import codeofkutulu.models.Minion;
import codeofkutulu.models.PlayerUnit;
import codeofkutulu.models.Playfield;
import codeofkutulu.models.Vector2D;
import codeofkutulu.models.effects.Effect;
import codeofkutulu.models.effects.ShelterEffect;
import codeofkutulu.tooltipModule.TooltipModule;
import codeofkutulu.view.AnimationFactory;
import codeofkutulu.view.TileFactory;
import codeofkutulu.view.ViewController;

public class Referee extends AbstractReferee {
  @Inject
  MultiplayerGameManager<Player> gameManager;
  @Inject
  GraphicEntityModule graphicEntityModule;
  @Inject
  private TooltipModule tooltipModule;
  @Inject
  private EndScreenModule endScreenModule;

  private static final String KILL_BY_MADNESS_MSG = " frightened to death";
  private static final String STUCK_MESSAGE = " is taken aback and won't act this turn";

  public static int NEXT_ID = 4; // the first fours (0, 1, 2, 3) are for players

  public static Maze maze;
  public static Playfield playfield;
  public static List<Minion> minions = new ArrayList<>();
  public static List<PlayerUnit> livingPlayers = new ArrayList<>();
  public static List<Effect> effects = new ArrayList<>();

  public static int turn;

  private ViewController view;

  @Override
  public void init() {
    this.turn = 0;
    Properties params = gameManager.getGameParameters();
    Constants.random = new Random(getSeed(params));

    setupLeague(gameManager.getLeagueLevel());
    gameManager.setFrameDuration(500);


    pickMaze(params);

    AnimationFactory.getInstance().init(graphicEntityModule);
    TileFactory.getInstance().init(graphicEntityModule);

    for (Player player : gameManager.getPlayers()) {
      PlayerUnit playerUnit = player.createUnit();
      playerUnit.move(playfield.getStartPos(player.getIndex()));
      Referee.livingPlayers.add(playerUnit);
      sendInitData(player);
    }

    updateIsolation(); // for icon in view at first turn
    createView();
  }

  private void pickMaze(Properties params) {
    String mazeName = params.getProperty("map", "");
    Maze maze = MazeFactory.getInstance().getMaze(mazeName);
    if (maze == Maze.NO_MAZE) {
      Referee.maze = MazeFactory.getInstance().pickRandomMaze(Constants.random.nextInt());
    } else {
      Referee.maze = maze;
    }
    Referee.playfield = new Playfield(Referee.maze);
  }

  private Long getSeed(Properties params) {
    try {
      return Long.parseLong(params.getProperty("seed", "0"));
    } catch(NumberFormatException nfe) {
      return 0L;
    }
  }

  private void setupLeague(int level) {
    League league = League.get(level);
    league.activateRules();
  }

  /**
   * The magic happens there
   */
  @Override
  public void gameTurn(int turn) {
    this.turn = turn;

    // out (to players)
    sendInputToPlayers();

    // in (from players)
    updatePlayerWishedMove();

    // play
    updateGame();
    updateView();
    checkGameEndConditions(turn);
  }

  /**
   * Send the 0 turn informations to the player
   *
   * @param player
   */
  private void sendInitData(Player player) {
    sendMap(player);
    Constants.sendParametrizedConstants(player);
  }

  private void sendMap(Player player) {
    player.sendInputLine(String.valueOf(Referee.maze.dim.x));
    player.sendInputLine(String.valueOf(Referee.maze.dim.y));
    playfield.sendMapTo(player);
  }

  private void createView() {
    view = new ViewController(graphicEntityModule, gameManager, tooltipModule);
    createPlayersView();
    createGrid();
    updateView();
  }

  private void createPlayersView() {
    for (Player player : gameManager.getPlayers()) {
      view.createPlayerView(player);
    }
  }

  private void createGrid() {
    for (int x = 0; x < maze.dim.x; x++) {
      for (int y = 0; y < maze.dim.y; y++) {
        view.createCellView(playfield.getCell(x, y));
      }
    }
  }

  private void sendInputToPlayers() {
    for (Player player : gameManager.getActivePlayers()) {
      sendInput(player);
    }
  }

  private void checkGameEndConditions(int turn) {
    if (turn >= Constants.MAX_TURNS) {
      for (Player player : gameManager.getActivePlayers()) {
        player.setScore(Constants.MAX_TURNS + player.getPlayerUnit().getSanity());
        player.deactivate();
      }
      gameManager.endGame();
    }
    if (gameManager.getActivePlayers().isEmpty()) {
      gameManager.endGame();
    }
  }

  private void updateView() {
    view.update();
  }

  private void updatePlayerWishedMove() {
    for (Player player : gameManager.getActivePlayers()) {
      try {
        player.setDisplayMessage("");

        List<String> outputs = player.getOutputs();
        // Check validity of the player output and compute the new game state
        try {
          PlayerAction wantedAction = new PlayerAction(player.getPlayerUnit(), outputs.get(0));
          player.getPlayerUnit().wantedAction = wantedAction;
          player.setDisplayMessage(wantedAction.message);
        } catch(Exception any) {
          killPlayer(player, "Invalid Input: '" + (outputs.isEmpty() ? "" : outputs.get(0)) + "'");
          gameManager.addToGameSummary("Invalid command by " + player.getNicknameToken() + ".");
        }
      } catch (TimeoutException e) {
        killPlayer(player, String.format("$%d timeout!", player.getIndex()));
      }
    }
  }

  void killPlayer(Player player, String message) {
    removeThePlayer(player.getPlayerUnit());
    player.setScore(-1);
    player.kill(message);
  }

  private void sendInput(Player player) {
    List<String> toSend = new ArrayList<String>();
    int thisPlayerIndex = player.getIndex();

    // always send players with this player at first
    for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
      Player p = gameManager.getPlayer((i + thisPlayerIndex) % Constants.MAX_PLAYERS);
      if (!p.isActive()) {
        continue;
      }
      toSend.add(p.getPlayerUnit().toOutput());
    }

    for (Minion minion : minions) {
      toSend.add(minion.toOutput());
    }

    for (Effect effect : effects) {
      String output = effect.toOutput();
      if (output != null) {
        toSend.add(output);
      }
    }

    player.sendInputLine(String.valueOf(toSend.size()));
    for (String send : toSend) {
      player.sendInputLine(send);
    }

    player.execute();
  }

  void updateGame() {
    resetEffects();

    spawnMinions();

    doPlayerAction();
    doEffects();

    updateMinions();
    recallMinions();

    spreadMadness();

    removeDeadPlayers();

    if (Rule.HAS_SHELTER) {
      spawnShelters();
    }
  }

  private void resetEffects() {
    playfield.resetEffects();
  }

  void spawnShelters() {
    if (turn != 0 && turn % Constants.SHELTER_SPAWN_INTERVAL == 0) {
      effects.removeIf(effect -> effect instanceof ShelterEffect);

      for (Cell cell : playfield.getShelterCells()) {
        ShelterEffect shelterEffect = new ShelterEffect(cell);
        effects.add(shelterEffect);
      }
    }
  }

  static void doEffects() {
    for (Effect effect : effects) {
      effect.doEffect();
    }
    effects.removeIf(effect -> effect.isFinished());
  }

  private void updateIsolation() {
    for (Player player : gameManager.getActivePlayers()) {
      PlayerUnit pu = player.getPlayerUnit();
      pu.updateIsolation(gameManager.getActivePlayers().stream()
              .map(p -> p.getPlayerUnit())
              .collect(Collectors.toList()));
    }
  }

  // Spread more if not near a player
  private void spreadMadness() {
    updateIsolation();

    if (isKutuluComing()) {
      PlayerUnit pu = gameManager.getActivePlayers().get(0).getPlayerUnit();
      pu.madden(Constants.SPREAD_KUTULU_MADNESS_PER_TURN_AMOUNT);
    } else {
      for (Player player : gameManager.getActivePlayers()) {
        PlayerUnit pu = player.getPlayerUnit();
        pu.spreadMadness();
      }
    }
    removeDeadPlayers();
  }

  static public String join(Object... args) {
    return Stream.of(args).map(String::valueOf).collect(Collectors.joining(" "));
  }

  private void doPlayerAction() {
    resetStuckedPlayerActions();
    handleYELLsFirst();
    resetStuckedPlayerActions(); // freshly stucked player have their actions reset too
    handleOtherCommands();
  }

  private void handleOtherCommands() {
    for (Player player : gameManager.getActivePlayers()) {
      PlayerAction wantedAction = player.getPlayerUnit().wantedAction;
      Vector2D pos = player.getPlayerUnit().pos;
      Cell cell = playfield.getCell(Vector2D.add(pos, wantedAction.action.move));

      if (player.getPlayerUnit().isStuck()) {
        if (player.getPlayerUnit().wantedAction != PlayerAction.WAIT) {
          player.getPlayerUnit().wantedAction = PlayerAction.WAIT;
          gameManager.addToGameSummary(player.getNicknameToken() + STUCK_MESSAGE);
        }
        continue;
      } else if (!cell.isWalkable() || player.getPlayerUnit().isPerformingInvalidMove()) {
        gameManager.addToGameSummary("Invalid move from " + player.getNicknameToken());
        continue;
      } else {
        player.getPlayerUnit().move(cell);
        if (wantedAction.isCastEffect()) {
          if (playerHasCurrentEffect(player.getPlayerUnit())) {
            gameManager.addToGameSummary("Invalid effect from " + player.getNicknameToken());
            continue;
          } else {
            Effect newEffect = null;
            if (Rule.HAS_PLAN && wantedAction.action == PlayerAction.Action.PLAN) {
              newEffect = player.getPlayerUnit().castPlanEffect();
            } else if (Rule.HAS_LIGHT && wantedAction.action == PlayerAction.Action.LIGHT) {
              newEffect = player.getPlayerUnit().castLightEffect();
            }

            if (newEffect != null) {
              effects.add(newEffect);
            } else {
              gameManager.addToGameSummary("Invalid effect from " + player.getNicknameToken());
            }
          }
        }
      }
    }
  }

  private void handleYELLsFirst() {
    for (Player player : gameManager.getActivePlayers()) {
      PlayerAction wantedAction = player.getPlayerUnit().wantedAction;
      if (Rule.HAS_YELL && wantedAction.isYell()) {
        for (Player opponent : gameManager.getActivePlayers()) {
          if (opponent.getPlayerUnit() == player.getPlayerUnit()) continue;
          boolean opponentFooled = player.getPlayerUnit().yell(opponent.getPlayerUnit());
          if (opponentFooled) {
            gameManager.addToGameSummary(opponent.getNicknameToken() + " fooled by " + player.getNicknameToken());
          }
        }
      }
    }
  }

  private void resetStuckedPlayerActions() {
    for (Player player : gameManager.getActivePlayers()) {
      if (player.getPlayerUnit().isStuck()
              && player.getPlayerUnit().wantedAction != PlayerAction.WAIT) {
        player.getPlayerUnit().wantedAction = PlayerAction.WAIT;
        gameManager.addToGameSummary(player.getNicknameToken() + STUCK_MESSAGE);
      }
    }
  }

  private boolean playerHasCurrentEffect(PlayerUnit player) {
    return effects.stream().filter(effect -> effect.getCaster() == player).count() >= 1;
  }

  private void removeDeadPlayers() {
    for (Player player : gameManager.getPlayers()) {
      if (player.isActive() && player.getPlayerUnit().isDead()) {
        removeThePlayer(player.getPlayerUnit());
        player.setScore(turn);
        player.kill(String.format("$%d %s", player.getIndex(), KILL_BY_MADNESS_MSG));
      }
    }
  }

  void removeThePlayer(PlayerUnit player) {
    if (livingPlayers.contains(player)) {
      effects.removeIf(effect -> {
        return effect.getCaster() == player
                && effect.getType() != Constants.EFFECT_YELL_TYPE;
      });

      livingPlayers.remove(player);
      player.kill();
    }
  }

  private void recallMinions() {
    minions.removeIf(minion -> minion.isBeingRecalled());
  }

  private void updateMinions() {
    minions.forEach(minion -> minion.update());
  }

  private boolean[] slasherSpawned = new boolean[] { false, false, false, false };

  private void spawnMinions() {
    if (Rule.HAS_SLASHER) {
      spawnPotentialSlashers();
    }

    if (Rule.HAS_WANDERER) {
      spawnWanderers();
    }
  }

  private void spawnWanderers() {
    if (turn % Constants.WANDERER_SPAWN_INTERVAL != 0)
      return; // no spawn this turn

    Vector2D[] wSpawns = Referee.maze.getWandererSpawns();
    if (wSpawns.length > 0) {

      int[] playerMinDistances = new int[wSpawns.length];
      for (int i = 0; i < playerMinDistances.length; i++)
        playerMinDistances[i] = Referee.maze.dim.manhattanDistance();

      for (int i = 0; i < wSpawns.length; i++) {
        for (PlayerUnit player : Referee.livingPlayers) {
          playerMinDistances[i] = Math.min(playerMinDistances[i], player.pos.manhattanDistance(wSpawns[i]));
        }
      }
      int maxOfMins = Arrays.stream(playerMinDistances).max().getAsInt();

      for (int i = 0; i < wSpawns.length; i++) {
        if (playerMinDistances[i] != maxOfMins)
          continue;

        Minion minion = Referee.playfield.spawnWanderer(NEXT_ID++, wSpawns[i]);

        minions.add(minion);
        view.createMinionView(minion);
      }
    }
  }

  private void spawnPotentialSlashers() {
    for (PlayerUnit player : Referee.livingPlayers) {
      if (!slasherSpawned[player.id] && player.getSanity() < Constants.SLASHER_SANITY_SPAWN) {
        Minion minion = Referee.playfield.spawnSlasher(NEXT_ID++, player);
        slasherSpawned[player.id] = true;

        minions.add(minion);
        view.createMinionView(minion);
      }
    }
  }

  // === Utilitary functions ===
  public static List<PlayerUnit> playersOnCell(Cell cell) {
    return Referee.livingPlayers.stream().filter(player -> player.isOnCell(cell))
            .collect(Collectors.toList());
  }

  @Override
  public void onEnd() {
    endScreenModule.setScores(gameManager.getPlayers().stream().mapToInt(p -> p.getScore()).toArray());
    endScreenModule.setSanities(gameManager.getPlayers().stream().mapToInt(p -> p.getPlayerUnit().getSanity()).toArray());
  }

  public static boolean isKutuluComing() {
    return livingPlayers.size() == 1;
  }
}