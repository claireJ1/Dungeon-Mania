package dungeonmania.entity.collectableEntity;

import org.json.JSONObject;

import dungeonmania.*;
import dungeonmania.Character;
import dungeonmania.util.Position;

public class TheOneRing extends CollectableEntity {
    private static int idNum = 1;

    public TheOneRing(Position pos, Dungeon dun) {
        super(pos, dun, "one_ring_" + Integer.toString(idNum), "one_ring");
        idNum++;
    }

    @Override
    public void use(Character c) {
        c.getDungeon().spawnPlayer();
        c.setHealthPoint(c.getFullHealthPoint());
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("type", "one_ring");
        return j;
    }

}


