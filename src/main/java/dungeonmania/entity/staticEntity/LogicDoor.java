package dungeonmania.entity.staticEntity;

import java.util.List;

import org.json.JSONObject;

import java.util.ArrayList;

import dungeonmania.Dungeon;
import dungeonmania.entity.EntityHelper;
import dungeonmania.entity.LogicEntity;
import dungeonmania.util.Position;

public class LogicDoor extends StaticEntity implements LogicEntity {
    public static int idNum = 1;

    private String logic;
    private boolean isOpen;

    public LogicDoor(Position pos, Dungeon dun, String logic) {
        super(pos.asLayer(2), dun, "logic_door_" + Integer.toString(idNum), "logic_door", false, null);
        idNum++;
        this.logic = logic;
        this.isOpen = false;
    }

    public LogicDoor(Position pos, Dungeon dun, String logic, boolean isOpen) {
        super(pos.asLayer(2), dun, "logic_door_" + Integer.toString(idNum), "logic_door", false, null);
        idNum++;
        this.logic = logic;
        this.isOpen = isOpen;
    }

    public boolean getIsOpen() { return isOpen; }

    public void setIsOpen(boolean arg) {
        this.isOpen = arg;
        if (arg) setPosition(getPosition().asLayer(1));
        else setPosition(getPosition().asLayer(2));
    }

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
        if (power()) setIsOpen(true); else setIsOpen(false);
    }

    @Override
    public JSONObject toJson() {
        JSONObject logicDoor = new JSONObject();
        logicDoor.put("x", getPosition().getX());
        logicDoor.put("y", getPosition().getY());
        logicDoor.put("z", getPosition().getLayer());
        logicDoor.put("type", "logic_door");
        logicDoor.put("logic", logic);
        logicDoor.put("isOpen", isOpen);
        return logicDoor;
    }
}
