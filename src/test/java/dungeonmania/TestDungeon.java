package dungeonmania;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.*;
import dungeonmania.util.*;

public class TestDungeon {

    @Test
    public void TestDungeonBasic() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("portals", "Peaceful");
        assertNotNull(game);

        assertEquals("portals", resD.getDungeonName());
        assertEquals(3, resD.getEntities().size());
    }

    @Test
    public void TestDungeonGoalString() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("maze", "Peaceful");
        assertNotNull(game);

        assertEquals(":exit", resD.getGoals());
    }

    @Test
    public void TestExceptionsTickInteract() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("advanced-2", "peaceful");
        assertNotNull(game);

        assertThrows(InvalidActionException.class, () -> { game.tick("(v0.0)v", Direction.RIGHT); });
        assertThrows(InvalidActionException.class, () -> { game.tick("wall_1", Direction.RIGHT); });
        assertThrows(new InvalidActionException("").getClass(), () -> { game.tick("bomb_1", Direction.RIGHT); });
        assertThrows(IllegalArgumentException.class, () -> { game.interact("(v0.0)v"); });
    }

    @Test
    public void TestBuild() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("advanced-2", "peaceful");
        assertNotNull(game);

        assertThrows(new InvalidActionException("Don't have sufficient items to craft a bow").getClass(), () -> { game.build("bow"); });
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) game.tick(null, Direction.RIGHT);
            for (int i = 0; i < 13; i++) game.tick(null, Direction.DOWN);
            game.tick(null, Direction.RIGHT);
        });
        assertThrows(IllegalArgumentException.class, () -> { game.build("(v0.0)v"); });
        assertDoesNotThrow(() -> { game.build("bow"); });
    }
}
