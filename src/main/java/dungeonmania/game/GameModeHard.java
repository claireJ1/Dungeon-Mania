package dungeonmania.game;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import dungeonmania.Character;
import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.collectableEntity.InvincibilityPotion;
import dungeonmania.entity.movingEntity.*;
import dungeonmania.entity.staticEntity.ZombieToastSpawner;

public class GameModeHard implements GameMode{
    
    @Override
    public void apply(Dungeon dungeon) {
        Spider.attackDamage = 1;
        Mercenary.attackDamage = 3;
        ZombieToast.attackDamage = 2;
        Assassin.attackDamage = 5;
        Hydra.attackDamage = 3;
        
        // disenable the functionality of invincibility potion
        List<Entity> entities = dungeon.getEntities();
        for (Entity e : entities) {
            if (e instanceof InvincibilityPotion) {
                InvincibilityPotion invincibility = (InvincibilityPotion)e;
                invincibility.setHardMode(true);
                break;
            }
        }

        // set the health point to half of origin
        Character c = dungeon.getCharacter();
        c.setFullHealthPoint(c.getFullHealthPoint()/2);

        // set zombie spawner speed to 15
        Predicate<Entity> isSpawner = e -> e.getType().equals("zombie_toast_spawner");
        Function<List<Entity>, List<Entity>> doSpawn = l -> l.stream().filter(isSpawner).collect(Collectors.toList());
        List<Entity> spawners = doSpawn.apply(dungeon.getEntities());
        for (Entity spawner : spawners) {
            ZombieToastSpawner s = (ZombieToastSpawner)spawner;
            s.setSpawnSpeed(15);
        }
    }

    @Override
    public String modeString() {
        return "hard";
    }
}
