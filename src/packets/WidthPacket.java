package packets;

public class WidthPacket extends Packet {

    int WIDTH;

    public WidthPacket(int width) {
        super(PacketIds.WIDTH);
        this.WIDTH = width;
    }

    public WidthPacket(String[] str){
        super(PacketIds.WIDTH);
        this.WIDTH = Integer.parseInt(str[1]);
    }

    public WidthPacket(String str){
        this(decode(str));
    }

    @Override
    public String encode(){
        return Integer.toString(PacketIds.WIDTH)  + PacketIds.SEPARATOR + Integer.toString(WIDTH);
    }
}
