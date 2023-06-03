package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.entity.Entity;
import dungeonmania.entity.collectableEntity.SunStone;
import dungeonmania.entity.collectableEntity.Treasure;
import dungeonmania.entity.collectableEntity.Wood;
import dungeonmania.entity.movingEntity.Mercenary;
import dungeonmania.entity.movingEntity.Spider;
import dungeonmania.entity.movingEntity.ZombieToast;
import dungeonmania.entity.staticEntity.Boulder;
import dungeonmania.entity.staticEntity.Exit;
import dungeonmania.entity.staticEntity.FloorSwitch;
import dungeonmania.entity.staticEntity.ZombieToastSpawner;
import dungeonmania.goal.GoalConjunction;
import dungeonmania.goal.GoalComponent;
import dungeonmania.goal.GoalEnemy;
import dungeonmania.goal.GoalExit;
import dungeonmania.goal.GoalNone;
import dungeonmania.goal.GoalSwitch;
import dungeonmania.goal.GoalTreasure;
import dungeonmania.goal.GoalDisjunction;
import dungeonmania.util.Position;

import java.util.*;

public class GoalTest {
    
    @Test
    public void testGoalEnemy() {
        GoalComponent goal = new GoalEnemy();
        Character c = new Character(new Position(4, 9), null);
        Dungeon d = new Dungeon("1", "1", new ArrayList<Entity>() ,c, goal);
        c.setDungeon(d);

        // Test goal hasn't achieved, spider and zombie still alive
        Spider e1 = new Spider(new Position(1, 1), d);
        ZombieToast e2 = new ZombieToast(new Position(1, 1), d);
        ZombieToastSpawner e3 = new ZombieToastSpawner(new Position(4, 4), d);
        d.addEntity(e1);
        d.addEntity(e2);
        d.addEntity(e3);

        d.notifyGoal();
        assertTrue(goal.goalToString().equals(":enemies"));
        assertTrue(d.getInfo().getGoals() != null);

        // Test goal hasn't achieved, all enemies removed but spawner haven't destroied
        d.removeEntity(e1);
        d.removeEntity(e2);

        d.notifyGoal();
        assertTrue(d.getInfo().getGoals() != null);

        // Test goal is a achieved 
        d.removeEntity(e3);
        d.notifyGoal();
        assertTrue(d.getInfo().getGoals() == "");
        
    }

    @Test
    public void testGoalSwitch() {
        GoalComponent goal = new GoalSwitch();
        Character c = new Character(new Position(4, 9), null);
        Dungeon d = new Dungeon("1", "1", new ArrayList<Entity>() ,c, goal);
        c.setDungeon(d);

        // Test for all switch was not on
        FloorSwitch f1 = new FloorSwitch(new Position(4, 4), d);
        FloorSwitch f2 = new FloorSwitch(new Position(4, 5), d);
        d.addEntity(f1);
        d.addEntity(f2);

        d.notifyGoal();
        assertTrue(goal.goalToString().equals(":switch"));
        assertTrue(d.getInfo().getGoals() != null);

        // Test for one switch off
        d.addEntity(new Boulder(new Position(4, 4), d));
        d.notifyGoal();
        assertTrue(d.getInfo().getGoals() != null);

        // Test for all switch off
        d.addEntity(new Boulder(new Position(4, 5), d));
        d.notifyGoal();
        assertTrue(d.getInfo().getGoals() == "");
    }

    @Test
    public void testGoalForTreasure() {
        GoalComponent goal = new GoalTreasure();
        Character c = new Character(new Position(4, 9), null);
        Dungeon d = new Dungeon("1", "1", new ArrayList<Entity>(), c, goal);
        c.setDungeon(d);

        // Test treasure hasn't been collected 
        Treasure t1 = new Treasure(new Position(5, 4), d);
        Treasure t2 = new Treasure(new Position(4, 4), d);
        d.addEntity(t1);
        d.addEntity(t2);
        
        d.notifyGoal();
        assertTrue(goal.goalToString().equals(":treasure"));
        assertTrue(d.getInfo().getGoals() != null);

        // Test goal has been achieved, no treasure in dungeon 
        d.removeEntity(t1);
        d.removeEntity(t2);
        d.notifyGoal();
        assertTrue(d.getInfo().getGoals() == "");
    }

    @Test
    public void testGoalForExit() {
        GoalComponent goal = new GoalExit();
        Character c = new Character(new Position(4, 9), null);
        Dungeon d = new Dungeon("1", "1", new ArrayList<Entity>(), c, goal);
        c.setDungeon(d);

        // Test character is not at the exit position
        Treasure t1 = new Treasure(new Position(5, 4), d);
        Exit e1 = new Exit(new Position(7, 4), d);
        d.addEntity(t1);
        d.addEntity(e1);

        d.notifyGoal();
        assertTrue(goal.goalToString().equals(":exit"));
        assertTrue(d.getInfo().getGoals() != null);

        // Test goal has been achieved, character is at the exit 
        c.setPosition(new Position(7, 4));
        d.notifyGoal();
        assertTrue(d.getInfo().getGoals() == "");

        // Test goal with no exit 
        Dungeon d1 = new Dungeon("1", "1", new ArrayList<>(), c, goal);
        d1.addEntity(t1);
        d1.notifyGoal();
        assertTrue(d.getInfo().getGoals() == "");

    }

    @Test
    public void testGoalForTreasureAndEnemy() {

        GoalComponent goal1 = new GoalTreasure();
        GoalComponent goal2 = new GoalEnemy();
        GoalComponent overall = new GoalConjunction(goal1, goal2); 

        Character c = new Character(new Position(4, 9), null);
        Dungeon d = new Dungeon("1", "1", new ArrayList<Entity>(), c, overall);
        c.setDungeon(d);

        // Test for treasure and enemy, 
        // where enemies haven't destroyed but treasure has been collected
        Spider e1 = new Spider(new Position(5, 4), d);
        Treasure t1 = new Treasure(new Position(1, 1), d);
        d.addEntity(e1);
        d.addEntity(t1);
  
        d.notifyGoal();
        assertTrue(overall.goalToString().equals("( :treasure AND :enemies )"));
        assertTrue(d.getInfo().getGoals() != null);

        // Test for treasure hasn't been collected but enemies have been destroyed
        d.removeEntity(e1);
        d.notifyGoal();
        assertTrue(d.getInfo().getGoals() != null);

        // Test both goal achieved
        d.removeEntity(t1);
        d.notifyGoal();
        assertTrue(d.getInfo().getGoals() == "");
    }

    @Test
    public void testGoalForAndWithExit() {

        GoalComponent goal1 = new GoalExit();
        GoalComponent goal2 = new GoalTreasure();
        GoalComponent overall = new GoalConjunction(goal1, goal2);

        Character c = new Character(new Position(4, 9), null);
        Dungeon d = new Dungeon("1", "1", new ArrayList<Entity>(), c, overall);
        c.setDungeon(d);

        // Test both goal hasn't achieved
        Treasure t1 = new Treasure(new Position(4, 4), d);
        Exit e1 = new Exit(new Position(5, 4), d);
        
        d.addEntity(t1);
        d.addEntity(e1);
        d.notifyGoal();
        assertTrue(overall.goalToString().contains("exit"));
        assertTrue(overall.goalToString().contains("AND"));
        assertTrue(overall.goalToString().contains("treasure"));
        assertTrue(d.getInfo().getGoals() != null);

        // Test exit before collect treasure
        c.setPosition(new Position(5, 4));
        d.notifyGoal();
        assertTrue(d.getInfo().getGoals() != null);

        // Test exit and collected treasure
        d.removeEntity(t1);
        d.notifyGoal();
        assertTrue(d.getInfo().getGoals() == "");

    }

    @Test
    public void testGoalForOr() {

        GoalComponent goal1 = new GoalExit();
        GoalComponent goal2 = new GoalTreasure();
        GoalComponent overall = new GoalDisjunction(goal1, goal2);

        Character c = new Character(new Position(4, 9), null);
        Dungeon d = new Dungeon("1", "1", new ArrayList<Entity>(), c, overall);
        c.setDungeon(d);

        // Test both goal hasn't achieved
        Treasure t1 = new Treasure(new Position(4, 4), d);
        Exit e1 = new Exit(new Position(5, 4), d);

        d.addEntity(t1);
        d.addEntity(e1);
        d.notifyGoal();
        
        assertTrue(overall.goalToString().contains("exit"));
        assertTrue(overall.goalToString().contains("OR"));
        assertTrue(overall.goalToString().contains("treasure"));
        assertTrue(d.getInfo().getGoals() != null);

        // Test exit goal achieved
        c.setPosition(new Position(5, 4));
        d.notifyGoal();
        assertTrue(d.getInfo().getGoals() == "");

        // Test treasure goal achieved
        Dungeon d1 = new Dungeon("1", "1", new ArrayList<Entity>(), c, overall);
        d1.addEntity(e1);

        d1.notifyGoal();
        assertTrue(d.getInfo().getGoals() == "");
    }

    @Test
    public void testGoalForAndWithOr() {
        GoalComponent goal1 = new GoalExit();
        GoalComponent goal2 = new GoalTreasure();
        GoalComponent or = new GoalDisjunction(goal1, goal2);
        GoalComponent goal3 = new GoalSwitch();
        GoalComponent overall = new GoalConjunction(goal3, or);

        Character c = new Character(new Position(5, 4), null);
        Dungeon d = new Dungeon("1", "1", new ArrayList<Entity>(), c, overall);
        c.setDungeon(d);

        // Test only goal exit achieved
        Treasure t1 = new Treasure(new Position(4, 4), d);
        Exit e1 = new Exit(new Position(5, 4), d);
        FloorSwitch f1 = new FloorSwitch(new Position(9, 4), d);
        
        d.addEntity(t1);
        d.addEntity(e1);
        d.addEntity(f1);
        d.notifyGoal();

        assertTrue(overall.goalToString().contains("switch AND"));
        assertTrue(overall.goalToString().contains(":exit OR :treasure"));
        assertTrue(d.getInfo().getGoals() != null);

        // Test goal switch hasn't avhieved 
        d.removeEntity(t1);
        d.notifyGoal();
        assertTrue(d.getInfo().getGoals() != null);

        // Test only goal switch achieved 
        d.addEntity(new Boulder(new Position(9, 4), d));
        d.addEntity(t1);
        c.setPosition(new Position(8, 8));

        d.notifyGoal();
        assertTrue(d.getInfo().getGoals() != null);

        // Test switch and exit achieved
        c.setPosition(new Position(5, 4));
        d.notifyGoal();
        assertTrue(d.getInfo().getGoals() == "");
    }

    @Test
    public void testGoalNone() {
        GoalComponent goal = new GoalNone();

        Character c = new Character(new Position(5, 4), null);
        Dungeon d = new Dungeon("1", "1", new ArrayList<Entity>(), c, goal);
        c.setDungeon(d);

        // Test treasure hasn't been collected 
        Treasure t1 = new Treasure(new Position(1, 1), d);
        d.addEntity(t1);
        d.notifyGoal();
        assertTrue(goal.goalToString().equals("NONE"));
        assertTrue(d.getInfo().getGoals() != null);

        // Test goal has been achieved, no treasure in dungeon 
        d.removeEntity(t1);
        assertTrue(d.getInfo().getGoals() != null);
    }

    @Test
    public void testGoalEnemyAndAlly() {
        GoalComponent goal = new GoalEnemy();

        Character c = new Character(new Position(4, 9), null);
        Dungeon d = new Dungeon("1", "1", new ArrayList<Entity>() ,c, goal);
        c.setDungeon(d);

        // Test goal hasn't achieved, mercenary alive
        assertEquals(":enemies", goal.goalToString());

        Mercenary m = new Mercenary(new Position(4, 8), d);
        d.addEntity(m);
        d.notifyGoal();

        // bribe mercenary with treasure 
        c.addItem(new Treasure(new Position(-1, -1), d));
        m.setToAlly(true);
        d.notifyGoal();
        assertEquals("", d.getInfo().getGoals());

        // bribe mercenary with sceptre
        Mercenary m2 = new Mercenary(new Position(5, 9), d);
        d.addEntity(m2);
        c.addItem(new SunStone(new Position(2, 2), d));
        c.addItem(new Wood(new Position(-1, -1), d));
        c.addItem(new Treasure(new Position(-1, -1), d));

        // build sceptre add bribe with sceptre
        assertDoesNotThrow(() -> c.build("sceptre"));
        m2.setToAlly(true);
        d.notifyGoal();

        assertEquals(":enemies", d.getInfo().getGoals());
    }


}
