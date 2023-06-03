package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.entity.collectableEntity.InvincibilityPotion;
import dungeonmania.entity.collectableEntity.InvisibilityPotion;
import dungeonmania.entity.movingEntity.Mercenary;

public class MercenaryControllerTest {
    
    DungeonManiaController d;

    public EntityResponse getEntityResponse(DungeonResponse res, String type) {
        EntityResponse ent = res.getEntities().stream()
                                            .filter(e -> type.equals(e.getType()))
                                            .findAny()
                                            .orElse(null);
        
        return ent;                                   
    }

    public Position getPosition (EntityResponse e) {
        return e.getPosition();
    }

    public int positionDiff(Position p1, Position p2) {
        Position diff = Position.calculatePositionBetween(p1, p2);
        return Math.abs(diff.getX()) + Math.abs(diff.getY());
    }

    @BeforeEach
    public void setup() {
        Character healthC = new Character(null);
        healthC.setFullHealthPoint(20);
    }

    @Test
    public void testNormalMotion() {
        InvincibilityPotion.idNum = 1;
        InvisibilityPotion.idNum = 1;
        Mercenary.idNum = 1;
        DungeonManiaController d = new DungeonManiaController();
        d.newGame("mercenaryControllerTest", "peaceful");

        DungeonResponse res = d.tick(null, Direction.DOWN);
        Position pFirst = getPosition(getEntityResponse(res, "player"));
        Position mFirst = getPosition(getEntityResponse(res, "mercenary"));

        DungeonResponse res2 = d.tick(null, Direction.DOWN);
        Position pAfter = getPosition(getEntityResponse(res2, "player"));
        Position mAfter = getPosition(getEntityResponse(res2, "mercenary"));

        // Test mercenary is heading to player
        assertFalse(mFirst.equals(mAfter));
        assertTrue(positionDiff(pFirst, mFirst) > positionDiff(pAfter, mAfter));

        // Test bribe mercenary 
        assertThrows(InvalidActionException.class, () -> d.interact("mercenary_1"));

    }

    @Test 
    public void testInvisible() {
        InvincibilityPotion.idNum = 1;
        InvisibilityPotion.idNum = 1;
        DungeonManiaController d = new DungeonManiaController();
        d.newGame("mercenaryControllerTest", "peaceful");

        d.tick(null, Direction.RIGHT);
        d.tick(null, Direction.RIGHT);
        d.tick(null, Direction.DOWN);
        assertDoesNotThrow(() -> d.tick("invisibility_potion_1", Direction.NONE));

        // Test mercenary does not move 
        DungeonResponse res1 = d.tick(null, Direction.LEFT);
        DungeonResponse res2 = d.tick(null, Direction.UP);
        Position mBefore = getPosition(getEntityResponse(res1, "mercenary"));
        Position mAfter = getPosition(getEntityResponse(res2, "mercenary"));

        assertTrue(mBefore.equals(mAfter));    

    }

    @Test
    public void testInvincible() {
        InvincibilityPotion.idNum = 1;
        InvisibilityPotion.idNum = 1;
        DungeonManiaController d = new DungeonManiaController();
        d.newGame("mercenaryControllerTest", "peaceful");

        d.tick(null, Direction.RIGHT);
        d.tick(null, Direction.RIGHT);
        d.tick(null, Direction.DOWN);
        d.tick(null, Direction.DOWN);
        assertDoesNotThrow(() -> d.tick("invincibility_potion_1", Direction.NONE));

        DungeonResponse res1  = d.tick(null, Direction.LEFT);
        Position pFirst = getPosition(getEntityResponse(res1, "player"));
        Position mFirst = getPosition(getEntityResponse(res1, "mercenary"));


        d.tick(null, Direction.LEFT);
        DungeonResponse res2  = d.tick(null, Direction.RIGHT);
        Position pAfter = getPosition(getEntityResponse(res2, "player"));
        Position mAfter = getPosition(getEntityResponse(res2, "mercenary"));

        // Test mercenary is moving away
        assertFalse(mFirst.equals(mAfter));
        assertTrue(positionDiff(pFirst, mFirst) < positionDiff(pAfter, mAfter));
    }

    @Test
    public void testMoveWithSwamp() {
        DungeonManiaController d = new DungeonManiaController();
        d.newGame("mapWithSwamp", "peaceful");

        DungeonResponse res = d.tick(null, Direction.NONE);
        EntityResponse eRes = res.getEntities().stream().filter(e -> e.getType().equals("assassin"))
                                                        .findAny()
                                                        .orElse(null);
        
        // mercenary move to a longer path less weighted path
        assertTrue(eRes.getPosition().equals(new Position(1, 0)));

        DungeonResponse res2 = d.tick(null, Direction.NONE);
        EntityResponse eRes2 = res2.getEntities().stream().filter(e -> e.getType().equals("assassin"))
                                                         .findAny()
                                                         .orElse(null);
        assertTrue(eRes2.getPosition().equals(new Position(1, 1)));

        DungeonResponse res3 = d.tick(null, Direction.NONE);
        EntityResponse eRes3 = res3.getEntities().stream().filter(e -> e.getType().equals("assassin"))
                                                         .findAny()
                                                         .orElse(null);
        assertTrue(eRes3.getPosition().equals(new Position(1, 1)));

        DungeonResponse res4 = d.tick(null, Direction.NONE);
        EntityResponse eRes4 = res4.getEntities().stream().filter(e -> e.getType().equals("assassin"))
                                                         .findAny()
                                                         .orElse(null);
        assertFalse(eRes4.getPosition().equals(new Position(1, 1)));

    }

}
