import java.util.LinkedList;
import java.util.Random;

public class LevelGenerator {
    private GroundTile[] ground; // array of ground tiles
    private LinkedList<Enemy> enemies;
    private LinkedList<Coin> coins;
    private Random r = new Random();
    
    public LevelGenerator(int groundLength, int courtWidth, int courtHeight) {
        // Create the ground array based on the length of the ground
        ground = new GroundTile[groundLength];
        for (int i = 0; i < ground.length; i++) {
            ground[i] = new GroundTile(courtWidth, courtHeight, GroundTile.SIZE * i, 
                    courtHeight - GroundTile.SIZE);
        }
        
        // Randomly place coins in groups of 3 along the screen
        coins = new LinkedList<Coin>();
        int i = 400;
        while (i < groundLength * GroundTile.SIZE - courtWidth) {
            coins.add(new Coin(courtWidth, courtHeight, i));
            coins.add(new Coin(courtWidth, courtHeight, i + 2 * Coin.WIDTH));
            coins.add(new Coin(courtWidth, courtHeight, i + 4 * Coin.WIDTH));
            i += r.nextInt((250 - 100) + 1) + 100;
        }
        
        // Randomly place enemies, first a goomba, then a koopa troop
        enemies = new LinkedList<Enemy>();
        i = 450;
        while (i < groundLength * GroundTile.SIZE - courtWidth) {
            enemies.add(new Goomba(courtWidth, courtHeight, i));
            i += r.nextInt((350 - 200) + 1) + 200;
            enemies.add(new GreenKoopaTroop(courtWidth, courtHeight, i));
            i += r.nextInt((350 - 200 + 1) + 200);
        }
        
    }
    
    public GroundTile[] getGroundTiles() {
        return ground.clone();
    }
    
    public LinkedList<Enemy> getEnemies() {
        LinkedList<Enemy> e = new LinkedList<Enemy>();
        for (Enemy enemy : enemies) {
            e.add(enemy);
        }
        return e;
    }
    
    public LinkedList<Coin> getCoins() {
        LinkedList<Coin> c = new LinkedList<Coin>();
        for (Coin coin : coins) {
            c.add(coin);
        }
        return c;
    }
}
