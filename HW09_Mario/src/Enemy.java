import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemy extends GameObj {
    public int WIDTH;
    public int HEIGHT;
    public int INIT_X = 0;
    public int INIT_Y;
    public int INIT_VEL_X = 0;
    public int INIT_VEL_Y = 0;
    public int MAX_HEIGHT = 200;
    public int courtHeight;
    
    private BufferedImage img;
    
    public Enemy(int v_x, int v_y, int pos_x, int pos_y, int max_x, int max_y, int width, int height, int court_width,
            int court_height, Direction direction) {
        super(v_x, v_y, pos_x, pos_y, max_x, max_y, width, height, court_width, court_height, direction);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void move() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleOffScreen() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void draw(Graphics g) {
        // TODO Auto-generated method stub
        
    }
}
