import jdk.jshell.execution.Util;
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
                BetterStift userPen = userPens.put(id, new BetterStift());
                userPen.setzeFuellmuster(Consts.DEFAULT_FILLMODE);
                userPen.setzeLinienBreite(Consts.DEFAULT_WIDTH);
                userPen.setPaintMode(Consts.DEFAULT_PAINTMODE);
                userPen.setzeFarbe(Consts.DEFAULT_COLOR);
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
            /*case RUNTER:
                penEvents.stiftRunter(pencil, (int) pencil.hPosition(), (int) pencil.vPosition(), paintMode, startPressX, startPressY, main);
                break;
            case HOCH:
                penEvents.stiftHoch(pencil, (int) pencil.hPosition(), (int) pencil.vPosition(), paintMode, startPressX, startPressY, Consts.FILL, main);
                break;
            case MOVE:
                int x = Integer.parseInt(packet[1]);
                int y = Integer.parseInt(packet[2]);
                penEvents.stiftBewegt(pencil, x, y, paintMode, startPressX, startPressY, Consts.FILL, main);
                break;
            case WIDTH:
                pencil.setzeLinienbreite(Integer.parseInt(packet[1]));
                break;
            case CLEAR:
                menuEvents.clearScreen(main.hatBildschirm);
                break;
            case COLOR:
                int r = Integer.parseInt(packet[1]);
                int g = Integer.parseInt(packet[2]);
                int b = Integer.parseInt(packet[3]);
                Utils.setColor(pencil, new Color(r, g, b));
                break;*/
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