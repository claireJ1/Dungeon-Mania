package dungeonmania.entity;

import org.json.JSONObject;

public interface DefenseEntity extends EntityEquip {
    public double getDefenseCoefficient(double init);
    public JSONObject toJson();
}
