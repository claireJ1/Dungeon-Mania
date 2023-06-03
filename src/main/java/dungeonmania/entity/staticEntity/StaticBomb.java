package dungeonmania.entity.staticEntity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.EntityHelper;
import dungeonmania.entity.LogicEntity;
import dungeonmania.util.Position;

public class StaticBomb extends StaticEntity implements LogicEntity {
    private static int idNum = 1;

    private String logic = null;

    public StaticBomb(Position pos, Dungeon dun) {
        super(pos.asLayer(2), dun, "static_bomb_" + Integer.toString(idNum), "bomb", true, null);
        idNum++;
    }

    public StaticBomb(Position pos, Dungeon dun, String logic) {
        super(pos.asLayer(2), dun, "static_bomb_" + Integer.toString(idNum), "bomb", true, null);
        this.logic = logic;
        idNum++;
    }

    /**
     * Check if there is switch next to the bomb is turned on
     * If the switch is turned on than the bomb blast
     */
    public void checkStaticBomb() {
        for (Entity e : getDungeon().getEntities()) {
            if (isAdjacentEntity(getPosition(), e.getPosition()) && (getDungeon().existEntityByPositionLayer(e.getPosition(), 1) instanceof FloorSwitch)) {
                if(((FloorSwitch)e).getIsTurnOn()) {
                    destroyAdjacentEntities();
                    getDungeon().removeEntity(this);
                    break;
                }
            }
        }
    }


    /**
     * Destroy adjacent entities around the bomb
     */
    public void destroyAdjacentEntities() {
        for(Position p : getPosition().getAdjacentPositions()) {
            List<Entity> entities = getDungeon().getPositionEntity(p);
            for(Entity e : entities) {
                getDungeon().removeEntity(e);
            }
        }
    }

    /**
     * Check if two entities are adjacent
     * @param p1 the position of the first entity
     * @param p2 the position of the second entity
     * @return true if these two entities are adjacent
     *         false if they are not adjacent
     */
    public boolean isAdjacentEntity(Position p1, Position p2) {
        if (p1.getX() == p2.getX()) {
            if (p1.getY() == p2.getY() + 1 || p1.getY() == p2.getY() - 1) {
                return true;
            }
        }
        if (p1.getY() == p2.getY()) {
            if (p1.getX() == p2.getX() + 1 || p1.getX() == p2.getX() - 1) {
                return true;
            }
        }
        return false;
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

        if (logic == null) {
            if (powered > 0) return true;
        } else {
            switch (logic) {
                case "and":
                    if (switches <= 2) {
                        if (powered >= 2) return true;
                    } else if (switches > 2 && powered >= switches) return true;
                    break;
                case "or": if (powered >= 1) return true; break;
                case "xor": if (powered == 1) return true; break;
                case "co_and": if (powered >= 2 && maxActivation >= 2) return true; break;
            }
        }
        return false;
    }

    @Override
    public void update() {
        if (power()) {
            destroyAdjacentEntities();
            getDungeon().removeEntity(this);
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject staticBomb = new JSONObject();
        staticBomb.put("x", getPosition().getX());
        staticBomb.put("y", getPosition().getY());
        staticBomb.put("z", getPosition().getLayer());
        staticBomb.put("type", "static_bomb");
        if (logic != null) {
            staticBomb.put("logic", logic);
        }
        return staticBomb;
    }
}
