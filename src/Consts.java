import java.awt.Color;

// Here we define the stuff we managed not to mindlessly hardcode

public class Consts {
  public final static int SCREEN_X = 1600, SCREEN_Y = 900;

  public final static int MENU_X = 170, MENU_Y = SCREEN_Y;

  public final static byte MODE_NORMAL = 1, MODE_BUCKETFILL = 2, MODE_LINE = 3,
                           MODE_RECTANGLE = 4;

  public final static byte NOFILL = 0, FILL = 1;

  public final static String DEFAULT_SERVER_IP = "localhost";
  public final static int DEFAULT_SERVER_PORT = 1243;

  public final static int DEFAULT_WIDTH = 10;
  public final static byte DEFAULT_PAINTMODE = MODE_NORMAL;
  public final static byte DEFAULT_FILLMODE = FILL;
  public final static Color DEFAULT_COLOR = Colors.BLACK;
}
