package dungeonmania.game;

import dungeonmania.Dungeon;
import dungeonmania.entity.movingEntity.*;

public class GameModeStandard implements GameMode {
    
    @Override
    public void apply(Dungeon dungeon) {
        Spider.attackDamage = 1;
        Mercenary.attackDamage = 3;
        ZombieToast.attackDamage = 2;
        Assassin.attackDamage = 5;
        Hydra.attackDamage = 3;
    }

    @Override
    public String modeString() {
        return "standard";
    }

}
