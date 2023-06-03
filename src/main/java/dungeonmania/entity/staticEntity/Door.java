package dungeonmania.entity.staticEntity;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.collectableEntity.Key;
import dungeonmania.util.Position;

public class Door extends StaticEntity {
    public static int idNum = 1;

    private boolean isOpen;
    private Key key;

    public Door(Position pos, Dungeon dun, String group) {
        super(pos.asLayer(2), dun, "door_" + Integer.toString(idNum), "door", false, group);
        idNum++;
        this.isOpen = false;
    }

    public Door(Position pos, Dungeon dun, String group, boolean isOpen) {
        super(pos.asLayer(2), dun, "door_" + Integer.toString(idNum), "door", false, group);
        idNum++;
        this.isOpen = isOpen;
    }

    public boolean getIsOpen() { return isOpen; }

    public void setIsOpen(boolean arg) {
        this.isOpen = arg;
        if (arg) setPosition(getPosition().asLayer(1));
        else setPosition(getPosition().asLayer(2));
    }

    public Key getKey() { return key; }

    public void setKey(Key key) { this.key = key; }
    
    @Override
    public JSONObject toJson() {
        JSONObject door = new JSONObject();
        door.put("x", getPosition().getX());
        door.put("y", getPosition().getY());
        door.put("z", getPosition().getLayer());
        door.put("type", "door");
        door.put("colour", getGroup());
        door.put("isOpen", isOpen);
        return door;
    }
}

