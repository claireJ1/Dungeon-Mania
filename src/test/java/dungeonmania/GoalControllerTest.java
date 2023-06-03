package dungeonmania;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class GoalControllerTest {
    
    @Test
    public void testGoalConjunction() {
        DungeonManiaController d = new DungeonManiaController();
        d.newGame("inventoryTest", "peaceful");

        // Treasure collected but goal is not complete
        DungeonResponse res1 = d.tick(null, Direction.DOWN);
        assertTrue(res1.getGoals().contains(":exit")); 
        assertTrue(res1.getGoals().contains("AND")); 
        assertTrue(res1.getGoals().contains("treasure")); 

        // Treasuer go to exit 
        d.tick(null, Direction.DOWN);
        d.tick(null, Direction.DOWN);
        d.tick(null, Direction.RIGHT);
        DungeonResponse res2 = d.tick(null, Direction.RIGHT);
        assertTrue(res2.getGoals() == "");
    }

    /* M23 External Tests */
    @Test
    public void FaultGoalEnemy() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("goalmapFaultEnemy", "standard");
        assertNotNull(game);

        assertEquals("", resD.getGoals());
    }

    @Test
    public void FaultGoalSwitch() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("goalmapFaultSwitch", "standard");
        assertNotNull(game);
        
        assertEquals("", resD.getGoals());
    }

    @Test
    public void FaultGoalTreasure() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("goalmapFaultTreasure", "standard");
        assertNotNull(game);
        
        assertEquals("", resD.getGoals());
    }

    @Test
    public void GoalExitAndEnemies() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("goalmapExitAndEnemies", "standard");
        assertNotNull(game);
        
        assertTrue(resD.getGoals().contains(":exit AND :enemies"));
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        assertTrue(resD.getGoals().contains(":exit"));
        if (!TestHelper.hasEntityByType(resD, "spider") && !TestHelper.hasEntityByType(resD, "mercenary")) {
            resD = game.tick(null, Direction.RIGHT);
            assertEquals("", resD.getGoals());
        }
 

    }

    @Test
    public void GoalExitAndSwitch() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("goalmapExitAndBoulder", "standard");
        assertNotNull(game);
        
        assertTrue(resD.getGoals().contains(":exit"));
        assertTrue(resD.getGoals().contains("AND"));
        assertTrue(resD.getGoals().contains(":switch"));
        resD = game.tick(null, Direction.RIGHT);
        assertEquals(new Position(1,0), TestHelper.fetchEntitiesByType(resD, "player").get(0).getPosition());
        assertEquals(new Position(2,0), TestHelper.fetchEntitiesByType(resD, "boulder").get(0).getPosition());
        assertTrue(resD.getGoals().contains(":exit"));
        resD = game.tick(null, Direction.DOWN);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.UP);
        assertEquals("", resD.getGoals());
    }

    @Test
    public void GoalEnemiesAndSwitch() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("goalmapEnemiesAndBoulder", "standard");
        assertNotNull(game);
        
        assertTrue(resD.getGoals().contains(":enemies"));
        assertTrue(resD.getGoals().contains("AND"));
        assertTrue(resD.getGoals().contains(":switch"));
        game.tick(null, Direction.RIGHT);
        assertTrue(resD.getGoals().contains(":switch"));
        if (!TestHelper.hasEntityByType(resD, "spider")) {
            resD = game.tick(null, Direction.RIGHT);
            assertEquals("", resD.getGoals());
        }
    }

    @Test
    public void GoalSwitchAndTreasure() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("goalmapBoulderAndTreasure", "standard");
        assertNotNull(game);
        
        assertTrue(resD.getGoals().contains(":switch"));
        assertTrue(resD.getGoals().contains("AND"));
        assertTrue(resD.getGoals().contains(":treasure"));
        resD = game.tick(null, Direction.RIGHT);
        assertTrue(resD.getGoals().contains(":treasure"));
        resD = game.tick(null, Direction.DOWN);
        assertEquals("", resD.getGoals());
    }

    @Test
    public void GoalEnemiesOrExit() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("goalmapEnemiesOrExit", "standard");
        assertNotNull(game);
        
        assertTrue(resD.getGoals().contains(":enemies"));
        assertTrue(resD.getGoals().contains("OR"));
        assertTrue(resD.getGoals().contains(":exit"));
        resD = game.tick(null, Direction.RIGHT);
        if (TestHelper.hasEntityByType(resD, "spider")) resD = game.tick(null, Direction.RIGHT);
        assertEquals("", resD.getGoals());
    }

    @Test
    public void GoalExitOrSwitch() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("goalmapExitOrBoulder", "standard");
        assertNotNull(game);
        
        assertTrue(resD.getGoals().contains(":exit"));
        assertTrue(resD.getGoals().contains("OR"));
        assertTrue(resD.getGoals().contains(":switch"));
        resD = game.tick(null, Direction.RIGHT);
        assertEquals("", resD.getGoals());

        game = new DungeonManiaController();
        resD = game.newGame("goalmapExitOrBoulder", "standard");
        assertNotNull(game);
        
        assertTrue(resD.getGoals().contains(":exit"));
        assertTrue(resD.getGoals().contains("OR"));
        assertTrue(resD.getGoals().contains(":switch"));
        resD = game.tick(null, Direction.DOWN);
        assertEquals("", resD.getGoals());
    }

    @Test
    public void GoalEnemiesOrSwitch() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("goalmapEnemiesOrBoulder", "standard");
        assertNotNull(game);
        
        assertTrue(resD.getGoals().contains(":enemies"));
        assertTrue(resD.getGoals().contains("OR"));
        assertTrue(resD.getGoals().contains(":switch"));
        resD = game.tick(null, Direction.RIGHT);
        if (TestHelper.hasEntityByType(resD, "spider")) resD = game.tick(null, Direction.RIGHT);
        assertEquals("", resD.getGoals());
    }

    @Test
    public void GoalEnemiesOrTreasure() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("goalmapEnemiesOrTreasure", "standard");
        assertNotNull(game);
        
        assertTrue(resD.getGoals().contains(":enemies"));
        assertTrue(resD.getGoals().contains("OR"));
        assertTrue(resD.getGoals().contains(":treasure"));
        resD = game.tick(null, Direction.RIGHT);
        if (TestHelper.hasEntityByType(resD, "spider")) {
            resD = game.tick(null, Direction.LEFT);
            resD = game.tick(null, Direction.DOWN);
        }
        assertEquals("", resD.getGoals());
    }

    @Test
    public void GoalSwitchOrTreasure() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("goalmapBoulderOrTreasure", "standard");
        assertNotNull(game);
        
        assertTrue(resD.getGoals().contains(":switch"));
        assertTrue(resD.getGoals().contains("OR"));
        assertTrue(resD.getGoals().contains(":treasure"));
        resD = game.tick(null, Direction.RIGHT);
        assertEquals("", resD.getGoals());

        game = new DungeonManiaController();
        resD = game.newGame("goalmapBoulderOrTreasure", "standard");
        assertNotNull(game);
        
        assertTrue(resD.getGoals().contains(":switch"));
        assertTrue(resD.getGoals().contains("OR"));
        assertTrue(resD.getGoals().contains(":treasure"));
        resD = game.tick(null, Direction.DOWN);
        resD = game.tick(null, Direction.RIGHT);
        assertEquals("", resD.getGoals());
    }

    @Test
    public void GoalExitOrTreasureOrSwitch() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("goalmapExitOrTreasureOrBoulder", "standard");
        assertNotNull(game);
        
        assertTrue(resD.getGoals().contains(":exit"));
        assertTrue(resD.getGoals().contains("OR"));
        assertTrue(resD.getGoals().contains(":treasure"));
        assertTrue(resD.getGoals().contains(":switch"));
        resD = game.tick(null, Direction.LEFT);
        assertEquals("", resD.getGoals());

        game = new DungeonManiaController();
        resD = game.newGame("goalmapExitOrTreasureOrBoulder", "standard");
        assertNotNull(game);

        resD = game.tick(null, Direction.DOWN);
        assertEquals("", resD.getGoals());

        game = new DungeonManiaController();
        resD = game.newGame("goalmapExitOrTreasureOrBoulder", "standard");
        assertNotNull(game);

        resD = game.tick(null, Direction.RIGHT);
        assertEquals("", resD.getGoals());
    }

    @Test
    public void GoalEnemiesAndExitAndTreasureAndBoulder() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("goalmapEnemiesAndExitAndTreasureAndBoulder", "standard");
        assertNotNull(game);
        
        assertTrue(resD.getGoals().contains(":enemies"));
        assertTrue(resD.getGoals().contains(":exit"));
        assertTrue(resD.getGoals().contains(":treasure"));
        assertTrue(resD.getGoals().contains(":switch"));

        resD = game.tick(null, Direction.RIGHT);
        assertFalse(TestHelper.hasEntityByType(resD, "spider"));
        resD = game.tick(null, Direction.RIGHT);
        resD = game.tick(null, Direction.DOWN);
        if (!TestHelper.hasEntityByType(resD, "spider")) {
            resD = game.tick(null, Direction.RIGHT);
            assertEquals("", resD.getGoals());
        }
    
    }

    @Test
    public void GoalEnemiesAndTreasureOrSwitch() {
        DungeonManiaController game = new DungeonManiaController();
        DungeonResponse resD = game.newGame("goalmapEnemiesAndTreasureOrBoulder", "standard");
        assertNotNull(game);

        assertTrue(resD.getGoals().contains(":enemies"));
        assertTrue(resD.getGoals().contains(":treasure"));
        assertTrue(resD.getGoals().contains(":switch"));

        resD = game.tick(null, Direction.RIGHT);
        if (TestHelper.hasEntityByType(resD, "spider")) {
            resD = game.tick(null, Direction.RIGHT);
            assertEquals("", resD.getGoals());
        }

        game = new DungeonManiaController();
        resD = game.newGame("goalmapEnemiesAndTreasureOrBoulder", "standard");
        assertNotNull(game);

        resD = game.tick(null, Direction.RIGHT);
       if (!TestHelper.hasEntityByType(resD, "spider")) {
            resD = game.tick(null, Direction.DOWN);
            resD = game.tick(null, Direction.RIGHT);
            assertEquals("", resD.getGoals());
       }
        
    }
}
