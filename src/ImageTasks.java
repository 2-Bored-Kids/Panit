import sum.ereignis.Bildschirm;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

public class ImageTasks {

    public static String encode(BufferedImage image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            return Arrays.toString(baos.toByteArray());
        }catch (Exception ignored){}
        return "";
    }


    public static void drawDecode(BetterStift pen, Bildschirm bildschirm, String image){
        try {
            byte[] img = image.getBytes();
            InputStream is = new ByteArrayInputStream(img);
            BufferedImage bufferedImage = ImageIO.read(is);
            pen.getBuffer().getGraphics().drawImage(bufferedImage, Consts.MENU_X, 0, bildschirm);
            pen.drawToScreen();
        }catch (Exception ignored){}
    }
}
