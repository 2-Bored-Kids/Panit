import sum.netz.Clientverbindung;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class Transmitter extends Clientverbindung {
    Main main;

    HashMap<String, BetterStift> userPens = new HashMap<>();

    public Transmitter(Main client, String server, int port, boolean logging) {
        super(server, port, logging);
        main = client;
    }

    public boolean isConnected() {
        return zVerbindungAktiv;
    }

    @Override
    public void bearbeiteNachricht(String message) {
        String[] packet = message.split(PacketIds.SEPARATOR);

        //Get the id+port header
        String id = packet[0] + PacketIds.SEPARATOR + packet[1];
        //And remove it
        packet = Arrays.copyOfRange(packet, 2, packet.length);

        switch (Integer.parseInt(packet[0])) {
            case PacketIds.JOIN:
                userPens.put(id, new BetterStift(main.image));
                main.sendPacket(new PenSettingsPacket(id, main.getPen(), main.getPen().getFillMode(), Utils.getColor(main.getPen()), main.getPen().getPaintMode()));
                System.out.println("User connected: " + id);
                break;
            case PacketIds.QUIT:
                userPens.remove(id);
                System.out.println("User disconnected: " + id);
                break;
            case PacketIds.CONNECT:
                UI.b_connection.setzeInhalt("Trennen");
                break;
            case PacketIds.DISCONNECT:
                UI.b_connection.setzeInhalt("Verbinden");
                this.gibFrei();
                break;
            case PacketIds.RUNTER:
                RunterPacket rnPk = new RunterPacket(packet);
                PenTasks.stiftRunter(userPens.get(id), rnPk.X, rnPk.Y);
                break;
            case PacketIds.HOCH:
                PenTasks.stiftHoch(userPens.get(id), (int)userPens.get(id).hPosition(), (int)userPens.get(id).vPosition());
                break;
            case PacketIds.MOVE:
                MovePacket move = new MovePacket(packet);
                PenTasks.stiftBewegt(userPens.get(id), move.X, move.Y);
                break;
            case PacketIds.WIDTH:
                userPens.get(id).setzeLinienbreite(Integer.parseInt(packet[1]));
                userPens.get(id).drawToScreen();
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
                    imgPk.IMG = ImageTasks.encode(main.image);

                    main.sendPacket(imgPk);
                } else {
                    ImageTasks.drawDecode(main.getPen(), imgPk.IMG);
                }
                break;
            case PacketIds.SETTING:
                PenSettingsPacket setPk = new PenSettingsPacket(packet);

                BetterStift pen = new BetterStift(main.image);

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
            default:
                System.out.println("Unknown packet: " + packet[0] + " | User: " + id);
                break;
        }
    }

    public void bearbeiteVerbindungsverlust() {
        UI.b_connection.setzeInhalt("Verbinden");

        if (main.transmitter != null) {
            main.transmitter.gibFrei();
        }

        main.transmitter = null;

        main.clearScreen();
    }
}
