package dungeonmania.goal;

import org.json.JSONObject;

import dungeonmania.Dungeon;

public abstract class GoalComponent {

    public abstract boolean goalAchieved(Dungeon d);
    public abstract String goalToString();
    public abstract JSONObject toJson();
    
    public void update(Dungeon d) {
        if (goalAchieved(d)) {
            d.setGoalString("");
        } else {
            d.setGoalString(this.goalToString());
        }
    }
}

