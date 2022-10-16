import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import sum.ereignis.Bildschirm;
import sum.ereignis.EBAnwendung;

public class Main extends EBAnwendung {

  // Initialize Main
  // Creating server & transmitter
  private static BetterStift pen;
  private static Main instance;

  public PanitServer server = null;

  public Transmitter transmitter = null;

  public BufferedImage image = new BufferedImage(Bildschirm.topFenster.breite(),
                                                 Bildschirm.topFenster.hoehe(),
                                                 BufferedImage.TYPE_INT_RGB);
  public static ResourceBundle resourceBundle;

  public Main() {
    // Creating Screen
    super(Consts.SCREEN_X, Consts.SCREEN_Y);

    resourceBundle = ResourceBundle.getBundle("resources/messages");

    this.hatBildschirm.setTitle("Panit");
    this.hatBildschirm.setResizable(false);
    Utils.setIcon(this.hatBildschirm, "resources/icon.png");

    instance = this;

    Utils.init();

    pen = new BetterStift(image);
    pen.setToDefault();

    this.hatBildschirm.addWindowListener(new WindowListener());

    fuehreAus();

    // Load UI
    UI.init();

    DrawingPanel drawingPanel = new DrawingPanel(this, getFrame());
    drawingPanel.setBounds(0, 0, Consts.SCREEN_X, Consts.SCREEN_Y);
    getFrame().add(drawingPanel);

    // We live in 2077
    getFrame().setBackground(Colors.GREY);
    getFrame().getContentPane().setBackground(Colors.GREY);

    clearScreen();
  }

  public static void main(String[] args) { new Main(); }

  public void clearScreen() {
    pen.clear();
    pen.drawToScreen();
  }

  // Overriding mouse functions
  @Override
  public void bearbeiteDoppelKlick(int x, int y) {
    bearbeiteMausLos(x, y);
  }

  @Override
  public void bearbeiteMausBewegt(int x, int y) {
    PenTasks.penMove(pen, x, y);

    if (pen.getStartPressX() + pen.getStartPressY() != 0) {
      sendPacket(new MovePacket(x, y));
    }
  }

  @Override
  public void bearbeiteMausLos(int x, int y) {
    PenTasks.penUp(pen, x, y);
    sendPacket(new HochPacket());
  }

  @Override
  public void bearbeiteMausDruck(int x, int y) {
    PenTasks.penDown(pen, x, y);
    sendPacket(new RunterPacket(x, y));
  }

  public static BetterStift getPen() { return pen; }

  public JFrame getFrame() {
    return (JFrame)SwingUtilities.getWindowAncestor(
      this.hatBildschirm.privatPanel());
  }

  // Server functions
  public void sendPacket(Packet packet) {
    if (transmitter != null) {
      transmitter.sende(packet.encode());
    }
  }

  public void connectToServer() {
    if (transmitter == null) {
      String[] parameters = UI.t_id.inhaltAlsText().split(PacketIds.SEPARATOR);

      try {
        transmitter = new Transmitter(
          this, parameters[0], Integer.parseInt(parameters[1]), false);
      } catch (Exception ignored) {
      }

      if (transmitter == null || !transmitter.vorhanden()) {
        transmitter = null;

        return;
      }

      clearScreen();
    }
  }

  public void disconnectFromServer() {
    if (transmitter != null) {
      sendPacket(new DisconnectPacket());

      transmitter.gibFrei();
      transmitter = null;

      clearScreen();
    }
  }

  private void stopServer() {
    if (server != null) {
      server.gibFrei();
      server = null;
    }
  }

  // UI listeners

  public void b_server() {
    if (server == null) {
      try {
        server = new PanitServer((int)UI.t_server_port.inhaltAlsZahl());
        UI.b_server.setzeInhalt(UI.getMenuText("stop"));
      } catch (Exception ignored) {
      }
    } else {
      disconnectFromServer();
      stopServer();
      UI.b_server.setzeInhalt(UI.getMenuText("start"));
    }
  }
  public void b_connection() {
    if (transmitter == null) {
      connectToServer();
    } else {
      disconnectFromServer();
    }
  }

  public void s_filling() {
    pen.setFillMode((byte)(UI.s_filling.angeschaltet() ? 1 : 0));
    sendPacket(new FillModePacket((byte)(UI.s_filling.angeschaltet() ? 1 : 0)));
  }

  public static void pickMode(byte newMode) {
    pen.setPaintMode(newMode);
    instance.sendPacket(new ModePacket(newMode));
  }

  public void b_del_all() {
    clearScreen();
    sendPacket(new ClearPacket());
  }

  public static void pickColor(Color color) {
    pen.setzeFarbe(color);
    instance.sendPacket(new ColorPacket(color));
  }

  public void r_line_width() {
    pen.setzeLinienBreite(UI.r_line_width.wert() * 2);
    sendPacket(new WidthPacket(UI.r_line_width.wert() * 2));
  }

  public void b_save() {
    String filePath = Utils.pickSaveImage();

    // Hack! Wait for the save menu to close
    try {
      Thread.sleep(500);
    } catch (Exception ignored) {
    }

    if (!filePath.equals("")) {
      Utils.saveImage(this.hatBildschirm, filePath);
    }
  }

  public void b_load() {
    File file = Utils.pickImage();

    if (file != null) {
      clearScreen();
      Utils.loadImage(this.hatBildschirm, file.getAbsolutePath());

      if (transmitter != null) {
        ImagePacket imgPk = new ImagePacket(" : ", " ");
        imgPk.IMG = ImageTasks.encode(image);
        sendPacket(imgPk);
      }
    }
  }

  private class WindowListener extends WindowAdapter {
    @Override
    public void windowClosing(final WindowEvent e) {
      disconnectFromServer();
      stopServer();
      beenden();
    }
  }
}
