package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import dungeonmania.entity.Entity;
import dungeonmania.entity.collectableEntity.*;
import dungeonmania.entity.movingEntity.Mercenary;
import dungeonmania.entity.staticEntity.Boulder;
import dungeonmania.entity.staticEntity.Door;
import dungeonmania.entity.staticEntity.FloorSwitch;
import dungeonmania.entity.staticEntity.StaticBomb;
import dungeonmania.entity.staticEntity.Wall;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class CollectableEntityTest {
    Dungeon d;
    Character c;
    @BeforeEach
    public void setUp() {
        assertDoesNotThrow(() -> d = new Dungeon("advanced"));
        c = d.getCharacter();
    }
    @Test
    public void testHealthPotion() {
        Inventory inv = c.getInventory();
        HealthPotion hP = new HealthPotion(new Position(3,3), d);
        inv.addItem(hP);
        assertEquals(c.getHealthPoint(), c.getFullHealthPoint());
        c.setHealthPoint(10);
        assertEquals(c.getHealthPoint(), 10);
        assertDoesNotThrow(() -> inv.useItem(hP.getId()));
        assertEquals(c.getHealthPoint(), c.getFullHealthPoint());
    }
    @Test
    public void testInvincibilityPotion() {
        Inventory inv = c.getInventory();
        InvincibilityPotion invincibilityP = new InvincibilityPotion(new Position(3,3), d);
        inv.addItem(invincibilityP);
        assertEquals(0, c.getInvincible());
        assertDoesNotThrow(() -> inv.useItem(invincibilityP.getId()));
        assertEquals(10, c.getInvincible());
    }
    @Test
    public void testInvisibilityPotion() {
        Inventory inv = c.getInventory();
        InvisibilityPotion invisibilityP = new InvisibilityPotion(new Position(3,3), d);
        inv.addItem(invisibilityP);
        assertEquals(0, c.getInvisible());
        assertDoesNotThrow(() -> inv.useItem(invisibilityP.getId()));
        assertEquals(10, c.getInvisible());
    }
    @Test
    public void testBomb() {
        Character c2 = new Character(null);
        Dungeon d2 = new Dungeon("gg", "GG", new ArrayList<Entity>(), c2, null);
        c2.setDungeon(d2);
        Inventory inv = c2.getInventory();
        Bomb bomb = new Bomb(new Position(3,3), d2);
        inv.addItem(bomb);
        c2.setPosition(new Position(3,3));
        assertDoesNotThrow(() -> inv.useItem(bomb.getId()));
        // character move away
        c2.setPosition(new Position(3,7));
        assertTrue(d2.getPositionEntity(new Position(3, 3)).get(d2.getPositionEntity(new Position(3, 3)).size() - 1) instanceof StaticBomb);
        d2.addEntity(new Wall(new Position(2, 2), d2));
        d2.addEntity(new Wall(new Position(2, 3), d2));
        d2.addEntity(new Wall(new Position(2, 4), d2));
        d2.addEntity(new Wall(new Position(3, 2), d2));
        d2.addEntity(new Wall(new Position(3, 4), d2));
        d2.addEntity(new Wall(new Position(4, 2), d2));
        d2.addEntity(new Wall(new Position(4, 4), d2));
        d2.addEntity(new FloorSwitch(new Position(4, 3), d2));
        d2.addEntity(new Boulder(new Position(4, 3), d2));
        assertTrue(((FloorSwitch)d2.getPositionEntity(new Position(4, 3)).get(d2.getPositionEntity(new Position(4, 3)).size() - 2)).getIsTurnOn());
        //c2.move(dungeonmania.util.Direction.DOWN);
        ((StaticBomb)d2.getPositionEntity(new Position(3, 3)).get(d2.getPositionEntity(new Position(3, 3)).size() - 1)).checkStaticBomb();
        assertEquals(new ArrayList<>(), d2.getPositionEntity(new Position(2, 2)));
        assertEquals(new ArrayList<>(), d2.getPositionEntity(new Position(2, 3)));
        assertEquals(new ArrayList<>(), d2.getPositionEntity(new Position(2, 4)));
        assertEquals(new ArrayList<>(), d2.getPositionEntity(new Position(3, 2)));
        assertEquals(new ArrayList<>(), d2.getPositionEntity(new Position(3, 3)));
        assertEquals(new ArrayList<>(), d2.getPositionEntity(new Position(3, 4)));
        assertEquals(new ArrayList<>(), d2.getPositionEntity(new Position(4, 2)));
        assertEquals(new ArrayList<>(), d2.getPositionEntity(new Position(4, 3)));
        assertEquals(new ArrayList<>(), d2.getPositionEntity(new Position(4, 4)));
    }
    @Test
    public void testKey() {
        Inventory inv = c.getInventory();
        Key key = new Key(new Position(3,3), d, "group-1");
        inv.addItem(key);
        Door door = new Door(new Position(3,4), d, "group-1");
        key.setDoor(door);
        door.setKey(key);
        assertEquals(key.getGroup(), door.getGroup());
        assertFalse(door.getIsOpen());
        key.openDoor(door);
        assertTrue(door.getIsOpen());
    }
    
    @Test
    public void testbribeMercenary() {
        Inventory inv = c.getInventory();
        Treasure treasure = new Treasure(new Position(5, 4), d);
        inv.addItem(treasure);
        
        Mercenary m = new Mercenary(new Position(16, 14), d);
        assertFalse(m.getIsAlly());
        assertThrows(InvalidActionException.class, () -> {m.setToAlly(true);});
        c.setPosition(new Position(15, 14));
        assertDoesNotThrow(() -> {m.setToAlly(true);});
        assertTrue(m.getIsAlly());
        assertThrows(InvalidActionException.class, () -> {inv.useTreasure();});
    }

    @Test
    public void testSunStonebribeMercenary() {
        Inventory inv = c.getInventory();
        SunStone sunStone = new SunStone(new Position(5, 4), d);
        inv.addItem(sunStone);
        
        Mercenary m = new Mercenary(new Position(6, 4), d);
        assertFalse(m.getIsAlly());
        assertThrows(InvalidActionException.class, () -> {m.setToAlly(true);});
        c.setPosition(new Position(6, 5));
        assertDoesNotThrow(() -> {m.setToAlly(true);});
        assertTrue(m.getIsAlly());
        CollectableEntity s =  inv.getItems().stream().filter(e -> e.getType().equals("sun_stone"))
                                                                    .findAny()
                                                                    .orElse(null);
        assertTrue(s != null);
    }

    @Test
    public void testSunStoneOpenDoor() {
        Character c2 = new Character(null);
        Dungeon d2 = new Dungeon("gg", "GG", new ArrayList<Entity>(), c2, null);
        c2.setDungeon(d2);
        Inventory inv = c2.getInventory();
        SunStone sunStone = new SunStone(new Position(5, 4), d2);
        inv.addItem(sunStone);
        
        c2.setPosition(new Position(3,3));
        d2.addEntity(new Door(new Position(3, 2), d2, "group_1"));
        assertFalse(((Door)d2.getPositionEntity(new Position(3, 2)).get(d2.getPositionEntity(new Position(3, 2)).size() - 1)).getIsOpen());
        c2.move(Direction.UP);
        assertTrue(((Door)d2.getPositionEntity(new Position(3, 2)).get(d2.getPositionEntity(new Position(3, 2)).size() - 1)).getIsOpen());
        CollectableEntity s =  inv.getItems().stream().filter(e -> e.getType().equals("sun_stone"))
                                                                    .findAny()
                                                                    .orElse(null);
        assertTrue(s != null);
    }
}
