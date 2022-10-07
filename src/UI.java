import sum.komponenten.*;

// Generiert durch BlueG

public class UI {
  public static Etikett e_lineWidth;
  public static Etikett e_paintMode;
  public static Radioknopf b_mode_paint;
  public static Radioknopf b_fill;
  public static Radioknopf b_mode_line;
  public static Radioknopf b_mode_rectangle;
  public static Knopf b_delAll;
  public static Knopf b_save;
  public static Knopf b_load;
  public static Radiogruppe a_colors;
  public static Radiogruppe a_paintModes;
  public static Radioknopf a_black;
  public static Radioknopf a_red;
  public static Radioknopf a_lightBlue;
  public static Radioknopf a_darkBlue;
  public static Radioknopf a_lightGreen;
  public static Radioknopf a_darkGreen;
  public static Radioknopf a_yellow;
  public static Radioknopf a_orange;
  public static Radioknopf a_brown;
  public static Radioknopf a_white;
  public static Regler r_linewidth;
  public static Schalter s_fillMode;

  // Generiert durch BlueG
  public static void init() {
    e_lineWidth = new Etikett(20, 305, 130, 30, "Pinselbreite");
    e_lineWidth.setzeAusrichtung(1);

    e_paintMode = new Etikett(20, 125, 130, 30, "Pinselmodus");
    e_paintMode.setzeAusrichtung(1);
    b_fill = new Radioknopf(20, 190, 130, 30, "F\u00fcllen");
    b_fill.setzeBearbeiterGeklickt("b_fillGeklickt");
    b_mode_paint = new Radioknopf(20, 160, 130, 30, "Malen");
    b_mode_paint.setzeBearbeiterGeklickt("b_mode_paintGeklickt");
    b_mode_line = new Radioknopf(20, 220, 130, 30, "Linie");
    b_mode_line.setzeBearbeiterGeklickt("b_mode_lineGeklickt");
    b_mode_rectangle = new Radioknopf(20, 250, 60, 30, "Viereck");
    b_mode_rectangle.setzeBearbeiterGeklickt("b_mode_rectangleGeklickt");
    s_fillMode = new Schalter(85, 255, 55, 25, "F\u00fcllung");
    s_fillMode.setzeBearbeiterGeklickt("s_fillModeGeklickt");

    b_mode_paint.waehle();

    a_paintModes = new Radiogruppe();
    a_paintModes.fuegeEin(b_mode_paint);
    a_paintModes.fuegeEin(b_mode_line);
    a_paintModes.fuegeEin(b_mode_rectangle);
    a_paintModes.fuegeEin(b_fill);

    b_save = new Knopf(20, 15, 130, 20, "Speichern");
    b_save.setzeBearbeiterGeklickt("b_saveGeklickt");
    b_load = new Knopf(20, 40, 130, 20, "\u00d6ffnen");
    b_load.setzeBearbeiterGeklickt("b_loadGeklickt");
    b_delAll = new Knopf(20, 80, 130, 30, "Alles l\u00f6schen");
    b_delAll.setzeBearbeiterGeklickt("b_delAllGeklickt");

    a_colors = new Radiogruppe();
    a_black = new Radioknopf(20, 380, 130, 20, "Schwarz");
    a_black.setzeBearbeiterGeklickt("a_blackGeklickt");
    a_colors.fuegeEin(a_black);
    a_black.waehle();
    a_red = new Radioknopf(20, 400, 130, 20, "Rot");
    a_red.setzeBearbeiterGeklickt("a_redGeklickt");
    a_colors.fuegeEin(a_red);
    a_lightBlue = new Radioknopf(20, 420, 130, 20, "Hellblau");
    a_lightBlue.setzeBearbeiterGeklickt("a_lightBlueGeklickt");
    a_colors.fuegeEin(a_lightBlue);
    a_darkBlue = new Radioknopf(20, 440, 130, 20, "Dunkelblau");
    a_darkBlue.setzeBearbeiterGeklickt("a_darkBlueGeklickt");
    a_colors.fuegeEin(a_darkBlue);
    a_lightGreen = new Radioknopf(20, 460, 130, 20, "Hellgr\u00fcn");
    a_lightGreen.setzeBearbeiterGeklickt("a_lightGreenGeklickt");
    a_colors.fuegeEin(a_lightGreen);
    a_darkGreen = new Radioknopf(20, 480, 130, 20, "Dunkelgr\u00fcn");
    a_darkGreen.setzeBearbeiterGeklickt("a_darkGreenGeklickt");
    a_colors.fuegeEin(a_darkGreen);
    a_yellow = new Radioknopf(20, 500, 130, 20, "Gelb");
    a_yellow.setzeBearbeiterGeklickt("a_yellowGeklickt");
    a_colors.fuegeEin(a_yellow);
    a_orange = new Radioknopf(20, 520, 130, 20, "Orange");
    a_orange.setzeBearbeiterGeklickt("a_orangeGeklickt");
    a_colors.fuegeEin(a_orange);
    a_brown = new Radioknopf(20, 540, 130, 20, "Braun");
    a_brown.setzeBearbeiterGeklickt("a_brownGeklickt");
    a_colors.fuegeEin(a_brown);
    a_white = new Radioknopf(20, 560, 130, 20, "L\u00f6shen");
    a_white.setzeBearbeiterGeklickt("a_whiteGeklickt");
    a_colors.fuegeEin(a_white);
    r_linewidth = new Regler(20, 340, 130, 30, Consts.DEFAULT_WIDTH, 1, 100);
    r_linewidth.setzeBearbeiterGeaendert("r_linewidthGeaendert");
  }
}

