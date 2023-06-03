package dungeonmania;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import dungeonmania.entity.*;
import dungeonmania.entity.staticEntity.*;
import dungeonmania.entity.collectableEntity.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.*;

public class TestEntityStaticUnit {

    @Test
    public void testStaticEntityLayer() {
        Character player = new Character(new Position(0,0), null);
        Dungeon game = new Dungeon("id", "name", new ArrayList<Entity>(), player, null);
        player.setDungeon(game);

        Wall w = new Wall(new Position(0, 0), game);
        Exit e = new Exit(new Position(0, 1), game);
        Boulder b = new Boulder(new Position(0, 2), game);
        FloorSwitch s = new FloorSwitch(new Position(0, 3), game);
        Door d = new Door(new Position(0, 4), game, "1");
        Portal p = new Portal(new Position(0, 5), game, "RED");
        ZombieToastSpawner z = new ZombieToastSpawner(new Position(0, 6), game);
        assertEquals(2, w.getPosition().getLayer());
        assertEquals(1, e.getPosition().getLayer());
        assertEquals(2, b.getPosition().getLayer());
        assertEquals(1, s.getPosition().getLayer());
        assertEquals(2, d.getPosition().getLayer());
        assertEquals(2, p.getPosition().getLayer());
        assertEquals(2, z.getPosition().getLayer());
        d.setIsOpen(true);
        assertEquals(1, d.getPosition().getLayer());
    }

    @Test
    public void testReachExit() {
        Character player = new Character(new Position(0,0), null);
        Dungeon game = new Dungeon("id", "name", new ArrayList<Entity>(), player, null);
        player.setDungeon(game);

        Exit e = new Exit(new Position(0, 1), game);
        assertEquals(false, e.getReachExit());
        e.setReachExit(true);
        assertEquals(true, e.getReachExit());
    }

    @Test
    public void testMoveBoulder() {
        Character player = new Character(new Position(0,0), null);
        Dungeon game = new Dungeon("game", "void", new ArrayList<Entity>(), player, null);
        player.setDungeon(game);
        
        Boulder b = new Boulder(new Position(1,1), game);
        Wall w = new Wall(new Position(1,0), game);
        game.addEntity(b);
        game.addEntity(w);
        b.move(Direction.UP);
        assertEquals(new Position(1, 1), b.getPosition());
        b.move(Direction.LEFT);
        assertEquals(new Position(0, 1), b.getPosition());
        b.move(Direction.DOWN);
        assertEquals(new Position(0, 2), b.getPosition());
        b.move(Direction.RIGHT);
        assertEquals(new Position(1, 2), b.getPosition());
    }

    @Test
    public void testSwitchTurnOn() {
        Character player = new Character(new Position(0,0), null);
        Dungeon game = new Dungeon("game", "void", new ArrayList<Entity>(), player, null);
        player.setDungeon(game);

        FloorSwitch s = new FloorSwitch(new Position(0,3), game);
        game.addEntity(s);
        assertEquals(false, s.getIsTurnOn());
        Boulder b = new Boulder(new Position(0,3), game);
        game.addEntity(b);
        assertEquals(true, s.getIsTurnOn());
    }

    @Test
    public void testDestroySpawnerWithSword() {
        Character c = new Character(new Position(0,0).asLayer(2), null);
        Dungeon game = new Dungeon("game", "void", new ArrayList<Entity>(), c, null);
        c.setDungeon(game);

        ZombieToastSpawner z = new ZombieToastSpawner(new Position(1,1), game);
        game.addEntity(z);
        assertThrows(new InvalidActionException("player is not cardinally adjacent to the spawner").getClass(), () -> {
            z.destroy();
        });
        c.move(Direction.DOWN);
        assertEquals(new Position(0,1), c.getPosition());
        assertThrows(new InvalidActionException("player does not have a weapon").getClass(), () -> {
            z.destroy();
        });
        Sword sword = new Sword(new Position(0,0), game);
        game.addEntity(sword);
        c.addWeapon(sword);
        assertDoesNotThrow(() -> {
            z.destroy();
        });
    }

    @Test
    public void testDestroySpawnerWithAnduril() {
        Character c = new Character(new Position(0,0).asLayer(2), null);
        Dungeon game = new Dungeon("game", "void", new ArrayList<Entity>(), c, null);
        c.setDungeon(game);

        ZombieToastSpawner z = new ZombieToastSpawner(new Position(1,1), game);
        game.addEntity(z);
        assertThrows(new InvalidActionException("player is not cardinally adjacent to the spawner").getClass(), () -> {
            z.destroy();
        });
        c.move(Direction.DOWN);
        assertEquals(new Position(0,1), c.getPosition());
        assertThrows(new InvalidActionException("player does not have a weapon").getClass(), () -> {
            z.destroy();
        });
        Anduril anduril = new Anduril(new Position(0,0), game);
        game.addEntity(anduril);
        c.addWeapon(anduril);
        assertDoesNotThrow(() -> {
            z.destroy();
        });
    }
    
}
