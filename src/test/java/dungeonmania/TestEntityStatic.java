package dungeonmania;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import dungeonmania.response.models.*;
import dungeonmania.util.*;

public class TestEntityStatic {

    public EntityResponse fetchPlayer(DungeonResponse resD) {
        for (EntityResponse e: resD.getEntities()) if (e.getType().equals("player")) return e;
        return null;
    }

    public EntityResponse fetchEntity(DungeonResponse resD, String type, Position pos) {
        for (EntityResponse e: resD.getEntities()) if (e.getType().equals(type) && e.getPosition().equals(pos)) return e;
        return null;
    }

    public EntityResponse fetchEntityByID(DungeonResponse resD, String id) {
        for (EntityResponse e: resD.getEntities()) if (e.getId().equals(id)) return e;
        return null;
    }

    public boolean hasEntityType(DungeonResponse resD, String type) {
        for (EntityResponse e: resD.getEntities()) if (e.getType().equals(type)) return true;
        return false;
    }

    @Test
    public void testWall() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("maze", "Peaceful");
        assertNotNull(game);
        
        EntityResponse resE = fetchPlayer(resD);
        assertNotNull(resE);
        Position chaPos1 = resE.getPosition();
        resD = game.tick(null, Direction.UP);
        resE = fetchPlayer(resD);
        assertNotNull(resE);
        Position chaPos2 = resE.getPosition();
        assertEquals(chaPos1, chaPos2);
    }

    @Test
    public void testExit() throws IOException {
        List<Direction> go = Arrays.asList(
            Direction.DOWN, Direction.DOWN, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.RIGHT, Direction.DOWN, Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.UP, Direction.UP, Direction.UP, Direction.UP, Direction.UP, Direction.UP, Direction.UP, Direction.UP, Direction.UP, Direction.UP, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN
        );
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("maze", "Peaceful");
        assertNotNull(game);

        for (Direction d: go) resD = game.tick(null, d);
    }

    @Test
    public void testBoulder() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("boulders", "Peaceful");
        assertNotNull(game);

        EntityResponse resE = fetchEntity(resD, "boulder", new Position(3,2));
        assertNotNull(resE);
        String bid = resE.getId();
        resD = game.tick(null, Direction.RIGHT);
        resE = fetchEntityByID(resD, bid);
        Position newPos = resE.getPosition();
        assertEquals(new Position(4, 2), newPos);
    }

    @Test
    public void testSwitch() throws IOException {
        List<Direction> go = Arrays.asList(
            Direction.RIGHT, Direction.UP, Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.LEFT, Direction.LEFT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.LEFT, Direction.DOWN, Direction.RIGHT, Direction.LEFT, Direction.LEFT, Direction.DOWN, Direction.LEFT, Direction.LEFT, Direction.UP, Direction.UP, Direction.DOWN, Direction.DOWN, Direction.RIGHT, Direction.RIGHT, Direction.UP, Direction.UP, Direction.RIGHT, Direction.RIGHT, Direction.UP, Direction.UP, Direction.UP, Direction.LEFT, Direction.LEFT, Direction.DOWN, Direction.RIGHT, Direction.DOWN, Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.LEFT, Direction.LEFT, Direction.DOWN, Direction.LEFT, Direction.LEFT, Direction.UP, Direction.RIGHT
        );
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("boulders", "Peaceful");
        assertNotNull(game);

        for (Direction d: go) resD = game.tick(null, d);
    }

    @Test
    public void testDoor() throws IOException {
        List<Direction> go = Arrays.asList(
            Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.UP
        );
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("advanced-2", "Peaceful");
        assertNotNull(game);

        EntityResponse resE = fetchPlayer(resD);
        assertNotNull(resE);
        String pid = resE.getId();
        for (Direction d: go) resD = game.tick(null, d);
        resE = fetchEntityByID(resD, pid);
        Position newPos = resE.getPosition();
        assertEquals(new Position(15, 9), newPos);
    }

    @Test
    public void testPortal() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("portals", "Peaceful");
        assertNotNull(game);

        EntityResponse resE = fetchPlayer(resD);
        assertNotNull(resE);
        String pid = resE.getId();
        resD = game.tick(null, Direction.RIGHT);
        resE = fetchEntityByID(resD, pid);
        Position newPos = resE.getPosition();
        assertEquals(new Position(5, 0), newPos);
    }

    @Test
    public void testZombieSpawnerHard() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("toaster", "Hard");
        assertNotNull(game);

        for (int i = 0; i < 15; i++) resD = game.tick(null, Direction.UP);
        assertTrue(hasEntityType(resD, "zombie_toast"));
    }

    @Test
    public void testZombieSpawnerNormal() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("toaster", "Standard");
        assertNotNull(game);

        for (int i = 0; i < 20; i++) resD = game.tick(null, Direction.UP);
        assertTrue(hasEntityType(resD, "zombie_toast"));
    }
}
