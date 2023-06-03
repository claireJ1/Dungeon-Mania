package dungeonmania;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.*;
import dungeonmania.util.*;

public class GameModeTest {

    private boolean hasZombie(List<EntityResponse> entities) {
        for (EntityResponse entity : entities) {
            if (entity.getType() == "zombie_toast") {
                return true;
            }
        }
        return false;
    }

    private DungeonResponse tick(DungeonManiaController d, int times) {
        for (int i = 0; i < times - 1; i++) {
            d.tick(null, Direction.NONE);
        }
        return d.tick(null, Direction.NONE);
    }

    private boolean hasHydra(List<EntityResponse> entities) {
        for (EntityResponse entity : entities) {
            if (entity.getType() == "hydra") {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testStandard() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse info = controller.newGame("gameModeTest", "Standard");
        assertFalse(hasZombie(info.getEntities()));
        // spawn every 20 ticks
        info = tick(controller, 15);
        assertFalse(hasZombie(info.getEntities()));
        info = tick(controller, 5);
        assertTrue(hasZombie(info.getEntities()));
    }
        
    @Test
    public void testHardZombie() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse info = controller.newGame("gameModeTest", "Hard");
        assertFalse(hasZombie(info.getEntities()));
        // spawn every 15 ticks
        info = tick(controller, 15);
        assertTrue(hasZombie(info.getEntities()));
    }

    @Test
    public void testPeaceful() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse info = controller.newGame("peacefulTest", "Peaceful");
        info = tick(controller, 1);
        List<EntityResponse> entities = info.getEntities();
        // no attack damage
        assertTrue(entities.stream().anyMatch(e->e.getType().equals("player")));
        assertFalse(entities.stream().anyMatch(e->e.getType().equals("mercenary")));
    }

    @Test
    public void testHardHydra() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse info = controller.newGame("gameModeTest", "Hard");
        assertFalse(hasHydra(info.getEntities()));
        // spawn every 50 ticks
        info = tick(controller, 50);
        assertTrue(hasHydra(info.getEntities()));
    }

    /* M23 External Tests */
    @Test
    public void HardModeNoInvincibility() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("advanced", "hard");
        assertNotNull(game);

        String pid = TestHelper.fetchEntitiesByType(resD, "invincibility_potion").get(0).getId();

        List<Direction> go = Arrays.asList(
            Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN, Direction.DOWN
        );
        for (Direction d: go) resD = game.tick(null, d);
        
        resD = game.tick(pid, Direction.NONE);
        double l1 = TestHelper.lengthByPosition(Position.calculatePositionBetween(TestHelper.fetchEntitiesByType(resD, "player").get(0).getPosition(), TestHelper.fetchEntitiesByType(resD, "mercenary").get(0).getPosition()));
        resD = game.tick(null, Direction.UP);
        double l2 = TestHelper.lengthByPosition(Position.calculatePositionBetween(TestHelper.fetchEntitiesByType(resD, "player").get(0).getPosition(), TestHelper.fetchEntitiesByType(resD, "mercenary").get(0).getPosition()));
        // mercenary is still moving close with potion 
        assertTrue(l1 >= l2);
    }
}
