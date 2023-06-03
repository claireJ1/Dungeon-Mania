package dungeonmania;

import dungeonmania.entity.*;
import dungeonmania.entity.staticEntity.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.entity.movingEntity.*;
import dungeonmania.entity.collectableEntity.*;
import dungeonmania.entity.buildableEntity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class Character {
    private Dungeon dungeon;
    private Position position;
    private static int fullHealthPoint = 20;
    private int healthPoint;
    private final int attackDamage = 3;
    private Inventory inventory;
    private List<Weapon> weapons = new ArrayList<Weapon>();
    private List<DefenseEntity> defense = new ArrayList<>();
    private List<Mercenary> allies = new ArrayList<>();
    private Key key;
    private Sceptre sceptreEquipped;
    private int invincible = 0;
    private int invisible = 0;
    private Direction direction = Direction.NONE;

    public Character(Dungeon d) {
        this.dungeon = d;
        this.inventory = new Inventory(this);
        this.healthPoint = fullHealthPoint;
        allies = new ArrayList<>();
    }

    public Character(Position position, Dungeon d) {
        this.dungeon = d;
        this.position = position;
        this.healthPoint = fullHealthPoint;
        this.inventory = new Inventory(this);
        allies = new ArrayList<>();
    }

    public Character(Position pos, Dungeon dun, int hp, Inventory inv) {
        dungeon = dun;
        position = pos;
        healthPoint = hp;
        inventory = inv;
    }

    // All getter method
    public Dungeon getDungeon() {
        return dungeon;
    }

    public Position getPosition() {
        return this.position;
    }

    public int getXPosition() {
        return position.getX();
    }

    public int getYPosition() {
        return position.getY();
    }

    public int getInvisible() {
        return invisible;
    }

    public int getInvincible() {
        return invincible;
    }

    public Key getKey() {
        return key;
    }

    public List<Mercenary> getAllies() {
        return this.allies;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public int getHealthPoint() {
        return this.healthPoint;
    }

    public int getFullHealthPoint() {
        return Character.fullHealthPoint;
    }

    public Sceptre getSceptreEquipped() {
        return sceptreEquipped;
    }

    // All setter method
    public void setDungeon(Dungeon d) {
        this.dungeon = d;
    }

    public void setPosition(Position p) {
        this.position = p;
    }

    public void setInvisible(int numTick) {
        this.invisible = numTick;
    }

    public void setInvincible(int numTick) {
        this.invincible = numTick;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
    }

    public void setFullHealthPoint(int full) {
        Character.fullHealthPoint = full;
    }

    public void setSceptreEquipped(Sceptre sceptreEquipped) {
        this.sceptreEquipped = sceptreEquipped;
    }

    public void setAllies(List<Mercenary> allies) {
        this.allies = allies;
    }

    /**
     * Get the itemResponse in inventory
     * @return
     */
    public List<ItemResponse> getItemInfoInInventory() {
        return inventory.itemInInventory();
    }

    /**
     * Get buildable items
     * @return
     */
    public List<String> getBuildableItems() {
        return inventory.buildables();
    }

    /**
     * Add item to the inventory and remove it from dungeon
     * @param e
     * @precondition e != null
     * @postcondition item exists in inventory, removed from dungeon
     */
    public void addAlly(Mercenary m) {
        allies.add(m);
    }

    public void removeAlly(Mercenary m) {
        allies.remove(m);
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void addItem(CollectableEntity e) {
        if (e instanceof Key && inventory.checkKeyExists()) {
            return;
        }

        inventory.addItem(e);
        dungeon.removeEntity(e);
    }

    public void useItem(String itemId) throws IllegalArgumentException, InvalidActionException {
        inventory.useItem(itemId);
    }

    public void addWeapon(Weapon w) {
        weapons.add(w);
    }

    public void removeWeapon(Weapon w) {
        weapons.remove(w);
    }

    public void addDefense(DefenseEntity d) {
        defense.add(d);
    }

    public void removeDefense(DefenseEntity d) {
        defense.remove(d);
    }

    public boolean hasWeapon(Weapon w) {
        return weapons.stream().anyMatch(e->e.getClass().getSimpleName().equals(w.getClass().getSimpleName()));
    }

    public boolean hasDefense(DefenseEntity d) {
        return defense.stream().anyMatch(e->e.getClass().getSimpleName().equals(d.getClass().getSimpleName()));
    }

    public boolean containsDefense(DefenseEntity d) {
        return defense.contains(d);
    }

    public boolean weaponsNotEmpty() {
        return !weapons.isEmpty();
    }

    /**
     * build entity according to the entity name given
     * 
     * @param entityName
     * @preconditions entityName != null
     * @postconditions items.length decrease && character is equipped is the entity
     * @throws IllegalArgumentException entityName is not bow or shield
     * @throws InvalidActionException   dont' have sufficient items to craft the
     *                                  entity
     */
    public void build(String buildable) throws InvalidActionException, IllegalArgumentException {
        inventory.buildEntity(buildable);
    }

    /**
     * Use to replace the item equipped by the character
     * 
     * @param e
     */
    public void replaceBuildableItem(BuildableEntity e) {
        EntityEquip item = inventory.replaceBuildableItem(e);
        if (item != null) {
            item.equip(this);
        }
    }

    /**
     * Use to replace the item equipped by the character
     * 
     * @param e
     */
    public void replaceItemEquipped(CollectableEntity e) {
        EntityEquip item = inventory.replaceItemEquipped(e);
        if (item != null) {
            item.equip(this);
        }
    }

    /**
     * Use the equipped key to open the door, and remove it from the inventory
     */
    public void useKey() {
        inventory.useKey(key);
        this.key = null;
    }

    /**
     * Determine the entity used to bribe enemy
     * if no valid entity found throw exception
     */
    public void bribeMercenary(Mercenary m) throws InvalidActionException {
        CollectableEntity sunStone = inventory.getItems().stream().filter(e -> e.getType().equals("sun_stone"))
                                                                    .findAny()
                                                                    .orElse(null);
        if (sunStone != null) {
            allies.add(m);
        } else if (getSceptreEquipped() != null) {
            getSceptreEquipped().use(this);
            m.setSceptrePeriod(10);
            allies.add(m);
            return;
        } else {
            inventory.useTreasure();
            allies.add(m);
        }
    }

    /**
     * Determine the entity used to bribe enemy
     * @param a
     * @throws InvalidActionException no valid entity found
     */
    public void bribeAssassin(Assassin a) throws InvalidActionException {
        if (getSceptreEquipped() != null) {
            getSceptreEquipped().use(this);
            a.setSceptrePeriod(10);
            return;
        }

        inventory.bribeAssassin();
        allies.add(a);
    }

    /**
     * Consume the durability of potions that the character used
     */
    public void consumePotion() {
        if (invincible > 0) {
            invincible--;
        }

        if (invisible > 0) {
            invisible--;
        }
    }

    /**
     * Battle with an enemy
     * 
     * @param enemy mercenary, spider or zombie toast
     */
    public void battle(MovingEntity enemy) {
        if (allies.contains(enemy)) {
            // would not battle with allies
            return;
        }

        if (invisible <= 0) {
            doBattle(enemy);
            notifyMercenary();
        }
    }

    /**
     * A recursion that each iteration indicated one round of the battle
     * 
     * @param enemy mercenary, spider or zombie toast
     */
    private void doBattle(MovingEntity enemy) {
        // character dies
        if (this.healthPoint <= 0) {
            // use the one ring if has
            inventory.useRing();
            if (this.healthPoint <= 0) {
                dungeon.setPlayer(null);
            }
            return;
        }

        int enemyHealth = enemy.getHealthPoint();
        // enemy dies
        if (invincible > 0 || enemyHealth <= 0) {
            dungeon.removeEntity(enemy);
            if (enemy.getArmour() != null) {
                inventory.addItem(enemy.getArmour());
            }
            winOneRing();
            return;
        }
        // compute character hp
        double charDefenseCoefficient = getDefenseCoefficient();
        healthPoint = (int) (healthPoint - ((enemyHealth * enemy.getAttackDamage() * charDefenseCoefficient) / 10));

        // compute enemy hp
        if (healthPoint > 0) {
            double enemyDefenseCoefficient = enemy.getDefenseCoefficient();
            int charAttack = computeCharAttackDamage(enemy, enemyDefenseCoefficient);
            enemyHealth = (int) (enemyHealth - ((healthPoint * charAttack * enemyDefenseCoefficient) / 5));
            for (Mercenary ally : allies) {
                enemyHealth = (int) (enemyHealth - ((healthPoint * ally.getAttackDamage() * enemyDefenseCoefficient) / 5));
            }
            enemy.setHealthPoint(enemyHealth);
        }
        // next round
        doBattle(enemy);
    }

    /**
     * Helper method to compute the character's defense coefficient
     * 
     * @return the defense coefficient of the current state
     */
    private double getDefenseCoefficient() {
        double coefficient = 1.0;
        List<DefenseEntity> curDefense = new ArrayList<>();
        curDefense.addAll(defense);
        for (DefenseEntity d : curDefense) {
           coefficient = d.getDefenseCoefficient(coefficient);
           d.use(this);
        }
        return coefficient;
    }

    /**
     * Helper method to compute the character's total attack damage
     * 
     * @return the total attack damage in this round
     */
    private int computeCharAttackDamage(MovingEntity enemy, double enemyDefenseCoefficient) {
        int charAttack = attackDamage;
        boolean hasAnduril = false;
        List<Weapon> curWeapon = new ArrayList<>();
        curWeapon.addAll(weapons);
        for (Weapon w : curWeapon) {
            if (w instanceof Anduril) {
                hasAnduril = true;
            }
            charAttack = w.getAttackDamage(charAttack, enemy);
            w.use(this);
        }

        if (hasAnduril && enemyDefenseCoefficient < 0) {
            charAttack *= -1;
        }
        return charAttack;
    }

    /**
     * Notify the mercenary(not ally) when the character is in battle
     */
    private void notifyMercenary() {
        List<Mercenary> mercs = this.dungeon.findMercenaryWithinRange(position);
        List<Mercenary> notBribed = mercs.stream().filter(e -> !allies.contains(e)).collect(Collectors.toList());
        notBribed.stream().forEach(m -> m.move());
        notBribed.stream().filter(m -> m.getPosition().equals(this.position) && this.healthPoint > 0)
                .forEach(m -> doBattle(m));

    }

    private void winOneRing() {
        Random random = new Random();
        int value = random.nextInt(10);

        if (value == 1) {
            this.inventory.addItem(new TheOneRing(new Position(-1, -1), this.dungeon));
        }
    }

    /**
     * 
     * @param position check the position of boulder is going to
     * @return
     */
    public boolean isPushable(Position position) {
        if (dungeon.existEntityByPositionLayer(position, 2) == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param position the direction choose to move
     * @param isPushable whether the boulder is pushable
     */
    private void characterNormalMove(Position position, boolean isPushable) {

        CollectableEntity sunStone = inventory.getItems().stream().filter(e -> e.getType().equals("sun_stone"))
                                                                    .findAny()
                                                                    .orElse(null);

        if (dungeon.isWall(position) || dungeon.isUsedBoom(position) || dungeon.isZombieToastSpawner(position)) {
            return;
        } else if (dungeon.isBoulder(position)) {
            if (isPushable) {
                setPosition(position);
            } else {
                return;
            }

        } else if (dungeon.isDoor(position)) {
            if (getKey() == null && sunStone == null) {
                return;
            } else if (sunStone != null) {
                ((SunStone)sunStone).sunStoneOpenDoor(((Door) getDungeon().existEntityByPositionLayer(position, 2)));
            } else {
                String ID = ((Door) getDungeon().existEntityByPositionLayer(position, 2)).getKey().getId();

                if (ID == getKey().getId()) {
                    setPosition(position);
                    getKey().openDoor(((Door) getDungeon().existEntityByPositionLayer(position, 2)));
                    useKey();
                } else {
                    return;
                }
            }
        } else if (getDungeon().getPortal(position) != null) {
            setPosition(getDungeon().getPortal(position).getCorrespondingPos(direction, getPosition()));

        } else {
            setPosition(position);
        }

    }

    /**
     * move up
     */
    private void characterMoveUp() {
        Position pos = new Position(getXPosition(), getYPosition() - 1).asLayer(2);
        Position pos2 = new Position(getXPosition(), getYPosition() - 2).asLayer(2);
        if (invisible > 0) {
            setPosition(pos);
        } else {
            characterNormalMove(pos, isPushable(pos2));
            if (dungeon.isBoulder(pos) && isPushable(pos2)) {
                dungeon.existEntityByPositionLayer(pos, 2).setPosition(pos2);
            }

        }
    }

    /**
     * move down
     */
    private void characterMoveDown() {
        Position pos = new Position(getXPosition(), getYPosition() + 1).asLayer(2);
        Position pos2 = new Position(getXPosition(), getYPosition() + 2).asLayer(2);
        if (invisible > 0) {
            setPosition(pos);
        } else {
            characterNormalMove(pos, isPushable(pos2));
            if (dungeon.isBoulder(pos) && isPushable(pos2)) {
                dungeon.existEntityByPositionLayer(pos, 2).setPosition(pos2);
            }

        }
    }

    /**
     * move right
     */
    private void characterMoveRight() {
        Position pos = new Position(getXPosition() + 1, getYPosition()).asLayer(2);
        Position pos2 = new Position(getXPosition() + 2, getYPosition()).asLayer(2);
        if (invisible > 0) {
            setPosition(pos);
        } else {
            characterNormalMove(pos, isPushable(pos2));
            if (dungeon.isBoulder(pos) && isPushable(pos2)) {
                dungeon.existEntityByPositionLayer(pos, 2).setPosition(pos2);
            }

        }
    }

    /**
     * move left
     */
    private void characterMoveLeft() {
        Position pos = new Position(getXPosition() - 1, getYPosition()).asLayer(2);
        Position pos2 = new Position(getXPosition() - 2, getYPosition()).asLayer(2);
        if (invisible > 0) {
            setPosition(pos);
        } else {
            characterNormalMove(pos, isPushable(pos2));
            if (dungeon.isBoulder(pos) && isPushable(pos2)) {
                dungeon.existEntityByPositionLayer(pos, 2).setPosition(pos2);
            }

        }
    }

    /**
     * 
     * @param direction direction char is going to move
     */
    public void move(Direction direction) {

        this.direction = direction;
        if (direction == Direction.UP)
            characterMoveUp();
        else if (direction == Direction.DOWN)
            characterMoveDown();
        else if (direction == Direction.RIGHT)
            characterMoveRight();
        else if (direction == Direction.LEFT)
            characterMoveLeft();
        else
            return;

    }

    public JSONObject toJson() {
        JSONObject character = new JSONObject();
        character.put("x", position.getX());
        character.put("y", position.getY());
        character.put("z", position.getLayer());
        character.put("hp", healthPoint);
        character.put("inventory", inventory.toJson());

        JSONArray allies = new JSONArray();
        for (Mercenary m : this.allies) {
            allies.put(m.toJson());
        }
        character.put("allies", allies);
        if (key != null) {
            character.put("key", key.toJson());
        }
        

        return character;
    }
}
