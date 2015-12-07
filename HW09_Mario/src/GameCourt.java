import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedList;


@SuppressWarnings("serial")
public class GameCourt extends JPanel {
    
    private Mario mario; // the Mario character, keyboard control
    private GroundTile[] ground = new GroundTile[40]; // array of ground tiles
    private LinkedList<Enemy> enemies;
    
    public boolean playing = false; // whether the game is running
    private JLabel status; // Current status text (i.e. Running...)

    // Game constants
    public static final int COURT_WIDTH = 640;
    public static final int COURT_HEIGHT = 400;
    public static final int MARIO_X_VELOCITY = 6;
    public static final int MARIO_Y_VELOCITY = 8;
    public static final int GROUND_X_VELOCITY = 6;
    public static final int ENEMY_X_VELOCITY = 5;
    public static final int MAX_MARIO_X = 350;
    public static final int MIN_MARIO_X = 300;
    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;
    
    public static int distanceTravelled;
    
    public GameCourt(JLabel status) {

        // The timer is an object which triggers an action periodically
        // with the given INTERVAL. One registers an ActionListener with
        // this timer, whose actionPerformed() method will be called
        // each time the timer triggers. We define a helper method
        // called tick() that actually does everything that should
        // be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key
        // events will be handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long
        // as an arrow key is pressed, by changing the square's
        // velocity accordingly. (The tick method below actually
        // moves the square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    mario.v_x = -MARIO_X_VELOCITY;
                    GroundTile.vel_x = GROUND_X_VELOCITY;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    mario.v_x = MARIO_X_VELOCITY;
                    GroundTile.vel_x = -GROUND_X_VELOCITY;
                }
                
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (!mario.gravityOn) mario.v_y = -MARIO_Y_VELOCITY;
                    mario.gravityOn = true;
                }
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    mario.v_x = 0;
                    GroundTile.vel_x = 0;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    mario.v_x = 0;
                    GroundTile.vel_x = 0;
                }
                else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (!mario.gravityOn) {
                        mario.v_y = 0;
                    }
                }
                
                
            }
        });

        this.status = status;
    }
    
    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        distanceTravelled = 0;
        mario = new Mario(COURT_WIDTH, COURT_HEIGHT);
        enemies = new LinkedList<Enemy>();
        enemies.add(new Goomba(COURT_WIDTH, COURT_HEIGHT, 400));
        for (int i = 0; i < ground.length; i++) {
            ground[i] = new GroundTile(COURT_WIDTH, COURT_HEIGHT, GroundTile.SIZE * i, 
                    COURT_HEIGHT - GroundTile.SIZE);
        }
        
        playing = true;
        status.setText("Running...");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }
    
    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing && mario.pos_y <= COURT_HEIGHT) {
            // Advance Mario in his current direction
            mario.move();
            
            if (mario.pos_x + mario.width >= MAX_MARIO_X && !mario.dead) {
                for (int i = 0; i < ground.length; i++) {
                    ground[i].move();
                }
            }
            
            Enemy[] es = new Enemy[enemies.size()];
            enemies.toArray(es);
            for (int i = 0; i < es.length; i++) {
                if (es[i].startDistance <= distanceTravelled + 50 && !mario.dead) {
                    es[i].onScreen = true;
                    es[i].v_x = -ENEMY_X_VELOCITY;
                }
                if (es[i].offScreenLeft()) enemies.remove(i);
                es[i].move();
                if (es[i].collidesTop(mario)) {
                    es[i].dead = true;
                } else if (es[i].collidesLeft(mario) || es[i].collidesRight(mario)) {
                    mario.dead = true;
                }
            }
            
            if (mario.dead) {
                GroundTile.vel_x = 0;
                for (Enemy enemy : enemies) enemy.v_x = 0;
            }

            // update the display
            repaint();
        } else {
            playing = false;
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < ground.length; i++) {
            if ((ground[i].pos_x <= COURT_WIDTH && ground[i].pos_x >= 0)
                    || (ground[i].pos_x + ground[i].width <= COURT_WIDTH 
                        && ground[i].pos_x + ground[i].width >= 0)) 
                ground[i].draw(g);
        }
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (enemy.pos_x <= COURT_WIDTH && enemy.pos_x + enemy.width >=0 && enemy.onScreen) {
                
                enemy.draw(g);
            }
        }
        mario.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}
