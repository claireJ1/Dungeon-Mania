package dungeonmania;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import dungeonmania.util.*;
import dungeonmania.entity.*;
import dungeonmania.entity.staticEntity.*;

public class TestLogicUnit {

    @Test
    public void testLayer() {
        Character player = new Character(new Position(0,0), null);
        Dungeon dun = new Dungeon("logic", "logic", new ArrayList<Entity>(), player, null);
        player.setDungeon(dun);

        LogicDoor ld = new LogicDoor(new Position(0, 0), dun, "or");
        StaticBomb sb = new StaticBomb(new Position(0, 1), dun, "and");
        LightBulb lb = new LightBulb(new Position(0, 2), dun, "xor");
        Wire w = new Wire(new Position(0, 3), dun);
        assertEquals(2, ld.getPosition().getLayer());
        assertEquals(2, sb.getPosition().getLayer());
        assertEquals(2, lb.getPosition().getLayer());
        assertEquals(2, w.getPosition().getLayer());
        ld.setIsOpen(true);
        assertEquals(1, ld.getPosition().getLayer());
    }

    @Test
    public void testWirePowered() {
        Character player = new Character(new Position(0,0), null);
        Dungeon dun = new Dungeon("logic", "logic", new ArrayList<Entity>(), player, null);
        player.setDungeon(dun);

        dun.addEntity(new FloorSwitch(new Position(1,1), dun));
        dun.addEntity(new Boulder(new Position(1,1), dun));
        Wire w1 = new Wire(new Position(1,0), dun);
        Wire w2 = new Wire(new Position(1,2), dun);
        Wire w3 = new Wire(new Position(0,1), dun);
        Wire w4 = new Wire(new Position(2,1), dun);
        Wire w5 = new Wire(new Position(5,5), dun);
        dun.addEntity(w1);
        dun.addEntity(w2);
        dun.addEntity(w3);
        dun.addEntity(w4);
        dun.addEntity(w5);
        assertTrue(w1.power());
        assertTrue(w2.power());
        assertTrue(w3.power());
        assertTrue(w4.power());
        assertFalse(w5.power());
        assertEquals(-1, w5.getLastActivation());
        w1.update();
        w2.update();
        w3.update();
        w4.update();
        w5.update();
        assertEquals(0, w1.getLastActivation());
        assertEquals(0, w2.getLastActivation());
        assertEquals(0, w3.getLastActivation());
        assertEquals(0, w4.getLastActivation());
        assertEquals(-1, w5.getLastActivation());
        w1.update();
        w2.update();
        w3.update();
        w4.update();
        w5.update();
        assertEquals(1, w1.getLastActivation());
        assertEquals(1, w2.getLastActivation());
        assertEquals(1, w3.getLastActivation());
        assertEquals(1, w4.getLastActivation());
        assertEquals(-1, w5.getLastActivation());
    }

    @Test
    public void testSwitchActivation() {
        Character player = new Character(new Position(0,0), null);
        Dungeon dun = new Dungeon("logic", "logic", new ArrayList<Entity>(), player, null);
        player.setDungeon(dun);

        FloorSwitch s = new FloorSwitch(new Position(1,1), dun);
        Boulder b = new Boulder(new Position(1,1), dun);
        dun.addEntity(s);
        dun.addEntity(b);
        assertTrue(s.power());
        s.update();
        assertEquals(0, s.getLastActivation());
        s.update();
        assertEquals(1, s.getLastActivation());

        b.setPosition(new Position(1,2));
        s.update();
        assertFalse(s.power());
        assertEquals(-1, s.getLastActivation());
    }

    @Test
    public void testLogicDoorPowered() {
        Character player = new Character(new Position(0,0), null);
        Dungeon dun = new Dungeon("logic", "logic", new ArrayList<Entity>(), player, null);
        player.setDungeon(dun);

        dun.addEntity(new FloorSwitch(new Position(1,1), dun));
        Boulder b = new Boulder(new Position(1,1), dun);
        dun.addEntity(b);
        LogicDoor ld = new LogicDoor(new Position(1,2), dun, "or");
        dun.addEntity(ld);
        ld.update();
        assertTrue(ld.power());
        assertTrue(ld.getIsOpen());
        assertEquals(1, ld.getPosition().getLayer());

        b.setPosition(new Position(2,1));
        ld.update();
        assertFalse(ld.power());
        assertFalse(ld.getIsOpen());
        assertEquals(2, ld.getPosition().getLayer());
    }

    @Test
    public void testLogicDoorPoweredByWire() {
        Character player = new Character(new Position(0,0), null);
        Dungeon dun = new Dungeon("logic", "logic", new ArrayList<Entity>(), player, null);
        player.setDungeon(dun);

        dun.addEntity(new FloorSwitch(new Position(1,1), dun));
        Boulder b = new Boulder(new Position(1,1), dun);
        dun.addEntity(b);
        dun.addEntity(new Wire(new Position(1,2), dun));
        LogicDoor ld = new LogicDoor(new Position(1,3), dun, "or");
        dun.addEntity(ld);
        ld.update();
        assertTrue(ld.power());
        assertTrue(ld.getIsOpen());
        assertEquals(1, ld.getPosition().getLayer());

        b.setPosition(new Position(2,1));
        ld.update();
        assertFalse(ld.power());
        assertFalse(ld.getIsOpen());
        assertEquals(2, ld.getPosition().getLayer());
    }

    @Test
    public void testStaticBombPowered() {
        Character player = new Character(new Position(0,0), null);
        Dungeon dun = new Dungeon("logic", "logic", new ArrayList<Entity>(), player, null);
        player.setDungeon(dun);

        dun.addEntity(new FloorSwitch(new Position(1,1), dun));
        Boulder b = new Boulder(new Position(1,1), dun);
        dun.addEntity(b);
        StaticBomb sb = new StaticBomb(new Position(1,2), dun, "or");
        dun.addEntity(sb);
        assertTrue(sb.power());

        b.setPosition(new Position(2,1));
        assertFalse(sb.power());
    }

    @Test
    public void testStaticBombPoweredByWire() {
        Character player = new Character(new Position(0,0), null);
        Dungeon dun = new Dungeon("logic", "logic", new ArrayList<Entity>(), player, null);
        player.setDungeon(dun);

        dun.addEntity(new FloorSwitch(new Position(1,1), dun));
        Boulder b = new Boulder(new Position(1,1), dun);
        dun.addEntity(b);
        dun.addEntity(new Wire(new Position(1,2), dun));
        StaticBomb sb = new StaticBomb(new Position(1,3), dun, "or");
        dun.addEntity(sb);
        assertTrue(sb.power());

        b.setPosition(new Position(2,1));
        assertFalse(sb.power());
    }

    @Test
    public void testLightBulbPowered() {
        Character player = new Character(new Position(0,0), null);
        Dungeon dun = new Dungeon("logic", "logic", new ArrayList<Entity>(), player, null);
        player.setDungeon(dun);

        dun.addEntity(new FloorSwitch(new Position(1,1), dun));
        Boulder b = new Boulder(new Position(1,1), dun);
        dun.addEntity(b);
        LightBulb lb = new LightBulb(new Position(1,2), dun, "or");
        dun.addEntity(lb);
        lb.update();
        assertTrue(lb.power());
        assertEquals("light_bulb_on", lb.getType());

        b.setPosition(new Position(2,1));
        lb.update();
        assertFalse(lb.power());
        assertEquals("light_bulb_off", lb.getType());
    }

    @Test
    public void testLightBulbPoweredByWire() {
        Character player = new Character(new Position(0,0), null);
        Dungeon dun = new Dungeon("logic", "logic", new ArrayList<Entity>(), player, null);
        player.setDungeon(dun);

        dun.addEntity(new FloorSwitch(new Position(1,1), dun));
        Boulder b = new Boulder(new Position(1,1), dun);
        dun.addEntity(b);
        dun.addEntity(new Wire(new Position(1,2), dun));
        LightBulb lb = new LightBulb(new Position(1,3), dun, "or");
        dun.addEntity(lb);
        lb.update();
        assertTrue(lb.power());
        assertEquals("light_bulb_on", lb.getType());

        b.setPosition(new Position(2,1));
        lb.update();
        assertFalse(lb.power());
        assertEquals("light_bulb_off", lb.getType());
    }

    @Test
    public void testLogicDoorLogicAnd() {
        Character player = new Character(new Position(0,0), null);
        Dungeon dun = new Dungeon("logic", "logic", new ArrayList<Entity>(), player, null);
        player.setDungeon(dun);

        dun.addEntity(new FloorSwitch(new Position(5,5), dun));
        Boulder b = new Boulder(new Position(5,5), dun);
        dun.addEntity(b);
        /*
         *      S x
         *      x D
         */
        dun.addEntity(new Wire(new Position(5,6), dun));
        LogicDoor and = new LogicDoor(new Position(6,6), dun, "and");
        dun.addEntity(and);
        and.update();
        assertFalse(and.power());
        assertFalse(and.getIsOpen());
        dun.addEntity(new Wire(new Position(6,5), dun));
        and.update();
        assertTrue(and.power());
        assertTrue(and.getIsOpen());
    }

    @Test
    public void testLogicDoorLogicXor() {
        Character player = new Character(new Position(0,0), null);
        Dungeon dun = new Dungeon("logic", "logic", new ArrayList<Entity>(), player, null);
        player.setDungeon(dun);

        dun.addEntity(new FloorSwitch(new Position(5,5), dun));
        Boulder b = new Boulder(new Position(5,5), dun);
        dun.addEntity(b);
        /*
         *      S x
         *      x D
         */
        dun.addEntity(new Wire(new Position(5,6), dun));
        LogicDoor ld = new LogicDoor(new Position(6,6), dun, "xor");
        dun.addEntity(ld);
        ld.update();
        assertTrue(ld.power());
        assertTrue(ld.getIsOpen());
        dun.addEntity(new Wire(new Position(6,5), dun));
        ld.update();
        assertFalse(ld.power());
        assertFalse(ld.getIsOpen());
    }

    @Test
    public void testLogicDoorLogicNot() {
        Character player = new Character(new Position(0,0), null);
        Dungeon dun = new Dungeon("logic", "logic", new ArrayList<Entity>(), player, null);
        player.setDungeon(dun);

        dun.addEntity(new FloorSwitch(new Position(5,5), dun));
        Boulder b = new Boulder(new Position(5,5), dun);
        dun.addEntity(b);
        /*
         *      S x
         *        D
         */
        LogicDoor ld = new LogicDoor(new Position(6,6), dun, "not");
        dun.addEntity(ld);
        ld.update();
        assertTrue(ld.power());
        assertTrue(ld.getIsOpen());
        dun.addEntity(new Wire(new Position(6,5), dun));
        ld.update();
        assertFalse(ld.power());
        assertFalse(ld.getIsOpen());
    }

    @Test
    public void testLogicDoorLogicCoAnd() {
        Character player = new Character(new Position(0,0), null);
        Dungeon dun = new Dungeon("logic", "logic", new ArrayList<Entity>(), player, null);
        player.setDungeon(dun);

        dun.addEntity(new FloorSwitch(new Position(5,5), dun));
        Boulder b = new Boulder(new Position(5,5), dun);
        dun.addEntity(b);
        /*
         *      S x
         *      x D
         */
        LogicDoor ld = new LogicDoor(new Position(6,6), dun, "co_and");
        dun.addEntity(ld);
        ld.update();
        assertFalse(ld.power());
        assertFalse(ld.getIsOpen());
        Wire w1 = new Wire(new Position(6,5), dun);
        Wire w2 = new Wire(new Position(5,6), dun);
        dun.addEntity(w1);
        dun.addEntity(w2);
        w1.update();
        w2.update();
        ld.update();
        assertTrue(ld.power());
        assertTrue(ld.getIsOpen());
        w1.update();
        ld.update();
        assertFalse(ld.power());
        assertFalse(ld.getIsOpen());
    }
}
