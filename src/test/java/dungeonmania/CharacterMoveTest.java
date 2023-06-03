package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import dungeonmania.entity.*;
import dungeonmania.entity.staticEntity.Boulder;
import dungeonmania.entity.staticEntity.Door;
import dungeonmania.entity.staticEntity.Wall;
import dungeonmania.entity.collectableEntity.*;

public class CharacterMoveTest {
    @Test
    public void testMove() {

        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 2), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        d.setEntities(list);

        p.setDungeon(d);

        p.move(Direction.UP);
        assertEquals(p.getPosition(), new Position(1, 1));

    }

    @Test
    public void testMoveFail() {
        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 2), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        Wall wall = new Wall(new Position(1, 1), d);
        list.add(wall);
        d.setEntities(list);

        p.setDungeon(d);

        p.move(Direction.UP);
        assertEquals(p.getPosition(), new Position(1, 2));

    }

    @Test
    public void pushBoulder() {
        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 4), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        Boulder b = new Boulder(new Position(1, 3), d);
        Wall wall = new Wall(new Position(1, 1), d);
        list.add(wall);
        list.add(b);
        d.setEntities(list);

        p.setDungeon(d);

        p.move(Direction.UP);
        assertEquals(p.getPosition(), new Position(1, 3));
        assertEquals(b.getPosition(), new Position(1, 2));

    }

    @Test
    public void pushBoulderFail() {
        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 4), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        Boulder b = new Boulder(new Position(1, 3), d);
        Boulder b1 = new Boulder(new Position(1, 2), d);
        list.add(b1);
        list.add(b);
        d.setEntities(list);

        p.setDungeon(d);

        p.move(Direction.UP);
        assertEquals(p.getPosition(), new Position(1, 4));
        assertEquals(b.getPosition(), new Position(1, 3));

    }

    @Test
    public void pushBoulderFailSecond() {
        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 4), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        Boulder b = new Boulder(new Position(1, 3), d);
        Wall w1 = new Wall(new Position(1, 2), d);
        list.add(w1);
        list.add(b);
        d.setEntities(list);

        p.setDungeon(d);

        p.move(Direction.UP);
        assertEquals(p.getPosition(), new Position(1, 4));
        assertEquals(b.getPosition(), new Position(1, 3));

    }

    @Test
    public void moveAround() {
        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 4), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        d.setEntities(list);
        p.setDungeon(d);

        p.move(Direction.UP);
        p.move(Direction.RIGHT);
        p.move(Direction.RIGHT);
        p.move(Direction.DOWN);
        assertEquals(p.getPosition(), new Position(3, 4));
        p.move(Direction.LEFT);
        assertEquals(p.getPosition(), new Position(2, 4));
    }

    @Test
    public void moveDoor() {
        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 4), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        Door d1 = new Door(new Position(1, 3), d, "123");
        Key k1 = new Key(new Position(5, 5), d, "123");
        d1.setKey(k1);
        list.add(d1);
        list.add(k1);
        d.setEntities(list);
        p.setDungeon(d);
        p.move(Direction.UP);
        assertEquals(p.getPosition(), new Position(1, 4));
        p.setKey(k1);
        p.move(Direction.UP);
        assertEquals(p.getPosition(), new Position(1, 3));
    }

    @Test
    public void moveInvisiable() {
        List<Entity> list = new ArrayList<Entity>();
        Character p = new Character(new Position(1, 3), null);
        Dungeon d = new Dungeon("1", "1", null, p, null);
        Wall w1 = new Wall(new Position(1, 2), d);
        Wall w2 = new Wall(new Position(2, 2), d);
        Wall w3 = new Wall(new Position(2, 3), d);
        list.add(w1);
        list.add(w2);
        list.add(w3);
        d.setEntities(list);
        p.setDungeon(d);

        p.move(Direction.UP);
        assertEquals(p.getPosition(), new Position(1, 3));
        p.setInvisible(20);
        p.move(Direction.UP);
        assertEquals(p.getPosition(), new Position(1, 2));
        p.move(Direction.RIGHT);
        assertEquals(p.getPosition(), new Position(2, 2));
        p.move(Direction.DOWN);
        assertEquals(p.getPosition(), new Position(2, 3));
        p.move(Direction.UP);
        p.move(Direction.LEFT);
        assertEquals(p.getPosition(), new Position(1, 2));
        p.move(Direction.NONE);
        assertEquals(p.getPosition(), new Position(1, 2));

    }

}
