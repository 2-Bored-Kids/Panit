import sum.ereignis.Bildschirm;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

public class connectPenTasks {

    public static void stiftBewegt(BetterStift connectPen, int x, int y) {
        byte paintMode = connectPen.getPaintMode();
        byte fillMode = connectPen.getFillMode();
        int startPressX = connectPen.getStartPressX();
        int startPressY = connectPen.getStartPressY();
            boolean touchesMenuArea = x < Consts.MENU_X + connectPen.linienBreite() / 2;
    boolean touchesBorders =
      (x >= Consts.SCREEN_X || y >= Consts.SCREEN_Y || x < 0 || y < 0);

    if (paintMode != Consts.MODE_NORMAL &&
        (touchesMenuArea || touchesBorders)) {
      bearbeiteMausLos((int)connectPen.hPosition(), (int)connectPen.vPosition());
      return;
    }

    if (paintMode == Consts.MODE_NORMAL) {
      if (connectPen.istUnten()) {
        if (!touchesMenuArea) {
          connectPen.normal();

          connectPen.setzeFuellmuster(Consts.FILL);
          connectPen.bewegeBis(x, y);
          connectPen.zeichneKreis(connectPen.linienBreite() / 2);
          connectPen.setzeFuellmuster(fillMode);

          connectPen.drawToScreen();
        }
      }
    } else if (paintMode == Consts.MODE_LINE) {
      if (startPressX + startPressY == 0) {
        connectPen.bewegeBis(x, y);
        return;
      }

      connectPen.wechsle();
      connectPen.runter();
      connectPen.setzeFuellmuster(Consts.FILL);

      connectPen.zeichneKreis(connectPen.linienBreite() / 2);
      connectPen.bewegeBis(startPressX, startPressY);

      connectPen.bewegeBis(x, y);
      connectPen.zeichneKreis(connectPen.linienBreite() / 2);
      connectPen.setzeFuellmuster(fillMode);

      connectPen.hoch();
      connectPen.normal();

      connectPen.drawToScreen();
    } else if (paintMode == Consts.MODE_RECTANGLE) {
      if (startPressX + startPressY == 0) {
        return;
      }

      connectPen.wechsle();

      drawViereck(
        startPressX, startPressY, (int)connectPen.hPosition(), (int)connectPen.vPosition());

      drawViereck(startPressX, startPressY, x, y);

      connectPen.bewegeBis(x, y);

      connectPen.normal();

      connectPen.drawToScreen();
    }
    }

    public static void stiftRunter(BetterStift connectPen, int x, int y) {
        boolean touchesMenuArea = x < Consts.MENU_X + connectPen.linienBreite() / 2 &&
                y < Consts.MENU_Y + connectPen.linienBreite() / 2;

        if (!touchesMenuArea) {
            connectPen.bewegeBis(x, y);

            if (connectPen.getPaintMode() == Consts.MODE_NORMAL) {
                connectPen.setFillMode(Consts.FILL);
                connectPen.zeichneKreis(connectPen.linienBreite() / 2);
                connectPen.setFillMode(connectPen.getFillMode());
                connectPen.drawToScreen();

                connectPen.runter();
            }

            connectPen.setStartPressX(x);
            connectPen.setStartPressY(y);
        }
    }

    public static void stiftHoch(BetterStift connectPen, int x, int y) {
        byte paintMode = connectPen.getPaintMode();
        int startPressX = connectPen.getStartPressX();
        int startPressY = connectPen.getStartPressY();
        connectPen.hoch();

        if (paintMode == Consts.MODE_LINE || paintMode == Consts.MODE_RECTANGLE ||
                paintMode == Consts.MODE_BUCKETFILL) {
            boolean touchesMenuArea = x < Consts.MENU_X + connectPen.linienBreite() / 2 &&
                    y < Consts.MENU_Y + connectPen.linienBreite() / 2;

            if (!touchesMenuArea) {
                switch (paintMode) {
                    case Consts.MODE_LINE:
                        if (startPressX + startPressY != 0) {
                            connectPen.wechsle();
                            connectPen.bewegeBis(startPressX, startPressY);

                            drawLinie(connectPen, startPressX, startPressY, x, y);
                            connectPen.drawToScreen();
                        }
                        break;

                    case Consts.MODE_RECTANGLE:
                        if (startPressX + startPressY != 0) {
                            connectPenTasks.drawViereck(connectPen, startPressX, startPressY, x, y);
                            connectPen.drawToScreen();
                        }
                        break;

                    case Consts.MODE_BUCKETFILL:

                        Thread fillThread = new Thread() {
                            public void run() {
                                bucketFill(connectPen, x, y, new Color(Utils.getColor(connectPen).getRGB()));
                            }
                        };

                        fillThread.start();
                        break;
                }
            }
        }

        connectPen.setStartPressX(x);
        connectPen.setStartPressY(y);
    }

    public static void bucketFill(BetterStift connectPen, int x, int y, Color fillColor) {
        try {
            BufferedImage snapshot =
                    Utils.createSnapshot(Bildschirm.topFenster.privatPanel(), null);

            Color colorReplaced = Utils.getColorAt(x, y, snapshot);

            if (fillColor.getRGB() == colorReplaced.getRGB()) {
                return;
            }

            Queue<Vector2> q = new LinkedList<Vector2>();

            q.add(new Vector2(x, y));

            final int offsets[][] = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

            while (!q.isEmpty()) {
                Vector2 pos = q.poll();

                for (int i = 0; i < offsets.length; i++) {
                    final int posX = pos.x + offsets[i][0];
                    final int posY = pos.y + offsets[i][1];

                    boolean touchesMenu = (posX < Consts.MENU_X && posY < Consts.MENU_Y);
                    boolean touchesBorders =
                            (posX >= Consts.SCREEN_X || posY >= Consts.SCREEN_Y || posX < 0 ||
                                    posY < 0);

                    if (!touchesMenu && !touchesBorders &&
                            connectPen.getBuffer().getRGB(posX, posY) == colorReplaced.getRGB()) {

                        connectPen.getBuffer().setRGB(posX, posY, fillColor.getRGB());

                        q.add(new Vector2(posX, posY));
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public static void drawViereck(BetterStift connectPen, int sX, int sY, int eX, int eY) {
        int minX = Math.min(sX, eX);
        int maxX = Math.max(sX, eX);

        int minY = Math.min(sY, eY);
        int maxY = Math.max(sY, eY);

        connectPen.bewegeBis(minX, minY);
        connectPen.zeichneRechteck(maxX - minX, maxY - minY);
        connectPen.bewegeBis(maxX, maxY);
    }

    public static void drawLinie(BetterStift connectPen, int sX, int sY, int eX, int eY) {
        connectPen.normal();
        connectPen.hoch();
        connectPen.bewegeBis(sX, sY);
        connectPen.runter();
        connectPen.setFillMode(Consts.FILL);
        connectPen.zeichneKreis(connectPen.linienBreite() / 2);
        connectPen.bewegeBis(eX, eY);
        connectPen.zeichneKreis(connectPen.linienBreite() / 2);
        connectPen.setFillMode(connectPen.getFillMode());
        connectPen.hoch();
    }
}
