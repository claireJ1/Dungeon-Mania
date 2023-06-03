package dungeonmania.entity.staticEntity;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.LogicEntity;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity implements LogicEntity {
    public static int idNum = 1;

    private int lastActivation = -1;

    public FloorSwitch(Position pos, Dungeon dun) {
        super(pos.asLayer(1), dun, "switch_" + Integer.toString(idNum), "switch", false, null);
        idNum++;
    }

    public FloorSwitch(Position pos, Dungeon dun, int lastActivation) {
        super(pos.asLayer(1), dun, "switch_" + Integer.toString(idNum), "switch", false, null);
        this.lastActivation = lastActivation;
        idNum++;
    }

    public int getLastActivation() { return lastActivation; }

    /**
     * detect if boulder on my head
     */
    public boolean getIsTurnOn() {
        for (Entity e: getDungeon().getPositionEntity(getPosition())) {
            if (e instanceof Boulder) return true;
        }
        return false;
    }

    @Override
    public boolean power() {
        return getIsTurnOn();
    }

    @Override
    public void update() {
        if (power()) if (lastActivation < 0) this.lastActivation = 0; else lastActivation++;
        else this.lastActivation = -1;
    }
    
    @Override
    public JSONObject toJson() {
        JSONObject floorSwitch = new JSONObject();
        floorSwitch.put("x", getPosition().getX());
        floorSwitch.put("y", getPosition().getY());
        floorSwitch.put("z", getPosition().getLayer());
        floorSwitch.put("type", "switch");
        floorSwitch.put("lastActivation", lastActivation);
        return floorSwitch;
    }
}
