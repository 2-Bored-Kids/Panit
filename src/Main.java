import sum.ereignis.Bildschirm;
import sum.ereignis.EBAnwendung;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

// TODO: mehr code kommentare

public class Main extends EBAnwendung {
  private static BetterStift pen;
  private static Main instance;

  public PanitServer server = null;

  public Transmitter transmitter = null;

  public BufferedImage image = new BufferedImage(Bildschirm.topFenster.breite(),
          Bildschirm.topFenster.hoehe(),
          BufferedImage.TYPE_INT_RGB);

  public Main() {
    super(Consts.SCREEN_X, Consts.SCREEN_Y);

    instance = this;

    Utils.init();

    pen = new BetterStift(image);
    pen.setToDefault();

    this.hatBildschirm.setTitle("Panit");
    this.hatBildschirm.setResizable(false);
    Utils.setIcon(this.hatBildschirm, "icon.png");

    fuehreAus();

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

  @Override
  public void bearbeiteDoppelKlick(int x, int y) {
    bearbeiteMausLos(x, y);
  }

  @Override
  public void bearbeiteMausBewegt(int x, int y) {
    PenTasks.stiftBewegt(pen, x, y);

    if (pen.getStartPressX() + pen.getStartPressY() != 0) {
      sendPacket(new MovePacket(x, y));
    }
  }

  @Override
  public void bearbeiteMausLos(int x, int y) {
    PenTasks.stiftHoch(pen, x, y);
    sendPacket(new HochPacket());
  }

  @Override
  public void bearbeiteMausDruck(int x, int y) {
    PenTasks.stiftRunter(pen, x, y);
    sendPacket(new RunterPacket(x, y));
  }

  public static BetterStift getPen() {
    return pen;
  }

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
      String ip = Consts.DEFAULT_SERVER_IP;
      int port = Consts.DEFAULT_SERVER_PORT;

      transmitter = new Transmitter(this, ip, port, false);

      if (!transmitter.isConnected()) {
        transmitter.gibFrei();
        transmitter = null;

        pen.setToDefault();
        clearScreen();
      }
    }
  }

  //TODO: fix multiple lines at once, send configs on join, sync picture, handle connections, read port and ip from textfield

  public void disconnectFromServer(){
    if (transmitter != null){
      sendPacket(new DisconnectPacket());

      transmitter.gibFrei();
      transmitter = null;

      pen.setToDefault();
      clearScreen();
    }
  }

  @Override
  public void beenden() {
    super.beenden();
    if (transmitter != null) {
      transmitter.gibFrei();
    }
  }
  // UI Funktionen

  public void b_serverGeklickt(){
    if (server == null){
      try{
        server = new PanitServer((int) UI.t_server_port.inhaltAlsZahl());
        UI.b_server.setzeInhalt("Trennen");
      }catch (Exception ignored){}
    }else {
      disconnectFromServer();
      server.gibFrei();
      server = null;
      UI.b_server.setzeInhalt("Verbinden");
    }
  }
  public void b_connectionGeklickt(){
    if (transmitter == null){
      connectToServer();
    }else {
      disconnectFromServer();
    }
  }

  public void s_fillModeGeklickt() {
    pen.setFillMode((byte)(UI.s_fillMode.angeschaltet() ? 1 : 0));
  }

  public void b_mode_paintGeklickt() {
    pen.setPaintMode(Consts.MODE_NORMAL);
    sendPacket(new ModePacket(Consts.MODE_NORMAL));
  }

  public void b_fillGeklickt() {
    pen.setPaintMode(Consts.MODE_BUCKETFILL);
    sendPacket(new ModePacket(Consts.MODE_BUCKETFILL));
  }

  public void b_mode_lineGeklickt() {
    pen.setPaintMode(Consts.MODE_LINE);
    sendPacket(new ModePacket(Consts.MODE_LINE));
  }

  public void b_mode_rectangleGeklickt() {
    pen.setPaintMode(Consts.MODE_RECTANGLE);
    sendPacket(new ModePacket(Consts.MODE_RECTANGLE));
  }

  public void b_delAllGeklickt() {
    clearScreen();
    sendPacket(new ClearPacket());
  }

  public static void pickColor(Color color) {
    pen.setzeFarbe(color);
    instance.sendPacket(new ColorPacket(color));
  }

  public void r_linewidthGeaendert() {
    pen.setzeLinienBreite(UI.r_linewidth.wert() * 2);
    sendPacket(new WidthPacket(UI.r_linewidth.wert() * 2));
  }

  public void b_saveGeklickt() {
    String filePath = Utils.pickSaveImage();

    // Hack! Wait for the save menu to close
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
