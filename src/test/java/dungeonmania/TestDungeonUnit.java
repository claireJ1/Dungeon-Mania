package dungeonmania;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import dungeonmania.entity.collectableEntity.Key;
import dungeonmania.entity.staticEntity.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.*;

public class TestDungeonUnit {

    @Test
    public void testLoadMap() throws IOException {
        Dungeon game = new Dungeon("advanced-2");
        assertEquals("advanced-2", game.getName());
        assertEquals(18, game.getWidth());
        assertEquals(16, game.getHeight());
        assertEquals(128, game.getEntities().size());
        assertEquals(new Position(1, 1), game.getCharacter().getPosition());
        assertTrue(game.getSpawnPoints().contains(new Position(1, 1)));
        assertEquals("( :enemies AND :treasure )", game.getGoal().goalToString());

        game = new Dungeon("portals");
        assertEquals(2, game.getEntityCountByType("portal"));

        game = new Dungeon("boulders");
        assertEquals(7, game.getEntityCountByType("boulder"));
        assertEquals(7, game.getEntityCountByType("switch"));

        game = new Dungeon("toaster");
        assertEquals(1, game.getEntityCountByType("zombie_toast_spawner"));
    }

    @Test
    public void testGroupEntity() throws IOException {
        Key.idNum = 1;
        Portal.idNum = 1;
        Door.idNum = 1;
        Dungeon game1 = new Dungeon("advanced-2");
        Door d1 = (Door)game1.getEntityById("door_1");
        Door d2 = (Door)game1.getEntityById("door_2");
        assertNotNull(d1);
        assertNotNull(d2);
        assertEquals("key_1", d1.getKey().getId());
        assertEquals("key_2", d2.getKey().getId());

        Dungeon game2 = new Dungeon("portals");

        Portal p1 = (Portal)game2.getEntityById("portal_1");
        Portal p2 = (Portal)game2.getEntityById("portal_2");
        assertNotNull(p1);
        assertNotNull(p2);
        assertEquals("portal_2", p1.getCorresponding().getId());
        assertEquals("portal_1", p2.getCorresponding().getId());
    }

    @Test
    public void testTick() throws IOException {
        assertDoesNotThrow(() -> {
            Dungeon game = new Dungeon("maze");
            game.tick(null, Direction.DOWN, 1);
        });
    }

    @Test
    public void testInteract() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {new Dungeon("advanced-2").interact("abaaba");} );
        assertTrue(new Dungeon("advanced-2").getEntityCountByType("mercenary") > 0);
        assertThrows(new InvalidActionException("Player is not within 2 cardinal tiles to the mercenary").getClass(), () -> {
            Dungeon game = new Dungeon("advanced-2");
            game.interact(game.getEntityByType("mercenary").get(0).getId());
        });
        assertThrows(new InvalidActionException("player is not cardinally adjacent to the spawner").getClass(), () -> {
            Dungeon game = new Dungeon("toaster");
            game.interact(game.getEntityByType("zombie_toast_spawner").get(0).getId());
        });
    }

    @Test
    public void testBuild() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> {new Dungeon("maze").build("abaaba");});
    }
    
    @Test
    public void testSpawnSpider() throws IOException {
        Dungeon game = new Dungeon("maze");
        for (int i = 0; i < 100; i++) {
            game.spawnSpider(i);
        }
        assertEquals(game.getEntityCountByType("spider"), 4);
    }

    @Test
    public void testSpawnZombie() throws IOException {
        Dungeon game = new Dungeon("toaster");
        game.spawnZombie(20);
        assertTrue(game.getEntityCountByType("zombie_toast") == 1);
    }
}
