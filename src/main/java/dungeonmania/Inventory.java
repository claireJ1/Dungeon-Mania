package dungeonmania;

import dungeonmania.entity.buildableEntity.BuildableEntity;
import dungeonmania.entity.buildableEntity.MidnightArmour;
import dungeonmania.entity.buildableEntity.Sceptre;
import dungeonmania.entity.collectableEntity.*;
import dungeonmania.exceptions.InvalidActionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.entity.Entity;
import dungeonmania.entity.EntityEquip;
import dungeonmania.entity.buildableEntity.Bow;
import dungeonmania.entity.buildableEntity.Shield;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class Inventory {
    private Character character;
    private List<CollectableEntity> items = new ArrayList<CollectableEntity>();
    private List<BuildableEntity> itemsEquipped = new ArrayList<BuildableEntity>();
    private Map<String, BuildableEntity> itemsBuildable = new HashMap<>();

    public Inventory(Character p) {
        this.character = p;
    }

    public Inventory(List<CollectableEntity> items, List<BuildableEntity> buildable) {
        this.items = items;
        itemsEquipped = buildable;
    }

    public void setCharacter(Character ch) {
        character = ch;
    }

    /**
     * Give a list of item character is equipped with or contained in the inventory 
     * @return list of item response 
     */
    public List<ItemResponse> itemInInventory() {
        List<ItemResponse> itemsInfo = new ArrayList<>();
        for (BuildableEntity b : itemsEquipped) {
            ItemResponse i = new ItemResponse(b.getId(), b.getType());
            itemsInfo.add(i);
        }
        for (CollectableEntity e : items) {
            ItemResponse i = new ItemResponse(e.getId(), e.getType());
            itemsInfo.add(i);
        } 
        return itemsInfo;
    }

    /**
     * Add item to the inventory 
     * @param e
     */
    public void addItem(CollectableEntity e) {
        if (e instanceof EntityEquip) {
            EntityEquip e1 = (EntityEquip) e;
            e1.equip(character);
        }
        this.items.add(e);
    } 

    /**
     * Check existence of key
     * @return
     */
    public boolean checkKeyExists() {

        CollectableEntity item = items.stream().filter(e -> e.getType().equals("key"))
                                               .findAny()
                                               .orElse(null);
        
        if (item == null) { return false; };
        return true;
    }

    /**
     * Given a item's id, use it, then remove it from inventory 
     * @param itemId
     * @preconditions 
     * @postcontitions items.length - 1
     * @throws IllegalArgumentException item is a bomb, health_potion, invincibility_potion, 
     * or an invisibility_potion item is not in the inventory
     */
    public void useItem(String itemId) throws IllegalArgumentException, InvalidActionException {
        
        if (itemId == null) {
            return;
        }
        CollectableEntity item = getItem(itemId);
        item.use(character);
        items.remove(item);
    }

    /**
     * build entity according to the entity name given
     * @param entityName
     * @preconditions entityName != null
     * @postconditions items.length decrease && character is equipped is the entity 
     * @throws IllegalArgumentException entityName is not bow, shield, sceptre or midnight armour
     * @throws InvalidActionException dont' have sufficient items to craft the entity
     */
    public void buildEntity(String entityName) throws IllegalArgumentException, InvalidActionException {
        
        // Check entity name is valid 
        if (entityName.equals("bow") || entityName.equals("shield") || entityName.equals("sceptre") || entityName.equals("midnight_armour")) {
            
            this.buildables();

            // check player has sufficient items to build it  
            if (itemsBuildable.containsKey(entityName)) {
                BuildableEntity b = itemsBuildable.get(entityName);
                b.build(items, itemsEquipped);
            } else {
                throw new InvalidActionException("Don't have sufficient items to craft a " + entityName);
            }
        } else {
            throw new IllegalArgumentException("Item is not buildable");
        }

    }

    /**
     * Give a list of buildable item types that the player can build, 
     * given their current inventory
     * @return
     */
    public List<String> buildables() {
        
        itemsBuildable.clear();
        Bow b = new Bow(new Position(-1, -1), character.getDungeon());
        Shield s = new Shield(new Position(-1, -1), character.getDungeon());
        Sceptre sceptre = new Sceptre(new Position(-1, -1), character.getDungeon());
        MidnightArmour midnightArmour = new MidnightArmour(new Position(-1, -1), character.getDungeon());

        Entity zombie = character.getDungeon().getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).findAny().orElse(null);

        if (b.builable(items)) {
            itemsBuildable.put("bow", b);
        }
        if (s.builable(items)) {
            itemsBuildable.put("shield", s);
        }
        if (sceptre.builable(items)) {
            itemsBuildable.put("sceptre", sceptre);
        }
        if (midnightArmour.builable(items) && zombie == null) {
            itemsBuildable.put("midnight_armour", midnightArmour);
        }
        List<String> output = new ArrayList<String>(itemsBuildable.keySet());
        return output;
    }

    /**
     * if inventory contains a treasure, remove it
     */
    public void useTreasure() throws InvalidActionException {
        CollectableEntity item = items.stream().filter(e -> e.getType().equals("treasure"))
                                               .findAny()
                                               .orElse(null);
        if (item == null) {
            throw new InvalidActionException("Player does not have any gold");
        }
        items.remove(item);                                 
    }

    /**
     * bribe assassin with ring + sun stone or ring + treasure 
     */
    public void bribeAssassin() throws InvalidActionException {
        CollectableEntity treasure = items.stream().filter(e -> e instanceof Treasure)
                                                   .findAny()
                                                   .orElse(null);

        CollectableEntity ring = items.stream().filter(e -> e instanceof TheOneRing)
                                               .findAny()
                                               .orElse(null);

        CollectableEntity sunStone = items.stream().filter(e -> e.getType().equals("sun_stone"))
                                               .findAny()
                                               .orElse(null);

        if (ring == null || (treasure == null && sunStone == null)) {
            throw new InvalidActionException("Player does not have any gold or ring");
        } else if (sunStone == null) {
            items.remove(treasure);
        } 

        items.remove(ring);
                                        
    }

    /**
     * if inventory contains a ring, apply it to character
     */
    public void useRing() {
        CollectableEntity item = items.stream().filter(e -> e.getType().equals("one_ring"))
                                               .findAny()
                                               .orElse(null);
        
        if (item != null) {
            item.use(character); 
            items.remove(item);
        }

        return;
        
    }

    /**
     * Remove used key from inventory
     * @param k
     */
    public void useKey(Key k) {
        items.remove(k);
    }

    /**
     * Given a type of buildable entity, return a same type of entity 
     * with durability > 0
     * @param item
     * @return
     */
    public EntityEquip replaceBuildableItem(BuildableEntity item) {
        EntityEquip newItem = itemsEquipped.stream().filter(e -> e.getType().equals(item.getType()) && !e.getId().equals(item.getId()))
                                            .map(e -> (EntityEquip) e)
                                            .filter(e -> e.getDurability() > 0)
                                            .findAny()
                                            .orElse(null);
        
        itemsEquipped.remove(item);
        
        return newItem;
    }
    
    /**
     * /**
     * Given a entity, return a same type of entity 
     * with durability > 0
     * @param item
     * @return
     */
    public EntityEquip replaceItemEquipped(CollectableEntity item) {
        EntityEquip newItem = items.stream().filter(e -> e.getType().equals(item.getType()) && !e.getId().equals(item.getId()))
                                            .map(e -> (EntityEquip) e)
                                            .filter(e -> e.getDurability() > 0)
                                            .findAny()
                                            .orElse(null);

        items.remove(item);
        
        return newItem;
    }

    /**
     * Helper method used to get the item with the corresponding itemId
     * @param itemId
     * @return
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
    private CollectableEntity getItem(String itemId) throws IllegalArgumentException, InvalidActionException {
        CollectableEntity item = items.stream().filter(e -> itemId.equals(e.getId()))
                                               .findAny()
                                               .orElse(null);
        // check item in inventory
        if (item == null) {
            throw new InvalidActionException("Item is not in the inventory");
        
        // check item is a bomb, health_potion, invincibility_potion, or an invisibility_potion
        } else if (!item.getType().equals("bomb") && !item.getType().equals("health_potion") 
                   && !item.getType().equals("invincibility_potion") && !item.getType().equals("invisibility_potion")) {
            
            throw new IllegalArgumentException("Item is not valid");
        }
        return item;
    }

    public List<CollectableEntity> getItems() {
        return items;
    }

    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        JSONArray jarray = new JSONArray();
        JSONArray buildable = new JSONArray();
        for (CollectableEntity i : items) {
            jarray.put(i.toJson());
        }

        for (BuildableEntity b : itemsEquipped) {
            buildable.put(b.toJson());
        }
        j.put("buildable", buildable);
        j.put("item", jarray);
        return j;
    }
}


