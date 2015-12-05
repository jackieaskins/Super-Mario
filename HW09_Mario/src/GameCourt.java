import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;


@SuppressWarnings("serial")
public class GameCourt extends JPanel {
    
    private Mario mario; // the Mario character, keyboard control
    private GroundTile[] ground = new GroundTile[21]; // array of ground tiles
    
    public boolean playing = false; // whether the game is running
    private JLabel status; // Current status text (i.e. Running...)

    // Game constants
    public static final int COURT_WIDTH = 640;
    public static final int COURT_HEIGHT = 400;
    public static final int MARIO_X_VELOCITY = 4;
    public static final int MARIO_Y_VELOCITY = 8;
    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;
    
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
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    mario.v_x = MARIO_X_VELOCITY;
                }
                
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (!mario.gravityOn) mario.v_y = -MARIO_Y_VELOCITY;
                    mario.gravityOn = true;
                }
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    mario.v_x = 0;
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
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

        mario = new Mario(COURT_WIDTH, COURT_HEIGHT);
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
        if (playing) {
            // Advance Mario in his current direction
            mario.move();

            // update the display
            repaint();
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < ground.length; i++) {
            ground[i].draw(g);
        }
        mario.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}
