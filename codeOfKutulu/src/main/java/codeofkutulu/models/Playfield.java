package codeofkutulu.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.codingame.game.Player;
import com.codingame.game.Referee;

import codeofkutulu.mazes.Maze;

public class Playfield {
  private Cell cells[][];
  private Maze maze;
  private List<Cell> shelterCells = new ArrayList<>();

  public Playfield(Maze maze) {
    this.maze = maze;
    this.cells = new Cell[this.maze.dim.y][this.maze.dim.x];

    for (int y = 0; y < this.maze.dim.y; y++) {
      for (int x = 0; x < this.maze.dim.x; x++) {
        Cell cell = new Cell(x, y);
        setCellAt(x, y, cell);

        switch (this.maze.getMap()[y].charAt(x)) {
        case Maze.WANDERER_SPAWN:
          cell.setAsSpawnPoint();
          break;
        case Maze.SHELTER:
          cell.evolveInShelter();
          shelterCells.add(cell);
          break;
        case Maze.WALL:
          cell.type = CellType.WALL;
          break;
        case Maze.EMPTY:
        default:
          cell.type = CellType.EMPTY;
        }
      }
    }
  }

  Cell[][] getCells() {
    return cells;
  }

  public List<PlayerUnit> getPlayersInCell(Cell cell) {
    return Referee.livingPlayers.stream().filter(player -> player.isOnCell(cell)).collect(Collectors.toList());
  }

  public boolean isInBoundaries(int x, int y) {
    return !(x < 0 || x >= this.maze.dim.x || y < 0 || y >= this.maze.dim.y);
  }

  public boolean isInBoundaries(Vector2D pos) {
    return isInBoundaries(pos.x, pos.y);
  }

  public boolean isInBoundaries(Cell cell) {
    return !cell.isInvalidCell() && isInBoundaries(cell.pos);
  }

  public Cell setCellAt(int x, int y, Cell cell) {
    return cells[y][x] = cell;
  }

  public Cell setCellAt(Vector2D pos, Cell cell) {
    return setCellAt(pos.x, pos.y, cell);
  }

  public Cell getCell(int x, int y) {
    return isInBoundaries(x, y) ? cells[y][x] : Cell.INVALID_CELL;
  }

  public Cell getCell(Vector2D a) {
    return getCell(a.x, a.y);
  }

  public Cell getCell(Unit unit) {
    return getCell(unit.pos);
  }

  public Cell getStartPos(int id) {
    Vector2D pos = maze.getStartPos()[id];
    return getCell(pos);
  }

  public void sendMapTo(Player player) {
    for (int i = 0; i < this.maze.dim.y; i++) {
      player.sendInputLine(maze.getLineForPlayer(i));
    }
  }

  public MinionWanderer spawnWanderer(int id, Vector2D spawnPos) {
    MinionWanderer minion = new MinionWanderer(id);

    minion.pos.set(spawnPos);

    return minion;
  }

  public MinionSlasher spawnSlasher(int id, PlayerUnit player) {
    MinionSlasher slasher = new MinionSlasher(id, player);

    return slasher;
  }

  public void resetEffects() {

    for (int y = 0; y < this.maze.dim.y; y++) {
      for (int x = 0; x < this.maze.dim.x; x++) {
        getCell(x, y).resetEffects();
      }
    }
  }

  public List<Cell> getShelterCells() {
    return shelterCells;
  }
}
