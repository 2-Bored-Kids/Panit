import sum.ereignis.*;
import sum.komponenten.*;
import sum.multimedia.*;
import sum.werkzeuge.*;
import sum.kern.Maus;

public class Main extends EBAnwendung
{
    // Objekte
    Buntstift pen;
    Maus cursor;

    Etikett Etikett1;
    Knopf b_mode_paint;
    Knopf b_mode_del;
    Knopf b_delAll;
    Radiogruppe a_colors;
    Radioknopf a_black;
    Radioknopf a_red;
    Radioknopf a_lightBlue;
    Radioknopf a_darkBlue;
    Radioknopf a_lightGreen;
    Radioknopf a_darkGreen;
    Radioknopf a_yellow;
    Radioknopf a_orange;
    Regler r_selection;

    public Main()
    {
        // Initialisierung der Oberklasse
        super(1920, 1080);

        Bildschirm.topFenster = this.hatBildschirm;

        pen = new Buntstift();
        cursor = new Maus();

        Etikett1 = new Etikett(20, 250, 200, 30, "Auswahl");
        Etikett1.setzeAusrichtung(1);
        b_mode_paint = new Knopf(20, 180, 200, 60, "Malen");
        b_mode_paint.setzeBearbeiterGeklickt("b_mode_paintGeklickt");
        b_mode_del = new Knopf(20, 100, 200, 60, "Loeschen");
        b_mode_del.setzeBearbeiterGeklickt("b_mode_delGeklickt");
        b_delAll = new Knopf(20, 20, 200, 60, "Alles loeschen");
        b_delAll.setzeBearbeiterGeklickt("b_delAllGeklickt");
        a_colors = new Radiogruppe();
        a_black = new Radioknopf(20, 330, 150, 20, "Schwarz");
        a_black.setzeBearbeiterGeklickt("a_blackGeklickt");
        a_colors.fuegeEin(a_black);
        a_red = new Radioknopf(20, 350, 150, 20, "Rot");
        a_red.setzeBearbeiterGeklickt("a_redGeklickt");
        a_colors.fuegeEin(a_red);
        a_lightBlue = new Radioknopf(20, 370, 150, 20, "Hellblau");
        a_lightBlue.setzeBearbeiterGeklickt("a_lightBlueGeklickt");
        a_colors.fuegeEin(a_lightBlue);
        a_darkBlue = new Radioknopf(20, 390, 150, 20, "Dunkelblau");
        a_darkBlue.setzeBearbeiterGeklickt("a_darkBlueGeklickt");
        a_colors.fuegeEin(a_darkBlue);
        a_lightGreen = new Radioknopf(20, 410, 150, 20, "Hellgruen");
        a_lightGreen.setzeBearbeiterGeklickt("a_lightGreenGeklickt");
        a_colors.fuegeEin(a_lightGreen);
        a_darkGreen = new Radioknopf(20, 430, 150, 20, "Dunkelgruen");
        a_darkGreen.setzeBearbeiterGeklickt("a_darkGreenGeklickt");
        a_colors.fuegeEin(a_darkGreen);
        a_yellow = new Radioknopf(20, 450, 150, 20, "Gelb");
        a_yellow.setzeBearbeiterGeklickt("a_yellowGeklickt");
        a_colors.fuegeEin(a_yellow);
        a_orange = new Radioknopf(20, 470, 150, 20, "Orange");
        a_orange.setzeBearbeiterGeklickt("a_orangeGeklickt");
        a_colors.fuegeEin(a_orange);
        r_selection = new Regler(20, 290, 200, 30, 5, 1, 30);
        r_selection.setzeBearbeiterGeaendert("r_selectionGeaendert");
        
        fuehreAus();
    }

    public void b_mode_paintGeklickt()
    {
        //   Hier wird der Methodeninhalt eingefgt
    }

    public void b_mode_delGeklickt()
    {
        //   Hier wird der Methodeninhalt eingefgt
    }

    public void b_delAllGeklickt()
    {
        //   Hier wird der Methodeninhalt eingefgt
    }

    public void a_blackGeklickt()
    {
        //   Hier wird der Methodeninhalt eingefgt
    }

    public void a_redGeklickt()
    {
        
    }

    public void a_lightBlueGeklickt()
    {
        //   Hier wird der Methodeninhalt eingefgt
    }

    public void a_darkBlueGeklickt()
    {
        //   Hier wird der Methodeninhalt eingefgt
    }

    public void a_lightGreenGeklickt()
    {
        //   Hier wird der Methodeninhalt eingefgt
    }

    public void a_darkGreenGeklickt()
    {
        //   Hier wird der Methodeninhalt eingefgt
    }

    public void a_yellowGeklickt()
    {
        //   Hier wird der Methodeninhalt eingefgt
    }

    public void a_orangeGeklickt()
    {
        //   Hier wird der Methodeninhalt eingefgt
    }

    public void r_selectionGeaendert()
    {
        //   Hier wird der Methodeninhalt eingefgt
    }
    
    
    @Override
    public void bearbeiteMausBewegt(int x, int y){
        pen.bewegeBis(x, y);
    }

    @Override
    public void bearbeiteMausLos(int x, int y){
        pen.hoch();
    }

    @Override
    public void bearbeiteMausDruck(int x, int y){
        pen.runter();
    }
}