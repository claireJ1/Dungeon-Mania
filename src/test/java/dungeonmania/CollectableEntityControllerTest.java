package dungeonmania;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import dungeonmania.entity.collectableEntity.Bomb;
import dungeonmania.response.models.*;
import dungeonmania.util.*;

public class CollectableEntityControllerTest {

    @BeforeEach
    public void setup() {
        Bomb.idNum = 1;
    }

    public EntityResponse fetchPlayer(DungeonResponse resD) {
        for (EntityResponse e: resD.getEntities()) if (e.getType().equals("player")) return e;
        return null;
    }

    public EntityResponse fetchEntity(DungeonResponse resD, String type, Position pos) {
        for (EntityResponse e: resD.getEntities()) if (e.getType().equals(type) && e.getPosition().equals(pos)) return e;
        return null;
    }

    public static List<ItemResponse> fetchEntitiesByType(DungeonResponse resD, String type) {

        List<ItemResponse> ret = new ArrayList<>();

        for (ItemResponse e: resD.getInventory()) if (e.getType().equals(type)) ret.add(e);

        return ret;

    }

    @Test
    public void testCollectBomb() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("bomb", "Peaceful");
        assertNotNull(game);
        
        resD = game.tick(null, Direction.RIGHT);
        EntityResponse resE = fetchPlayer(resD);
        Position posB = resE.getPosition();
        resD = game.tick(null, Direction.RIGHT);
        EntityResponse resb = fetchEntity(resD, "bomb", posB);
        assertEquals(null, resb);
    }
    
    @Test
    public void testPlaceBomb() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("bomb", "Peaceful");
        assertNotNull(game);
        
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        EntityResponse resE = fetchPlayer(resD);
        Position posB = resE.getPosition();
        List<ItemResponse> entities = fetchEntitiesByType(resD,"bomb");
        resD = game.tick(entities.get(0).getId(), null);
        EntityResponse resb = fetchEntity(resD, "bomb", posB);
        assertNotNull(resb);
    }

    @Test
    public void testBombBlast() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("bomb", "Peaceful");
        assertNotNull(game);
        
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        List<ItemResponse> entities = fetchEntitiesByType(resD,"bomb");
        resD = game.tick(entities.get(0).getId(), null);
        resD = game.tick(null, Direction.DOWN);
        resD = game.tick(null, Direction.DOWN);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.UP);
        EntityResponse resBomb = fetchEntity(resD, "bomb", new Position(3, 0));
        EntityResponse resSwitch = fetchEntity(resD, "switch", new Position(4, 0));
        EntityResponse resBoulder = fetchEntity(resD, "boulder", new Position(4, 0));
        assertEquals(null, resBomb);
        assertEquals(null, resSwitch);
        assertEquals(null, resBoulder);
    }

    @Test
    public void testBombDiagonallyAdjacentToActiveSwitch() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("bomb", "Peaceful");
        assertNotNull(game);
        
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.UP);
        List<ItemResponse> entities = fetchEntitiesByType(resD,"bomb");
        resD = game.tick(entities.get(0).getId(), null);
        resD = game.tick(null, Direction.DOWN);
        resD = game.tick(null, Direction.DOWN);
        resD = game.tick(null, Direction.DOWN);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.UP);
        EntityResponse resBomb = fetchEntity(resD, "bomb", new Position(3, -1));
        EntityResponse resSwitch = fetchEntity(resD, "switch", new Position(4, 0));
        EntityResponse resBoulder = fetchEntity(resD, "boulder", new Position(4, 0));
        assertNotNull(resBomb);
        assertNotNull(resSwitch);
        assertNotNull(resBoulder);
    }

    @Test
    public void testBombCardinallyAdjacentToInactiveSwitch() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("bomb", "Peaceful");
        assertNotNull(game);
        
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        List<ItemResponse> entities = fetchEntitiesByType(resD,"bomb");
        resD = game.tick(entities.get(0).getId(), null);
        EntityResponse resBomb = fetchEntity(resD, "bomb", new Position(3, 0));
        EntityResponse resSwitch = fetchEntity(resD, "switch", new Position(4, 0));
        EntityResponse resBoulder = fetchEntity(resD, "boulder", new Position(4, 1));
        assertNotNull(resBomb);
        assertNotNull(resSwitch);
        assertNotNull(resBoulder);
    }

    @Test
    public void testBombDiagonallyAdjacentToInactiveSwitch() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("bomb", "Peaceful");
        assertNotNull(game);
        
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.UP);
        List<ItemResponse> entities = fetchEntitiesByType(resD,"bomb");
        resD = game.tick(entities.get(0).getId(), null);
        EntityResponse resBomb = fetchEntity(resD, "bomb", new Position(3, -1));
        EntityResponse resSwitch = fetchEntity(resD, "switch", new Position(4, 0));
        EntityResponse resBoulder = fetchEntity(resD, "boulder", new Position(4, 1));
        assertNotNull(resBomb);
        assertNotNull(resSwitch);
        assertNotNull(resBoulder);
    }
}
