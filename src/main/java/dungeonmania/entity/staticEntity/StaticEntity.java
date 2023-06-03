package dungeonmania.entity.staticEntity;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public abstract class StaticEntity extends Entity {

    private String group;

    public StaticEntity(Position pos, Dungeon dun, String id, String type, Boolean interactable, String group) {
        super(pos, dun, id, type, interactable);
        this.group = group;
    }

    public String getGroup() {
        return group;
    }
    
}
