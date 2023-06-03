package dungeonmania.entity.staticEntity;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class SwampTile extends StaticEntity {
    public static int idNum = 1;
    private int movement_factor;

    public SwampTile(Position pos, Dungeon dun, int movement_factor) {
        super(pos.asLayer(1), dun, "swamp_tile_" + Integer.toString(idNum), "swamp_tile", false, null);
        this.movement_factor = movement_factor;
        idNum++;
    }

    public int getMovementFactor() {
        return movement_factor;
    }

    @Override
    public JSONObject toJson() {
        JSONObject swampTile = new JSONObject();
        swampTile.put("x", getPosition().getX());
        swampTile.put("y", getPosition().getY());
        swampTile.put("z", getPosition().getLayer());
        swampTile.put("type", "swamp_tile");
        swampTile.put("movement_factor", movement_factor);
        return swampTile;
    }
}
