package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.response.models.*;
import dungeonmania.util.Position;

public class TestHelper {

    public static List<EntityResponse> fetchEntitiesByType(DungeonResponse resD, String type) {
        List<EntityResponse> ret = new ArrayList<>();
        for (EntityResponse e: resD.getEntities()) if (e.getType().equals(type)) ret.add(e);
        return ret;
    }

    public static List<ItemResponse> fetchItemsByType(DungeonResponse resD, String type) {
        List<ItemResponse> ret = new ArrayList<>();
        for (ItemResponse e: resD.getInventory()) if (e.getType().equals(type)) ret.add(e);
        return ret;
    }

    public static boolean hasEntityByType(DungeonResponse resD, String type) {
        for (EntityResponse e: resD.getEntities()) {
            if (e.getType().equals(type)) return true;
        }
        return false;
    }

    public static boolean hasItemByType(DungeonResponse resD, String type) {
        for (ItemResponse i: resD.getInventory()) {
            if (i.getType().equals(type)) return true;
        }
        return false;
    }

    public static double lengthByPosition(Position pos) {
        return Math.pow(pos.getX(), 2) + Math.pow(pos.getY(), 2);
    }
    
}
