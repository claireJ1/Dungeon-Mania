package dungeonmania.goal;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.staticEntity.FloorSwitch;

public class GoalSwitch extends GoalComponent {
    @Override
    public boolean goalAchieved(Dungeon d) {
        // no switch, then always false
        List<Entity> allSwitch = d.getEntities().stream()
                                                .filter(e -> e instanceof FloorSwitch)
                                                .collect(Collectors.toList());
        if (allSwitch.isEmpty()) {
            return true;
        }
        // Get all the switch on dungeon that hasn't turned on 
        List<Entity> switchOn = d.getEntities().stream()
                                               .filter(e -> e instanceof FloorSwitch)
                                               .map(e -> (FloorSwitch) e)
                                               .filter(e -> !e.getIsTurnOn())
                                               .collect(Collectors.toList());
        
        if (switchOn.isEmpty()) {
            return true;
        }
        return false;
    }
    @Override
    public String goalToString() {
        return ":switch";
    }
    
    @Override
    public JSONObject toJson() {
        JSONObject goal = new JSONObject();
        goal.put("goal", "boulders");
        return goal;
    }

}

