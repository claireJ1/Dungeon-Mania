package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;

public class InventoryControllerTest {
    
    DungeonManiaController d;

    @BeforeEach
    public void pickItem() {
        d = new DungeonManiaController();
        assertDoesNotThrow(() -> d.newGame("inventoryTest", "Peaceful")); 
        
        // Pick up all the items 
        for (int i = 0; i < 3; i++) {
            assertDoesNotThrow(() -> d.tick(null, Direction.DOWN)); 
        }
        
        assertDoesNotThrow(() -> d.tick(null, Direction.RIGHT)); 

        for (int i = 0; i < 3; i++) {
            assertDoesNotThrow(() -> d.tick(null, Direction.UP)); 
        }
    }

    @Test
    public void checkItemInBag() throws InvalidActionException, IllegalArgumentException {
        DungeonResponse res = d.tick(null, Direction.RIGHT); 
        assertTrue(res.getBuildables().contains("bow"));

        ItemResponse enres = res.getInventory().stream().filter(e -> e.getType().equals("health_potion"))
                                                        .findAny()
                                                        .orElse(null);

        assertTrue(enres != null);
    }

    @Test
    public void checkBuildItem() throws InvalidActionException, IllegalArgumentException {
        DungeonResponse res = d.tick(null, Direction.RIGHT); 
        res =  d.build("bow");
        assertFalse(res.getBuildables().contains("bow"));

        // test inventory contains bow
        ItemResponse item = res.getInventory().stream()
                                                .filter(e -> e.getType().equals("bow"))
                                                .findAny()
                                                .orElse(null);
                                                
        assertTrue(item != null);
    }

    @Test
    public void checkUseItem() throws InvalidActionException, IllegalArgumentException {

        // Use item to open the door
        DungeonResponse res =  d.tick(null, Direction.RIGHT);
        res =  d.tick(null, Direction.RIGHT);

        // test inventory doesn not contain key
        ItemResponse item = res.getInventory().stream()
                                              .filter(e -> e.getType().equals("key"))
                                              .findAny()
                                              .orElse(null);
       
        assertFalse(item != null);
    }
}
