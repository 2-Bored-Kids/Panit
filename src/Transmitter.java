import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import sum.netz.Clientverbindung;

/* To prevent a lot of confusion: there is a distinction between "fill-mode"
   and "fill mode", with the first one being MODE_BUCKETFILL and the latter
   being whether the SuM pen should be filling the shapes it draws */

public class Transmitter extends Clientverbindung {
  // The String in this case is the user id: "<ip>:<port>"
  HashMap<String, BetterStift> userPens = new HashMap<>();

  public Transmitter(String server, int port, boolean logging) {
    // The 'logging' here being the SuM logging. Using such a library for tasks
    // like this one has consequences.
    super(server, port, logging);
  }

  // Handle packets
  @Override
  public void bearbeiteNachricht(String message) {
    String[] packet = message.split(PacketIds.SEPARATOR);

    // Get the user id as described above
    String id = packet[0] + PacketIds.SEPARATOR + packet[1];
    // And remove it from the packet so the individual packet constructors dont
    // have to do the same
    packet = Arrays.copyOfRange(packet, 2, packet.length);

    // The first byte of the packet determines its kind
    // The packet ids are pulled from the server submodule
    switch (Integer.parseInt(packet[0])) {
      case PacketIds.JOIN:
        // Hence we use an id -> pen hashmap to keep track of the users,
        // removing/adding them from it essentially disconnects/connects them
        userPens.put(id, new BetterStift(Main.instance.image));
        Main.instance.sendPacket(
          new PenSettingsPacket(id,
                                Main.instance.getPen(),
                                Main.instance.getPen().getFillMode(),
                                Utils.getColor(Main.instance.getPen()),
                                Main.instance.getPen().getPaintMode()));
        System.out.println("User connected: " + id);
        break;
      case PacketIds.QUIT:
        userPens.remove(id);
        System.out.println("User disconnected: " + id);
        break;
      case PacketIds.CONNECT:
        // Toggle the text on the connect button
        UI.b_connection.setzeInhalt(Main.getTranslated("disconnect"));
        break;
      case PacketIds.DISCONNECT:
        UI.b_connection.setzeInhalt(Main.getTranslated("connect"));
        this.gibFrei();
        break;
      case PacketIds.DOWN:
        RunterPacket rnPk = new RunterPacket(packet);
        PenTasks.penDown(userPens.get(id), rnPk.X, rnPk.Y);
        break;
      case PacketIds.UP:
        PenTasks.penUp(userPens.get(id),
                       (int)userPens.get(id).hPosition(),
                       (int)userPens.get(id).vPosition());
        break;
      case PacketIds.MOVE:
        MovePacket move = new MovePacket(packet);
        PenTasks.penMove(userPens.get(id), move.X, move.Y);
        break;
      case PacketIds.WIDTH:
        userPens.get(id).setzeLinienbreite(Integer.parseInt(packet[1]));
        break;
      case PacketIds.CLEAR:
        userPens.get(id).clear();
        userPens.get(id).drawToScreen();
        break;
      case PacketIds.COLOR:
        ColorPacket color = new ColorPacket(packet);
        Utils.setColor(userPens.get(id), new Color(color.R, color.G, color.B));
        break;
      case PacketIds.MODE:
        ModePacket mode = new ModePacket(packet);
        userPens.get(id).setPaintMode(mode.MODE);
        break;
      case PacketIds.IMAGE:
        ImagePacket imgPk = new ImagePacket(packet);

        if (imgPk.IMG == "") {
          imgPk.IMG = ImageTasks.encode(Main.instance.image);

          Main.instance.sendPacket(imgPk);
        } else {
          ImageTasks.drawDecode(Main.instance.getPen(), imgPk.IMG);
        }
        break;
      case PacketIds.SETTING:
        // Used to sync all pen settings at once
        PenSettingsPacket setPk = new PenSettingsPacket(packet);

        BetterStift pen = new BetterStift(Main.instance.image);

        pen.bewegeBis(setPk.PEN.hPosition(), setPk.PEN.vPosition());
        if (setPk.PEN.istUnten()) {
          pen.runter();
        }

        pen.setzeFarbe(Utils.getColor(setPk.PEN));
        pen.setPaintMode(setPk.PAINT_MODE);
        pen.setzeFuellMuster(setPk.FILL_MODE);
        pen.setzeLinienbreite(setPk.PEN.linienBreite());

        userPens.put(id, pen);

        break;
      case PacketIds.FILLING:
        // The "fill mode" one ^
        userPens.get(id).setFillMode(new FillModePacket(packet).FILLMODE);
        break;
      default:
        System.out.println("Unknown packet: " + packet[0] + " | User: " + id);
        break;
    }
  }

  // Is ran whenever we get separated from the server for whatever reason
  public void bearbeiteVerbindungsverlust() {
    UI.b_connection.setzeInhalt(Main.getTranslated("connect"));

    if (Main.instance.transmitter != null) {
      Main.instance.transmitter.gibFrei();
      Main.instance.transmitter = null;
    }
    Main.instance.clearScreen();
  }
}
