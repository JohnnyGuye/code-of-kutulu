package runner.tests.waiter;

import java.util.Random;
import java.util.Scanner;

/**
 * Such a lazy guy...
 * @author johnny
 *
 */
public class Player {
    public static void main(String[] args) {
    		Scanner scanner = new Scanner(System.in);
        
        // read the map !
        int WIDTH = scanner.nextInt();
        int HEIGHT = scanner.nextInt();
        for (int l=0;l<HEIGHT;l++) {
          String line = scanner.next();
        }
        int p0 = scanner.nextInt();
        int p1 = scanner.nextInt();
        int p2 = scanner.nextInt();
        int p3 = scanner.nextInt();
        
        while (true) {
          int N = scanner.nextInt();
          for (int p = 0; p < N; p++) {
            int type = scanner.nextInt();
            int index = scanner.nextInt();
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int sanity = scanner.nextInt();
            int remainingPlans = scanner.nextInt();
            int remainingLights = scanner.nextInt();
          }
          
          // think
          System.out.println("WAIT gonna stay still...");
          
        }
    }
}
