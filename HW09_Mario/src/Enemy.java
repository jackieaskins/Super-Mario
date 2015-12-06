import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class Enemy extends GameObj {
    public int startDistance;
    public boolean dead = false;
    public boolean onScreen = false;
    public static int INIT_VEL_X = 0;
    public static int INIT_VEL_Y = 0;
    
    
    public Enemy(int courtWidth, int courtHeight, int initWidth, int initHeight, int startDistance) {
        super(INIT_VEL_X, INIT_VEL_Y, courtWidth, courtHeight - initHeight - GroundTile.SIZE,
                courtWidth - initWidth, courtHeight - initHeight - GroundTile.SIZE, initWidth,
                initHeight, courtWidth, courtHeight, Direction.LEFT);
        
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
