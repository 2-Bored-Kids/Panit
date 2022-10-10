package packets;

public class MovePacket extends Packet {

    int X, Y;


    public MovePacket(int x, int y) {
        super(PacketIds.MOVE);
        this.X = x;
        this.Y = y;
    }

    public MovePacket(String str){
        super(PacketIds.MOVE);
        String[] move = decode(str);
        this.X = Integer.parseInt(move[1]);
        this.Y = Integer.parseInt(move[2]);
    }

    @Override
    public String encode(){
        return Integer.toString(PacketIds.MOVE) + PacketIds.SEPARATOR + Integer.toString(X) + PacketIds.SEPARATOR + Integer.toString(Y);
    }
}
