import sum.ereignis.*;
import sum.komponenten.*;
import sum.multimedia.*;
//import sum.werkzeuge.*;

public class SuMAnwendung extends EBAnwendung
{
    // Objekte
    Etikett e0;
    Knopf b0;
    Knopf b1;

    public SuMAnwendung()
    {
        // Initialisierung der Oberklasse
        super(640, 480);
        e0 = new Etikett(140, 10, 360, 30, "Panit");
        e0.setzeAusrichtung(1);
        e0.setzeSchriftgroesse(10);
        b0 = new Knopf(10, 10, 110, 30, "Malen");
        b0.setzeBearbeiterGeklickt("b0Geklickt");
        b1 = new Knopf(520, 10, 110, 30, "Löschen");
        b1.setzeBearbeiterGeklickt("b1Geklickt");
    }

    public void b0Geklickt()
    {
        //   Hier wird der Methodeninhalt eingefügt
    }

    public void b1Geklickt()
    {
        //   Hier wird der Methodeninhalt eingefügt
    }

}