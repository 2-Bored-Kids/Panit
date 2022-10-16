import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Taskbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import sum.ereignis.Bildschirm;
import sum.ereignis.Buntstift;

public abstract class Utils {
  private static Field colorField;

  // Set color of pen accessible

  public static void init() {
    try {
      colorField = BetterStift.class.getSuperclass().getDeclaredField("zFarbe");
      colorField.setAccessible(true);
    } catch (Exception ignored) {
    }
  }

  // Set pen color
  public static void setColor(Buntstift pen, int r, int g, int b) {
    try {
      colorField.set(pen, new Color(r, g, b));
    } catch (Exception ignored) {
    }
  }

  public static void setColor(Buntstift pen, Color color) {
    try {
      colorField.set(pen, color);
    } catch (Exception ignored) {
    }
  }

  public static Color getColor(Buntstift pen) {
    try {
      return (Color)colorField.get(pen);
    } catch (Exception ignored) {
    }

    return null;
  }

  // Tests if x, y is in menu
  public static boolean isInBounds(int x, int y, int radius) {
    return !(x < (Consts.MENU_X + radius)) &&
      !(x >= Consts.SCREEN_X || y >= Consts.SCREEN_Y || x < 0 || y < 0);
  }

  // Sets app icon
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
    } catch (Exception ignored) {
    }
  }

  // Crops the left side of the image & saves to path
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
    } catch (Exception ignored) {
    }
  }

  // Creates snapshot of JPanel
  public static BufferedImage createSnapshot(JPanel panel,
                                             Rectangle boundingBox) {
    if (boundingBox == null) {
      boundingBox = new Rectangle(panel.getLocationOnScreen(), panel.getSize());
    }

    try {
      return new Robot().createScreenCapture(boundingBox);
    } catch (Exception ignored) {
    }

    return null;
  }

  // Get pixel color at x, y
  public static Color getColorAt(int x, int y, BufferedImage image) {
    return new Color(image.getRGB(x, y));
  }

  // Loads image from path
  public static void loadImage(Bildschirm screen, String filePath) {
    JPanel panel = screen.privatPanel();

    BufferedImage image = new BufferedImage(
      panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
    try {
      image = ImageIO.read(new File(filePath));
    } catch (Exception ignored) {
    }

    Main.getPen().getBuffer().getGraphics().drawImage(
      image, Consts.MENU_X, 0, screen);
  }

  // Dialog for choosing a file path
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

  // Dialog for choosing an image
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
