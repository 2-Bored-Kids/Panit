package packets;

public class Packet {

    int ID;

    public Packet(int id) {

        ID = id;

    }

    public final int getID(){
        return ID;
    }


    public String encode(){
        return ID + "";
    }

    public String[] decode(String packet){
        return packet.split(PacketIds.SEPARATOR);
    }

}
