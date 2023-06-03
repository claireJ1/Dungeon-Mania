package dungeonmania.entity.collectableEntity;

import org.json.JSONObject;

import dungeonmania.*;
import dungeonmania.Character;
import dungeonmania.util.Position;

public class Wood extends CollectableEntity {
    public static int idNum = 1;

    public Wood(Position pos, Dungeon dun) {
        super(pos, dun, "wood_" + Integer.toString(idNum), "wood");
        idNum++;
    }

    @Override
    public void use(Character c) {
        // Do nothing;
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("type", "wood");
        return j;
    }

}

