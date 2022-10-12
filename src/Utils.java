import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Taskbar;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import sum.ereignis.Bildschirm;
import sum.ereignis.Buntstift;

public class Utils {
  private static Field farbeFeld;

  //Set color of pen accessible

  public static void init() {
    try {
      farbeFeld = BetterStift.class.getSuperclass().getDeclaredField("zFarbe");
      farbeFeld.setAccessible(true);
    } catch (Exception e) {
    }
  }


  //Set pen color
  public static void setColor(Buntstift stift, int r, int g, int b) {
    try {
      farbeFeld.set(stift, new Color(r, g, b));
    } catch (Exception e) {
    }
  }

  public static void setColor(Buntstift stift, Color color) {
    try {
      farbeFeld.set(stift, color);
    } catch (Exception e) {
    }
  }

  public static Color getColor(Buntstift stift) {
    try {
      return (Color)farbeFeld.get(stift);
    } catch (Exception e) {
    }

    return null;
  }

  //Tests if x, y is in menu
  public static boolean isInBounds(int x, int y, int radius) {
    return !(x < (Consts.MENU_X + radius)) &&
      !(x >= Consts.SCREEN_X || y >= Consts.SCREEN_Y || x < 0 || y < 0);
  }

  //Sets app icon
  public static void setIcon(Bildschirm screen, String filePath) {
    JPanel panel = screen.privatPanel();
    JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(panel);

    try {
      BufferedImage icon;

      if (Utils.class.getClassLoader()
            .getResource("Utils.class")
            .toString()
            .startsWith("jar:")) {
        icon = ImageIO.read(
          Utils.class.getClassLoader().getResourceAsStream(filePath));
      } else {
        icon = ImageIO.read(new File(filePath));
      }

      frame.setIconImage(icon);
      Taskbar.getTaskbar().setIconImage(icon);
    } catch (Exception e) {
    }
  }

  //Crops the left side of the image & saves to path
  public static void saveImage(Bildschirm screen, String filePath) {
    JPanel panel = screen.privatPanel();

    Rectangle panelBoundingBox =
      new Rectangle(panel.getLocationOnScreen(), panel.getSize());
    panelBoundingBox.x += Consts.MENU_X;
    panelBoundingBox.width -= Consts.MENU_X;

    try {
      ImageIO.write(createSnapshot(screen.privatPanel(), panelBoundingBox),
                    "png",
                    new File(filePath));
    } catch (Exception e) {
    }
  }


  //Creates snapshot of JPanel
  public static BufferedImage createSnapshot(JPanel panel,
                                             Rectangle boundingBox) {
    if (boundingBox == null) {
      boundingBox = new Rectangle(panel.getLocationOnScreen(), panel.getSize());
    }

    try {
      return new Robot().createScreenCapture(boundingBox);
    } catch (Exception e) {
    }

    return null;
  }

  //Get pixel color at x, y
  public static Color getColorAt(int x, int y, BufferedImage image) {
    int color = image.getRGB(x, y);
    int a = (color >> 24) & 255;
    int r = (color >> 16) & 255;
    int g = (color >> 8) & 255;
    int b = color & 255;

    return new Color(r, g, b, a);
  }


  //Loads image from path
  public static void loadImage(Bildschirm screen, String filePath) {
    JPanel panel = screen.privatPanel();

    BufferedImage image = new BufferedImage(
      panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
    try {
      image = ImageIO.read(new File(filePath));
    } catch (Exception e) {
    }

    Main.getPen().getBuffer().getGraphics().drawImage(
      image, Consts.MENU_X, 0, screen);
  }

  //Dialog for choosing a file path
  public static String pickSaveImage() {
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Save image");

    int option = chooser.showSaveDialog(null);
    if (option == JFileChooser.APPROVE_OPTION) {
      String filePath = chooser.getSelectedFile().getAbsolutePath();

      return filePath + (filePath.endsWith(".png") ? "" : ".png");
    }

    return "";
  }


  //Dialog for choosing an image
  public static File pickImage() {
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Load image");

    FileNameExtensionFilter filter =
      new FileNameExtensionFilter("PNG Images", "png");
    chooser.setFileFilter(filter);

    int option = chooser.showOpenDialog(null);
    if (option == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFile();
    }

    return null;
  }
}
