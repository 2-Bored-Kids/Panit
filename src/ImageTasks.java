import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import javax.imageio.ImageIO;
import sum.ereignis.Bildschirm;

// Here we do all the image encoding/decoding to sync it between the clients

public class ImageTasks {

  public static String encode(BufferedImage image) {
    try {
      ByteArrayOutputStream outputStr = new ByteArrayOutputStream();
      ImageIO.write(image, "png", outputStr);
      return Base64.getEncoder().encodeToString(outputStr.toByteArray());
    } catch (Exception e) {
    }

    return "";
  }

  public static void drawDecode(BetterStift pen, String image) {
    try {
      byte[] bytes = Base64.getDecoder().decode(image);
      BufferedImage bufferedImage =
        ImageIO.read(new ByteArrayInputStream(bytes));
      pen.getBuffer().getGraphics().drawImage(
        bufferedImage, 0, 0, Bildschirm.topFenster);
    } catch (Exception e) {
    }
  }
}
