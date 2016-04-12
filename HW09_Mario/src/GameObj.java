import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class GameObj {
    
    /** Current position of the object (in terms of graphics coordinates)
     *  
     * Coordinates are given by the upper-left hand corner of the object.
     * This position should always be within bounds.
     *  0 <= pos_x <= max_x 
     *  0 <= pos_y <= max_y 
     */
    public int pos_x; 
    public int pos_y;

    /** Size of object, in pixels */
    public int width;
    public int height;
    
    /** Velocity: number of pixels to move every time move() is called */
    public int v_x;
    public int v_y;

    /** Upper bounds of the area in which the object can be positioned.  
     *    Maximum permissible x, y positions for the upper-left 
     *    hand corner of the object
     */
    public int max_x;
    public int max_y; 
    
    public int court_width;
    public int court_height;
    
    public Direction direction;
    
    protected BufferedImage img;
    
    /**
     * Constructor
     */
    public GameObj(int v_x, int v_y, int pos_x, int pos_y, int max_x, int max_y, 
        int width, int height, int court_width, int court_height, Direction direction) {
        this.v_x = v_x;
        this.v_y = v_y;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.max_x = max_x;
        this.max_y = max_y;
        this.width = width;
        this.height = height;
        this.court_width = court_width;
        this.court_height = court_height;
        
        this.direction = direction;
    }
    
    /**
     * Moves the object by its velocity.  Ensures that the object does
     * not go outside its bounds by clipping.
     */
    public abstract void move();
    
    public abstract void handleOffScreen();
    
    /**
     * Determine whether this game object is currently intersecting
     * another object.
     * 
     * Intersection is determined by comparing bounding boxes. If the 
     * bounding boxes overlap, then an intersection is considered to occur.
     * 
     * @param obj : other object
     * @return whether this object intersects the other object.
     */
    public boolean intersects(GameObj obj) {
        return (intersectsTop(obj) || intersectsBottom(obj) ||
                intersectsRight(obj) || intersectsLeft(obj));
    }
    
    public boolean intersectsTop(GameObj obj) {
        return (obj.pos_y + obj.height >= pos_y
                && obj.pos_y + obj.height <= pos_y + ((double) height / 2)
                && obj.pos_x + obj.width >= pos_x
                && obj.pos_x <= pos_x + width
                );
    }
    
    public boolean intersectsBottom(GameObj obj) {
        return (obj.pos_y <= pos_y + height
                && obj.pos_y > pos_y + ((double) height / 2)
                && obj.pos_x + obj.width >= pos_x
                && obj.pos_x <= pos_x + width
                );
    }
    
    public boolean intersectsRight(GameObj obj) {
        return (obj.pos_x <= pos_x + width
                && obj.pos_x >= pos_x + ((double) width / 2)
                && obj.pos_y + obj.height >= pos_y
                && obj.pos_y <= pos_y + height
                );
    }
    
    public boolean intersectsLeft(GameObj obj) {
        return (obj.pos_x + obj.width >= pos_x
                && obj.pos_x + obj.width < pos_x + ((double) height / 2)
                && obj.pos_y + obj.height >= pos_y
                && obj.pos_y <= pos_y + height
                );
    }
    
    public boolean collides(GameObj obj) {
        return (collidesTop(obj) || collidesBottom(obj) || collidesLeft(obj) || collidesRight(obj));
    }
    
    public boolean collidesTop(GameObj obj) {
        return (obj.pos_y + obj.height == pos_y
                && obj.pos_x + obj.width >= pos_x
                && obj.pos_x <= pos_x + width
                );
    }
    
    public boolean collidesBottom(GameObj obj) {
        return (obj.pos_y == pos_y + height
                && obj.pos_x + obj.width >= pos_x
                && obj.pos_x <= pos_x + width
                );
    }
    
    public boolean collidesLeft(GameObj obj) {
        return (obj.pos_x + obj.width == pos_x
                && obj.pos_y + obj.height >= pos_y
                && obj.pos_y <= pos_y + height
                );
    }
    
    public boolean collidesRight(GameObj obj) {
        return (obj.pos_x == pos_x + width
                && obj.pos_y + obj.height >= pos_y
                && obj.pos_y <= pos_y + height
                );
    }
    
    public void draw(Graphics g) {
        g.drawImage(img, pos_x, pos_y, width, height, null);
    }
}
