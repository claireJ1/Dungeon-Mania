package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class RandomMapTest {
    DungeonResponse res;

    public Position getCharacterPos(DungeonResponse res) {
        Position p = res.getEntities().stream().filter(e -> e.getType().equals("player"))
                                               .map(e -> e.getPosition())
                                               .findAny()
                                               .orElse(null);

        return p;
    }

    public EntityResponse getResponse(DungeonResponse res, Position p) {
        EntityResponse result = res.getEntities().stream().filter(e -> e.getPosition().equals(p))
                                                          .findAny()
                                                          .orElse(null);

        return result;
    }
    
    @Test
    public void randomMapTestNoError() {

        DungeonManiaController d = new DungeonManiaController();
        Random rand = new Random();
        int xStart = rand.nextInt(24) * 2 + 1;
        int yStart = rand.nextInt(24) * 2 + 1;
        int xEnd = rand.nextInt(24) * 2 + 1;
        int yEnd = rand.nextInt(24) * 2 + 1;

        
        assertDoesNotThrow(() -> res = d.generateDungeon(xStart, yStart, xEnd, yEnd, "peaceful"));

        // check player on the map
        assertTrue(getCharacterPos(res) != null);

        // check exit on the map
        Position exitPos = res.getEntities().stream().filter(e -> e.getType().equals("exit"))
                                                     .map(e -> e.getPosition())
                                                     .findAny()
                                                     .orElse(null);
        assertEquals(new Position(xEnd, yEnd), exitPos);

        // check goal
        assertEquals(":exit", res.getGoals());

        // check player is not stuck
        List<Position> adjs = getCharacterPos(res).getCardinallyAdjacentPositions();

        boolean findPath = false;
        for (Position p : adjs) {
            if (getResponse(res, p) == null) {
                findPath = true;
                break;
            }
        }

        assertTrue(findPath);
        
    }

    @Test
    public void invalidInput() {
        DungeonManiaController d = new DungeonManiaController();
        
        assertThrows(IllegalArgumentException.class, () -> d.generateDungeon(3, 3, 9, 9, "faefbe"));
    }
}
