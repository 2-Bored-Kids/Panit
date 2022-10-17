import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import sum.ereignis.EBAnwendung;

// Interfaces with user input and does basic SuM initialisation

public class Main extends EBAnwendung {
  private BetterStift pen;

  public static Main instance;

  public PanitServer server = null;

  public Transmitter transmitter = null;

  // Everything is drawn onto this image, then onto the screen
  public BufferedImage image = new BufferedImage(Consts.SCREEN_X,
                                                 Consts.SCREEN_Y,
                                                 BufferedImage.TYPE_INT_RGB);

  private static ResourceBundle languageBundle;


  public static boolean isPicking = false;

  public static boolean isMouseDown = false;

  public Main() {
    super(Consts.SCREEN_X, Consts.SCREEN_Y);

    this.hatBildschirm.setTitle("Panit");
    this.hatBildschirm.setResizable(false);
    Utils.setIcon(this.hatBildschirm, "resources/icon.png");

    languageBundle = ResourceBundle.getBundle("resources/messages");

    instance = this;

    Utils.init();

    pen = new BetterStift(image);
    getPen().setToDefault();

    this.hatBildschirm.addWindowListener(new WindowListener());

    fuehreAus();

    // Load UI
    UI.init();

    DrawingPanel drawingPanel = new DrawingPanel(getFrame());
    drawingPanel.setBounds(0, 0, Consts.SCREEN_X, Consts.SCREEN_Y);
    getFrame().add(drawingPanel);

    // We live in 2077
    getFrame().setBackground(Colors.GREY);
    getFrame().getContentPane().setBackground(Colors.GREY);

    clearScreen();
  }

  public static void main(String[] args) { new Main(); }

  public void clearScreen() {
    getPen().clear();
    getPen().drawToScreen();
  }

  @Override
  public void bearbeiteDoppelKlick(int x, int y) {
    bearbeiteMausLos(x, y);
  }

  @Override
  public void bearbeiteMausBewegt(int x, int y) {
    PenTasks.penMove(pen, x, y);
    if (getPen().getStartPressX() + getPen().getStartPressY() != 0) {
      sendPacket(new MovePacket(x, y));
    }
  }

  @Override
  public void bearbeiteMausLos(int x, int y) {
    isMouseDown = false;
    PenTasks.penUp(pen, x, y);
    sendPacket(new PenUpPacket());
  }

  @Override
  public void bearbeiteMausDruck(int x, int y) {
    if (isPicking){
      changeColor(new Color(image.getRGB(x, y)));
      UI.a_colors.clearSelection();
     isPicking = false; return;
    }
    if (isMouseDown){
      return;
    }
    isMouseDown = true;
    PenTasks.penDown(pen, x, y);
    sendPacket(new PenDownPacket(x, y));
  }

  public static BetterStift getPen() { return instance.pen; }

  public JFrame getFrame() {
    return (JFrame)SwingUtilities.getWindowAncestor(
      this.hatBildschirm.privatPanel());
  }

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
          parameters[0], Integer.parseInt(parameters[1]), false);
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

  public static String getTranslated(String str) {
    return languageBundle.getString(str);
  }

  // UI listeners

  private class WindowListener extends WindowAdapter {
    @Override
    public void windowClosing(final WindowEvent e) {
      disconnectFromServer();
      stopServer();
      beenden();
    }
  }

  public void b_server() {
    if (server == null) {
      try {
        server = new PanitServer((int)UI.t_server_port.inhaltAlsZahl());
        UI.b_server.setzeInhalt(Main.getTranslated("stop"));
      } catch (Exception ignored) {
      }
    } else {
      disconnectFromServer();
      stopServer();
      UI.b_server.setzeInhalt(Main.getTranslated("start"));
    }
  }
  public void b_connection() {
    if (transmitter == null) {
      connectToServer();
    } else {
      disconnectFromServer();
    }
  }

  public void b_colorPicker() {
    Utils.chooseColor();
  }

  public void b_pickColor() {
    isPicking = true;
  }

  public void s_filling() {
    getPen().setFillMode((byte)(UI.s_filling.angeschaltet() ? 1 : 0));
    sendPacket(new FillModePacket((byte)(UI.s_filling.angeschaltet() ? 1 : 0)));
  }

  public static void pickMode(byte newMode) {
    getPen().setPaintMode(newMode);
    instance.sendPacket(new ModePacket(newMode));
  }

  public void b_del_all() {
    clearScreen();
    sendPacket(new ClearPacket());
  }

  public static void changeColor(Color color) {
    UI.p_colorPreviewPanel.setBackground(color);
    getPen().setzeFarbe(color);
    instance.sendPacket(new ColorPacket(color));
  }

  public void r_line_width() {
    getPen().setzeLinienBreite(UI.r_line_width.wert() * 2);
    sendPacket(new WidthPacket(UI.r_line_width.wert() * 2));
  }

  public void b_save() {
    String filePath = Utils.pickSaveImage();

    // Hack! Wait for the save dialog to close
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
        imgPk.IMG = Utils.encodeImage(image);
        sendPacket(imgPk);
      }
    }
  }
}
