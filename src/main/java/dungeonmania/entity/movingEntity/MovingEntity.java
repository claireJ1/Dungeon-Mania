package dungeonmania.entity.movingEntity;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.staticEntity.*;
import dungeonmania.entity.collectableEntity.Armour;
import dungeonmania.util.Position;

public abstract class MovingEntity extends Entity {
    private static final int layer = 2;
    private int frozen = 0;

    public abstract int getHealthPoint();

    public abstract int getAttackDamage();

    public abstract void setHealthPoint(int curHealth);

    public abstract double getDefenseCoefficient();

    public abstract Armour getArmour();

    public abstract void move();

    public MovingEntity(Position pos, Dungeon dun, String id, String type) {
        super(pos.asLayer(MovingEntity.layer), dun, id, type);
    }

    public MovingEntity(Position pos, Dungeon dun, String id, String type, Boolean interactable) {
        super(pos.asLayer(MovingEntity.layer), dun, id, type, interactable);
    }

    public int posY() {
        return getPosition().getY();
    }

    public int posX() {
        return getPosition().getX();
    }

    public int getFrozen() {
        return frozen;
    }

    /**
     * 
     * @param pos judge whether the pos entity stand is the swamp
     */
    public void swampMove(Position pos) {
        if (frozen == 0) {
            if (getDungeon().existEntityByPositionLayer(pos, 1) != null) {
                if (getDungeon().existEntityByPositionLayer(pos, 1).getType() == "swamp_tile") {
                    frozen = ((SwampTile) getDungeon().existEntityByPositionLayer(pos, 1)).getMovementFactor();
                }
            }
        }
        if (frozen > 0) {
            frozen--;
        }
    }

}

