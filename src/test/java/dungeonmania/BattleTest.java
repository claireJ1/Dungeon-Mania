package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import dungeonmania.entity.collectableEntity.*;
import dungeonmania.entity.movingEntity.Assassin;
import dungeonmania.entity.movingEntity.Hydra;
import dungeonmania.entity.movingEntity.Mercenary;
import dungeonmania.entity.movingEntity.Spider;
import dungeonmania.entity.movingEntity.ZombieToast;
import dungeonmania.entity.Entity;
import dungeonmania.entity.buildableEntity.MidnightArmour;
import dungeonmania.util.*;

public class BattleTest {

    @BeforeEach
    public void setup() {
        Spider.attackDamage = 1;
        Mercenary.attackDamage = 3;
        ZombieToast.attackDamage = 2;
        Assassin.attackDamage = 5;
        Hydra.attackDamage = 3;
        Character healthC = new Character(null);
        healthC.setFullHealthPoint(20);
    }
    
    @Test
    public void testNormalBattleWithMercenary() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        Mercenary m = new Mercenary(new Position(1, 1, 2), d);
        d.addEntity(m);
        if (m.getArmour() == null) {
            c.battle(m);
            assertEquals(12, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
        } else {
            c.battle(m);
            assertEquals(5, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
            assertTrue(c.hasDefense(new Armour(new Position(-1, -1), d)));
        }
    }

    @Test
    public void testNormalBattleWithSpider() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        Spider s = new Spider(new Position(1, 1, 2), d);
        d.addEntity(s);

        c.battle(s);
        assertEquals(19, c.getHealthPoint());
        assertTrue(d.getEntities().isEmpty());
    }

    @Test
    public void testNormalBattleWithZombieToast() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        ZombieToast z = new ZombieToast(new Position(1, 1, 2), d);
        d.addEntity(z);

        if (z.getArmour() == null) {
            c.battle(z);
            assertEquals(16, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
        } else {
            c.battle(z);
            assertEquals(14, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
            assertTrue(c.hasDefense(new Armour(new Position(-1, -1), d)));
        }
    }

    @Test
    public void testCharacterDieWithoutOneRing() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);

        Mercenary m1 = new Mercenary(new Position(1, 1, 2), d);
        d.addEntity(m1);
        if (m1.getArmour() == null) {
            c.battle(m1);
            assertEquals(12, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
        } else {
            c.battle(m1);
            assertEquals(5, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
            assertTrue(c.hasDefense(new Armour(new Position(-1, -1), d)));
        }

        CollectableEntity oneRing = c.getInventory().getItems().stream().filter(e -> e.getType().equals("one_ring"))
                                               .findAny()
                                               .orElse(null);

        Mercenary m2 = new Mercenary(new Position(1, 1, 2), d);
        d.addEntity(m2);
        if (oneRing == null) {
            c.battle(m2);
            assertEquals(null, d.getCharacter());
        }
    }

    @Test
    public void testCharacterDieWithOneRing() {
        Dungeon d2 = new Dungeon("advanced");
        Character c2 = d2.getCharacter();

        Inventory inv = c2.getInventory();
        TheOneRing oneRing = new TheOneRing(new Position(3,3,1), d2);
        inv.addItem(oneRing);

        Mercenary m1 = new Mercenary(new Position(1, 1, 2), d2);
        d2.addEntity(m1);
        if (m1.getArmour() == null) {
            c2.battle(m1);
            assertEquals(12, c2.getHealthPoint());
        } else {
            c2.battle(m1);
            assertEquals(5, c2.getHealthPoint());
            assertTrue(c2.hasDefense(new Armour(new Position(-1, -1), d2)));
        }

        Mercenary m2 = new Mercenary(new Position(1, 1, 2), d2);
        d2.addEntity(m2);
        c2.battle(m2);
        assertEquals(20, d2.getCharacter().getHealthPoint());
    }

    @Test
    public void testCharacterInvincible() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        
        // Use invincibilityPosition
        Inventory inv = c.getInventory();
        InvincibilityPotion invincibilityP = new InvincibilityPotion(new Position(3,3,1), d);
        inv.addItem(invincibilityP);
        assertDoesNotThrow(() -> inv.useItem(invincibilityP.getId()));

        Mercenary m = new Mercenary(new Position(1, 1, 2), d);
        d.addEntity(m);
        c.battle(m);
        assertEquals(20, c.getHealthPoint());
        assertTrue(d.getEntities().isEmpty());
    }
    
    @Test
    public void testCharacterWithArmour() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        
        // Carry with Armour
        Inventory inv = c.getInventory();
        Armour armour = new Armour(new Position(3,3,1), d);
        inv.addItem(armour);

        Mercenary m = new Mercenary(new Position(1, 1, 2), d);
        d.addEntity(m);
        if (m.getArmour() == null) {
            c.battle(m);
            assertEquals(16, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
        } else {
            c.battle(m);
            assertEquals(12, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
            assertTrue(c.hasDefense(new Armour(new Position(-1, -1), d)));
        }
    }

    @Test
    public void testCharacterWithShield() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        
        // Carry with shield
        Inventory inv = c.getInventory();
        inv.addItem(new Wood(new Position(2, 3), d));
        inv.addItem(new Wood(new Position(1, 3), d));
        inv.addItem(new Treasure(new Position(3, 3), d));
        assertDoesNotThrow(() -> inv.buildEntity("shield"));

        Mercenary m = new Mercenary(new Position(1, 1, 2), d);
        d.addEntity(m);
        if (m.getArmour() == null) {
            c.battle(m);
            assertEquals(17, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
        } else {
            c.battle(m);
            assertEquals(14, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
            assertTrue(c.hasDefense(new Armour(new Position(-1, -1), d)));
        }
    }

    @Test
    public void testCharacterWithArmourAndShield() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        
        // Carry with armour and shield
        Inventory inv = c.getInventory();
        Armour armour = new Armour(new Position(3,3,1), d);
        inv.addItem(armour);
        inv.addItem(new Wood(new Position(2, 3), d));
        inv.addItem(new Wood(new Position(1, 3), d));
        inv.addItem(new Treasure(new Position(3, 3), d));
        assertDoesNotThrow(() -> inv.buildEntity("shield"));

        Mercenary m = new Mercenary(new Position(1, 1, 2), d);
        d.addEntity(m);
        if (m.getArmour() == null) {
            c.battle(m);
            assertEquals(18, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
        } else {
            c.battle(m);
            assertEquals(16, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
            assertTrue(c.hasDefense(new Armour(new Position(-1, -1), d)));
        }
    }

    @Test
    public void testCharacterWithSword() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        
        // Carry with Sword
        Inventory inv = c.getInventory();
        Sword sword = new Sword(new Position(3,3,1), d);
        inv.addItem(sword);

        Mercenary m = new Mercenary(new Position(1, 1, 2), d);
        d.addEntity(m);
        if (m.getArmour() == null) {
            c.battle(m);
            assertEquals(14, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
        } else {
            c.battle(m);
            assertEquals(11, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
            assertTrue(c.hasDefense(new Armour(new Position(-1, -1), d)));
        }
    }

    @Test
    public void testCharacterWithBow() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        
        // Carry with bow
        Inventory inv = c.getInventory();
        inv.addItem(new Wood(new Position(2, 3), d));
        inv.addItem(new Arrows(new Position(1, 3), d));
        inv.addItem(new Arrows(new Position(1, 4), d));
        inv.addItem(new Arrows(new Position(1, 5), d));
        assertDoesNotThrow(() -> inv.buildEntity("bow"));

        Mercenary m = new Mercenary(new Position(1, 1, 2), d);
        d.addEntity(m);
        if (m.getArmour() == null) {
            c.battle(m);
            assertEquals(15, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
        } else {
            c.battle(m);
            assertEquals(12, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
            assertTrue(c.hasDefense(new Armour(new Position(-1, -1), d)));
        }
    }

    @Test
    public void testCharacterWithSwordAndBow() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        
        // Carry with Sword and bow
        Inventory inv = c.getInventory();
        Sword sword = new Sword(new Position(3,3,1), d);
        inv.addItem(sword);
        inv.addItem(new Wood(new Position(2, 3), d));
        inv.addItem(new Arrows(new Position(1, 3), d));
        inv.addItem(new Arrows(new Position(1, 4), d));
        inv.addItem(new Arrows(new Position(1, 5), d));
        assertDoesNotThrow(() -> inv.buildEntity("bow"));

        Mercenary m = new Mercenary(new Position(1, 1, 2), d);
        d.addEntity(m);
        if (m.getArmour() == null) {
            c.battle(m);
            assertEquals(15, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
        } else {
            c.battle(m);
            assertEquals(14, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
            assertTrue(c.hasDefense(new Armour(new Position(-1, -1), d)));
        }
    }

    @Test
    public void testCharacterWithAlly() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);

        Mercenary m = new Mercenary(new Position(1, 1, 2), d);
        Mercenary a = new Mercenary(new Position(1, 3, 2), d);
        c.addAlly(a);
        d.addEntity(m);
        d.addEntity(a);
        if (m.getArmour() == null) {
            c.battle(m);
            assertEquals(15, c.getHealthPoint());
            assertTrue(d.getEntities().contains(a));
            assertTrue(!d.getEntities().contains(m));
        } else {
            c.battle(m);
            assertEquals(13, c.getHealthPoint());
            assertTrue(d.getEntities().contains(a));
            assertTrue(!d.getEntities().contains(m));
            assertTrue(c.hasDefense(new Armour(new Position(-1, -1), d)));
        }
    }

    @Test
    public void testBattleWithAssassin() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        Assassin assa = new Assassin(new Position(1, 1, 2), d);
        d.addEntity(assa);
        if (assa.getArmour() == null) {
            c.battle(assa);
            assertEquals(6, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
        } else {
            c.battle(assa);
            assertEquals(null, d.getCharacter());
            assertTrue(!d.getEntities().isEmpty());
        }
    }

    @Test
    public void testBattleWithHydra() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        Hydra hydra = new Hydra(new Position(1, 1, 1), d);
        d.addEntity(hydra);
        c.battle(hydra);
        assertEquals(null, d.getCharacter());
        assertTrue(!d.getEntities().isEmpty());
        assertEquals(19, hydra.getHealthPoint());
    }

    @Test
    public void testCharacterWithMidnightArmour() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        
        // equip midnight armour
        Inventory inv = c.getInventory();
        inv.addItem(new Armour(new Position(-1, -1), d));
        inv.addItem(new Armour(new Position(-1, -1), d));
        inv.addItem(new SunStone(new Position(-1, -1), d));
        assertDoesNotThrow(() -> inv.buildEntity("midnight_armour"));

        Mercenary m = new Mercenary(new Position(1, 1, 2), d);
        d.addEntity(m);
        if (m.getArmour() == null) {
            c.battle(m);
            assertEquals(17, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
            assertTrue(c.hasDefense(new MidnightArmour(new Position(-1, -1), d)));
            assertTrue(c.hasWeapon(new MidnightArmour(new Position(-1, -1), d)));
        } else {
            c.battle(m);
            assertEquals(16, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
            assertTrue(!c.hasDefense(new MidnightArmour(new Position(-1, -1), d)));
            assertTrue(!c.hasWeapon(new MidnightArmour(new Position(-1, -1), d)));
        }
    }

    @Test
    public void testCharacterWithAndurilNotBoss() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        
        // equip midnight armour
        Inventory inv = c.getInventory();
        inv.addItem(new Anduril(new Position(-1, -1), d));

        Mercenary m = new Mercenary(new Position(1, 1, 2), d);
        d.addEntity(m);
        if (m.getArmour() == null) {
            c.battle(m);
            assertEquals(12, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
        } else {
            c.battle(m);
            assertEquals(5, c.getHealthPoint());
            assertTrue(d.getEntities().isEmpty());
            assertTrue(c.hasDefense(new Armour(new Position(-1, -1), d)));
        }
    }

    @Test
    public void testCharacterWithAndurilBoss() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        c.setHealthPoint(10);
        
        // equip midnight armour
        Inventory inv = c.getInventory();
        inv.addItem(new Anduril(new Position(-1, -1), d));
        Hydra hydra = new Hydra(new Position(1, 1, 2), d);
        d.addEntity(hydra);

        c.battle(hydra);
        assertEquals(3, c.getHealthPoint());
        assertTrue(d.getEntities().isEmpty());
        assertTrue(c.hasWeapon(new Anduril(new Position(-1, -1), d)));
    }

    @Test
    public void testCharacterInvincibleWithSpider() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        
        // Use invincibilityPosition
        Inventory inv = c.getInventory();
        InvincibilityPotion invincibilityP = new InvincibilityPotion(new Position(3,3,1), d);
        inv.addItem(invincibilityP);
        assertDoesNotThrow(() -> inv.useItem(invincibilityP.getId()));

        Spider s = new Spider(new Position(1, 1, 2), d);
        d.addEntity(s);
        c.battle(s);
        assertEquals(20, c.getHealthPoint());
        assertTrue(d.getEntities().isEmpty());
    }

    @Test
    public void testCharacterInvincibleWithZombie() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);
        
        // Use invincibilityPosition
        Inventory inv = c.getInventory();
        InvincibilityPotion invincibilityP = new InvincibilityPotion(new Position(3,3,1), d);
        inv.addItem(invincibilityP);
        assertDoesNotThrow(() -> inv.useItem(invincibilityP.getId()));

        ZombieToast z = new ZombieToast(new Position(1, 1, 2), d);
        d.addEntity(z);
        c.battle(z);
        assertEquals(20, c.getHealthPoint());
        assertTrue(d.getEntities().isEmpty());
    }

    @Test
    public void testCharacterWithAssassinAsAlly() {
        List<Entity> entities = new ArrayList<>();
        Character c = new Character(new Position(1, 1, 1), null);
        Dungeon d = new Dungeon("1", "1", entities, c, null);
        c.setDungeon(d);

        Mercenary m = new Mercenary(new Position(1, 1, 2), d);
        Assassin a = new Assassin(new Position(1, 3, 2), d);
        c.addAlly(a);
        d.addEntity(m);
        d.addEntity(a);
        if (m.getArmour() == null) {
            c.battle(m);
            assertEquals(15, c.getHealthPoint());
            assertTrue(d.getEntities().contains(a));
            assertTrue(!d.getEntities().contains(m));
        } else {
            c.battle(m);
            assertEquals(14, c.getHealthPoint());
            assertTrue(d.getEntities().contains(a));
            assertTrue(!d.getEntities().contains(m));
            assertTrue(c.hasDefense(new Armour(new Position(-1, -1), d)));
        }
    }
}