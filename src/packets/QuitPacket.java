package packets;

public class QuitPacket extends Packet{
    public QuitPacket() {
        super(PacketIds.QUIT);
    }
}
