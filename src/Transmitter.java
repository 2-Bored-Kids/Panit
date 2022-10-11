import packets.ColorPacket;
import packets.ModePacket;
import packets.MovePacket;
import sum.ereignis.Bildschirm;
import sum.netz.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;

import packets.PacketIds;

public class Transmitter extends Clientverbindung {
    Main main;

    HashMap<String, BetterStift> userPens = new HashMap<>();

    public Transmitter(Main client, String server, int port, boolean logging) {
        super(server, port, logging);
        main = client;
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
                break;
            case PacketIds.QUIT:
                userPens.remove(id);
                break;
            case PacketIds.CONNECT:
                UI.b_join.verstecke();
                UI.b_quit.zeige();
                UI.e_status.setzeInhalt("Verbunden");
                break;
            case PacketIds.DISCONNECT:
                UI.b_join.zeige();
                UI.b_quit.verstecke();
                UI.e_status.setzeInhalt("Getrennt");
                this.gibFrei();
                break;
            case PacketIds.RUNTER:
                PenTasks.stiftRunter(userPens.get(id), (int)userPens.get(id).hPosition(), (int)userPens.get(id).vPosition());
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
            default:
                System.out.println("Unknown packet: " + packet[0] + " | User: " + id);
                break;
        }
    }

    public void bearbeiteVerbindungsverlust() {
        UI.b_join.zeige();
        UI.b_quit.verstecke();
        UI.e_status.setzeInhalt("Getrennt");

        main.transmitter = null;
    }
}
