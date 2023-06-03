package dungeonmania.entity.buildableEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.Character;
import dungeonmania.Dungeon;
import dungeonmania.entity.DefenseEntity;
import dungeonmania.entity.Weapon;
import dungeonmania.entity.collectableEntity.CollectableEntity;
import dungeonmania.entity.movingEntity.MovingEntity;
import dungeonmania.util.Position;

public class MidnightArmour extends BuildableEntity implements Weapon, DefenseEntity {

    public static int idNum = 1;
    private int durability = 6;

    public MidnightArmour(Position pos, Dungeon dun) {
        super(pos, dun, "midnight_armour_" + Integer.toString(idNum), "midnight_armour");
        idNum++;
    }

    public MidnightArmour(Position pos, Dungeon d, int durability) {
        super(pos, d, "midnight_armour_" + Integer.toString(idNum), "midnight_armour");
        this.durability = durability;
        idNum++;
    }

    @Override
    public void build(List<CollectableEntity> items, List<BuildableEntity> itemsEquipped) {

        CollectableEntity sunStone = items.stream().filter(e -> e.getType().equals("sun_stone")).findFirst().orElse(null);
        items.remove(sunStone);


        CollectableEntity armour = items.stream().filter(e -> e.getType().equals("armour")).filter(e -> !super.getCharacter().containsDefense((DefenseEntity)e)).findFirst().orElse(null);
        items.remove(armour);

        itemsEquipped.add(this);
        this.equip(super.getCharacter());
    }

    @Override
    public void equip(Character c) {
        if (!c.hasDefense(this) && !c.hasWeapon(this)) {
            c.addDefense(this);
            c.addWeapon(this);
        }
        
    }

    @Override
    public boolean builable(List<CollectableEntity> items) {
        Map<String, Long> count = items.stream().collect(Collectors.groupingBy(i -> i.getType(), Collectors.counting()));

        if (!(count.containsKey("sun_stone") && count.get("sun_stone") >= 1) || 
            !(count.containsKey("armour") && count.get("armour") >= 2)) {
            return false;
        }                           

        return true;
    }

    @Override
    public void use(Character ch) {
        // reduce durability
        if (this.durability > 0) {
            this.durability--;
        }

        // equip a new one if there exists one in inventory
        if (this.durability == 0) {
            ch.removeDefense(this);
            ch.removeWeapon(this);
            ch.replaceBuildableItem(this);
        }

    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public double getDefenseCoefficient(double init) {
        return init * 0.5;
    }

    @Override
    public int getAttackDamage(int init, MovingEntity enemy) {
        return init++;
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("durability", durability);
        j.put("type", "midnight_armour");
        return j;
    }

}