package dungeonmania.entity.collectableEntity;

import org.json.JSONObject;

import dungeonmania.*;
import dungeonmania.Character;
import dungeonmania.entity.staticEntity.*;
import dungeonmania.entity.EntityEquip;
import dungeonmania.util.Position;

public class Key extends CollectableEntity implements EntityEquip {

    private String group;
    private Door correspondingDoor;

    public static int idNum = 1;

    public Key(Position pos, Dungeon dun, String group) {
        super(pos, dun, "key_" + Integer.toString(idNum), "key");
        idNum++;
        this.group = group;
    }

    @Override
    public void use(Character c) {
    }

    @Override
    public void equip(Character c) {
        if (c.getKey() == null) {
            c.setKey(this);
        }
    }

    @Override
    public int getDurability() {
        return 0;
    }
    
    public String getGroup() {
        return group;
    }

    public Door getDoor() {
        return correspondingDoor;
    }

    public void setDoor(Door correspondingDoor) {
        this.correspondingDoor = correspondingDoor;
    }

    /**
     * If the door is the corresponding Door of the key then open it else do nothing
     * @param door
     */
    public void openDoor(Door door) {
        if (getDoor() == door) {
            door.setIsOpen(true);
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("type", "key");
        j.put("colour", group);
        return j;
    }

}


