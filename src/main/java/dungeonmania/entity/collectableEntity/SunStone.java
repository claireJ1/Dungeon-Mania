package dungeonmania.entity.collectableEntity;

import org.json.JSONObject;

import dungeonmania.*;
import dungeonmania.Character;
import dungeonmania.util.Position;
import dungeonmania.entity.staticEntity.*;

public class SunStone extends CollectableEntity{
    public static int idNum = 1;

    public SunStone(Position pos, Dungeon dun) {
        super(pos, dun, "sun_stone_" + Integer.toString(idNum), "sun_stone");
        idNum++;
    }

    @Override
    public void use(Character c) {
        // bribe mercenary
        // open door
    }

    public void sunStoneOpenDoor(Door door) {
        door.setIsOpen(true);
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("type", "sun_stone");
        return j;
    }
}
