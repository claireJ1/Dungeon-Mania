package dungeonmania.entity.buildableEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.Character;
import dungeonmania.Dungeon;
import dungeonmania.entity.collectableEntity.CollectableEntity;
import dungeonmania.util.Position;

public class Sceptre extends BuildableEntity {

    public static int idNum = 1;
    private int durability = 3;

    public Sceptre(Position pos, Dungeon dun) {
        super(pos, dun, "sceptre_" + Integer.toString(idNum), "sceptre");
        idNum++;
    }

    public Sceptre(Position pos, Dungeon dun, int durability) {
        super(pos, dun, "sceptre_" + Integer.toString(idNum), "sceptre");
        this.durability = durability;
        idNum++;
    }

    @Override
    public void build(List<CollectableEntity> items, List<BuildableEntity> itemsEquipped) {

        CollectableEntity sunStone = items.stream().filter(e -> e.getType().equals("sun_stone")).findFirst()
                                                   .orElse(null);
        items.remove(sunStone);
        
        CollectableEntity treasure = items.stream().filter(e -> e.getType().equals("treasure")).findFirst()
                                                   .orElse(null);

        CollectableEntity wood = items.stream().filter(e -> e.getType().equals("wood")).findFirst()
                                               .orElse(null);

        if (treasure != null) {
            items.remove(treasure);
        } else {
            CollectableEntity key = items.stream().filter(e -> e.getType().equals("key")).findFirst().orElse(null);
            items.remove(key);
        }

        if (wood != null) {
            items.remove(wood);
        } else {
            List<CollectableEntity> arrows = items.stream().filter(e -> e.getType().equals("arrow"))
                .collect(Collectors.toList());
            items.remove(arrows.get(0));
            items.remove(arrows.get(1));
        }


        itemsEquipped.add(this);
        this.equip(super.getCharacter());
    }

    @Override
    public void equip(Character c) {
        c.setSceptreEquipped(this);
    }

    @Override
    public boolean builable(List<CollectableEntity> items) {
        Map<String, Long> count = items.stream()
                .collect(Collectors.groupingBy(i -> i.getType(), Collectors.counting()));

        if (!(count.containsKey("sun_stone") && count.get("sun_stone") >= 1) || 
            !((count.containsKey("treasure") && count.get("treasure") >= 1) || (count.containsKey("key") && count.get("key") >= 1)) ||
            !((count.containsKey("wood") && count.get("wood") >= 1) || (count.containsKey("arrow") && count.get("arrow") >= 2))) {
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
            ch.setSceptreEquipped(null);
            ch.replaceBuildableItem(this);
        }

    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("durability", durability);
        j.put("type", "sceptre");
        return j;
    }

}

