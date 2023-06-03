package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import dungeonmania.entity.collectableEntity.InvincibilityPotion;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.response.models.*;
import dungeonmania.util.*;

public class BattleControllerTest {
    
    @BeforeEach
    public void setup() {
        InvincibilityPotion.idNum = 1;
    }

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

    public static List<EntityResponse> fetchEntitiesByType(DungeonResponse resD, String type) {
        List<EntityResponse> ret = new ArrayList<>();
        for (EntityResponse e: resD.getEntities()) if (e.getType().equals(type)) ret.add(e);
        return ret;
    }

    @Test
    public void testBattle30Enemies() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("30Enemies", "Standard");
        assertNotNull(game);
        
        int i;
        for (i = 0; i < 30; i++){
            resD = game.tick(null, Direction.RIGHT);
            EntityResponse resE = fetchPlayer(resD);
            if(resE == null) {
                break;
            }
        }
        EntityResponse resE = fetchPlayer(resD);
        assertEquals(null, resE);
    }

    @Test
    public void testAssassinMoveTwiceFaster() throws IOException {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("Assassin", "Standard");
        assertNotNull(game);

        resD = game.tick(null, Direction.RIGHT);
        EntityResponse resE = fetchPlayer(resD);
        assertEquals(null, resE);
    }
}
