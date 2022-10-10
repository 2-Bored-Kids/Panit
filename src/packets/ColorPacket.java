package packets;

import java.awt.Color;

public class ColorPacket extends Packet {

    public int R, G, B;

    public ColorPacket(Color color){
        super(PacketIds.COLOR);
        this.R = color.getRed();
        this.G = color.getGreen();
        this.B = color.getBlue();
    }

    public ColorPacket(String str){
        super(PacketIds.COLOR);
        String[] color = decode(str);
        this.R = Integer.parseInt(color[1]);
        this.G = Integer.parseInt(color[2]);
        this.B = Integer.parseInt(color[3]);
    }

    @Override
    public String encode(){
        return Integer.toString(PacketIds.COLOR) + PacketIds.SEPARATOR + Integer.toString(R) + PacketIds.SEPARATOR  + Integer.toString(G) + PacketIds.SEPARATOR + Integer.toString(B);
    }
}
