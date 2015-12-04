import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Mario extends GameObj {
    
    public static String standing_rt_img_file = "Mario_Standing.gif";
    public static String standing_lt_img_file = "Mario_StandingL.gif";
    public static String jumping_rt_img_file = "Mario_Jump.gif";
    public static String jumping_lt_img_file = "Mario_JumpL.gif";
    public static String[] walking_rt_img_files = {"Mario_Walk_1.gif", "Mario_Walk_1.gif", 
            "Mario_Walk_2.gif", "Mario_Walk_2.gif", "Mario_Walk_3.gif", "Mario_Walk_3.gif"};
    public static String[] walking_lt_img_files = {"Mario_Walk_1L.gif", "Mario_Walk_1L.gif", 
            "Mario_Walk_2L.gif", "Mario_Walk_2L.gif", "Mario_Walk_3L.gif", "Mario_Walk_3L.gif"};
    public static final int WIDTH = 26;
    public static final int HEIGHT = 32;
    public static final int INIT_X = 0;
    public static int INIT_Y;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    
    public static int courtHeight;
    
    public boolean reachedMaxHeight = false;
    public boolean gravityOn = false;
    
    private static BufferedImage img;
    private static BufferedImage standing_rt_img;
    private static BufferedImage standing_lt_img;
    private static BufferedImage jumping_rt_img;
    private static BufferedImage jumping_lt_img;
    private static BufferedImage[] walking_rt_imgs;
    private static BufferedImage[] walking_lt_imgs;
    
    public Mario(int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_X, courtHeight - 63, WIDTH, HEIGHT, courtWidth, courtHeight,
                Direction.RIGHT);
        
        INIT_Y = courtHeight - HEIGHT - 32;
        max_y = courtHeight - HEIGHT - 32;
        this.courtHeight = courtHeight;
        
        try {
            if (img == null) {
                standing_rt_img = ImageIO.read(new File(standing_rt_img_file));
                standing_lt_img = ImageIO.read(new File(standing_lt_img_file));
                jumping_rt_img = ImageIO.read(new File(jumping_rt_img_file));
                jumping_lt_img = ImageIO.read(new File(jumping_lt_img_file));
                walking_rt_imgs = new BufferedImage[walking_rt_img_files.length];
                for (int i = 0; i < walking_rt_img_files.length; i++) {
                    walking_rt_imgs[i] = ImageIO.read(new File(walking_rt_img_files[i]));
                }
                walking_lt_imgs = new BufferedImage[walking_lt_img_files.length];
                for (int i = 0; i < walking_lt_img_files.length; i++) {
                    walking_lt_imgs[i] = ImageIO.read(new File(walking_lt_img_files[i]));
                }
                img = standing_rt_img;
            }            
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    @Override
    public void move() {
        if (v_x < 0) direction = Direction.LEFT;
        else if (v_x > 0) direction = Direction.RIGHT;
        
        pos_x += v_x;
        
        if (gravityOn) {
            if (!reachedMaxHeight) {
                pos_y += v_y;
            } else {
                pos_y -= v_y;
            }
        }

        handleOffScreen();
        
        if (pos_y <= 150) {
            reachedMaxHeight = true;
        } else if (pos_y == max_y) {
            reachedMaxHeight = false;
        }
        
        if (gravityOn && pos_y == max_y) {
            gravityOn = false;
            v_y = 0;
        }
        
        if (v_x == 0 && v_y == 0) {
            if (direction == Direction.RIGHT) img = standing_rt_img;
            else img = standing_lt_img;
        } else if (gravityOn) {
            if (direction == Direction.RIGHT) img = jumping_rt_img;
            else img = jumping_lt_img;
        } else {
            BufferedImage[] walkingDirection;
            BufferedImage[] oppositeDirection;
            if (direction == Direction.RIGHT) {
                walkingDirection = walking_rt_imgs;
                oppositeDirection = walking_lt_imgs;
            } else {
                walkingDirection = walking_lt_imgs;
                oppositeDirection = walking_rt_imgs;
            }
            if (img.equals(standing_rt_img) || img.equals(standing_lt_img) ||
                    img.equals(jumping_rt_img) || img.equals(jumping_lt_img)) {
                img = walkingDirection[0];
            } else {
                for (int i = 0; i < walkingDirection.length; i++) {
                    if ((i == walkingDirection.length - 1 && img.equals(walkingDirection[i])) ||
                            img.equals(oppositeDirection[i])) {
                        img = walkingDirection[0];
                        break;
                    } else if (img.equals(walkingDirection[i])) {
                        img = walkingDirection[i+1];
                        break;
                    }
                }
            }
        }
        
        max_y = courtHeight - 32 - img.getHeight();
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
        g.drawImage(img, pos_x, pos_y, img.getWidth(), img.getHeight(), null);
    }
    
}
