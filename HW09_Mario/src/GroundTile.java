import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GroundTile extends GameObj {
    
    public static String img_file = "Ground.gif";
    public static final int SIZE = 32;
    public static int INIT_X = 0;
    public static int INIT_Y;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    
    private static BufferedImage img;
    
    public GroundTile(int courtWidth, int courtHeight, int initX) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_X, courtHeight - 32, SIZE, SIZE, courtWidth, courtHeight, 
                Direction.RIGHT);
        
        INIT_X = initX;
        
        try {
            if (img == null) {
                img = ImageIO.read(new File(img_file));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    @Override
    public void handleOffScreen() {
        if (pos_x < 0) pos_x = 0;
        else if (pos_x > max_x) pos_x = max_x;
        
        if (pos_y < 0) pos_y = 0;
        else if (pos_y > max_y) pos_y = max_y;
    }
    
    @Override
    public void draw(Graphics g) {
        g.drawImage(img, pos_x, pos_y, width, height, null);
    }
}
