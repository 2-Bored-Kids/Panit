import sum.ereignis.Bildschirm;

import java.util.Base64;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

public class ImageTasks {

    public static String encode(BufferedImage image) {
        try {
            ByteArrayOutputStream outputStr = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStr);
            return Base64.getEncoder().encodeToString(outputStr.toByteArray());
        }catch (Exception e){}

        return "";
    }

    public static void drawDecode(BetterStift pen, String image){
        try {
            byte[] bytes = Base64.getDecoder().decode(image);
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
            pen.getBuffer().getGraphics().drawImage(bufferedImage, 0, 0, Bildschirm.topFenster);
        }catch (Exception e){}
    }

}
