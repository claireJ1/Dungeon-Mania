package dungeonmania.entity.collectableEntity;

import org.json.JSONObject;

import dungeonmania.*;
import dungeonmania.Character;
import dungeonmania.util.Position;

public class Treasure extends CollectableEntity {
    public static int idNum = 1;

    public Treasure(Position pos, Dungeon dun) {
        super(pos, dun, "treasure_" + Integer.toString(idNum), "treasure");
        idNum++;
    }

    @Override
    public void use(Character c) {
        // bribe mercenary
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("type", "treasure");
        return j;
    }
}

