package dungeonmania.entity.movingEntity;

import java.util.Random;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.collectableEntity.Armour;
import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity {
    private static int idNum = 1;
    private int healthPoint = 12;
    public static int attackDamage = 2;
    private Armour armour;

    public ZombieToast(Position pos, Dungeon dun) {
        super(pos.asLayer(2), dun, "zombie_toast_" + Integer.toString(idNum), "zombie_toast");
        this.armour = generateArmour();
        idNum++;
    }

    public ZombieToast(Position pos, Dungeon dun, int hp) {
        super(pos, dun, "zombie_toast_" + Integer.toString(idNum), "zombie_toast");
        this.armour = generateArmour();
        healthPoint = hp;
        idNum++;
    }

    @Override
    public int getHealthPoint() {
        return this.healthPoint;
    }

    @Override
    public int getAttackDamage() {
        return ZombieToast.attackDamage;
    }

    @Override
    public void setHealthPoint(int curHealth) {
        this.healthPoint = curHealth;
    }

    public double getDefenseCoefficient() {
        if (this.armour != null) {
            return 0.5;
        }
        return 1.0;
    }

    public Armour getArmour() {
        return this.armour;
    }

    /**
     * Helper method to generate armour randomly
     * 
     * @return
     */
    private Armour generateArmour() {
        Random random = new Random();
        int value = random.nextInt(10);

        if (value < 4) {
            return new Armour(new Position(-1, -1), super.getDungeon());
        }

        return null;
    }

    /**
     * 
     * @param position the position zimbie go
     */
    private void zombiesMove(Position position) {
        if (getDungeon().isWall(position) || getDungeon().isUsedBoom(position)
                || getDungeon().isZombieToastSpawner(position) || getDungeon().isBoulder(position)
                || getDungeon().isDoor(position)) {
            return;
        } else {
            setPosition(position.asLayer(2));
        }

    }

    /**
     * get a random for it to choose and moving different directions when the random
     * number is the same as setting
     */
    @Override
    public void move() {
        Position posi = getPosition();
        swampMove(posi);

        if (getFrozen() == 0) {
            Position up = new Position(posX(), posY() - 1);
            Position down = new Position(posX(), posY() + 1);
            Position right = new Position(posX() + 1, posY());
            Position left = new Position(posX() - 1, posY());
            Random rand = new Random();
            int randnum = rand.nextInt(4);
            if (randnum == 0)
                zombiesMove(up);
            else if (randnum == 1)
                zombiesMove(down);
            else if (randnum == 2)
                zombiesMove(right);
            else if (randnum == 3)
                zombiesMove(left);
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("hp", healthPoint);
        j.put("type", "zombie");
        return j;
    }

}
