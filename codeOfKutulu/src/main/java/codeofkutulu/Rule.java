package codeofkutulu;

public class Rule {
  public static boolean HAS_WANDERER = false;
  public static boolean HAS_SLASHER = false;

  public static boolean HAS_SHELTER = false;
  
  public static boolean HAS_PLAN = false;
  public static boolean HAS_LIGHT = false;
  public static boolean HAS_YELL = false;
 
  public static boolean HAS_ISOLATED_FEAR = false;
  
  public static void reset() {
    HAS_WANDERER = false;
    HAS_SLASHER = false;

    HAS_SHELTER = false;
    
    HAS_PLAN = false;
    HAS_LIGHT = false;
    HAS_YELL = false;
   
    HAS_ISOLATED_FEAR = false;
  }
}
