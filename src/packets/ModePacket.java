package packets;

public class ModePacket extends Packet {

    byte MODE;

    public ModePacket(byte mode) {
        super(PacketIds.MODE);
        this.MODE = mode;
    }

    public ModePacket(String str){
        super(PacketIds.MODE);
        String[] mode = decode(str);
        this.MODE = Byte.parseByte(mode[1]);
    }

    public String encode(){
        return Integer.toString(PacketIds.MODE) + PacketIds.SEPARATOR + Byte.toString(MODE);
    }
}
