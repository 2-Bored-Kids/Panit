package packets;

public class MovePacket extends Packet {

    public int X, Y;


    public MovePacket(int x, int y) {
        super(PacketIds.MOVE);
        this.X = x;
        this.Y = y;
    }

    public MovePacket(String str){
        this(decode(str));
    }

    public MovePacket(String[] str){
        super(PacketIds.MOVE);
        this.X = Integer.parseInt(str[1]);
        this.Y = Integer.parseInt(str[2]);
    }

    @Override
    public String encode(){
        return Integer.toString(PacketIds.MOVE) + PacketIds.SEPARATOR + Integer.toString(X) + PacketIds.SEPARATOR + Integer.toString(Y);
    }
}
