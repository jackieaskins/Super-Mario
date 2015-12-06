import static org.junit.Assert.*;
import org.junit.Test;

public class CollisionTest {
    
    @Test
    public void testCollidesTop() {
        Mario mario = new Mario(500, 500);
        mario.pos_y = 0;
        
        Goomba goomba = new Goomba(500, 500, 0);
        goomba.pos_x = mario.width;
        goomba.pos_y = mario.height - 1;
        
        // Goomba & Mario shouldn't collide top to bottom if
        // mario.pos_y + mario.height > goomba.pos_y
        for (mario.pos_x = 0; mario.pos_x <= goomba.pos_x + goomba.width; mario.pos_x++) {
            assertFalse(goomba.collidesTop(mario) && mario.collidesBottom(goomba));
            assertTrue(goomba.intersectsTop(mario) && mario.intersectsBottom(goomba));
        }
        
        goomba.pos_y++;
        
        // Goomba & Mario should collide top to bottom if
        // mario.pos_y + mario.height == goomba.pos_y
        for (mario.pos_x = 0; mario.pos_x <= goomba.pos_x + goomba.width; mario.pos_x++) {
            assertTrue(goomba.collidesTop(mario) && mario.collidesBottom(goomba));
            assertTrue(goomba.intersectsTop(mario) && mario.intersectsBottom(goomba));
        }
        
        goomba.pos_y++;
        
        // Goomba & Mario should collide top to bottom if
        // mario.pos_y += mario.height < goomba.pos_y
        for (mario.pos_x = 0; mario.pos_x <= goomba.pos_x + goomba.width; mario.pos_x++) {
            assertFalse(goomba.collidesTop(mario) && mario.collidesBottom(goomba));
            assertFalse(goomba.intersectsTop(mario) && mario.intersectsBottom(goomba));
        }
    }
    
    @Test
    public void testCollidesLeft() {
        Mario mario = new Mario(500, 500);
        mario.pos_y = 0;
        
        Goomba goomba = new Goomba(500, 500, 0);
        goomba.pos_x = mario.width - 1;
        goomba.pos_y = mario.height;
        
        // Goomba & Mario shouldn't collide left to right if
        // mario.pos_x + mario.width > goomba.pos_x
        for (mario.pos_y = 0; mario.pos_y <= goomba.pos_y + goomba.height; mario.pos_y++) {
            assertFalse(goomba.collidesLeft(mario) && mario.collidesRight(goomba));
            assertTrue(goomba.intersectsLeft(mario) && mario.intersectsRight(goomba));
        }
        
        goomba.pos_x++;
        
        // Goomba & Mario should collide left to right if
        // mario.pos_x + mario.width == goomba.pos_x
        for (mario.pos_y = 0; mario.pos_y <= goomba.pos_y + goomba.height; mario.pos_y++) {
            assertTrue(goomba.collidesLeft(mario) && mario.collidesRight(goomba));
            assertTrue(goomba.intersectsLeft(mario) && mario.intersectsRight(goomba));
        }
        
        goomba.pos_x++;
        
        // Goomba & Mario shouldn't collide left to right if
        // mario.pos_x + mario.width < goomba.pos_x
        for (mario.pos_y = 0; mario.pos_y <= goomba.pos_y + goomba.height; mario.pos_y++) {
            assertFalse(goomba.collidesLeft(mario) && mario.collidesRight(goomba));
            assertFalse(goomba.intersectsLeft(mario) && mario.intersectsRight(goomba));
        }
    }
    
    @Test
    public void testCollidesBottom() {
        Goomba goomba = new Goomba(500, 500, 0);
        goomba.pos_x = 0;
        goomba.pos_y = 0;

        Mario mario = new Mario(500, 500);
        mario.pos_x = goomba.width;
        mario.pos_y = goomba.height - 1;
        
        // Goomba & Mario shouldn't collide bottom to top if
        // goomba.pos_y + goomba.height > mario.pos_y
        for (goomba.pos_x = 0; goomba.pos_x <= mario.pos_x + mario.width; goomba.pos_x++) {
            assertFalse(goomba.collidesBottom(mario) && mario.collidesTop(goomba));
            assertTrue(goomba.intersectsBottom(mario) && mario.intersectsTop(goomba));
        }
        
        mario.pos_y++;
        
        // Goomba & Mario should collide bottom to top if
        // goomba.pos_y + goomba.height == mario.pos_y
        for (goomba.pos_x = 0; goomba.pos_x <= mario.pos_x + mario.width; goomba.pos_x++) {
            assertTrue(goomba.collidesBottom(mario) && mario.collidesTop(goomba));
            assertTrue(goomba.intersectsBottom(mario) && mario.intersectsTop(goomba));
        }
        
        mario.pos_y++;
        
        // Goomba & Mario should collide bottom to top if
        // goomba.pos_y += goomba.height < mario.pos_y
        for (goomba.pos_x = 0; goomba.pos_x <= mario.pos_x + mario.width; goomba.pos_x++) {
            assertFalse(goomba.collidesBottom(mario) && mario.collidesTop(goomba));
            assertFalse(goomba.intersectsBottom(mario) && mario.intersectsTop(goomba));
        }   
    }
    
    @Test
    public void testCollidesRight() {
        Goomba goomba = new Goomba(500, 500, 0);
        goomba.pos_x = 0;
        goomba.pos_y = 0;

        Mario mario = new Mario(500, 500);
        mario.pos_x = goomba.width - 1;
        mario.pos_y = goomba.height;
        
        // Goomba & Mario shouldn't collide right to left if
        // goomba.pos_x + goomba.width > mario.pos_x
        for (goomba.pos_y = 0; goomba.pos_y <= mario.pos_y + mario.height; goomba.pos_y++) {
            assertFalse(goomba.collidesRight(mario) && mario.collidesLeft(goomba));
            assertTrue(goomba.intersectsRight(mario) && mario.intersectsLeft(goomba));
        }
        
        mario.pos_x++;
        
        // Goomba & Mario should collide right to left it
        // goomba.pos_x + goomba.width == mario.pos_x
        for (goomba.pos_y = 0; goomba.pos_y <= mario.pos_y + mario.height; goomba.pos_y++) {
            assertTrue(goomba.collidesRight(mario) && mario.collidesLeft(goomba));
            assertTrue(goomba.intersectsRight(mario) && mario.intersectsLeft(goomba));
        }
        
        mario.pos_x++;
        
        // Goomba & Mario should collide right to left if
        // goomba.pos_x += goomba.width < mario.pos_x
        for (goomba.pos_y = 0; goomba.pos_y <= mario.pos_y + mario.height; goomba.pos_y++) {
            assertFalse(goomba.collidesRight(mario) && mario.collidesLeft(goomba));
            assertFalse(goomba.intersectsRight(mario) && mario.intersectsLeft(goomba));
        }   
    }
}
