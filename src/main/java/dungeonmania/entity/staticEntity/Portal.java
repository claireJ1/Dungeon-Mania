package dungeonmania.entity.staticEntity;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Portal extends StaticEntity {
    public static int idNum = 1;
    private Portal corresponding;

    public Portal(Position pos, Dungeon dun, String group) {
        super(pos.asLayer(2), dun, "portal_" + Integer.toString(idNum), "portal", false, group);
        idNum++;
        corresponding = null;
    }

    public Portal getCorresponding() { return corresponding; }
    public void setCorresponding(Portal pair) { this.corresponding = pair; }

    /**
     * Get the corresponding position
     * @param direction
     * @return
     */
    public Position getCorrespondingPos(Direction direction, Position origin) { 
        Dungeon d = super.getDungeon();
        Position portal = corresponding.getPosition();
        Position transTo = new Position(portal.getX() + direction.getX(), portal.getY() + direction.getY());

        // check the position transfer to is valid 
        Entity transToEnt = d.getEntities().stream()
                                           .filter(e -> e.getPosition().equals(transTo))
                                           .filter(e -> e instanceof StaticEntity &&
                                                   !(e instanceof Exit) && !(e instanceof FloorSwitch)
                                                   && !(e instanceof Portal) && !(e instanceof Door))
                                           .findAny()
                                           .orElse(null);
        if (transToEnt == null) {
            return transTo;
        } else {
            return origin;
        }
     }
    
    @Override
    public JSONObject toJson() {
        JSONObject protal = new JSONObject();
        protal.put("x", getPosition().getX());
        protal.put("y", getPosition().getY());
        protal.put("z", getPosition().getLayer());
        protal.put("type", "protal");
        protal.put("colour", getGroup());
        return protal;
    }
}

