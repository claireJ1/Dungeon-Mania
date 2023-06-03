package dungeonmania.entity.staticEntity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.EntityHelper;
import dungeonmania.entity.LogicEntity;
import dungeonmania.util.Position;

public class LightBulb extends StaticEntity implements LogicEntity {
    public static int idNum = 1;

    private String logic;
    private boolean isOn;

    public LightBulb(Position pos, Dungeon dun, String logic) {
        super(pos.asLayer(2), dun, "light_bulb_" + Integer.toString(idNum), "light_bulb_off", false, null);
        idNum++;
        this.logic = logic;
        this.isOn = false;
    }

    public LightBulb(String id, Position pos, Dungeon dun, String logic) {
        super(pos.asLayer(2), dun, id, "light_bulb_off", false, null);
        this.logic = logic;
        this.isOn = false;
    }

    public Boolean getIsOn() { return isOn; }

    @Override
    public boolean power() {
        int switches = 0;
        int powered = 0;
        List<Integer> activations = new ArrayList<>();
        for (LogicEntity e: getDungeon().getAdjacentLogics(getPosition())) {
            if (e instanceof FloorSwitch) {
                switches++;
                if (e.power()) powered++;
                if (((FloorSwitch)e).getLastActivation() >= 0) activations.add(((FloorSwitch)e).getLastActivation());
            } else if (e instanceof Wire) {
                switches++;
                if (e.power()) powered++;
                if (((Wire)e).getLastActivation() >= 0) activations.add(((Wire)e).getLastActivation());
            }
        }
        int maxActivation = EntityHelper.maxSameActivation(activations);

        switch (logic) {
            case "and":
                if (switches <= 2) {
                    if (powered >= 2) return true;
                } else if (switches > 2 && powered >= switches) return true;
                break;
            case "or": if (powered >= 1) return true; break;
            case "xor": if (powered == 1) return true; break;
            case "not": if (powered == 0) return true; break;
            case "co_and": if (powered >= 2 && maxActivation >= 2) return true; break;
        }
        return false;
    }

    @Override
    public void update() {
        if (power()) setType("light_bulb_on"); else setType("light_bulb_off");
    }

    @Override
    public JSONObject toJson() {
        JSONObject lightBulb = new JSONObject();
        lightBulb.put("x", getPosition().getX());
        lightBulb.put("y", getPosition().getY());
        lightBulb.put("z", getPosition().getLayer());
        lightBulb.put("type", "light_bulb_off");
        lightBulb.put("logic", logic);
        return lightBulb;
    }
}
