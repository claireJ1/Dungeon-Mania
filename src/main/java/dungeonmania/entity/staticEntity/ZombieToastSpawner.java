package dungeonmania.entity.staticEntity;

import java.util.List;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.movingEntity.ZombieToast;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.*;

public class ZombieToastSpawner extends StaticEntity {
    private static int idNum = 1;

    private int spawnSpeed = 20;

    public ZombieToastSpawner(Position pos, Dungeon dun) {
        super(pos.asLayer(2), dun, "zombie_toast_spawner_" + Integer.toString(idNum), "zombie_toast_spawner", true, null);
        idNum++;
    }

    public void setSpawnSpeed(int speed) {
        this.spawnSpeed = speed;
    }

    public void spawnZombie(int tickNum) {
        if (tickNum % spawnSpeed == 0) {
            Position spawnPosition = this.getPosition();
            Position zombiePos = generateZombiePosition(spawnPosition);
            if (zombiePos != null) {
                ZombieToast newZombie = new ZombieToast(zombiePos.asLayer(2), this.getDungeon());
                this.getDungeon().addEntity(newZombie);
            }
        }
    }

    private Position generateZombiePosition(Position spawnPosition) {
        List<Position> adjacent = spawnPosition.getAdjacentPositions();
        Position zombiePos = null;
        for (Position p : adjacent) {
            if (this.getDungeon().existEntityByPositionLayer(p, 2) == null) {
                zombiePos = p;
                break;
            }
        }
        return zombiePos;
    }

    public void destroy() throws InvalidActionException {
        if (!getPosition().getAdjacentPositions().contains(getCharacter().getPosition())) {
            throw new InvalidActionException("player is not cardinally adjacent to the spawner");
        } else if (!getCharacter().weaponsNotEmpty()) {
            throw new InvalidActionException("player does not have a weapon");
        } else {
            getDungeon().removeEntity(this);
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject spawner = new JSONObject();
        spawner.put("x", getPosition().getX());
        spawner.put("y", getPosition().getY());
        spawner.put("z", getPosition().getLayer());
        spawner.put("type", "zombie_toast_spawner");
        return spawner;
    }

}
