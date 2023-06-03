package dungeonmania.goal;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;

public class GoalTreasure extends GoalComponent {

    @Override
    public boolean goalAchieved(Dungeon d) {
        List<Entity> treasures = d.getEntities().stream()
                                                .filter(e -> e.getType().equals("treasure"))
                                                .collect(Collectors.toList());
        
        if (treasures.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public String goalToString() {
        return ":treasure";
    }
    
    @Override
    public JSONObject toJson() {
        JSONObject goal = new JSONObject();
        goal.put("goal", "treasure");
        return goal;
    }

}
