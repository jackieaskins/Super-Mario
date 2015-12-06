import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Goomba extends Enemy {
    
    private static String img_file = "Goomba.gif";
    
    public static int INIT_WIDTH = 32;
    public static int INIT_HEIGHT = 32;
    
    public Goomba(int courtWidth, int courtHeight, int startDistance) {
        super(courtWidth, courtHeight, INIT_WIDTH, INIT_HEIGHT, startDistance);
        
        try {
            if (img == null) img = ImageIO.read(new File(img_file));
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
}
