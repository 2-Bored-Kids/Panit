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

  public BetterStift() {
    super();

    try {
      Field dbGraphics = Bildschirm.class.getDeclaredField("dbGraphics");
      dbGraphics.setAccessible(true);

      Field withDb = Bildschirm.class.getDeclaredField("zMitDoubleBuffering");
      withDb.setAccessible(true);

      this.buffer = new BufferedImage(Bildschirm.topFenster.breite(),
                                      Bildschirm.topFenster.hoehe(),
                                      BufferedImage.TYPE_INT_RGB);

      this.bufferGraphics = this.buffer.createGraphics();
      this.screenGraphics =
        this.get2DGraphics(Bildschirm.topFenster.privatPanel().getGraphics());

      dbGraphics.set(Bildschirm.topFenster, this.bufferGraphics);
      withDb.set(Bildschirm.topFenster, true);

      this.frame = (JFrame)SwingUtilities.getWindowAncestor(
        Bildschirm.topFenster.privatPanel());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public BufferedImage getBuffer() { return this.buffer; }

  public void drawToScreen() { drawToGraphics(this.screenGraphics); }

  public void drawToGraphics(Graphics g) {
    g.drawImage(buffer.getSubimage(Consts.MENU_X,
                                   0,
                                   this.buffer.getWidth() - Consts.MENU_X - 16,
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
