package dungeonmania.goal;

import org.json.JSONObject;

import dungeonmania.Dungeon;

public class GoalNone extends GoalComponent {

    @Override
    public boolean goalAchieved(Dungeon d) {
        return false;
    }

    @Override
    public String goalToString() {
        return "NONE";
    }
    
    @Override
    public JSONObject toJson() {
        return null;
    }
    
}
