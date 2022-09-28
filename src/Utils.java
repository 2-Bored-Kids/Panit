import sum.ereignis.Buntstift;
import sum.ereignis.Bildschirm;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Robot;
import javax.swing.JPanel;

import java.io.File;
import javax.imageio.ImageIO;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Utils {
  private static Field farbeFeld, panelFeld;

  public static void init() {
    try {
      farbeFeld = Buntstift.class.getDeclaredField("zFarbe");
      farbeFeld.setAccessible(true);

      panelFeld = Bildschirm.class.getDeclaredField("hatPanel");
      panelFeld.setAccessible(true);
    } catch (Exception exc) {}
  }

  public static void setColor(Buntstift stift, int r, int g, int b) {
    try {
        farbeFeld.set(stift, new Color(r, g, b));
    } catch (Exception exc) {}
  }

  public static JPanel getPanel(Bildschirm screen) {
    try {
        return (JPanel)panelFeld.get(Bildschirm.topFenster);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public static void saveImage(Bildschirm screen, String filePath) {
    //TODO: get rid of this hack
    JPanel panel = Utils.getPanel(screen);
    BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);

    try {
        image = new Robot().createScreenCapture(panel.bounds());

        ImageIO.write(image, "png", new File(filePath));
    } catch (Exception e) {}
  }

  public static void loadImage(Bildschirm screen, String filePath) {
    JPanel panel = Utils.getPanel(screen);

    BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
    try {
      image = ImageIO.read(new File(filePath));
    } catch (Exception e) {}

    panel.getGraphics().drawImage(image, 0, 0, screen);
  }
}

