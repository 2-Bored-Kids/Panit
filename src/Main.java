import java.awt.Color;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import sum.ereignis.*;

// TODO: mehr code kommentare

public class Main extends EBAnwendung {
  Buntstift pen;

  byte paintMode = Consts.MODE_NORMAL, fillMode = Consts.NOFILL;

  int startPressX, startPressY;

  public Main() {
    super(Consts.SCREEN_X, Consts.SCREEN_Y);

    Utils.init();

    pen = new Buntstift();
    pen.setzeFuellmuster(fillMode);

    this.hatBildschirm.setTitle("Panit");
    Utils.setIcon(this.hatBildschirm, "icon.png");

    clearScreen();

    UI.init();

    pen.setzeLinienBreite(Consts.DEFAULT_WIDTH);

    fuehreAus();
  }

  public static void main(String[] args) { Main main = new Main(); }

  public void clearScreen() {
    this.hatBildschirm.loescheAlles();
    drawMenu();
  }

  public void drawMenu() {
    Buntstift menuPen = new Buntstift();

    // Dunkles Grau
    Utils.setColor(menuPen, 100, 100, 100);

    menuPen.setzeFuellmuster(Consts.FILL);

    menuPen.bewegeBis(0, 0);

    menuPen.zeichneRechteck(Consts.MENU_X, Consts.MENU_Y);
  }

  @Override
  public void bearbeiteDoppelKlick(int x, int y) {
    pen.hoch();
  }

  @Override
  public void bearbeiteMausBewegt(int x, int y) {
    boolean touchesMenuArea = x < Consts.MENU_X + pen.linienBreite() / 2;

    if (paintMode == Consts.MODE_NORMAL) {
      pen.normal();
    }

    if (paintMode == Consts.MODE_NORMAL) {
      if (pen.istUnten()) {
        if (!touchesMenuArea) {
          pen.bewegeBis(x, y);
          pen.zeichneKreis(pen.linienBreite() / 2);
        }
      }
    } else if (!touchesMenuArea) {
      if (paintMode == Consts.MODE_LINE) {
        if (startPressX + startPressY == 0) {
          pen.bewegeBis(x, y);
          return;
        }

        pen.wechsle();
        pen.runter();
        pen.setzeFuellmuster(Consts.FILL);

        pen.zeichneKreis(pen.linienBreite() / 2);
        pen.bewegeBis(startPressX, startPressY);

        pen.bewegeBis(x, y);
        pen.zeichneKreis(pen.linienBreite() / 2);
        pen.setzeFuellmuster(fillMode);

        pen.hoch();
        pen.normal();
      } else if (paintMode == Consts.MODE_RECTANGLE) {
        if (startPressX + startPressY == 0) {
          return;
        }

        pen.wechsle();

        drawViereck(
          startPressX, startPressY, (int)pen.hPosition(), (int)pen.vPosition());

        drawViereck(startPressX, startPressY, x, y);

        pen.bewegeBis(x, y);

        pen.normal();
      }
    }
  }

  @Override
  public void bearbeiteMausLos(int x, int y) {
    pen.hoch();

    if (paintMode == Consts.MODE_LINE || paintMode == Consts.MODE_RECTANGLE) {
      boolean touchesMenuArea = x < Consts.MENU_X + pen.linienBreite() / 2 &&
                                y < Consts.MENU_Y + pen.linienBreite() / 2;

      if (!touchesMenuArea) {
        switch (paintMode) {
          case Consts.MODE_LINE:
            if (startPressX + startPressY != 0) {
              pen.wechsle();
              pen.bewegeBis(startPressX, startPressY);

              drawLinie(startPressX, startPressY, x, y);
            }
            break;

          case Consts.MODE_RECTANGLE:
            if (startPressX + startPressY != 0) {
              drawViereck(startPressX, startPressY, x, y);
            }
            break;
        }
      }
    }

    startPressX = 0;
    startPressY = 0;
  }

  @Override
  public void bearbeiteMausDruck(int x, int y) {
    boolean touchesMenuArea = x < Consts.MENU_X + pen.linienBreite() / 2 &&
                              y < Consts.MENU_Y + pen.linienBreite() / 2;

    if (!touchesMenuArea) {
      pen.bewegeBis(x, y);

      if (paintMode == Consts.MODE_NORMAL) {
        pen.runter();
      } else if (paintMode == Consts.MODE_FILL) {
        pen.setzeLinienBreite(1);

        bucketFill(x, y);

        pen.setzeLinienBreite(UI.r_linewidth.wert() * 2);
      }
      startPressX = x;
      startPressY = y;
    }
  }

  public void bucketFill(int x, int y) {
    try {
      BufferedImage snapshot =
        Utils.createSnapshot(Utils.getPanel(this.hatBildschirm), null);

      Color colorReplaced = Utils.getColorAt(x, y, snapshot);

      JPanel panel = Utils.getPanel(this.hatBildschirm);
      JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(panel);

      int taskbarHeight = Toolkit.getDefaultToolkit()
                            .getScreenInsets(frame.getGraphicsConfiguration())
                            .bottom +
                          2;

      Queue<Vector2> q = new LinkedList<Vector2>();

      q.add(new Vector2(x, y));

      while (!q.isEmpty()) {
        Vector2 pos = q.poll();

        boolean touchesMenu = (pos.x < Consts.MENU_X && pos.y < Consts.MENU_Y);
        boolean touchesBorders =
          (pos.x >= Consts.SCREEN_X ||
           pos.y >= Consts.SCREEN_Y - taskbarHeight || pos.x < 0 || pos.y < 0);

        if (!touchesMenu && !touchesBorders &&
            Utils.getColorAt(pos.x, pos.y, snapshot).equals(colorReplaced)) {
          snapshot.setRGB(pos.x, pos.y, Utils.getColor(pen).getRGB());

          // TODO: optimize this

          pen.bewegeBis(pos.x, pos.y);
          pen.zeichneKreis(0.5);

          final int offsets[][] = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

          for (int i = 0; i < offsets.length; i++) {
            q.add(new Vector2(pos.x + offsets[i][0], pos.y + offsets[i][1]));
          }
        }
      }

    } catch (Exception e) {
    }
  }

  public void drawLinie(int sX, int sY, int eX, int eY) {
    pen.normal();
    pen.hoch();
    pen.bewegeBis(sX, sY);
    pen.runter();
    pen.setzeFuellmuster(Consts.FILL);
    pen.zeichneKreis(pen.linienBreite() / 2);
    pen.bewegeBis(eX, eY);
    pen.zeichneKreis(pen.linienBreite() / 2);
    pen.setzeFuellmuster(fillMode);
    pen.hoch();
  }

  public void drawViereck(int sX, int sY, int eX, int eY) {
    int minX = Math.min(sX, eX);
    int maxX = Math.max(sX, eX);

    int minY = Math.min(sY, eY);
    int maxY = Math.max(sY, eY);

    pen.bewegeBis(minX, minY);
    pen.zeichneRechteck(maxX - minX, maxY - minY);
    pen.bewegeBis(maxX, maxY);
  }

  // UI Funktionen

  public void s_fillModeGeklickt() {
    pen.setzeFuellMuster(UI.s_fillMode.angeschaltet() ? 1 : 0);
  }

  public void b_mode_paintGeklickt() { paintMode = Consts.MODE_NORMAL; }

  public void b_fillGeklickt() { paintMode = Consts.MODE_FILL; }

  public void b_mode_lineGeklickt() { paintMode = Consts.MODE_LINE; }

  public void b_mode_rectangleGeklickt() { paintMode = Consts.MODE_RECTANGLE; }

  public void b_delAllGeklickt() { clearScreen(); }

  public void a_blackGeklickt() { Utils.setColor(pen, 0, 0, 0); }

  public void a_redGeklickt() { Utils.setColor(pen, 180, 20, 20); }

  public void a_lightBlueGeklickt() { Utils.setColor(pen, 0, 190, 255); }

  public void a_darkBlueGeklickt() { Utils.setColor(pen, 0, 72, 192); }

  public void a_lightGreenGeklickt() { Utils.setColor(pen, 0, 230, 0); }

  public void a_darkGreenGeklickt() { Utils.setColor(pen, 16, 180, 34); }

  public void a_yellowGeklickt() { Utils.setColor(pen, 255, 236, 20); }

  public void a_orangeGeklickt() { Utils.setColor(pen, 255, 182, 20); }

  public void a_brownGeklickt() { Utils.setColor(pen, 100, 44, 44); }

  public void a_whiteGeklickt() { Utils.setColor(pen, 255, 255, 255); }

  public void r_linewidthGeaendert() {
    pen.setzeLinienBreite(UI.r_linewidth.wert() * 2);
  }

  public void b_saveGeklickt() {
    String filePath = Utils.pickSaveImage();

    // TODO: get rid of this hack
    try {
      Thread.sleep(500);
    } catch (Exception e) {
    }

    if (filePath != "") {
      Utils.saveImage(this.hatBildschirm, filePath);
    }
  }

  public void b_loadGeklickt() {
    File file = Utils.pickImage();

    if (file != null) {
      clearScreen();
      Utils.loadImage(this.hatBildschirm, file.getAbsolutePath());
    }
  }
}
