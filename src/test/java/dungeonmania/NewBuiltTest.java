package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.*;

public class NewBuiltTest {


    public EntityResponse fetchPlayer(DungeonResponse resD) {
        for (EntityResponse e: resD.getEntities()) if (e.getType().equals("player")) return e;
        return null;
    }

    public EntityResponse fetchEntity(DungeonResponse resD, String type, Position pos) {
        for (EntityResponse e: resD.getEntities()) if (e.getType().equals(type) && e.getPosition().equals(pos)) return e;
        return null;
    }

    public static List<ItemResponse> fetchItemsByType(DungeonResponse resD, String type) {
        List<ItemResponse> ret = new ArrayList<>();
        for (ItemResponse e: resD.getInventory()) if (e.getType().equals(type)) ret.add(e);
        return ret;
    }

    @Test
    public void testBuiltSceptreAfterMidnightArmour() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("newBuilt", "Standard");
        assertNotNull(game);
        
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.DOWN);
        resD = game.tick(null, Direction.LEFT);
        resD = game.tick(null, Direction.LEFT);
        resD = game.tick(null, Direction.DOWN);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.DOWN);
        resD = game.tick(null, Direction.LEFT);

        resD = game.build("midnight_armour");
        assertFalse(resD.getBuildables().contains("midnight_armour"));
        ItemResponse midnightArmour = resD.getInventory().stream()
                                                .filter(e -> e.getType().equals("midnight_armour"))
                                                .findAny()
                                                .orElse(null);
        assertTrue(midnightArmour != null);

        resD = game.build("sceptre");
        assertFalse(resD.getBuildables().contains("sceptre"));
        ItemResponse sceptre = resD.getInventory().stream()
                                                .filter(e -> e.getType().equals("sceptre"))
                                                .findAny()
                                                .orElse(null);
        assertTrue(sceptre != null);
    }
}
