package packets;

public class ModePacket extends Packet {

    public byte MODE;

    public ModePacket(byte mode) {
        super(PacketIds.MODE);
        this.MODE = mode;
    }

    public ModePacket(String[] str){
        super(PacketIds.MODE);
        this.MODE = Byte.parseByte(str[1]);
    }

    public ModePacket(String str){
        this(decode(str));
    }



    public String encode(){
        return Integer.toString(PacketIds.MODE) + PacketIds.SEPARATOR + Byte.toString(MODE);
    }
}
