package dungeonmania.entity.movingEntity;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;

public class Assassin extends Mercenary implements Boss {
    public static int idNum = 1;
    public static int attackDamage = 5;

    public Assassin(Position pos, Dungeon dun) {
        super(pos, dun, "assassin_" + Integer.toString(idNum), "assassin");
        idNum++;
    }

    public Assassin(Position pos, Dungeon dun, int hp, int sceptrePeriod, boolean isAlly){
        super(pos, dun, "assassin_" + Integer.toString(idNum), "assassin");
        setSceptrePeriod(sceptrePeriod);
        setHealthPoint(hp);
        setIsAlly(isAlly);
        idNum++;
    }    

    @Override
    public int getAttackDamage() { return attackDamage; }

    @Override
    public void bribeAction() throws InvalidActionException {
        getCharacter().bribeAssassin(this);
    }

    @Override
    public JSONObject toJson() {
        
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("hp", getHealthPoint());
        j.put("sceptre_period", getSceptrePeriod());
        j.put("type", "assassin");
        j.put("isAlly", getIsAlly());
        return j;
    }

}
