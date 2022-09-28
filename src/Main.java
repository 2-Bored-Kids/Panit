import sum.ereignis.*;
import sum.komponenten.*;

import java.io.File;

public class Main extends EBAnwendung {
    Buntstift pen;

    byte paintMode = Consts.MODE_NORMAL;

    int startPressX, startPressY;

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
        Utils.setColor(menuPen, 100, 100, 100);

        menuPen.setzeFuellmuster(1);

        menuPen.bewegeBis(0, 0);

        menuPen.zeichneRechteck(Consts.MENU_X,Consts.MENU_Y);
    }

    @Override
    public void bearbeiteDoppelKlick(int x, int y) {
        pen.hoch();

        clearScreen();
    }

    @Override
    public void bearbeiteMausBewegt(int x, int y) {
        boolean touchesMenuArea = x < Consts.MENU_X + pen.linienBreite() / 2;

        pen.bewegeBis(x, y);

        switch(paintMode){
            case Consts.MODE_NORMAL:
                pen.normal();
                break;
            case Consts.MODE_ERASE:
                pen.radiere();
                break;
        }

        if (paintMode == Consts.MODE_NORMAL || paintMode == Consts.MODE_ERASE) {
            if (pen.istUnten() && !touchesMenuArea) {
                pen.runter();
                pen.zeichneKreis(pen.linienBreite() / 2);
            } else {
                pen.hoch();
            }
        }

    }

    @Override
    public void bearbeiteMausLos(int x, int y) {
        pen.hoch();

        if (paintMode == Consts.MODE_LINE || paintMode == Consts.MODE_RECTANGLE) {
            boolean touchesMenuArea = x < Consts.MENU_X + pen.linienBreite() / 2  && y < Consts.MENU_Y + pen.linienBreite() / 2;

            if (!touchesMenuArea) {
                switch (paintMode) {
                    case Consts.MODE_LINE:
                        drawLinie(startPressX, startPressY, x, y);
                    break;

                    case Consts.MODE_RECTANGLE:
                        drawViereck(startPressX, startPressY, x, y);
                    break;
                }
            }
        }
    }

    @Override
    public void bearbeiteMausDruck(int x, int y) {
        boolean touchesMenuArea = x < Consts.MENU_X + pen.linienBreite() / 2  && y < Consts.MENU_Y + pen.linienBreite() / 2;

        if (!touchesMenuArea) {
            pen.bewegeBis(x, y);

            if (paintMode == Consts.MODE_NORMAL || paintMode == Consts.MODE_ERASE) {
                pen.runter();
            }
            startPressX = x;
            startPressY = y;
        }
    }

    public void drawLinie(int sX, int sY, int eX, int eY){
        pen.normal();
        pen.hoch();
        pen.bewegeBis(sX, sY);
        pen.runter();
        pen.zeichneKreis(pen.linienBreite() / 2);
        pen.bewegeBis(eX, eY);
        pen.zeichneKreis(pen.linienBreite() / 2);
        pen.hoch();
    }

    public void drawViereck(int sX, int sY, int eX, int eY){
        pen.normal();
        pen.hoch();

        int minX = Math.min(sX, eX);
        int maxX = Math.max(sX, eX);

        int minY = Math.min(sY, eY);
        int maxY = Math.max(sY, eY);

        pen.bewegeBis(minX, minY);
        pen.zeichneRechteck(maxX - minX, maxY - minY);
        pen.bewegeBis(maxX, maxY);
    }

    //Generiert durch BlueG
    Etikett e_lineWidth;
    Etikett e_paintMode;
    Radioknopf b_mode_del;
    Radioknopf b_mode_paint;
    Radioknopf b_mode_line;
    Radioknopf b_mode_rectangle;
    Knopf b_delAll;
    Knopf b_save;
    Knopf b_load;
    Radiogruppe a_colors;
    Radiogruppe a_paintModes;
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

        e_lineWidth = new Etikett(20, 255, 130, 30, "Linienbreite");
        e_lineWidth.setzeAusrichtung(1);

        e_paintMode = new Etikett(20, 125, 130, 30, "Modus");
        e_paintMode.setzeAusrichtung(1);

        b_mode_del = new Radioknopf(20, 190, 130, 30, "L\u00f6schen");
        b_mode_del.setzeBearbeiterGeklickt("b_mode_delGeklickt");
        b_mode_paint = new Radioknopf(20, 160, 130, 30, "Malen");
        b_mode_paint.setzeBearbeiterGeklickt("b_mode_paintGeklickt");
        b_mode_line = new Radioknopf(20, 220, 130, 30, "Linie");
        b_mode_line.setzeBearbeiterGeklickt("b_mode_lineGeklickt");
        b_mode_rectangle = new Radioknopf(20, 250, 130, 30, "Viereck");
        b_mode_rectangle.setzeBearbeiterGeklickt("b_mode_rectangleGeklickt");
        b_mode_paint.waehle();

        a_paintModes = new Radiogruppe();
        a_paintModes.fuegeEin(b_mode_paint);
        a_paintModes.fuegeEin(b_mode_del);
        a_paintModes.fuegeEin(b_mode_line);
        a_paintModes.fuegeEin(b_mode_rectangle);

        b_save = new Knopf(20, 15, 130, 20, "Speichern");
        b_save.setzeBearbeiterGeklickt("b_saveGeklickt");
        b_load = new Knopf(20, 40, 130, 20, "\u00d6ffnen");
        b_load.setzeBearbeiterGeklickt("b_loadGeklickt");
        b_delAll = new Knopf(20, 80, 130, 30, "Alles l\u00f6schen");
        b_delAll.setzeBearbeiterGeklickt("b_delAllGeklickt");

        a_colors = new Radiogruppe();
        a_black = new Radioknopf(20, 330, 130, 20, "Schwarz");
        a_black.setzeBearbeiterGeklickt("a_blackGeklickt");
        a_colors.fuegeEin(a_black);
        a_black.waehle();
        a_red = new Radioknopf(20, 350, 130, 20, "Rot");
        a_red.setzeBearbeiterGeklickt("a_redGeklickt");
        a_colors.fuegeEin(a_red);
        a_lightBlue = new Radioknopf(20, 370, 130, 20, "Hellblau");
        a_lightBlue.setzeBearbeiterGeklickt("a_lightBlueGeklickt");
        a_colors.fuegeEin(a_lightBlue);
        a_darkBlue = new Radioknopf(20, 390, 130, 20, "Dunkelblau");
        a_darkBlue.setzeBearbeiterGeklickt("a_darkBlueGeklickt");
        a_colors.fuegeEin(a_darkBlue);
        a_lightGreen = new Radioknopf(20, 410, 130, 20, "Hellgr\u00fcn");
        a_lightGreen.setzeBearbeiterGeklickt("a_lightGreenGeklickt");
        a_colors.fuegeEin(a_lightGreen);
        a_darkGreen = new Radioknopf(20, 430, 130, 20, "Dunkelgr\u00fcn");
        a_darkGreen.setzeBearbeiterGeklickt("a_darkGreenGeklickt");
        a_colors.fuegeEin(a_darkGreen);
        a_yellow = new Radioknopf(20, 450, 130, 20, "Gelb");
        a_yellow.setzeBearbeiterGeklickt("a_yellowGeklickt");
        a_colors.fuegeEin(a_yellow);
        a_orange = new Radioknopf(20, 470, 130, 20, "Orange");
        a_orange.setzeBearbeiterGeklickt("a_orangeGeklickt");
        a_colors.fuegeEin(a_orange);
        a_brown = new Radioknopf(20, 490, 130, 20, "Braun");
        a_brown.setzeBearbeiterGeklickt("a_brownGeklickt");
        a_colors.fuegeEin(a_brown);
        r_linewidth = new Regler(20, 290, 130, 30, 10, 1, 50);
        r_linewidth.setzeBearbeiterGeaendert("r_linewidthGeaendert");

    }

    //UI Funktionen
    public void b_mode_paintGeklickt() {
        paintMode = Consts.MODE_NORMAL;
    }

    public void b_mode_delGeklickt() {
        paintMode = Consts.MODE_ERASE;
    }

    public void b_mode_lineGeklickt(){
        paintMode = Consts.MODE_LINE;
    }

    public void b_mode_rectangleGeklickt(){
        paintMode = Consts.MODE_RECTANGLE;
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

    public void b_saveGeklickt() {
        String filePath = Utils.pickSaveImage();

        //TODO: get rid of this hack
        try {
            Thread.sleep(500);
        } catch (Exception e) {}

        if (filePath != "") {
            Utils.saveImage(this.hatBildschirm, filePath);
        }
    }

    public void b_loadGeklickt() {
        File file = Utils.pickImage();

        if (file != null) {
            clearScreen();
            Utils.loadImage(this.hatBildschirm, file.getAbsolutePath());
        }
    }
}

