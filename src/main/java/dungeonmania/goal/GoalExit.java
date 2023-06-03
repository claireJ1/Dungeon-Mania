package dungeonmania.goal;

import org.json.JSONObject;

import dungeonmania.Character;
import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;

public class GoalExit extends GoalComponent {

    @Override
    public boolean goalAchieved(Dungeon d) {
        Entity exit = d.getEntities().stream()
                                     .filter(e -> e.getType().equals("exit"))
                                     .findAny()
                                     .orElse(null);

        Character player = d.getCharacter();
        if (exit == null) {
            return false;
        } else if (exit.getPosition().equals(player.getPosition())) {
            return true;
        }
        
        return false;
    }

    @Override
    public String goalToString() {

        return ":exit";
    }
    
    @Override
    public JSONObject toJson() {
        JSONObject goal = new JSONObject();
        goal.put("goal", "exit");
        return goal;
    }

}

