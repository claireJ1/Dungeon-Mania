package dungeonmania.entity.movingEntity;

import java.util.List;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.collectableEntity.Armour;
import dungeonmania.util.Position;
import java.util.ArrayList;

public class Spider extends MovingEntity {
    public static int idNum = 1;
    private int healthPoint = 8;
    public static int attackDamage = 1;

    private boolean iscircle = false;
    private int moveCount = 1;
    boolean judge = true;
    boolean issetup = false;
    List<Position> spidermove = new ArrayList<>();

    public Spider(Position pos, Dungeon dun) {
        super(pos.asLayer(3), dun, "spider_" + Integer.toString(idNum), "spider");
        idNum++;

    }

    public Spider(Position pos, Dungeon dun, int hp) {
        super(pos, dun, "spider_" + Integer.toString(idNum), "spider");
        healthPoint = hp;
        idNum++;
    }

    @Override
    public int getHealthPoint() {
        return this.healthPoint;
    }

    @Override
    public int getAttackDamage() {
        return Spider.attackDamage;
    }

    @Override
    public void setHealthPoint(int curHealth) {
        this.healthPoint = curHealth;
    }

    public double getDefenseCoefficient() {
        return 1.0;
    }

    public Armour getArmour() {
        return null;
    }

    /**
     * 
     * @param originalpos using the original position of spider to form the list it
     *                    moves
     */
    private void setup(Position originalpos) {
        spidermove.add(new Position(originalpos.getX(), originalpos.getY() - 1).asLayer(3));
        spidermove.add(new Position(originalpos.getX() + 1, originalpos.getY() - 1).asLayer(3));
        spidermove.add(new Position(originalpos.getX() + 1, originalpos.getY()).asLayer(3));
        spidermove.add(new Position(originalpos.getX() + 1, originalpos.getY() + 1).asLayer(3));
        spidermove.add(new Position(originalpos.getX(), originalpos.getY() + 1).asLayer(3));
        spidermove.add(new Position(originalpos.getX() - 1, originalpos.getY() + 1).asLayer(3));
        spidermove.add(new Position(originalpos.getX() - 1, originalpos.getY()).asLayer(3));
        spidermove.add(new Position(originalpos.getX() - 1, originalpos.getY() - 1).asLayer(3));

    }

    /**
     * circle the list normal way when it meet nothing
     */
    private void normalcircle() {
        int num = moveCount & 7;

        if (!getDungeon().isBoulder(spidermove.get(num))) {
            setPosition(spidermove.get(num));
            moveCount++;
        } else {
            moveCount = moveCount - 2;
            judge = false;
            if (moveCount < 0) {
                moveCount = moveCount + 8;
            }
            num = moveCount & 7;
            setPosition(spidermove.get(num));
            moveCount--;
        }
    }

    /**
     * circle the list reverse when meet boulder
     */
    private void reversecircle() {
        int num = moveCount & 7;

        if (!getDungeon().isBoulder(spidermove.get(num))) {
            setPosition(spidermove.get(num));
            moveCount--;
        } else {
            moveCount = moveCount + 2;
            judge = true;
            num = moveCount & 7;
            setPosition(spidermove.get(num));
            moveCount++;
        }
    }

    /**
     * judge when situation the spider is and choose moving method
     */
    private void circlemove() {
        if (judge) {
            normalcircle();
        } else {
            reversecircle();
        }

    }

    /**
     * spider move
     */
    @Override
    public void move() {
        Position posi = getPosition();
        swampMove(posi);

        if (getFrozen() == 0) {
            if (!issetup) {
                setup(getPosition());
                issetup = true;
            }
            if (!iscircle) {
                Position pos = new Position(posX(), posY() - 1).asLayer(3);
                if (getDungeon().isBoulder(pos)) {
                    return;
                } else {
                    setPosition(pos);
                    iscircle = true;
                }
            } else {
                circlemove();
            }
        }

    }

    @Override
    public JSONObject toJson() {

        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("hp", healthPoint);
        j.put("type", "spider");
        return j;
    }

}
