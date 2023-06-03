package dungeonmania;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.*;
import dungeonmania.util.*;

public class TestMovinfEntity {

    @BeforeEach
    public void setup() {
        Character c = new Character(null);
        c.setFullHealthPoint(20);
    }
    
    /* M23 External Tests */
    @Test
    public void ZombieWithArmour() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("zombieCorridor", "peaceful");
        assertNotNull(game);

        assertFalse(TestHelper.hasItemByType(resD, "armour"));
        boolean hasArmour = false;
        for (int i = 0; i < 2; i++) resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.DOWN);
        for (int i = 0; i < 2; i++) resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.UP);
        for (int i = 0; i < 2; i++) resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.DOWN);
        for (int i = 0; i < 2; i++) resD = game.tick(null, Direction.LEFT);
        for (int i = 0; i < 2; i++) resD = game.tick(null, Direction.RIGHT);
        for (int i = 0; i < 200; i++) {
            resD = game.tick(null, Direction.NONE);
            if (TestHelper.hasItemByType(resD, "armour")) { hasArmour = true; break; }
        }
        // Pick your SSR armour
        assertTrue(hasArmour);
    }

    @Test
    public void MercenaryWithArmour() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("30Enemies", "peaceful");
        assertNotNull(game);

        assertFalse(TestHelper.hasItemByType(resD, "armour"));
        boolean hasArmour = false;
        for (int i = 0; i < 32; i++) {
            resD = game.tick(null, Direction.RIGHT);
            if (TestHelper.hasItemByType(resD, "armour")) { hasArmour = true; break; }
        }
        // Pick your SSR armour
        assertTrue(hasArmour);
    }

    @Test
    public void MercenaryMoveAgainstWallBoulder() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("trappedMob", "peaceful");
        assertNotNull(game);

        assertTrue(TestHelper.hasEntityByType(resD, "mercenary"));
        for (int i = 0; i < 10; i++) resD = game.tick(null, Direction.RIGHT);
        assertEquals(new Position(2, 2), TestHelper.fetchEntitiesByType(resD, "mercenary").get(0).getPosition());
    }

    @Test
    public void SceptreControlMercenary() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("sceptreCorridor", "standard");
        assertNotNull(game);

        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.build("sceptre");
        assertFalse(TestHelper.hasItemByType(resD, "treasure"));
        assertFalse(TestHelper.hasItemByType(resD, "sun_stone"));
        resD = game.interact(TestHelper.fetchEntitiesByType(resD, "mercenary").get(0).getId());
        resD = game.tick(null, Direction.DOWN);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.UP);
        resD = game.tick(null, Direction.RIGHT);
        assertTrue(TestHelper.hasEntityByType(resD, "mercenary"));
        resD = game.tick(null, Direction.LEFT);
        resD = game.tick(null, Direction.LEFT);
        resD = game.tick(null, Direction.LEFT);
        resD = game.tick(null, Direction.LEFT);
        resD = game.tick(null, Direction.LEFT);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        assertFalse(TestHelper.hasEntityByType(resD, "mercenary"));
    }
}
