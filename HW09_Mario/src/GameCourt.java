import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedList;


@SuppressWarnings("serial")
public class GameCourt extends JPanel {
    
    private Mario mario; // the Mario character, keyboard control
    private GroundTile[] ground = new GroundTile[41]; // array of ground tiles
    private EndCastle castle;
    private LinkedList<Enemy> enemies;
    private LinkedList<Coin> coins;
    
    public static String username = "";
    
    public static boolean playing = false; // whether the game is running
    public boolean gameOver = false;
    public boolean gameWon = false;
    public boolean playAgainSet = false;
    public static boolean endTile = false;
    private JLabel coins_label; // Current status text (i.e. Running...)
    private JLabel score_label;
    private JLabel lives_label;
    private JLabel wonLabel;
    
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
    public static final int MIN_MARIO_X = 300;
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
        //setFocusable(true);

        // This key listener allows the square to move as long
        // as an arrow key is pressed, by changing the square's
        // velocity accordingly. (The tick method below actually
        // moves the square.)
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
                
                if (e.getKeyCode() == KeyEvent.VK_Y) {
                    if (gameWon) {
                        num_lives = 3;
                        gameWon = false;
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

        this.score_label = score_label;
        this.coins_label = coins_label;
        this.lives_label = lives_label;
        
        wonLabel = new JLabel("");
    }
    
    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        wonLabel.setText("");
        distanceTravelled = 0;
        endTile = false;
        removeAll();
        playAgainSet = false;
        mario = new Mario(COURT_WIDTH, COURT_HEIGHT);
        coins = new LinkedList<Coin>();
        coins.add(new Coin(COURT_WIDTH, COURT_HEIGHT, 400));
        enemies = new LinkedList<Enemy>();
        enemies.add(new Goomba(COURT_WIDTH, COURT_HEIGHT, 400));
        enemies.add(new GreenKoopaTroop(COURT_WIDTH, COURT_HEIGHT, 650));
        for (int i = 0; i < ground.length; i++) {
            ground[i] = new GroundTile(COURT_WIDTH, COURT_HEIGHT, GroundTile.SIZE * i, 
                    COURT_HEIGHT - GroundTile.SIZE);
        }
        castle = new EndCastle(COURT_WIDTH, COURT_HEIGHT, (ground.length - 1) * GroundTile.SIZE - 160);
        score = 0;
        num_coins = 0;
        
        playing = true;
        

        add(wonLabel);
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
            wonLabel.setText("Congratulations! You've won!\nDo you want to play again?");
            playing = false;
        } else if (playing && mario.pos_y <= COURT_HEIGHT) {
            if (!(ground[ground.length-2].pos_x + GroundTile.SIZE > COURT_WIDTH)) {   
                endTile = true;
            }
            
            // Advance Mario in his current direction        
            mario.move();
            
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
            
            Enemy[] es = new Enemy[enemies.size()];
            enemies.toArray(es);
            for (Enemy enemy : enemies) {
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
            
            if (castle.intersectsCastle(mario)) {
               gameWon = true;
               return;
            }
            
            if (mario.dead) {
                GroundTile.vel_x = 0;
                for (Enemy enemy : enemies) enemy.v_x = 0;
            }
            
            score_label.setText("Score: " + score);
            lives_label.setText("Lives: " + num_lives);
            coins_label.setText("Coins: " + num_coins);
            

            // update the display
            repaint();
        } else {
            lives_label.setText("Lives: " + num_lives);
            playing = false;
            if (num_lives > 0) reset();
            else gameOver = true;
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        castle.draw(g);
        for (int i = 0; i < ground.length; i++) {
            if ((ground[i].pos_x <= COURT_WIDTH && ground[i].pos_x >= 0)
                    || (ground[i].pos_x + ground[i].width <= COURT_WIDTH 
                        && ground[i].pos_x + ground[i].width >= 0)) 
                ground[i].draw(g);
        }
        for (Enemy enemy : enemies) {
            if (enemy.pos_x <= COURT_WIDTH && enemy.pos_x + enemy.width >=0 && enemy.onScreen) {
                enemy.draw(g);
            }
        }
        for (Coin coin : coins) {
            if (coin.pos_x <= COURT_WIDTH && coin.pos_x + coin.width >= 0) {
                coin.draw(g);
            }
        }
        mario.draw(g);
        
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}
