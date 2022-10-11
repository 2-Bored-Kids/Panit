import packets.ColorPacket;
import packets.ModePacket;
import packets.MovePacket;
import sum.ereignis.Bildschirm;
import sum.netz.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;

import static packets.PacketIds.*;

public class Transmitter extends Clientverbindung {
    Main main;

    HashMap<String, BetterStift> userPens = new HashMap<>();

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
                userPens.put(id, new BetterStift(main.image));
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
                PenTasks.stiftRunter(userPens.get(id), (int)userPens.get(id).hPosition(), (int)userPens.get(id).vPosition());
                break;
            case HOCH:
                PenTasks.stiftHoch(userPens.get(id), (int)userPens.get(id).hPosition(), (int)userPens.get(id).vPosition());
                break;
            case MOVE:
                MovePacket move = new MovePacket(packet);
                PenTasks.stiftBewegt(userPens.get(id), move.X, move.Y);
                break;
            case WIDTH:
                userPens.get(id).setzeLinienbreite(Integer.parseInt(packet[1]));
                userPens.get(id).drawToScreen();
                break;
            case CLEAR:
                userPens.get(id).clear();
                userPens.get(id).drawToScreen();
                break;
            case COLOR:
                ColorPacket color = new ColorPacket(packet);
                Utils.setColor(userPens.get(id), new Color(color.R, color.G, color.B));
                break;
            case MODE:
                ModePacket mode = new ModePacket(packet);
                userPens.get(id).setPaintMode(mode.MODE);
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