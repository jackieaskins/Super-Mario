import java.awt.Graphics;

public class GameObj {
    
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
    
    public Direction direction;
    
    /**
     * Constructor
     */
    public GameObj(int v_x, int v_y, int pos_x, int pos_y, 
        int width, int height, int court_width, int court_height, Direction direction) {
        this.v_x = v_x;
        this.v_y = v_y;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.width = width;
        this.height = height;
        
        // take the width and height into account when setting the 
        // bounds for the upper left corner of the object.
        this.max_x = court_width - width;
        this.max_y = court_height - height;
        
        this.direction = direction;
    }
    
    /**
     * Moves the object by its velocity.  Ensures that the object does
     * not go outside its bounds by clipping.
     */
    public void move() {
        if (v_x < 0) direction = Direction.LEFT;
        else if (v_x > 0) direction = Direction.RIGHT;
        
        pos_x += v_x;
        pos_y += v_y;

        handleOffScreen();
    }
    
    public void handleOffScreen() {
        
    }
    
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
        return intersectsTop(obj) || intersectsBottom(obj) || 
                intersectsRight(obj) || intersectsLeft(obj);
    }
    
    public boolean intersectsTop(GameObj obj) {
        return false;
    }
    
    public boolean intersectsBottom(GameObj obj) {
        return false;
    }
    
    public boolean intersectsRight(GameObj obj) {
        return false;
    }
    
    public boolean intersectsLeft(GameObj obj) {
        return false;
    }
    
    public void draw(Graphics g) {
        
    }
}