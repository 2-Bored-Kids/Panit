import sum.ereignis.*;
import sum.komponenten.*;

public class Main extends EBAnwendung {
    Buntstift pen;

    boolean isDrawing = false;

    final int MENU_X = 300, MENU_Y = 575;

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

        //Dark grey
        Utils.setColor(menuPen, 150, 150, 150);

        menuPen.setzeFuellmuster(1);

        menuPen.bewegeBis(0, 0);

        menuPen.zeichneRechteck(300, 575);
    }

    @Override
    public void bearbeiteDoppelKlick(int x, int y) {
        pen.hoch();
        isDrawing = false;
        
        clearScreen();
    }

    @Override
    public void bearbeiteMausBewegt(int x, int y) {
        boolean touchesMenuArea = x < MENU_X + pen.linienBreite() / 2 && y < MENU_Y + pen.linienBreite() / 2;

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

    //Autogenerated using BlueG
    void loadUI() {
        e_linewidth = new Etikett(20, 250, 200, 30, "Linienbreite");
        e_linewidth.setzeAusrichtung(1);
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
        r_linewidth = new Regler(20, 290, 200, 30, 10, 1, 50);
        r_linewidth.setzeBearbeiterGeaendert("r_linewidthGeaendert");
    }

    //Autogenerated using BlueG
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
    Regler r_linewidth;

    //UI Functions
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
        Utils.setColor(pen, 180, 40, 40);
    }

    public void a_lightBlueGeklickt() {
        Utils.setColor(pen, 56, 220, 255);
    }

    public void a_darkBlueGeklickt() {
        Utils.setColor(pen, 56, 64, 255);
    }

    public void a_lightGreenGeklickt() {
        Utils.setColor(pen, 80, 255, 56);
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

    public void r_linewidthGeaendert() {
        pen.setzeLinienBreite(r_linewidth.wert() * 2);
    }
}

