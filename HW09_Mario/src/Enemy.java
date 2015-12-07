import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class Enemy extends GameObj {
    public String[] img_files;
    
    public int startDistance;
    public boolean dead = false;
    public boolean onScreen = false;
    public static int INIT_VEL_X = 0;
    public static int INIT_VEL_Y = 0;
    
    private BufferedImage[] imgs;
    
    
    public Enemy(int courtWidth, int courtHeight, int initWidth, int initHeight, int startDistance,
            String[] img_files) {
        super(INIT_VEL_X, INIT_VEL_Y, courtWidth, courtHeight - initHeight - GroundTile.SIZE,
                courtWidth - initWidth, courtHeight - initHeight - GroundTile.SIZE, initWidth,
                initHeight, courtWidth, courtHeight, Direction.LEFT);
        
        try {
            imgs = new BufferedImage[img_files.length];
            for (int i = 0; i < img_files.length; i++) {
                imgs[i] = ImageIO.read(new File(img_files[i]));
            }
            if (img == null) img = imgs[0];
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        
        this.startDistance = startDistance;
    }

    @Override
    public void move() {
        if (dead) {
            v_x = 0;
            pos_y += 5;
            return;
        }
        
        if (v_x < 0) direction = Direction.LEFT;
        else if (v_x > 0) direction = Direction.RIGHT;
        
        pos_x += v_x;
        
        for (int i = 0; i < imgs.length; i++) {
            if (i == imgs.length - 1 && img.equals(imgs[i])) {
                img = imgs[0];
                break;
            } else if (img.equals(imgs[i])) {
                img = imgs[i + 1];
                break;
            }
        }
        
        handleOffScreen();        
    }

    @Override
    public void handleOffScreen() {
        if (pos_y < 0) pos_y = 0;
        else if (pos_y > max_y) pos_y = max_y;   
    }
    
    public boolean offScreenLeft() {
        return pos_x + width < 0;
    }
}
