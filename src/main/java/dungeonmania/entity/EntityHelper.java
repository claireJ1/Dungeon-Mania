package dungeonmania.entity;

import java.util.List;

public class EntityHelper {
    
    public static int maxSameActivation(List<Integer> list) {
        int max = 0;
        for (int o: list) {
            if (o < 0) continue;
            int appear = 0;
            for (int cur: list) if (o == cur) appear++;
            if (appear > max) max = appear;
        }
        return max;
    }
}
