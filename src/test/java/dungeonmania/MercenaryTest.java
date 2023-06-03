package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.entity.collectableEntity.*;
import dungeonmania.entity.movingEntity.Mercenary;
import dungeonmania.util.Position;

public class MercenaryTest {
    
    Dungeon d;
    Character c;
    Mercenary m;

    @BeforeEach
    public void setUp() {
        assertDoesNotThrow(() -> d = new Dungeon("maze"));
        c = d.getCharacter();
        m = new Mercenary(new Position(6, 4), d);
        d.addEntity(m);
    }

    @Test
    public void testMercenaryNormalMotion() {

        c = d.getCharacter();
        m = new Mercenary(new Position(6, 4), d);
        d.addEntity(m);

        // Test mercenary is walking towards character 
        for (int i = 0; i < 30; i++) { 
            m.move(); 
        }

        assertTrue(m.getPosition().equals(c.getPosition()));;
    }

    @Test
    public void testMercenaryInvincibleMotion() {

        // Test Mercenary will never reach the player
        c.setPosition(new Position(6, 10));
        c.setInvincible(30);
        for (int i = 0; i < 30; i++) {
            m.move();
            assertFalse(m.getPosition().equals(c.getPosition()));
        }

        // Test Mercenary is moving 
        c.setPosition(new Position(6, 9));
        Position prev = m.getPosition();
        m.move();
        assertFalse(m.getPosition().equals(prev));
    }

    @Test
    public void testMercenaryInvisibleMotion() {

        // Test Mercenary will not move
        c.setInvisible(30);
        c.setPosition(new Position(6, 10));
        for (int i = 0; i < 30; i++) {
            m.move();
            assertTrue(m.getPosition().equals(new Position(6, 4)));
        }
    }

    @Test
    public void testMercenaryAllyMotion() {

        // Test mercenary will follow player 
        for (int i = 0; i < 30; i++) { 
            m.move(); 
        }

        assertTrue(m.getPosition().equals(c.getPosition()));

        for (int i = 0; i < 30; i++) { 
            m.move(); 
            assertTrue(m.getPosition().equals(c.getPosition()));
        }
    }

    @Test 
    public void testDungeonWithBarrier() {
        d = new Dungeon("mercenaryTest");
        m = new Mercenary(new Position(1, 4), d);
        c = d.getCharacter();
        d.addEntity(m);

        // Test mercenary walk through the portal
        m.move(); 
        assertTrue(m.getPosition().equals(new Position(2, 2)));

        // Test mercenary walk towards character
        for (int i = 0; i < 5; i++) { 
            m.move(); 
        }

        assertTrue(m.getPosition().equals(c.getPosition()));
    }

    @Test
    public void testUseSceptre() {

        m.setPosition(new Position(1, 1));
        // Bribe mercenary with sceptre
        c.addItem(new SunStone(new Position(2, 2), d));
        c.addItem(new Wood(new Position(-1, -1), d));
        c.addItem(new Treasure(new Position(-1, -1), d));

        // build with 1 wood, 1 treasure and 1 sun stone
        assertDoesNotThrow(() -> c.build("sceptre"));
        assertDoesNotThrow(() -> m.setToAlly(true));
        assertEquals(true, m.getIsAlly());

        for (int i = 0; i < 11; i++) {
            m.move();
        }

        assertEquals(false, m.getIsAlly());        
    }

}
