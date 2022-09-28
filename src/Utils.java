import sum.ereignis.Buntstift;
import sum.ereignis.Bildschirm;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Robot;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

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
    } catch (Exception e) {}
  }

  public static void setColor(Buntstift stift, int r, int g, int b) {
    try {
        farbeFeld.set(stift, new Color(r, g, b));
    } catch (Exception e) {}
  }

  public static JPanel getPanel(Bildschirm screen) {
    try {
        return (JPanel)panelFeld.get(Bildschirm.topFenster);
    } catch (Exception e) {}

    return null;
  }

  //Crops the left side of the image
  public static void saveImage(Bildschirm screen, String filePath) {
    JPanel panel = Utils.getPanel(screen);
    BufferedImage image = new BufferedImage(panel.getWidth() - Consts.MENU_X, panel.getHeight(), BufferedImage.TYPE_INT_ARGB);

    Rectangle panelBoundingBox = new Rectangle(panel.getLocationOnScreen(), panel.getSize());
    panelBoundingBox.x += Consts.MENU_X;
    panelBoundingBox.width -= Consts.MENU_X;

    try {
        image = new Robot().createScreenCapture(panelBoundingBox);

        ImageIO.write(image, "png", new File(filePath));
    } catch (Exception e) {}
  }

  public static void loadImage(Bildschirm screen, String filePath) {
    JPanel panel = Utils.getPanel(screen);

    BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
    try {
      image = ImageIO.read(new File(filePath));
    } catch (Exception e) {}

    panel.getGraphics().drawImage(image, Consts.MENU_X, 0, screen);
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

    FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
    chooser.setFileFilter(filter);

    int option = chooser.showOpenDialog(null);
    if (option == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFile();
   }

   return null;
  }
}

