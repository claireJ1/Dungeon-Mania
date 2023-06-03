package dungeonmania.entity.collectableEntity;

import org.json.JSONObject;

import dungeonmania.*;
import dungeonmania.Character;
import dungeonmania.util.Position;

public class Arrows extends CollectableEntity {
    public static int idNum = 1;

    public Arrows(Position pos, Dungeon dun) {
        super(pos, dun, "arrow_" + Integer.toString(idNum), "arrow");
        idNum++;
    }

    @Override
    public void use(Character c) {
        // Do nothing
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("type", "arrow");
        return j;
    }

}

