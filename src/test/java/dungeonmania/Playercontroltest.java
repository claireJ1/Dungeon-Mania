package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Playercontroltest {
    DungeonManiaController d;

    public EntityResponse getEntityResponse(DungeonResponse res, String type) {
        EntityResponse ent = res.getEntities().stream().filter(e -> type.equals(e.getType())).findAny().orElse(null);

        return ent;
    }

    public Position getPosition(EntityResponse e) {
        return e.getPosition();
    }

    @Test
    public void testPlayerTeleportWall() {
        DungeonManiaController d = new DungeonManiaController();
        d.newGame("Playercontroltest", "peaceful");

        DungeonResponse res = d.tick(null, Direction.RIGHT);
        Position pFirst = getPosition(getEntityResponse(res, "player"));

        DungeonResponse res2 = d.tick(null, Direction.RIGHT);
        Position pAfter = getPosition(getEntityResponse(res2, "player"));

        assertTrue(pFirst.equals(pAfter));
    }

    @Test
    public void testPlayerPickTwoKeyFail() {
        DungeonManiaController d = new DungeonManiaController();
        d.newGame("Playercontroltest", "peaceful");

        DungeonResponse res = d.tick(null, Direction.DOWN);
        assertEquals(res.getInventory().size(), 1);

        DungeonResponse res2 = d.tick(null, Direction.DOWN);
        assertEquals(res2.getInventory().size(), 1);

    }

    @Test
    public void testPlayerPickTwoKey() {
        DungeonManiaController d = new DungeonManiaController();
        d.newGame("Playercontroltest", "peaceful");

        DungeonResponse res = d.tick(null, Direction.DOWN);
        assertEquals(res.getInventory().size(), 1);

        DungeonResponse res2 = d.tick(null, Direction.RIGHT);
        assertEquals(res2.getInventory().size(), 0);

        d.tick(null, Direction.LEFT);

        DungeonResponse res3 = d.tick(null, Direction.DOWN);
        assertEquals(res3.getInventory().size(), 1);

    }

    @Test
    public void testPlayerOpendoorWithWrongKey() {
        DungeonManiaController d = new DungeonManiaController();
        d.newGame("Playercontroltest", "peaceful");

        DungeonResponse res = d.tick(null, Direction.DOWN);
        assertEquals(res.getInventory().size(), 1);

        DungeonResponse res2 = d.tick(null, Direction.DOWN);
        Position pFirst = getPosition(getEntityResponse(res2, "player"));

        DungeonResponse res3 = d.tick(null, Direction.RIGHT);
        Position pAfter = getPosition(getEntityResponse(res3, "player"));

        assertEquals(pFirst, pAfter);

    }

}
