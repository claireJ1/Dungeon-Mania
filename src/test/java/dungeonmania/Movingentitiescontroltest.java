package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Movingentitiescontroltest {

    DungeonManiaController d;

    public EntityResponse getEntityResponse(DungeonResponse res, String type) {
        EntityResponse ent = res.getEntities().stream().filter(e -> type.equals(e.getType())).findAny().orElse(null);

        return ent;
    }

    public Position getPosition(EntityResponse e) {
        return e.getPosition();
    }

    @Test
    public void testSpiderMove() {
        DungeonManiaController d = new DungeonManiaController();
        d.newGame("Movingentitiecontrol", "peaceful");

        DungeonResponse re = d.newGame("Movingentitiecontrol", "peaceful");
        Position s1 = getPosition(getEntityResponse(re, "spider"));

        DungeonResponse re2 = d.tick(null, Direction.RIGHT);
        Position s2 = getPosition(getEntityResponse(re2, "spider"));

        DungeonResponse res = d.tick(null, Direction.RIGHT);
        Position sFirst = getPosition(getEntityResponse(res, "spider"));

        DungeonResponse res2 = d.tick(null, Direction.RIGHT);
        Position sAfter = getPosition(getEntityResponse(res2, "spider"));

        DungeonResponse res3 = d.tick(null, Direction.RIGHT);
        Position sAfter2 = getPosition(getEntityResponse(res3, "spider"));

        assertTrue(s1.equals(s2));
        assertFalse(s2.equals(sFirst));

        assertFalse(sFirst.equals(sAfter));
        assertFalse(sAfter.equals(sAfter2));

    }

    @Test
    public void testSwampMove() {
        DungeonManiaController d = new DungeonManiaController();
        d.newGame("Movingentitiecontrol", "peaceful");

        DungeonResponse res = d.newGame("Movingentitiecontrol", "peaceful");
        Position zFirst = getPosition(getEntityResponse(res, "zombie_toast"));

        DungeonResponse res2 = d.tick(null, Direction.RIGHT);
        Position zAfter = getPosition(getEntityResponse(res2, "zombie_toast"));

        DungeonResponse res3 = d.tick(null, Direction.RIGHT);
        Position zAfter2 = getPosition(getEntityResponse(res3, "zombie_toast"));

        assertTrue(zFirst.equals(zAfter));
        assertFalse(zAfter.equals(zAfter2));

    }

}
