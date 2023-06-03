package dungeonmania.entity.collectableEntity;

import org.json.JSONObject;

import dungeonmania.*;
import dungeonmania.Character;
import dungeonmania.util.Position;

public class InvincibilityPotion extends CollectableEntity {
    public static int idNum = 1;
    private Boolean hardMode = false;
    private int lastTime = 10;

    public InvincibilityPotion(Position pos, Dungeon dun) {
        super(pos, dun, "invincibility_potion_" + Integer.toString(idNum), "invincibility_potion");
        idNum++;
        this.hardMode = false;
    }

    public void setHardMode(boolean isHard) {
        this.hardMode = isHard;
    }

    @Override
    public void use(Character c) {
        if(!this.hardMode) {
            c.setInvincible(c.getInvincible() + lastTime);
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("type", "invincibility_potion");
        return j;
    }

}

