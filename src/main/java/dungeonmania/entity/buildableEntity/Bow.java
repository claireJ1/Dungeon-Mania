package dungeonmania.entity.buildableEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.Character;
import dungeonmania.Dungeon;
import dungeonmania.entity.Weapon;
import dungeonmania.entity.collectableEntity.CollectableEntity;
import dungeonmania.entity.movingEntity.MovingEntity;
import dungeonmania.util.Position;

public class Bow extends BuildableEntity implements Weapon {

    public static int idNum = 1;
    private int durability = 2;

    public Bow(Position pos, Dungeon dun) {
        super(pos, dun, "bow_" + Integer.toString(idNum), "bow");
        idNum++;
    }

    public Bow(Position p, Dungeon d, int durability) {
        super(p, d, "bow_" + Integer.toString(idNum), "bow");
        this.durability = durability;
        idNum++;
    }


    @Override
    public void build(List<CollectableEntity> items, List<BuildableEntity> itemsEquipped) {
        CollectableEntity wood = items.stream().filter(e -> e.getType().equals("wood")).findAny().orElse(null);

        List<CollectableEntity> arrows = items.stream().filter(e -> e.getType().equals("arrow"))
                .collect(Collectors.toList());

        items.remove(wood);
        items.remove(arrows.get(0));
        items.remove(arrows.get(1));
        items.remove(arrows.get(2));

        itemsEquipped.add(this);
        this.equip(super.getCharacter());
    }

    @Override
    public void equip(Character c) {
        if (!c.hasWeapon(this)) {
            c.addWeapon(this);
        }
    }

    @Override
    public boolean builable(List<CollectableEntity> items) {
        Map<String, Long> count = items.stream()
                .collect(Collectors.groupingBy(i -> i.getType(), Collectors.counting()));

        if (!count.containsKey("wood") || !count.containsKey("arrow") 
            || count.get("wood") < 1 || count.get("arrow") < 3) {
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
            ch.removeWeapon(this);
            ch.replaceBuildableItem(this);
        }

    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public int getAttackDamage(int init, MovingEntity enemy) {
        return init * 2;
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("durability", durability);
        j.put("type", "bow");
        return j;
    }
}

