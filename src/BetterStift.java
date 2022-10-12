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

  private byte paintMode, fillMode;

  private int startPressX, startPressY;

  public BetterStift(BufferedImage bufferedImage) {
    super();

    setToDefault();

    try {
      Field dbGraphics = Bildschirm.class.getDeclaredField("dbGraphics");
      dbGraphics.setAccessible(true);

      Field withDb = Bildschirm.class.getDeclaredField("zMitDoubleBuffering");
      withDb.setAccessible(true);

      this.buffer = bufferedImage;

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

  public BetterStift() {
    this(new BufferedImage(Bildschirm.topFenster.breite(),
            Bildschirm.topFenster.hoehe(),
            BufferedImage.TYPE_INT_RGB));
  }

  public void setToDefault() {
    this.setFillMode(Consts.DEFAULT_FILLMODE);
    this.setzeLinienBreite(Consts.DEFAULT_WIDTH);
    this.setPaintMode(Consts.DEFAULT_PAINTMODE);
    this.setzeFarbe(Consts.DEFAULT_COLOR);
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

  public byte getPaintMode(){
    return paintMode;
  }
  public void setPaintMode(byte paintMode){
    this.paintMode = paintMode;
  }

  public int getStartPressX() {
    return startPressX;
  }

  public int getStartPressY() {
    return startPressY;
  }

  public void setStartPressX(int newStartPressX) {
    this.startPressX = newStartPressX;
  }

  public void setStartPressY(int newStartPressY) {
    this.startPressY = newStartPressY;
  }
  public byte getFillMode(){
    return fillMode;
  }
  public void setFillMode(byte fillMode){
    this.fillMode = fillMode;
    this.setzeFuellmuster(fillMode);
  }
}
