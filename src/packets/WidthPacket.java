package packets;

public class WidthPacket extends Packet {

    int WIDTH;

    public WidthPacket(int width) {
        super(PacketIds.WIDTH);
        this.WIDTH = width;
    }

    public WidthPacket(String str){
        super(PacketIds.WIDTH);
        String[] width = decode(str);
        this.WIDTH = Integer.parseInt(width[1]);
    }

    @Override
    public String encode(){
        return Integer.toString(PacketIds.WIDTH)  + PacketIds.SEPARATOR + Integer.toString(WIDTH);
    }
}
