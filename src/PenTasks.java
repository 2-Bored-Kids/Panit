import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;
import sum.ereignis.Bildschirm;

// The whole pen-handling for each user, including ourselves is performed here

/* The way we draw the previews for shapes is:
   1. Invert the colors on our old mouse position
   2. Move the pen to the new one
   3. Invert the colors again */

  // Creating functions for BetterStift

  public static void penMove(BetterStift connectPen, int x, int y) {
    byte paintMode = connectPen.getPaintMode();
    byte fillMode = connectPen.getFillMode();
    int startPressX = connectPen.getStartPressX();
    int startPressY = connectPen.getStartPressY();

    if (paintMode != Consts.MODE_NORMAL &&
        paintMode != Consts.MODE_BUCKETFILL &&
        !Utils.isInBounds(x, y, connectPen.linienBreite() / 2)) {
      penUp(
        connectPen, (int)connectPen.hPosition(), (int)connectPen.vPosition());
      return;
    }

    if (paintMode == Consts.MODE_NORMAL) {
      connectPen.bewegeBis(x, y);
      if (connectPen.istUnten()) {
        connectPen.normal();
        connectPen.setzeFuellmuster(Consts.FILL);
        connectPen.zeichneKreis(connectPen.linienBreite() / 2);
        connectPen.setzeFuellmuster(fillMode);
      }
    } else if (startPressX + startPressY != 0) {
      if (paintMode == Consts.MODE_LINE) {
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
      } else if (paintMode == Consts.MODE_RECTANGLE) {
        connectPen.wechsle();

        drawRectangle(connectPen,
                      startPressX,
                      startPressY,
                      (int)connectPen.hPosition(),
                      (int)connectPen.vPosition());

        drawRectangle(connectPen, startPressX, startPressY, x, y);

        connectPen.bewegeBis(x, y);

        connectPen.normal();
      }
    } else {
      connectPen.bewegeBis(x, y);
    }
  }

  public static void penDown(BetterStift connectPen, int x, int y) {
    if (Utils.isInBounds(x, y, connectPen.linienBreite() / 2)) {
      connectPen.bewegeBis(x, y);

      if (connectPen.getPaintMode() == Consts.MODE_NORMAL) {
        connectPen.setFillMode(Consts.FILL);
        connectPen.zeichneKreis(connectPen.linienBreite() / 2);
        connectPen.setFillMode(connectPen.getFillMode());

        connectPen.runter();
      }

      connectPen.setStartPressX(x);
      connectPen.setStartPressY(y);
    }
  }

  public static void penUp(BetterStift connectPen, int x, int y) {
    byte paintMode = connectPen.getPaintMode();
    int startPressX = connectPen.getStartPressX();
    int startPressY = connectPen.getStartPressY();

    connectPen.hoch();

    if (paintMode == Consts.MODE_LINE || paintMode == Consts.MODE_RECTANGLE ||
        paintMode == Consts.MODE_BUCKETFILL) {

      if (Utils.isInBounds(x, y, connectPen.linienBreite() / 2)) {
        switch (paintMode) {
          case Consts.MODE_LINE:
            if (startPressX + startPressY != 0) {
              connectPen.wechsle();
              connectPen.bewegeBis(startPressX, startPressY);

              drawLine(connectPen, startPressX, startPressY, x, y);
            }
            break;

          case Consts.MODE_RECTANGLE:
            if (startPressX + startPressY != 0) {
              PenTasks.drawRectangle(
                connectPen, startPressX, startPressY, x, y);
            }
            break;

          case Consts.MODE_BUCKETFILL:

            Thread fillThread = new Thread() {
              public void run() {
                bucketFill(connectPen,
                           x,
                           y,
                           new Color(Utils.getColor(connectPen).getRGB()));
              }
            };

            fillThread.start();
            break;
        }
      }
    }

    connectPen.setStartPressX(0);
    connectPen.setStartPressY(0);
  }

  private static class Vector2 {
    public int x, y;

    public Vector2(int X, int Y) {
      x = X;
      y = Y;
    }
  }


  // Masterfully ported from the pseudocode on wikipedia
  public static void bucketFill(BetterStift connectPen,
                                int x,
                                int y,
                                Color fillColor) {
    try {
      BufferedImage snapshot =
        Utils.createSnapshot(Bildschirm.topFenster.privatPanel(), null);

      assert snapshot != null;
      Color colorReplaced = Utils.getColorAt(x, y, snapshot);

      if (fillColor.getRGB() == colorReplaced.getRGB()) {
        return;
      }

      Queue<Vector2> q = new LinkedList<Vector2>();

      q.add(new Vector2(x, y));

      final int[][] offsets = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

      while (!q.isEmpty()) {
        Vector2 pos = q.poll();

        for (int[] offset : offsets) {
          final int posX = pos.x + offset[0];
          final int posY = pos.y + offset[1];

          if (Utils.isInBounds(posX, posY, 0) &&
              connectPen.getBuffer().getRGB(posX, posY) ==
                colorReplaced.getRGB()) {

            connectPen.getBuffer().setRGB(posX, posY, fillColor.getRGB());

            q.add(new Vector2(posX, posY));
          }
        }
      }
    } catch (Exception ignored) {
    }
  }

  public static void drawRectangle(BetterStift connectPen,
                                   int sX,
                                   int sY,
                                   int eX,
                                   int eY) {
    int minX = Math.min(sX, eX);
    int maxX = Math.max(sX, eX);

    int minY = Math.min(sY, eY);
    int maxY = Math.max(sY, eY);

    connectPen.bewegeBis(minX, minY);
    connectPen.zeichneRechteck(maxX - minX, maxY - minY);
    connectPen.bewegeBis(maxX, maxY);
  }

  public static void drawLine(BetterStift connectPen,
                              int sX,
                              int sY,
                              int eX,
                              int eY) {
    byte oldMode = connectPen.getFillMode();

    connectPen.normal();
    connectPen.hoch();
    connectPen.bewegeBis(sX, sY);
    connectPen.runter();
    connectPen.setFillMode(Consts.FILL);
    connectPen.zeichneKreis(connectPen.linienBreite() / 2);
    connectPen.bewegeBis(eX, eY);
    connectPen.zeichneKreis(connectPen.linienBreite() / 2);
    connectPen.setFillMode(oldMode);
    connectPen.hoch();
  }
}
