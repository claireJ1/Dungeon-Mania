package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import dungeonmania.entity.collectableEntity.*;
import dungeonmania.entity.movingEntity.Mercenary;
import dungeonmania.entity.Entity;
import dungeonmania.entity.buildableEntity.*;
import dungeonmania.exceptions.*;
import dungeonmania.response.models.*;
import dungeonmania.util.*;
import dungeonmania.entity.movingEntity.*;

public class InventoryTest {

    private Character p;
    private Dungeon d;
    private Inventory inv;


    @BeforeEach
    public void setup() {
        p = new Character(new Position(1, 1), null);
        d = new Dungeon("1", "1", new ArrayList<Entity>() ,p, null);
        inv = p.getInventory();
        p.setDungeon(d);

        Wood.idNum = 1;
        Key.idNum = 1;
        Arrows.idNum = 1;
        Bomb.idNum = 1;
        Treasure.idNum = 1;
        HealthPotion.idNum = 1;
        InvincibilityPotion.idNum = 1;
        InvisibilityPotion.idNum = 1;

        // add 2 wood, 1 key, 1 arrows, 1 bomb, 1 treasure, 1 HealthPotion,
        // 1 InvincibilityPotion, 1 InvisibilityPotioninto inventory
        List<Position> itemPosition = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Position pos = new Position(i, i);
            itemPosition.add(pos);
        }

        inv.addItem(new Wood(itemPosition.get(0), d));
        inv.addItem(new Wood(itemPosition.get(1), d));
        inv.addItem(new Key(itemPosition.get(2), d, null));
        inv.addItem(new Arrows(itemPosition.get(3), d));
        inv.addItem(new Bomb(itemPosition.get(4), d));
        inv.addItem(new Treasure(itemPosition.get(5), d));
        inv.addItem(new HealthPotion(itemPosition.get(6), d));
        inv.addItem(new InvincibilityPotion(itemPosition.get(7), d));
        inv.addItem(new InvisibilityPotion(itemPosition.get(8), d));
    }

    @Test
    public void testInvalidInputForUseItem() {

        // item is wood, key, arrow, treasure
        assertThrows(IllegalArgumentException.class, () -> inv.useItem("wood_1"));
        assertThrows(IllegalArgumentException.class, () -> inv.useItem("key_1"));
        assertThrows(IllegalArgumentException.class, () -> inv.useItem("arrow_1"));
        assertThrows(IllegalArgumentException.class, () -> inv.useItem("treasure_1"));

        // item is valid but not in the list 
        new Treasure(new Position(4, 5), d);
        assertThrows(InvalidActionException.class, () -> inv.useItem("treasure_2"));
    }

    @Test
    public void testInvalidInputForBuildEntity() {

        // item is not a builable 
        assertThrows(IllegalArgumentException.class, () -> inv.buildEntity("key"));

        // no sufficient arrows to craft bow
        assertFalse(inv.buildables().contains("bow"));
        assertThrows(InvalidActionException.class, () -> inv.buildEntity("bow"));
        
        // craft bow
        inv.addItem(new Arrows(new Position(1, 3), d));
        inv.addItem(new Arrows(new Position(1, 4), d));
        assertDoesNotThrow(() -> inv.buildEntity("bow"));

        // no sufficient wood to crafy shield  
        assertThrows(InvalidActionException.class, () -> inv.buildEntity("shield"));
    }

    @Test
    public void testValidInputForUseItem() {

        assertDoesNotThrow(() -> inv.useItem(null));

        // use a bomb
        assertDoesNotThrow(() -> inv.useItem("bomb_1"));
        assertFalse(inv.itemInInventory().stream().anyMatch(e -> e.equals(new ItemResponse("5", "bomb"))));
        
        // use health potion
        assertDoesNotThrow(() -> inv.useItem("health_potion_1"));
        assertFalse(inv.itemInInventory().stream().anyMatch(e -> e.equals(new ItemResponse("health_potion_1", "health_potion"))));

        // use invincibility potion
        assertDoesNotThrow(() -> inv.useItem("invincibility_potion_1"));
        assertFalse(inv.itemInInventory().stream().anyMatch(e -> e.equals(new ItemResponse("invincibility_potion_1", "invincibility_potion"))));

        // use invisibility potion
        assertDoesNotThrow(() -> inv.useItem("invisibility_potion_1"));
        assertFalse(inv.itemInInventory().stream().anyMatch(e -> e.equals(new ItemResponse("invisibility_potion_1", "invisibility_potion"))));
    }

    @Test
    public void testValidInputForBuildEntity() {

        // build a shield with treasure and check it's equipped by character
        assertTrue(inv.buildables().contains("shield"));
        assertDoesNotThrow(() -> inv.buildEntity("shield"));
        assertFalse(inv.itemInInventory().stream().anyMatch(e -> e.equals(new ItemResponse("treasure_1", "treasure"))));
        assertTrue(inv.itemInInventory().stream().anyMatch(e -> e.getType().equals("shield")));
        assertTrue(p.hasDefense(new Shield(new Position(-1, -1), d)));

        // can't build another shield or bow
        assertTrue(inv.buildables().isEmpty());

        // use key to build shield
        inv.addItem(new Wood(new Position(2, 3), d));
        inv.addItem(new Wood(new Position(1, 3), d));
        assertDoesNotThrow(() -> inv.buildEntity("shield"));
        assertFalse(inv.itemInInventory().stream().anyMatch(e -> e.equals(new ItemResponse("key_1", "key"))));
        
        // build a bow
        inv.addItem(new Wood(new Position(2, 3), d));
        inv.addItem(new Arrows(new Position(1, 3), d));
        inv.addItem(new Arrows(new Position(1, 4), d));
        assertDoesNotThrow(() -> inv.buildEntity("bow"));
        assertTrue(inv.itemInInventory().stream().anyMatch(e -> e.getType().equals("bow")));
        assertTrue(p.hasDefense(new Shield(new Position(-1, -1), d)));
    }

    @Test
    public void testEquipItem() {

        // Check key is equipped by the character and is not removed from invenotry 
        assertTrue(p.getKey() != null);
        assertTrue(inv.itemInInventory().stream().anyMatch(e -> e.getType().equals("key")));

        // Check armour, sword is equipped by the character
        inv.addItem(new Armour(new Position(-1, -1), d));
        assertTrue(inv.itemInInventory().stream().anyMatch(e -> e.getType().equals("armour")));
        assertTrue(p.hasDefense(new Armour(new Position(-1, -1), d)));

        inv.addItem(new Sword(new Position(-1, -1), d));    
        assertTrue(inv.itemInInventory().stream().anyMatch(e -> e.getType().equals("sword")));   
        assertTrue(p.hasWeapon(new Sword(new Position(-1, -1), d)));
    }

    @Test
    public void testUseSpecialItem() {

        // Use key
        assertTrue(inv.checkKeyExists());
        assertTrue(inv.itemInInventory().stream().anyMatch(e -> e.getType().equals("key")));
        p.useKey();
        assertFalse(inv.itemInInventory().stream().anyMatch(e -> e.getType().equals("key")));
        assertFalse(inv.checkKeyExists());

        // Use Treasure
        assertTrue(inv.itemInInventory().stream().anyMatch(e -> e.getType().equals("treasure")));
        assertDoesNotThrow(() -> p.bribeMercenary(null));
        assertThrows(InvalidActionException.class, () -> p.bribeMercenary(null));
    }

    @Test
    public void testBribeMercenary() {

        Mercenary m = new Mercenary(new Position(5, 1), d);
        d.addEntity(m);

        // Test mercenary is not in range
        assertThrows(InvalidActionException.class, () -> m.setToAlly(true));

        // Bribe mercenary
        m.setPosition(new Position(3, 1));
        assertTrue(inv.itemInInventory().stream().anyMatch(e -> e.getType().equals("treasure")));
        assertTrue(p.getAllies().isEmpty());
        assertDoesNotThrow(() -> m.setToAlly(true));

        // set ally as ally
        assertDoesNotThrow(() -> m.setToAlly(true));
        
        // Don't have treasure to bribe
        m.setToAlly(false);
        assertEquals(1, p.getAllies().size());
        assertThrows(InvalidActionException.class, () -> m.setToAlly(true));

    }

    @Test
    public void testBuildSceptre() {


        // no sufficient sunStone to craft bow
        assertFalse(inv.buildables().contains("sceptre"));
        assertThrows(InvalidActionException.class, () -> inv.buildEntity("sceptre"));

        
        inv.addItem(new SunStone(new Position(2, 2), d));
        // build with 1 wood, 1 treasure and 1 sun stone
        assertDoesNotThrow(() -> inv.buildEntity("sceptre"));
        inv.addItem(new SunStone(new Position(2, 2), d));
        // build with 1 wood, 1 key and 1 sun stone
        assertDoesNotThrow(() -> inv.buildEntity("sceptre"));

        assertThrows(InvalidActionException.class, () -> inv.buildEntity("sceptre"));
        
        // no sufficient arrow to craft bow
        inv.addItem(new SunStone(new Position(2, 2), d));
        inv.addItem(new Key(new Position(1, 3), d, null));
        assertThrows(InvalidActionException.class, () -> inv.buildEntity("sceptre"));
        inv.addItem(new Arrows(new Position(1, 3), d));
        inv.addItem(new Arrows(new Position(1, 4), d));
        // build with 2 arrow, 1 key and 1 sun stone
        assertDoesNotThrow(() -> inv.buildEntity("sceptre"));

        // no sufficient wood to craft bow
        inv.addItem(new SunStone(new Position(2, 2), d));
        inv.addItem(new Key(new Position(1, 3), d, null));
        assertThrows(InvalidActionException.class, () -> inv.buildEntity("sceptre"));
        inv.addItem(new Wood(new Position(1, 3), d));
        // build with 1 wood, 1 key and 1 sun stone
        assertDoesNotThrow(() -> inv.buildEntity("sceptre"));

        // no sufficient key to craft bow
        inv.addItem(new SunStone(new Position(2, 2), d));
        inv.addItem(new Wood(new Position(1, 3), d));
        assertThrows(InvalidActionException.class, () -> inv.buildEntity("sceptre"));
        inv.addItem(new Key(new Position(1, 3), d, null));
        // build with 1 wood, 1 key and 1 sun stone
        assertDoesNotThrow(() -> inv.buildEntity("sceptre"));

        // no sufficient treasure to craft bow
        inv.addItem(new SunStone(new Position(2, 2), d));
        inv.addItem(new Arrows(new Position(1, 3), d));
        inv.addItem(new Arrows(new Position(1, 4), d));
        assertThrows(InvalidActionException.class, () -> inv.buildEntity("sceptre"));
        inv.addItem(new Treasure(new Position(1, 3), d));
        // build with 2 arrows, 1 treasure and 1 sun stone
        assertDoesNotThrow(() -> inv.buildEntity("sceptre"));
        
        
    }

    @Test
    public void testBuildMidnightArmour() {

        // no sufficient sunStone and Armour to craft midnight armour
        assertFalse(inv.buildables().contains("midnight_armour"));
        assertThrows(InvalidActionException.class, () -> inv.buildEntity("midnight_armour"));

        // no sufficient Armour to craft midnight armour
        inv.addItem(new SunStone(new Position(2, 2), d));
        inv.addItem(new Armour(new Position(2, 2), d));
        assertThrows(InvalidActionException.class, () -> inv.buildEntity("midnight_armour"));

        // build with 1 armour and 1 sun stone
        inv.addItem(new Armour(new Position(3, 2), d));
        assertDoesNotThrow(() -> inv.buildEntity("midnight_armour"));

        // no sufficient sunStone to craft bow
        inv.addItem(new Armour(new Position(2, 2), d));
        inv.addItem(new Armour(new Position(3, 2), d));
        assertThrows(InvalidActionException.class, () -> inv.buildEntity("midnight_armour"));

        // test build with zombie in the dungeon
        d.addEntity(new ZombieToast(new Position(1, 1, 2), d));
        inv.addItem(new SunStone(new Position(2, 2), d));
        assertThrows(InvalidActionException.class, () -> inv.buildEntity("midnight_armour"));
    }

}

