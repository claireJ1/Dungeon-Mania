package dungeonmania.entity.movingEntity;

import java.util.Random;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.collectableEntity.Armour;
import dungeonmania.util.Position;

public class Hydra extends MovingEntity implements Boss {
    public static int idNum = 1;
    private int healthPoint = 15;
    public static int attackDamage = 3;
    private Random random = new Random(0);

    /**
     * Constructor for Hydra
     * 
     * @param pos
     * @param dun
     */
    public Hydra(Position pos, Dungeon dun) {
        super(pos, dun, "hydra_" + Integer.toString(idNum), "hydra");
        idNum++;
    }

    public Hydra(Position pos, Dungeon dun, int hp) {
        super(pos, dun, "hydra_" + Integer.toString(idNum), "hydra");
        healthPoint = hp;
        idNum++;
    }

    @Override
    public int getHealthPoint() {
        return this.healthPoint;
    }

    @Override
    public int getAttackDamage() {
        return attackDamage;
    }

    @Override
    public void setHealthPoint(int curHealth) {
        this.healthPoint = curHealth;

    }

    /**
     * Get the defense coefficient for battle
     * 50% chance to raise health point, so coefficient is 1
     * otherwise, coefficient = 1
     */
    @Override
    public double getDefenseCoefficient() {
        int value = random.nextInt(100);
        // 50% chance to raise health point
        if (value < 50) {
            return -1;
        }
        return 1;
    }


    @Override
    public Armour getArmour() {
        return null;
    }

    /**
     * 
     * @param position the position Hydra go
     */
    private void hydraMove(Position position) {
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
            int randnum = rand.nextInt(3);
            if (randnum == 0)
                hydraMove(up);
            else if (randnum == 1)
                hydraMove(down);
            else if (randnum == 2)
                hydraMove(right);
            else if (randnum == 3)
                hydraMove(left);
        }
    }

    @Override
    public JSONObject toJson() {

        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("hp", healthPoint);
        j.put("type", "hydra");
        return j;
    }

}
