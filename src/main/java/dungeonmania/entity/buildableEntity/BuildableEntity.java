package dungeonmania.entity.buildableEntity;

import java.util.List;

import dungeonmania.Character;
import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.collectableEntity.CollectableEntity;
import dungeonmania.entity.*;
import dungeonmania.util.Position;

public abstract class BuildableEntity extends Entity implements EntityEquip {

    public BuildableEntity(Position pos, Dungeon dun, String id, String type) {
        super(pos, dun, id, type);
    }

    public abstract boolean builable(List<CollectableEntity> items);

    public abstract void build(List<CollectableEntity> items, List<BuildableEntity> itemsEquipped);

    public abstract void use(Character ch);

}

