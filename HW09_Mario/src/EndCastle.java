import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class EndCastle extends GameObj {
    
    public static String img_file = "Castle.gif";
    
    public static final int INIT_WIDTH = 160;
    public static final int INIT_HEIGHT = 160;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    
    public int startDistance;
    
    public EndCastle(int courtWidth, int courtHeight, int startDistance) {
        super(INIT_VEL_X, INIT_VEL_Y, startDistance,
                courtHeight - GroundTile.SIZE - INIT_HEIGHT, courtWidth, courtHeight, INIT_WIDTH,
                INIT_HEIGHT, courtWidth, courtHeight, Direction.LEFT);
        
        try {
            if (img == null) img = ImageIO.read(new File(img_file));
        } catch (IOException e) {
            System.out.println("Internal Error: " + e.getMessage());
        }
    }

    @Override
    public void move() {
        if (v_x < 0) direction = Direction.LEFT;
        if (v_x > 0) direction = Direction.RIGHT;
        
        pos_x += v_x;
        
        handleOffScreen();        
    }

    @Override
    public void handleOffScreen() {
        if (pos_y < 0) pos_y = 0;
        else if (pos_y > max_y) pos_y = max_y; 
    }
    
    public boolean intersectsCastle(GameObj obj) {
        return (obj.pos_x + obj.width >= pos_x + width /2
                && obj.pos_y + obj.height == pos_y + height
                && obj.pos_x < pos_x + width);
    }
}
