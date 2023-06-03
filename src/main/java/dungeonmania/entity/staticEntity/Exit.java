package dungeonmania.entity.staticEntity;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class Exit extends StaticEntity {
    public static int idNum = 1;

    private boolean reachExit;

    public Exit(Position pos, Dungeon dun) {
        super(pos.asLayer(1), dun, "exit_" + Integer.toString(idNum), "exit", false, null);
        idNum++;
        this.reachExit = false;
    }

    public boolean getReachExit() { return reachExit; }

    public void setReachExit(boolean arg) { this.reachExit = arg; }

    @Override
    public JSONObject toJson() {
        JSONObject exit = new JSONObject();
        exit.put("x", getPosition().getX());
        exit.put("y", getPosition().getY());
        exit.put("z", getPosition().getLayer());
        exit.put("type", "exit");
        return exit;
    }
    
}
