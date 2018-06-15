package runner.tests.debugmove;

import java.util.Scanner;

public class Player {
  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    int width = in.nextInt();
    int height = in.nextInt();
    if (in.hasNextLine()) {
        in.nextLine();
    }
    for (int i = 0; i < height; i++) {
        String line = in.nextLine();
    }
    int p0 = in.nextInt();
    int p1 = in.nextInt();
    int p2 = in.nextInt();
    int p3 = in.nextInt();
    // game loop
    while (true) {
        int san = Integer.MIN_VALUE;
        int a = 0, b = 0;
        int entityCount = in.nextInt();
        for (int i = 0; i < entityCount; i++) {
            String entityType = in.next();
            int id = in.nextInt();
            int x = in.nextInt();
            int y = in.nextInt();
            int sanity = in.nextInt();
            int param1 = in.nextInt();
            int param2 = in.nextInt();
            
            if(san < sanity && "WANDERER".equals(entityType)) {
                sanity = san;
                a=x;
                b=y;
            }
            
        }

        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");

        System.out.println("MOVE "+a+" "+b); // MOVE <x> <y> | WAIT | UP | DOWN | LEFT | RIGHT
    }
  }
}
