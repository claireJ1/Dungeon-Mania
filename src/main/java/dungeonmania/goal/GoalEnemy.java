package dungeonmania.goal;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.movingEntity.MovingEntity;

public class GoalEnemy extends GoalComponent {

    @Override
    public boolean goalAchieved(Dungeon d) {
        List<Entity> enemies = d.getEntities().stream()
                                              .filter(e -> e instanceof MovingEntity 
                                                    || e.getType().equals("zombie_toast_spawner"))
                                              .collect(Collectors.toList());
        
        List<MovingEntity> allies = d.getCharacter().getAllies().stream()
                                                                .filter(e -> e.getSceptrePeriod() == -1)
                                                                .collect(Collectors.toList());

        for (MovingEntity m : allies) {
            if (enemies.contains(m)) {
                enemies.remove(m);
            }
        }

        if (enemies.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public String goalToString() {
        return ":enemies";
    }

    @Override
    public JSONObject toJson() {
        JSONObject goal = new JSONObject();
        goal.put("goal", "enemies");
        return goal;
    }

}

