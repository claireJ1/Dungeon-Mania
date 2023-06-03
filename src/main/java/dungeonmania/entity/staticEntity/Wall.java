package dungeonmania.entity.staticEntity;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class Wall extends StaticEntity {
    public static int idNum = 1;

    public Wall(Position pos, Dungeon dun) {
        super(pos.asLayer(2), dun, "wall_" + Integer.toString(idNum), "wall", false, null);
        idNum++;
    }

    @Override
    public JSONObject toJson() {
        JSONObject wall = new JSONObject();
        wall.put("x", getPosition().getX());
        wall.put("y", getPosition().getY());
        wall.put("z", getPosition().getLayer());
        wall.put("type", "wall");
        return wall;
    }

}
