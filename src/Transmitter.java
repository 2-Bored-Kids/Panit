import packets.MovePacket;
import sum.netz.*;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

import static packets.PacketIds.*;

public class Transmitter extends Clientverbindung {
    Main main;

    byte paintMode = Consts.DEFAULT_PAINTMODE;

    byte fillMode = Consts.DEFAULT_FILLMODE;

    HashMap<String, BetterStift> userPens = new HashMap<>();

    int startPressX = 0;
    int startPressY = 0;

    public Transmitter(Main client, String server, int port, boolean mitProtokoll) {
        super(server, port, mitProtokoll);
        main = client;
    }

    @Override
    public void bearbeiteNachricht(String message) {



        String[] packet = message.split(SEPARATOR);

        String id = packet[0] + SEPARATOR + packet[1];

        packet = Arrays.copyOfRange(packet, 2, packet.length);


        switch (Integer.parseInt(packet[0])) {
            case JOIN:
                userPens.put(id, new BetterStift());
                BetterStift userPen = userPens.get(id);
                userPen.setFillMode(Consts.DEFAULT_FILLMODE);
                userPen.setzeLinienBreite(Consts.DEFAULT_WIDTH);
                userPen.setPaintMode(Consts.DEFAULT_PAINTMODE);
                userPen.setzeFarbe(Consts.DEFAULT_COLOR);
                System.out.println(userPens);
                break;
            case QUIT:
                userPens.remove(id);
                break;
            case CONNECT:
                UI.b_join.verstecke();
                UI.b_quit.zeige();
                UI.e_status.setzeInhalt("Verbunden");
                break;
            case DISCONNECT:
                UI.b_join.zeige();
                UI.b_quit.verstecke();
                UI.e_status.setzeInhalt("Getrennt");
                this.gibFrei();
                break;
            case RUNTER:
                penTasks.stiftRunter(userPens.get(id), (int)userPens.get(id).hPosition(), (int)userPens.get(id).vPosition());
                break;
            case HOCH:
                penTasks.stiftHoch(userPens.get(id), (int)userPens.get(id).hPosition(), (int)userPens.get(id).vPosition());
                break;
            case MOVE:
                MovePacket move = new MovePacket(packet);
                int x = move.X;
                int y = move.Y;
                penTasks.stiftBewegt(userPens.get(id), x, y);
                break;
            case WIDTH:
                userPens.get(id).setzeLinienbreite(Integer.parseInt(packet[1]));
                userPens.get(id).drawToScreen();
                break;
            case CLEAR:
                userPens.get(id).clear();
                break;
            case COLOR:
                int r = Integer.parseInt(packet[1]);
                int g = Integer.parseInt(packet[2]);
                int b = Integer.parseInt(packet[3]);
                Utils.setColor(userPens.get(id), new Color(r, g, b));
                break;
            case MODE:
                paintMode = Byte.parseByte(packet[1]);
                break;
            default:
                System.out.println(message);
                break;
        }

    }

    public void bearbeiteVerbindungsverlust() {
        UI.b_join.zeige();
        UI.b_quit.verstecke();
        UI.e_status.setzeInhalt("Getrennt");
    }
}