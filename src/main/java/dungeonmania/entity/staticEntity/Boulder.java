package dungeonmania.entity.staticEntity;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.util.*;

public class Boulder extends StaticEntity {
    private static int idNum = 1;

    public Boulder(Position pos, Dungeon dun) {
        super(pos.asLayer(2), dun, "boulder_" + Integer.toString(idNum), "boulder", false, null);
        idNum++;
    }

    /**
     * called by player when her's pushing a boulder
     * @param direction
     */
    public void move(Direction direction) {
        switch (direction) {
            case UP:
                if (getDungeon().existEntityByPositionLayer(getPosition().translateBy(Direction.UP), 2) == null) setPosition(getPosition().translateBy(Direction.UP)); break;
            case LEFT:
                if (getDungeon().existEntityByPositionLayer(getPosition().translateBy(Direction.LEFT), 2) == null) setPosition(getPosition().translateBy(Direction.LEFT)); break;
            case DOWN:
                if (getDungeon().existEntityByPositionLayer(getPosition().translateBy(Direction.DOWN), 2) == null) setPosition(getPosition().translateBy(Direction.DOWN)); break;
            case RIGHT:
                if (getDungeon().existEntityByPositionLayer(getPosition().translateBy(Direction.RIGHT), 2) == null) setPosition(getPosition().translateBy(Direction.RIGHT)); break;
            case NONE:
                return;
        }
    }
    
    @Override
    public JSONObject toJson() {
        JSONObject boulder = new JSONObject();
        boulder.put("x", getPosition().getX());
        boulder.put("y", getPosition().getY());
        boulder.put("z", getPosition().getLayer());
        boulder.put("type", "boulder");
        return boulder;
    }
}
