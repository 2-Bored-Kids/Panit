import sum.komponenten.*;

import javax.swing.*;

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
  public static Radioknopf a_darkGreen;
  public static Radioknopf a_orange;
  public static Radioknopf a_white;

  public static Knopf b_colorPicker;
  public static Knopf b_pickColor;
  public static JPanel p_colorPreviewPanel;

  public static Etikett e_multiplayer;
  public static Textfeld t_id;
  public static Knopf b_connection;

  public static Etikett e_server;
  public static Textfeld t_server_port;
  public static Knopf b_server;

  public static void init() {

    b_save = new Knopf(20, 15, 130, 20, Main.getTranslated("save"));
    b_save.setzeBearbeiterGeklickt("b_save");
    b_load = new Knopf(20, 40, 130, 20, Main.getTranslated("load"));
    b_load.setzeBearbeiterGeklickt("b_load");
    b_del_all = new Knopf(20, 80, 130, 30, Main.getTranslated("del_all"));
    b_del_all.setzeBearbeiterGeklickt("b_del_all");

    e_paint_mode =
      new Etikett(20, 130, 130, 30, Main.getTranslated("pen_mode"));
    e_paint_mode.setzeAusrichtung(1);

    b_mode_paint = new ModeOption(
      20, 160, 130, 30, Main.getTranslated("draw"), Consts.MODE_NORMAL);
    b_mode_fill = new ModeOption(
      20, 190, 130, 30, Main.getTranslated("fill"), Consts.MODE_BUCKETFILL);
    b_mode_line = new ModeOption(
      20, 220, 130, 30, Main.getTranslated("line"), Consts.MODE_LINE);
    b_mode_rectangle = new ModeOption(
      20, 250, 60, 30, Main.getTranslated("rectangle"), Consts.MODE_RECTANGLE);
    s_filling = new Schalter(85, 255, 65, 25, Main.getTranslated("filling"));
    s_filling.setzeBearbeiterGeklickt("s_filling");
    s_filling.schalteAn();
    b_mode_paint.waehle();

    a_paint_modes = new Radiogruppe();
    a_paint_modes.fuegeEin(b_mode_paint);
    a_paint_modes.fuegeEin(b_mode_line);
    a_paint_modes.fuegeEin(b_mode_rectangle);
    a_paint_modes.fuegeEin(b_mode_fill);

    e_line_width =
      new Etikett(20, 300, 130, 30, Main.getTranslated("pen_width"));
    e_line_width.setzeAusrichtung(1);
    r_line_width =
      new Regler(20, 330, 130, 30, Consts.DEFAULT_WIDTH / 2, 1, 50);
    r_line_width.setzeBearbeiterGeaendert("r_line_width");

    e_color = new Etikett(20, 370, 130, 30, Main.getTranslated("pen_color"));
    e_color.setzeAusrichtung(1);
    a_colors = new Radiogruppe();
    a_black = new ColorOption(
      20, 400, 130, 20, Main.getTranslated("black"), Colors.BLACK);
    a_colors.fuegeEin(a_black);
    a_black.waehle();
    a_red =
      new ColorOption(20, 420, 130, 20, Main.getTranslated("red"), Colors.RED);
    a_colors.fuegeEin(a_red);
    a_lightBlue = new ColorOption(
      20, 440, 130, 20, Main.getTranslated("light_blue"), Colors.LIGHT_BLUE);
    a_colors.fuegeEin(a_lightBlue);
    a_darkGreen = new ColorOption(
      20, 460, 130, 20, Main.getTranslated("dark_green"), Colors.DARK_GREEN);
    a_colors.fuegeEin(a_darkGreen);
    a_orange = new ColorOption(
      20, 480, 130, 20, Main.getTranslated("orange"), Colors.ORANGE);
    a_colors.fuegeEin(a_orange);
    a_white = new ColorOption(
      20, 500, 130, 20, Main.getTranslated("white"), Colors.WHITE);
    a_colors.fuegeEin(a_white);

    b_colorPicker = new Knopf(
      20, 520, 130, 20, Main.getTranslated("color_button"), "b_colorPicker");
    b_pickColor = new Knopf(
            20, 540, 130, 20, Main.getTranslated("color_picker_button"), "b_pickColor");
    p_colorPreviewPanel = new JPanel(null);
    p_colorPreviewPanel.setBounds(20, 570, 130, 20);
    p_colorPreviewPanel.setBackground(Consts.DEFAULT_COLOR);
    Main.instance.hatBildschirm.add(p_colorPreviewPanel);

    e_multiplayer =
      new Etikett(20, 625, 130, 30, Main.getTranslated("network"));
    e_multiplayer.setzeAusrichtung(1);
    t_id =
      new Textfeld(20,
                   655,
                   130,
                   20,
                   Consts.DEFAULT_SERVER_IP + ":" + Consts.DEFAULT_SERVER_PORT);
    t_id.setzeHinweis("Ip:Port");
    b_connection = new Knopf(20, 690, 130, 20, Main.getTranslated("connect"));
    b_connection.setzeBearbeiterGeklickt("b_connection");

    e_server = new Etikett(20, 710, 130, 30, Main.getTranslated("server"));
    e_server.setzeAusrichtung(1);
    t_server_port = new Textfeld(
      20, 740, 130, 20, Integer.toString(Consts.DEFAULT_SERVER_PORT));
    t_server_port.setzeHinweis("Port");
    b_server = new Knopf(20, 775, 130, 20, Main.getTranslated("start"));
    b_server.setzeBearbeiterGeklickt("b_server");
  }
}
