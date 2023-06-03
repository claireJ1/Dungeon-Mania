package dungeonmania.entity;

import org.json.JSONObject;

import dungeonmania.entity.movingEntity.MovingEntity;

public interface Weapon extends EntityEquip {
    public int getAttackDamage(int init, MovingEntity enemy);
    public JSONObject toJson();
}
