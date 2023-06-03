package dungeonmania;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.*;
import dungeonmania.util.*;

public class TestCollactableEntity {

    /* M23 External Tests */
    @Test
    public void InvisibilityNoBattle() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("battleCorridor", "hard");
        assertNotNull(game);

        assertTrue(TestHelper.hasEntityByType(resD, "mercenary"));
        resD = game.tick(null, Direction.UP);
        resD = game.tick(TestHelper.fetchItemsByType(resD, "invisibility_potion").get(0).getId(), Direction.NONE);
        for (int i = 0; i < 5; i++) resD = game.tick(null, Direction.RIGHT);
        assertTrue(TestHelper.hasEntityByType(resD, "mercenary"));
    }
}
