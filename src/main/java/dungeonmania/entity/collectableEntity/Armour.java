package dungeonmania.entity.collectableEntity;

import org.json.JSONObject;

import dungeonmania.*;
import dungeonmania.Character;
import dungeonmania.entity.DefenseEntity;
import dungeonmania.util.Position;

public class Armour extends CollectableEntity implements DefenseEntity {

    private int durability = 2;

    private static int idNum = 1;

    public Armour(Position pos, Dungeon dun) {
        super(pos, dun, "armour_" + Integer.toString(idNum), "armour");
        idNum++;
    }

    public Armour(Position pos, Dungeon dun, int durability) {
        super(pos, dun, "armour_" + Integer.toString(idNum), "armour");
        this.durability = durability;
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
            c.removeDefense(this);
            c.replaceItemEquipped(this);
        }
    }

    @Override
    public void equip(Character c) {
        if (!c.hasDefense(this)) {
            c.addDefense(this);
        }
    }

    @Override
    public double getDefenseCoefficient(double init) {
        return init * 0.5;
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("durability", durability);
        j.put("type", "armour");
        return j;
    }

}

