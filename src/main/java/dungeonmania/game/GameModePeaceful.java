package dungeonmania.game;

import dungeonmania.Dungeon;
import dungeonmania.entity.movingEntity.Assassin;
import dungeonmania.entity.movingEntity.Hydra;
import dungeonmania.entity.movingEntity.Mercenary;
import dungeonmania.entity.movingEntity.Spider;
import dungeonmania.entity.movingEntity.ZombieToast;

public class GameModePeaceful implements GameMode{
    
    @Override
    public void apply(Dungeon dungeon) {
        // set the attack damage of enemies to 0
        Spider.attackDamage = 0;
        Mercenary.attackDamage = 0;
        ZombieToast.attackDamage = 0;
        Assassin.attackDamage = 0;
        Hydra.attackDamage = 0;
    }

    @Override
    public String modeString() {
        return "peaceful";
    }

}
