import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedList;


@SuppressWarnings("serial")
public class GameCourt extends JPanel {
    
    LevelGenerator lg;
    
    private Mario mario; // the Mario character, keyboard control
    private GroundTile[] ground; // array of ground tiles
    private EndCastle castle; // castle at the end of the level
    private LinkedList<Enemy> enemies; // list of enemies contained in the game
    private LinkedList<Coin> coins; // list containing all coins in the game
    
    public HighScores hs = new HighScores(); // High Scores object to write high scores
    public static String username = ""; // User set username
    
    public static boolean playing = false; // whether the game is running
    public boolean gameOver = false; // whether the user has lost
    public boolean gameWon = false; // whether the user has won
    public static boolean endTile = false; // whether the user has reached the end
    
    private JLabel coins_label;
    private JLabel score_label;
    private JLabel lives_label;
    private JLabel doneLabel; // label that shows if the user wins or loses
    
    private static int finalScore; // the user's final score once they've won
    private static int score = 0;
    private static int num_coins = 0;
    private static int num_lives = 3;

    // Game constants
    public static final int COURT_WIDTH = 640;
    public static final int COURT_HEIGHT = 400;
    public static final int MARIO_X_VELOCITY = 6;
    public static final int MARIO_Y_VELOCITY = 10;
    public static final int GROUND_X_VELOCITY = 6;
    public static final int ENEMY_X_VELOCITY = 5;
    public static final int MAX_MARIO_X = 350;
    
    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;
    
    public static int distanceTravelled;
    
    public GameCourt(JLabel score_label, JLabel coins_label, JLabel lives_label) {

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

        // This key listener allows the characters on the screen to move as
        // long as an arrow key is pressed, by changing the objects' velocity
        // accordingly. (The tick method below actually moves the objects.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    mario.v_x = -MARIO_X_VELOCITY;
                    GroundTile.vel_x = GROUND_X_VELOCITY;
                    Coin.vel_x = GROUND_X_VELOCITY;
                    castle.v_x = GROUND_X_VELOCITY;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    mario.v_x = MARIO_X_VELOCITY;
                    GroundTile.vel_x = -GROUND_X_VELOCITY;
                    Coin.vel_x = -GROUND_X_VELOCITY;
                    castle.v_x = -GROUND_X_VELOCITY;
                }
                
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (!mario.gravityOn) mario.v_y = -MARIO_Y_VELOCITY;
                    mario.gravityOn = true;
                }
                
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    if (gameWon || gameOver) {
                        num_lives = 3;
                        gameWon = false;
                        gameOver = false;
                        reset();
                    }
                }
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    mario.v_x = 0;
                    GroundTile.vel_x = 0;
                    Coin.vel_x = 0;
                    castle.v_x = 0;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    mario.v_x = 0;
                    GroundTile.vel_x = 0;
                    Coin.vel_x = 0;
                    castle.v_x = 0;
                }
                else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (!mario.gravityOn) {
                        mario.v_y = 0;
                    }
                }         
            }
        });
        
        // Initialize the game state labels
        this.score_label = score_label;
        this.coins_label = coins_label;
        this.lives_label = lives_label;
        
        doneLabel = new JLabel("");
    }
    
    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        doneLabel.setText("");
        distanceTravelled = 0;
        endTile = false;
        removeAll();
        
        // Reset all of the game objects
        mario = new Mario(COURT_WIDTH, COURT_HEIGHT);
        lg = new LevelGenerator(121, COURT_WIDTH, COURT_HEIGHT);
        coins = lg.getCoins();
        enemies = lg.getEnemies();
        ground = lg.getGroundTiles();
        castle = new EndCastle(COURT_WIDTH, COURT_HEIGHT, (ground.length - 1) * GroundTile.SIZE -
                    EndCastle.INIT_WIDTH);
        score = 0;
        num_coins = 0;
        
        // Start playing the game
        playing = true;
        
        // Appropriately set all of the game labels
        add(doneLabel);
        score_label.setText("Score: " + score);
        lives_label.setText("Lives: " + num_lives);
        coins_label.setText("Coins: " + num_coins);

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }
    
    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (gameWon && playing) {
            // Handle the user winning the game
            doneLabel.setText("<html>Congratulations! You've won! <br> Press 'S' to play again!" +
                    "<br> Your score: " + finalScore + "<html>");
            playing = false;
        } else if (gameOver && playing) {
            // Handle the user losing the game
            doneLabel.setText("Sorry! You've lost!\nPress 'S' to play again!");
            playing = false;
        } else if (playing && mario.pos_y <= COURT_HEIGHT) {
            if (!(ground[ground.length-2].pos_x + GroundTile.SIZE > COURT_WIDTH)) {   
                endTile = true;
            }
            
            // Advance Mario in his current direction        
            mario.move();
            
            // Advance the ground & coins in their current direction
            if (mario.pos_x + mario.width >= MAX_MARIO_X && !mario.dead &&
                    ground[ground.length-2].pos_x + GroundTile.SIZE > COURT_WIDTH) {
                for (int i = 0; i < ground.length; i++) {
                    ground[i].move();
                }
                for (int i = 0; i < coins.size(); i++) {
                    coins.get(i).move();
                }
                castle.move();
            }
            
            // Spin the coins & remove them if Mario collects them
            Coin[] cs = new Coin[coins.size()];
            coins.toArray(cs);
            for (Coin coin : cs) {
                coin.spinCoin();
                if (coin.intersects(mario)) {
                    num_coins++;
                    score += 100;
                    coins.remove(coin);
                }
            }
            
            // Move the enemies & handle their death & Mario's death
            Enemy[] es = new Enemy[enemies.size()];
            enemies.toArray(es);
            for (Enemy enemy : es) {
                if (enemy.dead) {
                    enemies.remove(enemy);
                }
                if (enemy.startDistance <= distanceTravelled + 50 && !mario.dead) {
                    enemy.onScreen = true;
                    enemy.v_x = -ENEMY_X_VELOCITY;
                }
                if (enemy.offScreenLeft()) enemies.remove(enemy);
                if (!mario.dead && !enemy.dead && enemy.intersectsTop(mario) && mario.reachedMaxHeight) {
                    score = enemy.incrementScore(score);
                    enemy.dead = true;
                } else if (!mario.dead && !enemy.dead && (enemy.intersectsLeft(mario)
                        || enemy.intersectsRight(mario))) {
                    num_lives--;
                    mario.dead = true;
                }
                enemy.move();
            }
            
            // win the game if Mario intersects the right half of the castle
            if (castle.intersectsCastle(mario)) {
               gameWon = true;
               finalScore = score;
               hs.addHighScore(new HighScore(username, finalScore));
               return;
            }
            
            // Stop everything from moving if mario is dead;
            if (mario.dead) {
                GroundTile.vel_x = 0;
                Coin.vel_x = 0;
                for (Enemy enemy : enemies) enemy.v_x = 0;
            }
            
            score_label.setText("Score: " + score);
            lives_label.setText("Lives: " + num_lives);
            coins_label.setText("Coins: " + num_coins);
            

            // update the display
            repaint();
        } else {
            // If Mario is dead, check to see if the game should be over
            lives_label.setText("Lives: " + num_lives);
            if (num_lives > 0) reset();
            else gameOver = true;
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the castle first since it should be in the background
        castle.draw(g);
        
        // Draw the ground if they're on screen
        for (int i = 0; i < ground.length; i++) {
            if ((ground[i].pos_x <= COURT_WIDTH && ground[i].pos_x >= 0)
                    || (ground[i].pos_x + ground[i].width <= COURT_WIDTH 
                        && ground[i].pos_x + ground[i].width >= 0)) 
                ground[i].draw(g);
        }
        
        // Draw the enemies if they're on screen
        for (Enemy enemy : enemies) {
            if (enemy.pos_x <= COURT_WIDTH && enemy.pos_x + enemy.width >=0 && enemy.onScreen) {
                enemy.draw(g);
            }
        }
        
        // Draw the coins if they're on screen
        for (Coin coin : coins) {
            coin.draw(g);
            if (coin.pos_x <= COURT_WIDTH && coin.pos_x + coin.width >= 0) {
                coin.draw(g);
            }
        }
        
        // Draw Mario
        mario.draw(g);
        
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}
