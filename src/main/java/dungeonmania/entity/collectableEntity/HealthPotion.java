package dungeonmania.entity.collectableEntity;

import org.json.JSONObject;

import dungeonmania.*;
import dungeonmania.Character;
import dungeonmania.util.Position;

public class HealthPotion extends CollectableEntity {
    public static int idNum = 1;

    public HealthPotion(Position pos, Dungeon dun) {
        super(pos, dun, "health_potion_" + Integer.toString(idNum), "health_potion");
        idNum++;
    }

    @Override
    public void use(Character c) {
        c.setHealthPoint(c.getFullHealthPoint());
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("type", "health_potion");
        return j;
    }

}

