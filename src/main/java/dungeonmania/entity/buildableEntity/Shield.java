package dungeonmania.entity.buildableEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.Character;
import dungeonmania.Dungeon;
import dungeonmania.entity.DefenseEntity;
import dungeonmania.entity.collectableEntity.CollectableEntity;
import dungeonmania.util.Position;

public class Shield extends BuildableEntity implements DefenseEntity {

    public static int idNum = 1;
    private int durability = 2;

    public Shield(Position pos, Dungeon dun) {
        super(pos, dun, "shield_" + Integer.toString(idNum), "shield");
        idNum++;
    }

    public Shield(Position pos, Dungeon dun, int durability) {
        super(pos, dun, "shield_" + Integer.toString(idNum), "shield");
        this.durability = durability;
        idNum++;
    }

    @Override
    public void build(List<CollectableEntity> items, List<BuildableEntity> itemsEquipped) {

        CollectableEntity treasure = items.stream().filter(e -> e.getType().equals("treasure")).findFirst()
                .orElse(null);

        List<CollectableEntity> woods = items.stream().filter(e -> e.getType().equals("wood"))
                .collect(Collectors.toList());
        items.remove(woods.get(0));
        items.remove(woods.get(1));

        if (treasure != null) {
            items.remove(treasure);
        } else {
            CollectableEntity key = items.stream().filter(e -> e.getType().equals("key")).findFirst().orElse(null);
            items.remove(key);
        }

        itemsEquipped.add(this);
        this.equip(super.getCharacter());
    }

    @Override
    public void equip(Character c) {
        if (!c.hasDefense(this)) {
            c.addDefense(this);
        }
    }

    @Override
    public boolean builable(List<CollectableEntity> items) {
        Map<String, Long> count = items.stream()
                .collect(Collectors.groupingBy(i -> i.getType(), Collectors.counting()));

        if (!(count.containsKey("wood") && count.get("wood") >= 2)
                || !((count.containsKey("treasure") && count.get("treasure") >= 1)
                        || (count.containsKey("key") && count.get("key") >= 1))) {
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
            ch.replaceBuildableItem(this);
        }

    }

    @Override
    public int getDurability() {
        return this.durability;
    }

    @Override
    public double getDefenseCoefficient(double init) {
        return init * 0.35;
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("durability", durability);
        j.put("type", "shield");
        return j;
    }

}


