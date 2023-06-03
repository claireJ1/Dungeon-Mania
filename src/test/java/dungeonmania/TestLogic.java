package dungeonmania;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import dungeonmania.response.models.*;
import dungeonmania.util.*;

public class TestLogic {

    public EntityResponse fetchPlayer(DungeonResponse resD) {
        for (EntityResponse e: resD.getEntities()) if (e.getType().equals("player")) return e;
        return null;
    }

    public boolean hasEntityType(DungeonResponse resD, String type) {
        for (EntityResponse e: resD.getEntities()) if (e.getType().equals(type)) return true;
        return false;
    }

    @Test
    public void testBulbOr() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logic", "peaceful");
        assertNotNull(game);

        assertTrue(hasEntityType(resD, "wire"));
        assertTrue(hasEntityType(resD, "light_bulb_off"));
        resD = game.tick(null, Direction.RIGHT);
        assertTrue(hasEntityType(resD, "light_bulb_on"));
    }

    @Test
    public void testLogicDoorOrOpen() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logic", "peaceful");
        assertNotNull(game);

        List<Direction> go = Arrays.asList(
            Direction.RIGHT, Direction.LEFT, Direction.UP, Direction.UP, Direction.UP, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.DOWN
        );
        for (Direction d: go) resD = game.tick(null, d);
        assertEquals(new Position(4, 5), fetchPlayer(resD).getPosition());
    }

    @Test
    public void testLogicDoorAndOpen() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logic", "peaceful");
        assertNotNull(game);

        List<Direction> go = Arrays.asList(
            Direction.RIGHT, Direction.LEFT, Direction.UP, Direction.UP, Direction.UP, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.DOWN
        );
        for (Direction d: go) resD = game.tick(null, d);
        assertEquals(new Position(3, 4), fetchPlayer(resD).getPosition());
    }

    @Test
    public void testLogicDoorAndClose() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logic", "peaceful");
        assertNotNull(game);

        List<Direction> go = Arrays.asList(
            Direction.RIGHT, Direction.UP
        );
        for (Direction d: go) resD = game.tick(null, d);
        assertEquals(new Position(1, 5), fetchPlayer(resD).getPosition());
    }

    @Test
    public void testLogicDoorXorOpen() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logic", "peaceful");
        assertNotNull(game);

        List<Direction> go = Arrays.asList(
            Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.RIGHT
        );
        for (Direction d: go) resD = game.tick(null, d);
        assertEquals(new Position(2, 7), fetchPlayer(resD).getPosition());
    }

    @Test
    public void testLogicDoorXorClose() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logic", "peaceful");
        assertNotNull(game);

        List<Direction> go = Arrays.asList(
            Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.LEFT, Direction.DOWN, Direction.RIGHT, Direction.UP, Direction.RIGHT
        );
        for (Direction d: go) resD = game.tick(null, d);
        assertEquals(new Position(1, 7), fetchPlayer(resD).getPosition());
    }

    @Test
    public void testLogicDoorNotOpen() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logic", "peaceful");
        assertNotNull(game);

        List<Direction> go = Arrays.asList(
            Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.LEFT, Direction.DOWN, Direction.DOWN, Direction.RIGHT, Direction.RIGHT
        );
        for (Direction d: go) resD = game.tick(null, d);
        assertEquals(new Position(2, 9), fetchPlayer(resD).getPosition());
    }

    @Test
    public void testLogicDoorNotClose() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logic", "peaceful");
        assertNotNull(game);

        List<Direction> go = Arrays.asList(
            Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.LEFT, Direction.DOWN, Direction.RIGHT, Direction.DOWN, Direction.RIGHT
        );
        for (Direction d: go) resD = game.tick(null, d);
        assertEquals(new Position(1, 9), fetchPlayer(resD).getPosition());
    }

    @Test
    public void testLogicDoorCoOpen() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logic", "peaceful");
        assertNotNull(game);

        List<Direction> go = Arrays.asList(
            Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.RIGHT, Direction.RIGHT, Direction.UP
        );
        for (Direction d: go) resD = game.tick(null, d);
        assertEquals(new Position(3, 6), fetchPlayer(resD).getPosition());
    }

    @Test
    public void testBulbNotOn() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logic", "peaceful");
        assertNotNull(game);

        assertTrue(hasEntityType(resD, "light_bulb_on"));
    }

    @Test
    public void testBulbNotOff() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logic", "peaceful");
        assertNotNull(game);

        List<Direction> go = Arrays.asList(
            Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.RIGHT
        );
        for (Direction d: go) resD = game.tick(null, d);
        assertFalse(hasEntityType(resD, "light_bulb_on"));
    }

    @Test
    public void testBulbAndOn() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logic", "peaceful");
        assertNotNull(game);

        List<Direction> go = Arrays.asList(
            Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.UP
        );
        for (Direction d: go) resD = game.tick(null, d);
        assertTrue(hasEntityType(resD, "light_bulb_on"));
    }

    @Test
    public void testBulbAndOff() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logic", "peaceful");
        assertNotNull(game);

        List<Direction> go = Arrays.asList(
            Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.RIGHT
        );
        for (Direction d: go) resD = game.tick(null, d);
        assertFalse(hasEntityType(resD, "light_bulb_on"));
    }

    @Test
    public void testBulbXorOn() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logic", "peaceful");
        assertNotNull(game);

        List<Direction> go = Arrays.asList(
            Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.RIGHT, Direction.LEFT, Direction.UP, Direction.UP, Direction.UP, Direction.UP, Direction.UP, Direction.UP, Direction.UP, Direction.UP, Direction.RIGHT
        );
        for (Direction d: go) resD = game.tick(null, d);
        assertTrue(hasEntityType(resD, "light_bulb_on"));
    }

    @Test
    public void testLogicBombOrOn() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logic", "peaceful");
        assertNotNull(game);

        resD = game.tick(null, Direction.DOWN);
        resD = game.tick(null, Direction.UP);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(TestHelper.fetchItemsByType(resD, "bomb").get(0).getId(), Direction.NONE);
        assertFalse(hasEntityType(resD, "bomb"));
    }

    @Test
    public void testLogicBombAnd() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logicBombAnd", "peaceful");
        assertNotNull(game);

        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(TestHelper.fetchItemsByType(resD, "bomb").get(0).getId(), Direction.NONE);
        assertTrue(hasEntityType(resD, "bomb"));
        resD = game.tick(null, Direction.LEFT);
        resD = game.tick(null, Direction.LEFT);
        resD = game.tick(null, Direction.DOWN);
        resD = game.tick(null, Direction.RIGHT);
        assertFalse(hasEntityType(resD, "bomb"));
    }

    @Test
    public void testLogicBombXor() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logicBombXor", "peaceful");
        assertNotNull(game);

        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(TestHelper.fetchItemsByType(resD, "bomb").get(0).getId(), Direction.NONE);
        assertTrue(hasEntityType(resD, "bomb"));
        resD = game.tick(null, Direction.DOWN);
        assertFalse(hasEntityType(resD, "bomb"));
    }

    @Test
    public void testLogicBombCo() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("logicBombCo", "peaceful");
        assertNotNull(game);

        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.DOWN);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.DOWN);
        resD = game.tick(null, Direction.DOWN);
        resD = game.tick(null, Direction.LEFT);
        resD = game.tick(TestHelper.fetchItemsByType(resD, "bomb").get(0).getId(), Direction.NONE);
        assertFalse(hasEntityType(resD, "bomb"));
    }
}