package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import dungeonmania.util.Position;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import dungeonmania.entity.*;
import dungeonmania.entity.movingEntity.Hydra;
import dungeonmania.entity.staticEntity.Boulder;

import dungeonmania.entity.staticEntity.*;

public class HydraTest {

    @Test
    public void testHydraMove() {

        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 4), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        p.setDungeon(d);
        Hydra h = new Hydra(new Position(5, 6), d);
        list.add(h);
        d.setEntities(list);
        assertEquals(h.getPosition(), new Position(5, 6));
        h.move();
        assertNotEquals(h.getPosition(), new Position(5, 6));

    }

    @Test
    public void testHydraMoveFail() {

        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 4), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        p.setDungeon(d);
        Hydra h = new Hydra(new Position(5, 6), d);
        Boulder b = new Boulder(new Position(4, 6), d);
        ZombieToastSpawner z1 = new ZombieToastSpawner(new Position(6, 6), d);
        Door door = new Door(new Position(5, 5), d, "123");
        Wall wall1 = new Wall(new Position(5, 7), d);
        list.add(h);
        list.add(b);
        list.add(z1);
        list.add(door);
        list.add(wall1);
        d.setEntities(list);
        assertEquals(h.getPosition(), new Position(5, 6));
        for (int i = 0; i < 15; i++) {
            h.move();
        }
        assertEquals(h.getPosition(), new Position(5, 6));

    }

    @Test
    public void testHydraMoveSwampTile() {

        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 4), null);

        Dungeon d = new Dungeon("1", "1", null, p, null);
        p.setDungeon(d);
        Hydra h = new Hydra(new Position(5, 6), d);
        SwampTile t = new SwampTile(new Position(5, 6), d, 2);
        list.add(h);
        list.add(t);
        d.setEntities(list);
        assertEquals(h.getPosition(), new Position(5, 6));
        h.move();
        assertEquals(h.getPosition(), new Position(5, 6));
        h.move();
        assertNotEquals(h.getPosition(), new Position(5, 6));

    }

    @Test
    public void testHydraMoveSwampTile2() {

        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 4), null);

        Dungeon d = new Dungeon("1", "1", null, p, null);
        p.setDungeon(d);
        Hydra h = new Hydra(new Position(5, 6), d);
        SwampTile t = new SwampTile(new Position(5, 6), d, 3);
        list.add(h);
        list.add(t);
        d.setEntities(list);
        assertEquals(h.getPosition(), new Position(5, 6));
        h.move();
        assertEquals(h.getPosition(), new Position(5, 6));
        h.move();
        assertEquals(h.getPosition(), new Position(5, 6));
        h.move();
        assertNotEquals(h.getPosition(), new Position(5, 6));

    }

}
