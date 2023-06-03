package dungeonmania.entity.staticEntity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.*;
import dungeonmania.util.Position;

public class Wire extends StaticEntity implements LogicEntity {
    public static int idNum = 1;

    private int lastActivation = -1;

    public Wire(Position pos, Dungeon dun) {
        super(pos.asLayer(2), dun, "wire_" + Integer.toString(idNum), "wire", false, null);
        idNum++;
    }

    public Wire(Position pos, Dungeon dun, int lastActivation) {
        super(pos.asLayer(2), dun, "wire_" + Integer.toString(idNum), "wire", false, null);
        this.lastActivation = lastActivation;
        idNum++;
    }

    public int getLastActivation() { return lastActivation; }

    @Override
    public boolean power() {
        List<Entity> adj = new ArrayList<>();
        for (Position pos: getPosition().getCardinallyAdjacentPositions()) adj.addAll(getDungeon().getPositionEntity(pos));
        for (Entity e: adj) {
            if (e instanceof FloorSwitch && ((FloorSwitch)e).getIsTurnOn()) return true;
            if (e instanceof Wire && ((Wire)e).getLastActivation() >= 0) return true;
        }
        return false;
    }

    @Override
    public void update() {
        if (power()) if (lastActivation < 0) this.lastActivation = 0; else lastActivation++;
        else this.lastActivation = -1;
    }

    @Override
    public JSONObject toJson() {
        JSONObject wire = new JSONObject();
        wire.put("x", getPosition().getX());
        wire.put("y", getPosition().getY());
        wire.put("z", getPosition().getLayer());
        wire.put("type", "wire");
        wire.put("lastActivation", lastActivation);
        return wire;
    }
}
