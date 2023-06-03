package dungeonmania.goal;

import org.json.JSONObject;

import dungeonmania.Dungeon;

public class GoalDisjunction extends GoalComponent {

    private GoalComponent g1;
    private GoalComponent g2;

    public GoalDisjunction(GoalComponent g1, GoalComponent g2) {
        this.g1 = g1;
        this.g2 = g2;
    }

    @Override
    public boolean goalAchieved(Dungeon d) {
        if (g1.goalAchieved(d) || g2.goalAchieved(d)) {
            return true;
        }
        return false;
    }

    @Override
    public String goalToString() {
        return "( " + g1.goalToString() + " OR " + g2.goalToString() + " )";
    }
    
    @Override
    public JSONObject toJson() {
        JSONObject goal = new JSONObject();
        goal.put("goal", "OR");
        goal.put("subgoals", g1.toJson());
        goal.put("subgoals", g2.toJson());
        return goal;
    }

}
