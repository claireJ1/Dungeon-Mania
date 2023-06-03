package dungeonmania.entity.collectableEntity;

import org.json.JSONObject;

import dungeonmania.*;
import dungeonmania.Character;
import dungeonmania.entity.Weapon;
import dungeonmania.entity.movingEntity.MovingEntity;
import dungeonmania.util.Position;

public class Sword extends CollectableEntity implements Weapon {

    private static final int attackDamage = 2;
    private int durability = 10;

    private static int idNum = 1;

    public Sword(Position pos, Dungeon dun) {
        super(pos, dun, "sword_" + Integer.toString(idNum), "sword");
        idNum++;
    }

    public Sword(Position pos, Dungeon dun, int durability) {
        super(pos, dun, "sword_" + Integer.toString(idNum), "sword");
        this.durability = durability;
        idNum++;
    }

    public int getAttackDamage() {
        return Sword.attackDamage;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public void use(Character c) {
        // reduce durability
        if (this.durability > 0) {
            this.durability--;
        } 
        // equip a new one if there exists one in inventory
        if (this.durability == 0) {
            c.removeWeapon(this);
            c.replaceItemEquipped(this);
        }
    }

    @Override
    public void equip(Character c) {
        if (!c.hasWeapon(this)) {
            c.addWeapon(this);
        }
    }

    @Override
    public int getAttackDamage(int init, MovingEntity enemy) {
        return init + attackDamage;
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("durability", durability);
        j.put("type", "sword");
        return j;
    }

}
