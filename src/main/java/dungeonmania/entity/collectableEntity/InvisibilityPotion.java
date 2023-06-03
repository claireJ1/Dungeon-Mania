package dungeonmania.entity.collectableEntity;

import org.json.JSONObject;

import dungeonmania.*;
import dungeonmania.Character;
import dungeonmania.util.Position;

public class InvisibilityPotion extends CollectableEntity {
    public static int idNum = 1;

    private int lastTime = 10;

    public InvisibilityPotion(Position pos, Dungeon dun) {
        super(pos, dun, "invisibility_potion_" + Integer.toString(idNum), "invisibility_potion");
        idNum++;
    }

    @Override
    public void use(Character c) {
        c.setInvisible(c.getInvisible() + lastTime);
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("type", "invisibility_potion");
        return j;
    }
}

