import sum.ereignis.Buntstift;
import java.awt.Color;
import java.lang.reflect.Field;

public class Utils {
  private static Field farbeFeld;

  public static void init() {
    try {
      farbeFeld = Buntstift.class.getDeclaredField("zFarbe");
      farbeFeld.setAccessible(true);
    } catch (Exception exc) {}
  }

  public static void setColor(Buntstift stift, int r, int g, int b) {
    try {
        farbeFeld.set(stift, new Color(r, g, b));
    } catch (Exception exc) {}
  }
}

