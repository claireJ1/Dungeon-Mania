package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import dungeonmania.util.Position;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import dungeonmania.entity.*;
import dungeonmania.entity.movingEntity.Spider;
import dungeonmania.entity.movingEntity.ZombieToast;
import dungeonmania.entity.staticEntity.Boulder;

import dungeonmania.entity.staticEntity.*;

public class MovingEntityTests {

    @Test
    public void testZombieMove() {

        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 4), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        p.setDungeon(d);
        ZombieToast z = new ZombieToast(new Position(5, 6), d);
        list.add(z);
        d.setEntities(list);
        assertEquals(z.getPosition(), new Position(5, 6));
        z.move();
        assertNotEquals(z.getPosition(), new Position(5, 6));

    }

    @Test
    public void testZombieMoveFail() {

        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 4), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        p.setDungeon(d);
        ZombieToast z = new ZombieToast(new Position(5, 6), d);
        Boulder b = new Boulder(new Position(4, 6), d);
        ZombieToastSpawner z1 = new ZombieToastSpawner(new Position(6, 6), d);
        Door door = new Door(new Position(5, 5), d, "123");
        Wall wall1 = new Wall(new Position(5, 7), d);
        list.add(z);
        list.add(b);
        list.add(z1);
        list.add(door);
        list.add(wall1);
        d.setEntities(list);
        assertEquals(z.getPosition(), new Position(5, 6));
        for (int i = 0; i < 15; i++) {
            z.move();
        }
        assertEquals(z.getPosition(), new Position(5, 6));

    }

    @Test
    public void testSpiderMove() {

        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 4), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        p.setDungeon(d);
        Spider s = new Spider(new Position(5, 6), d);
        Wall wall = new Wall(new Position(5, 5), d);
        list.add(s);
        list.add(wall);
        d.setEntities(list);
        assertEquals(s.getPosition(), new Position(5, 6));
        s.move();
        assertEquals(s.getPosition(), new Position(5, 5));
        s.move();
        assertEquals(s.getPosition(), new Position(6, 5));
        s.move();
        assertEquals(s.getPosition(), new Position(6, 6));
    }

    @Test
    public void testSpiderMoveFail() {

        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 4), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        p.setDungeon(d);
        Spider s = new Spider(new Position(5, 6), d);
        Boulder b = new Boulder(new Position(5, 5), d);
        list.add(b);
        list.add(s);
        d.setEntities(list);
        assertEquals(s.getPosition(), new Position(5, 6));
        s.move();
        assertEquals(s.getPosition(), new Position(5, 6));
        s.move();
        assertEquals(s.getPosition(), new Position(5, 6));

    }

    @Test
    public void testSpiderMoveReverse() {

        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 4), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        p.setDungeon(d);
        Spider s = new Spider(new Position(5, 6), d);
        Boulder b = new Boulder(new Position(6, 5), d);
        Boulder b1 = new Boulder(new Position(4, 7), d);
        list.add(b);
        list.add(b1);
        list.add(s);
        d.setEntities(list);
        assertEquals(s.getPosition(), new Position(5, 6));
        s.move();
        assertEquals(s.getPosition(), new Position(5, 5));
        s.move();
        assertEquals(s.getPosition(), new Position(4, 5));
        s.move();
        assertEquals(s.getPosition(), new Position(4, 6));
        s.move();
        assertEquals(s.getPosition(), new Position(4, 5));
    }

    @Test
    public void testSpiderMoveSwampTile() {

        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 4), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        p.setDungeon(d);
        Spider s = new Spider(new Position(5, 6), d);
        SwampTile t = new SwampTile(new Position(5, 5), d, 2);
        list.add(s);
        list.add(t);
        d.setEntities(list);
        assertEquals(s.getPosition(), new Position(5, 6));
        s.move();
        assertEquals(s.getPosition(), new Position(5, 5));
        s.move();
        assertEquals(s.getPosition(), new Position(5, 5));
        s.move();
        assertEquals(s.getPosition(), new Position(6, 5));

    }

    @Test
    public void testZombieMoveSwampTile() {

        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 4), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        p.setDungeon(d);
        ZombieToast z = new ZombieToast(new Position(5, 6), d);
        SwampTile t = new SwampTile(new Position(5, 6), d, 2);
        list.add(t);
        list.add(z);
        d.setEntities(list);
        assertEquals(z.getPosition(), new Position(5, 6));
        z.move();
        assertEquals(z.getPosition(), new Position(5, 6));
        z.move();
        assertNotEquals(z.getPosition(), new Position(5, 6));

    }

}
