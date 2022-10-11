import sum.komponenten.*;

public class UI {

  public static Knopf b_delAll;
  public static Knopf b_save;
  public static Knopf b_load;

  public static Etikett e_paintMode;
  public static Radioknopf b_mode_paint;
  public static Radioknopf b_mode_fill;
  public static Radioknopf b_mode_line;
  public static Radioknopf b_mode_rectangle;
  public static Schalter s_fillMode;


  public static Etikett e_lineWidth;
  public static Regler r_linewidth;

  public static Etikett e_color;
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

  public static Etikett e_multiplayer;
  public static Textfeld t_id;
  public static Knopf b_join;
  public static Knopf b_quit;
  public static Etikett e_status;

  public static void init() {


    b_save = new Knopf(20, 15, 130, 20, "Speichern");
    b_save.setzeBearbeiterGeklickt("b_saveGeklickt");
    b_load = new Knopf(20, 40, 130, 20, "\u00d6ffnen");
    b_load.setzeBearbeiterGeklickt("b_loadGeklickt");
    b_delAll = new Knopf(20, 80, 130, 30, "Alles l\u00f6schen");
    b_delAll.setzeBearbeiterGeklickt("b_delAllGeklickt");


    e_paintMode = new Etikett(20, 130, 130, 30, "-- Pinselmodus --");
    e_paintMode.setzeAusrichtung(1);

    b_mode_paint = new Radioknopf(20, 160, 130, 30, "Malen");
    b_mode_paint.setzeBearbeiterGeklickt("b_mode_paintGeklickt");
    b_mode_fill = new Radioknopf(20, 190, 130, 30, "F\u00fcllen");
    b_mode_fill.setzeBearbeiterGeklickt("b_fillGeklickt");
    b_mode_line = new Radioknopf(20, 220, 130, 30, "Linie");
    b_mode_line.setzeBearbeiterGeklickt("b_mode_lineGeklickt");
    b_mode_rectangle = new Radioknopf(20, 250, 60, 30, "Viereck");
    b_mode_rectangle.setzeBearbeiterGeklickt("b_mode_rectangleGeklickt");
    s_fillMode = new Schalter(85, 255, 65, 25, "F\u00fcllung");
    s_fillMode.setzeBearbeiterGeklickt("s_fillModeGeklickt");
    b_mode_paint.waehle();

    a_paintModes = new Radiogruppe();
    a_paintModes.fuegeEin(b_mode_paint);
    a_paintModes.fuegeEin(b_mode_line);
    a_paintModes.fuegeEin(b_mode_rectangle);
    a_paintModes.fuegeEin(b_mode_fill);

    e_lineWidth = new Etikett(20, 300, 130, 30, "-- Pinselbreite --");
    e_lineWidth.setzeAusrichtung(1);
    r_linewidth = new Regler(20, 330, 130, 30, Consts.DEFAULT_WIDTH / 2, 1, 50);
    r_linewidth.setzeBearbeiterGeaendert("r_linewidthGeaendert");

    e_color = new Etikett(20, 370, 130, 30, "-- Pinselfarbe --");
    e_color.setzeAusrichtung(1);
    a_colors = new Radiogruppe();
    a_black = new ColorOption(20, 400, 130, 20, "Schwarz", Colors.BLACK);
    a_colors.fuegeEin(a_black);
    a_black.waehle();
    a_red = new ColorOption(20, 420, 130, 20, "Rot", Colors.RED);
    a_colors.fuegeEin(a_red);
    a_lightBlue = new ColorOption(20, 440, 130, 20, "Hellblau", Colors.LIGHT_BLUE);
    a_colors.fuegeEin(a_lightBlue);
    a_darkBlue = new ColorOption(20, 460, 130, 20, "Dunkelblau", Colors.DARK_BLUE);
    a_colors.fuegeEin(a_darkBlue);
    a_lightGreen = new ColorOption(20, 480, 130, 20, "Hellgr\u00fcn", Colors.LIGHT_GREEN);
    a_colors.fuegeEin(a_lightGreen);
    a_darkGreen = new ColorOption(20, 500, 130, 20, "Dunkelgr\u00fcn", Colors.DARK_GREEN);
    a_colors.fuegeEin(a_darkGreen);
    a_yellow = new ColorOption(20, 520, 130, 20, "Gelb", Colors.YELLOW);
    a_colors.fuegeEin(a_yellow);
    a_orange = new ColorOption(20, 540, 130, 20, "Orange", Colors.ORANGE);
    a_colors.fuegeEin(a_orange);
    a_brown = new ColorOption(20, 560, 130, 20, "Braun", Colors.BROWN);
    a_colors.fuegeEin(a_brown);
    a_white = new ColorOption(20, 580, 130, 20, "L\u00f6shen", Colors.WHITE);
    a_colors.fuegeEin(a_white);

    e_multiplayer = new Etikett(20, 625, 130, 30, "-- Multiplayer --");
    e_multiplayer.setzeAusrichtung(1);
    t_id = new Textfeld(20, 655, 130, 20, Consts.DEFAULT_SERVER_IP + ":" + Consts.DEFAULT_SERVER_PORT);
    t_id.setzeHinweis("ip:port");
    b_join = new Knopf(20, 690, 130, 20, "Verbinden");
    b_join.setzeBearbeiterGeklickt("b_joinGeklickt");
    b_quit = new Knopf(20, 690, 130, 20, "Trennen");
    b_quit.setzeBearbeiterGeklickt("b_quitGeklickt");
    b_quit.verstecke();
    e_status = new Etikett(20, 725, 130, 20, "Getrennt");
    e_status.setzeAusrichtung(1);
  }
}
