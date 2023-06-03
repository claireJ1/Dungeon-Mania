package dungeonmania.entity.collectableEntity;

import org.json.JSONObject;

import dungeonmania.Character;
import dungeonmania.Dungeon;
import dungeonmania.entity.Weapon;
import dungeonmania.entity.movingEntity.Boss;
import dungeonmania.entity.movingEntity.MovingEntity;
import dungeonmania.util.Position;

public class Anduril extends CollectableEntity implements Weapon {
    private static int idNum = 1;
    private int durability = 5;

    public Anduril(Position pos, Dungeon dun) {
        super(pos, dun, "anduril_" + Integer.toString(idNum), "anduril");
        idNum++;
    }

    public Anduril(Position pos, Dungeon dun, int dubrability) {
        super(pos, dun, "anduril_" + Integer.toString(idNum), "anduril");
        this.durability = dubrability;
        idNum++;
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
        if (enemy instanceof Boss) {
            init *= 3;
        }
        return init;
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("durability", durability);
        j.put("type", "anduril");
        return j;
    }
}
