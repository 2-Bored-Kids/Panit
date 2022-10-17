import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import sum.ereignis.Bildschirm;
import sum.ereignis.Buntstift;

// A collection of reflection hacks we use to get into the guts of the SuM
// library & some utility functions

public class Utils {
  private static Field colorField;

  public static void init() {
    try {
      colorField = BetterStift.class.getSuperclass().getDeclaredField("zFarbe");
      colorField.setAccessible(true);
    } catch (Exception ignored) {
    }
  }

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

  // Tests if a position is valid for drawing something other than UI
  public static boolean isInBounds(int x, int y, int radius) {
    return !(x < (Consts.MENU_X + radius)) &&
      !(x >= Consts.SCREEN_X || y >= Consts.SCREEN_Y || x < 0 || y < 0);
  }

  // Sets the program's icon
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
    } catch (Exception ignored) {
    }
  }

  // Saves a screenshot of the drawable area
  public static void saveImage(Bildschirm screen, String filePath) {
    JPanel panel = screen.privatPanel();

    Rectangle panelBoundingBox =
      new Rectangle(panel.getLocationOnScreen(), panel.getSize());
    panelBoundingBox.x += Consts.MENU_X;
    panelBoundingBox.width -= Consts.MENU_X + 16;

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

  public static void chooseColor() {
    JFrame frame = new JFrame(Main.getTranslated("color_choose_title"));
    frame.setSize(580, 300);
    frame.setResizable(false);
    frame.setAlwaysOnTop(true);

    JColorChooser colorChooser =
      new JColorChooser(Utils.getColor(Main.getPen()));
    colorChooser.setPreviewPanel(new JPanel());

    colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        Main.changeColor(colorChooser.getColor());
        UI.a_colors.clearSelection();
      }
    });

    frame.add(colorChooser);
    frame.setVisible(true);
  }

  public static String encodeImage(BufferedImage image) {
    try {
      ByteArrayOutputStream outputStr = new ByteArrayOutputStream();
      ImageIO.write(image, "png", outputStr);
      return Base64.getEncoder().encodeToString(outputStr.toByteArray());
    } catch (Exception e) {
    }

    return "";
  }

  public static void drawDecodeImage(BetterStift pen, String image) {
    try {
      byte[] bytes = Base64.getDecoder().decode(image);
      pen.getBuffer().getGraphics().drawImage(
        ImageIO.read(new ByteArrayInputStream(bytes)),
        0,
        0,
        Bildschirm.topFenster);
    } catch (Exception e) {
    }
  }
}
