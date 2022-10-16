import sum.komponenten.*;

// A mostly auto-generated class with UI definitions, nothing to see here

public class UI {

  public static Knopf b_del_all;
  public static Knopf b_save;
  public static Knopf b_load;

  public static Etikett e_paint_mode;
  public static Radioknopf b_mode_paint;
  public static Radioknopf b_mode_fill;
  public static Radioknopf b_mode_line;
  public static Radioknopf b_mode_rectangle;
  public static Schalter s_filling;

  public static Etikett e_line_width;
  public static Regler r_line_width;

  public static Etikett e_color;
  public static Radiogruppe a_colors;
  public static Radiogruppe a_paint_modes;
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
  public static Knopf b_connection;

  public static Etikett e_server;
  public static Textfeld t_server_port;
  public static Knopf b_server;

  public static void init() {

    b_save = new Knopf(20, 15, 130, 20, getMenuText("save"));
    b_save.setzeBearbeiterGeklickt("b_save");
    b_load = new Knopf(20, 40, 130, 20, getMenuText("load"));
    b_load.setzeBearbeiterGeklickt("b_load");
    b_del_all = new Knopf(20, 80, 130, 30, getMenuText("del_all"));
    b_del_all.setzeBearbeiterGeklickt("b_del_all");

    e_paint_mode = new Etikett(20, 130, 130, 30, getMenuText("pen_mode"));
    e_paint_mode.setzeAusrichtung(1);

    b_mode_paint =
      new ModeOption(20, 160, 130, 30, getMenuText("draw"), Consts.MODE_NORMAL);
    b_mode_fill = new ModeOption(
      20, 190, 130, 30, getMenuText("fill"), Consts.MODE_BUCKETFILL);
    b_mode_line =
      new ModeOption(20, 220, 130, 30, getMenuText("line"), Consts.MODE_LINE);
    b_mode_rectangle = new ModeOption(
      20, 250, 60, 30, getMenuText("rectangle"), Consts.MODE_RECTANGLE);
    s_filling = new Schalter(85, 255, 65, 25, getMenuText("filling"));
    s_filling.setzeBearbeiterGeklickt("s_filling");
    s_filling.schalteAn();
    b_mode_paint.waehle();

    a_paint_modes = new Radiogruppe();
    a_paint_modes.fuegeEin(b_mode_paint);
    a_paint_modes.fuegeEin(b_mode_line);
    a_paint_modes.fuegeEin(b_mode_rectangle);
    a_paint_modes.fuegeEin(b_mode_fill);

    e_line_width = new Etikett(20, 300, 130, 30, getMenuText("pen_width"));
    e_line_width.setzeAusrichtung(1);
    r_line_width =
      new Regler(20, 330, 130, 30, Consts.DEFAULT_WIDTH / 2, 1, 50);
    r_line_width.setzeBearbeiterGeaendert("r_line_width");

    e_color = new Etikett(20, 370, 130, 30, getMenuText("pen_color"));
    e_color.setzeAusrichtung(1);
    a_colors = new Radiogruppe();
    a_black =
      new ColorOption(20, 400, 130, 20, getMenuText("black"), Colors.BLACK);
    a_colors.fuegeEin(a_black);
    a_black.waehle();
    a_red = new ColorOption(20, 420, 130, 20, getMenuText("red"), Colors.RED);
    a_colors.fuegeEin(a_red);
    a_lightBlue = new ColorOption(
      20, 440, 130, 20, getMenuText("light_blue"), Colors.LIGHT_BLUE);
    a_colors.fuegeEin(a_lightBlue);
    a_darkBlue = new ColorOption(
      20, 460, 130, 20, getMenuText("dark_blue"), Colors.DARK_BLUE);
    a_colors.fuegeEin(a_darkBlue);
    a_lightGreen = new ColorOption(
      20, 480, 130, 20, getMenuText("light_green"), Colors.LIGHT_GREEN);
    a_colors.fuegeEin(a_lightGreen);
    a_darkGreen = new ColorOption(
      20, 500, 130, 20, getMenuText("dark_green"), Colors.DARK_GREEN);
    a_colors.fuegeEin(a_darkGreen);
    a_yellow =
      new ColorOption(20, 520, 130, 20, getMenuText("yellow"), Colors.YELLOW);
    a_colors.fuegeEin(a_yellow);
    a_orange =
      new ColorOption(20, 540, 130, 20, getMenuText("orange"), Colors.ORANGE);
    a_colors.fuegeEin(a_orange);
    a_brown =
      new ColorOption(20, 560, 130, 20, getMenuText("brown"), Colors.BROWN);
    a_colors.fuegeEin(a_brown);
    a_white =
      new ColorOption(20, 580, 130, 20, getMenuText("white"), Colors.WHITE);
    a_colors.fuegeEin(a_white);

    e_multiplayer = new Etikett(20, 625, 130, 30, getMenuText("network"));
    e_multiplayer.setzeAusrichtung(1);
    t_id =
      new Textfeld(20,
                   655,
                   130,
                   20,
                   Consts.DEFAULT_SERVER_IP + ":" + Consts.DEFAULT_SERVER_PORT);
    t_id.setzeHinweis("Ip:Port");
    b_connection = new Knopf(20, 690, 130, 20, getMenuText("connect"));
    b_connection.setzeBearbeiterGeklickt("b_connection");

    e_server = new Etikett(20, 710, 130, 30, getMenuText("server"));
    e_server.setzeAusrichtung(1);
    t_server_port = new Textfeld(
      20, 740, 130, 20, Integer.toString(Consts.DEFAULT_SERVER_PORT));
    t_server_port.setzeHinweis("Port");
    b_server = new Knopf(20, 775, 130, 20, getMenuText("start"));
    b_server.setzeBearbeiterGeklickt("b_server");
  }

  public static String getMenuText(String str) {
    return Main.resourceBundle.getString(str);
  }
}
