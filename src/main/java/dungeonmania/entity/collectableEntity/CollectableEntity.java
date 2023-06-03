package dungeonmania.entity.collectableEntity;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.util.*;

import dungeonmania.Character;

public abstract class CollectableEntity extends Entity {
    public static final int layer = 1;

    public CollectableEntity(Position pos, Dungeon dun, String id, String type) {
        super(pos.asLayer(CollectableEntity.layer), dun, id, type);
    }

    public abstract void use(Character c);

}

