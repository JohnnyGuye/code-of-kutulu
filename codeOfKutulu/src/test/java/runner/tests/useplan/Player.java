package runner.tests.useplan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * BOT that goes randomly, but only in the direction he possibly can (or WAIT)
 * @author nmahoude
 *
 */
public class Player {
  static int cells[][];
  private static int WIDTH;
  private static int HEIGHT;

  private final static String WAIT = "WAIT";
  private final static String UP = "UP";
  private final static String DOWN = "DOWN";
  private final static String LEFT = "LEFT";
  private final static String RIGHT = "RIGHT";
  private final static String PLAN = "PLAN";
  
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    Random random = new Random(0);

    List<String> possibleOutputs = new ArrayList<>();
    readPlayfield(scanner);
    int p0 = scanner.nextInt();
    int p1 = scanner.nextInt();
    int p2 = scanner.nextInt();
    int p3 = scanner.nextInt();
    while (true) {
      int N = scanner.nextInt();
      for (int p = 0; p < N; p++) {
        String type = scanner.next();
        int index = scanner.nextInt();
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        int sanity = scanner.nextInt();
        int remainingPlans = scanner.nextInt();
        int remainingLights = scanner.nextInt();        
        
        if (p == 0) {
          possibleOutputs.clear();
          possibleOutputs.add(WAIT);
          if (x > 0 && cells[x-1][y] == 0) possibleOutputs.add(LEFT);
          if (x < WIDTH-1 && cells[x+1][y] == 0) possibleOutputs.add(RIGHT);
          if (y > 0 && cells[x][y-1] == 0) possibleOutputs.add(UP);
          if (y < HEIGHT-1 && cells[x][y+1] == 0) possibleOutputs.add(DOWN);
        }
      }

      // think
      System.out.println(PLAN);

    }
  }

  private static void readPlayfield(Scanner scanner) {
    WIDTH = scanner.nextInt();
    HEIGHT = scanner.nextInt();
    cells = new int[WIDTH][HEIGHT];
    for (int y = 0; y < HEIGHT; y++) {
      String line = scanner.next();
      for (int x=0;x<WIDTH;x++) {
        cells[x][y] = line.charAt(x) == '#' ? 1 : 0;
      }
    }
  }
}
