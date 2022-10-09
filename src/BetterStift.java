import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import sum.ereignis.Bildschirm;
import sum.ereignis.Buntstift;

public class BetterStift extends Buntstift {
  private BufferedImage buffer;

  private JFrame frame;

  private Graphics2D bufferGraphics, screenGraphics;

  public BetterStift(Bildschirm screen) {
    super();

    try {
      Field dbGraphics = Bildschirm.class.getDeclaredField("dbGraphics");
      dbGraphics.setAccessible(true);

      Field withDb = Bildschirm.class.getDeclaredField("zMitDoubleBuffering");
      withDb.setAccessible(true);

      this.buffer = new BufferedImage(
        screen.breite(), screen.hoehe(), BufferedImage.TYPE_INT_RGB);

      this.bufferGraphics = this.buffer.createGraphics();
      this.screenGraphics =
        this.get2DGraphics(screen.privatPanel().getGraphics());

      dbGraphics.set(screen, this.bufferGraphics);
      withDb.set(screen, true);

      this.frame =
        (JFrame)SwingUtilities.getWindowAncestor(Utils.getPanel(screen));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public BufferedImage getBuffer() { return this.buffer; }

  public void drawToScreen() {
    this.screenGraphics.drawImage(
      buffer.getSubimage(Consts.MENU_X,
                         0,
                         this.buffer.getWidth() - Consts.MENU_X,
                         this.buffer.getHeight()),
      Consts.MENU_X,
      0,
      this.kenntPrivatschirm);
  }

  public void clear() {
    this.bufferGraphics.setPaint(new Color(255, 255, 255));
    this.bufferGraphics.fillRect(
      0, 0, this.buffer.getWidth(), this.buffer.getHeight());
  }
}
