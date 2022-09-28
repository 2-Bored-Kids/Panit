import sum.ereignis.*;
import sum.komponenten.*;

public class Main extends EBAnwendung {
    Buntstift pen;

    boolean isDrawing = false;

    public final static int MENU_X = 190, MENU_Y = 1080;

    public Main() {
        super(1920, 1080);

        Utils.init();

        pen = new Buntstift();
        pen.setzeFuellmuster(1);

        this.hatBildschirm.setTitle("Panit");

        clearScreen();

        loadUI();

        pen.setzeLinienBreite(r_linewidth.wert());

        fuehreAus();
    }

    public static void main(String[] args) {
        Main main = new Main();
    }

    public void clearScreen() {
        this.hatBildschirm.loescheAlles();
        drawMenu();
    }

    public void drawMenu() {
        Buntstift menuPen = new Buntstift();

        //Dunkles Grau
        Utils.setColor(menuPen, 150, 150, 150);

        menuPen.setzeFuellmuster(1);

        menuPen.bewegeBis(0, 0);

        menuPen.zeichneRechteck(MENU_X, MENU_Y);
    }

    @Override
    public void bearbeiteDoppelKlick(int x, int y) {
        pen.hoch();
        isDrawing = false;

        clearScreen();
    }

    @Override
    public void bearbeiteMausBewegt(int x, int y) {
        boolean touchesMenuArea = x < MENU_X + pen.linienBreite() / 2;

        pen.bewegeBis(x, y);
        if (isDrawing && !touchesMenuArea) {
            pen.runter();
            pen.zeichneKreis(pen.linienBreite() / 2);
        } else {
            pen.hoch();
        }

    }

    @Override
    public void bearbeiteMausLos(int x, int y) {
        isDrawing = false;
    }

    @Override
    public void bearbeiteMausDruck(int x, int y) {
        boolean touchesMenuArea = x < MENU_X + pen.linienBreite() / 2  && y < MENU_Y + pen.linienBreite() / 2;
        if (!touchesMenuArea) {
            isDrawing = true;
        }
    }

    //Generiert durch BlueG
    Etikett e_linewidth;
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
    Radioknopf a_brown;
    Regler r_linewidth;

    //Generiert durch BlueG
    void loadUI() {
        e_linewidth = new Etikett(20, 250, 150, 30, "Linienbreite");
        e_linewidth.setzeAusrichtung(1);
        b_mode_paint = new Knopf(20, 180, 150, 60, "Malen");
        b_mode_paint.setzeBearbeiterGeklickt("b_mode_paintGeklickt");
        b_mode_del = new Knopf(20, 100, 150, 60, "Loeschen");
        b_mode_del.setzeBearbeiterGeklickt("b_mode_delGeklickt");
        b_delAll = new Knopf(20, 20, 150, 60, "Alles loeschen");
        b_delAll.setzeBearbeiterGeklickt("b_delAllGeklickt");
        a_colors = new Radiogruppe();
        a_black = new Radioknopf(20, 330, 150, 20, "Schwarz");
        a_black.setzeBearbeiterGeklickt("a_blackGeklickt");
        a_colors.fuegeEin(a_black);
        a_black.waehle();
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
        a_brown = new Radioknopf(20, 490, 150, 20, "Braun");
        a_brown.setzeBearbeiterGeklickt("a_brownGeklickt");
        a_colors.fuegeEin(a_brown);
        r_linewidth = new Regler(20, 290, 150, 30, 10, 1, 50);
        r_linewidth.setzeBearbeiterGeaendert("r_linewidthGeaendert");
    }

    //UI Funktionen
    public void b_mode_paintGeklickt() {
        pen.normal();
    }

    public void b_mode_delGeklickt() {
        pen.radiere();
    }

    public void b_delAllGeklickt() {
        clearScreen();
    }

    public void a_blackGeklickt() {
        Utils.setColor(pen, 0, 0, 0);
    }

    public void a_redGeklickt() {
        Utils.setColor(pen, 180, 20, 20);
    }

    public void a_lightBlueGeklickt() {
        Utils.setColor(pen, 0, 190, 255);
    }

    public void a_darkBlueGeklickt() {
        Utils.setColor(pen, 0, 72, 192);
    }

    public void a_lightGreenGeklickt() {
        Utils.setColor(pen, 0, 230, 0);
    }

    public void a_darkGreenGeklickt() {
        Utils.setColor(pen, 16, 180, 34);
    }

    public void a_yellowGeklickt() {
        Utils.setColor(pen, 255, 236, 20);
    }

    public void a_orangeGeklickt() {
        Utils.setColor(pen, 255, 182, 20);
    }
    
    public void a_brownGeklickt(){
        Utils.setColor(pen, 100, 44, 44);
    }

    public void r_linewidthGeaendert() {
        pen.setzeLinienBreite(r_linewidth.wert() * 2);
    }
}

