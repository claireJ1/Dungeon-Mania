package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.entity.collectableEntity.SunStone;
import dungeonmania.entity.collectableEntity.TheOneRing;
import dungeonmania.entity.collectableEntity.Treasure;
import dungeonmania.entity.collectableEntity.Wood;
import dungeonmania.entity.movingEntity.Assassin;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;

public class AssassinTest {
    Dungeon d;
    Character c;
    Inventory i;
    Assassin a;
    
    @BeforeEach
    public void setUp() {
        assertDoesNotThrow(() -> d = new Dungeon("maze"));
        c = d.getCharacter();
        i = c.getInventory();
        a = new Assassin(new Position(6, 4), d);
        d.addEntity(a);
    }

    @Test
    public void testNormalBribe() {
        a.setPosition(new Position(1, 1));

        // Test bribe with no treasure or sun stone, one ring or sceptre
        assertThrows(InvalidActionException.class, () -> a.setToAlly(true));

        // test bribe with no ring 
        i.addItem(new Treasure(new Position(-1, -1), d));
        assertThrows(InvalidActionException.class, () -> a.setToAlly(true));

        // Test bribe with treasures and one ring
        i.addItem(new TheOneRing(new Position(-1, -1), d));

        assertDoesNotThrow(() -> a.setToAlly(true));
        assertFalse(i.itemInInventory().stream().anyMatch(e -> e.getType().equals("treasure")));
        assertFalse(i.itemInInventory().stream().anyMatch(e -> e.getType().equals("one_ring")));
    }

    @Test
    public void testNormalBribeWithSunstone() {
        a.setPosition(new Position(1, 1));

        // Test bribe with no treasure or sun stone, one ring or sceptre
        assertThrows(InvalidActionException.class, () -> a.setToAlly(true));

        // test bribe with ring but no treasure and sunstone 
        i.addItem(new TheOneRing(new Position(-1, -1), d));
        assertThrows(InvalidActionException.class, () -> a.setToAlly(true));

        // Test bribe with treasures and one ring
        i.addItem(new SunStone(new Position(-1, -1), d));

        assertDoesNotThrow(() -> a.setToAlly(true));
        assertTrue(i.itemInInventory().stream().anyMatch(e -> e.getType().equals("sun_stone")));
        assertFalse(i.itemInInventory().stream().anyMatch(e -> e.getType().equals("one_ring")));
    }

    @Test
    public void testNormalBribeWithSunstoneBeforeTreasure() {
        a.setPosition(new Position(1, 1));

        // test bribe with ring, treasure and sunstone all in inventory
        i.addItem(new TheOneRing(new Position(-1, -1), d));
        i.addItem(new SunStone(new Position(-1, -1), d));
        i.addItem(new Treasure(new Position(-1, -1), d));
        assertDoesNotThrow(() -> a.setToAlly(true));

        // Test bribe with treasures and one ring
        assertDoesNotThrow(() -> a.setToAlly(true));
        assertTrue(i.itemInInventory().stream().anyMatch(e -> e.getType().equals("sun_stone")));
        assertTrue(i.itemInInventory().stream().anyMatch(e -> e.getType().equals("treasure")));
        assertFalse(i.itemInInventory().stream().anyMatch(e -> e.getType().equals("one_ring")));
    }


    @Test
    public void testBribeWithSceptre() {
        a.setPosition(new Position(1, 1));

        // Test bribe with sceptre
        i.addItem(new Wood(new Position(-1, -1), d));
        i.addItem(new Treasure(new Position(-1, -1), d));
        i.addItem(new SunStone(new Position(-1, -1), d));
        i.addItem(new Treasure(new Position(-1, -1), d));
        i.addItem(new TheOneRing(new Position(-1, -1), d));

        i.buildEntity("sceptre");
        assertDoesNotThrow(() -> a.setToAlly(true));
        assertTrue(i.itemInInventory().stream().anyMatch(e -> e.getType().equals("one_ring")));

        for (int i = 0; i < 10; i++) {
            a.move();
        }

        // Test ally status duration 
        a.move();
        assertEquals(false, a.getIsAlly());
    }
    
    @Test
    public void testNormalMove() {
        Dungeon d = new Dungeon("maze");
        c = d.getCharacter();
        a = new Assassin(new Position(6, 4), d);
        d.addEntity(a);

        // Test mercenary is walking towards character 
        for (int i = 0; i < 30; i++) { 
            a.move(); 
        }

        assertTrue(a.getPosition().equals(c.getPosition()));;
    }

}
