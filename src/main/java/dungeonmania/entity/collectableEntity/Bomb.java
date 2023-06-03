package dungeonmania.entity.collectableEntity;

import org.json.JSONObject;

import dungeonmania.*;
import dungeonmania.Character;
import dungeonmania.util.Position;
import dungeonmania.entity.staticEntity.*;

public class Bomb extends CollectableEntity {
    public static int idNum = 1;

    private String logic = null;

    public Bomb(Position pos, Dungeon dun) {
        super(pos, dun, "bomb_" + Integer.toString(idNum), "bomb");
        idNum++;
    }

    public Bomb(Position pos, Dungeon dun, String logic) {
        super(pos, dun, "bomb_" + Integer.toString(idNum), "bomb");
        this.logic = logic;
        idNum++;
    }

    public String getLogic() {
        return logic;
    }

    @Override
    public void use(Character c) {
        StaticBomb staticBomb = new StaticBomb(c.getPosition(),c.getDungeon(), logic);
        this.getDungeon().addEntity(staticBomb);
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        if (logic != null) {
            j.put("logic", logic);
        }
        j.put("type", "bomb");
        return j;
    }
}

