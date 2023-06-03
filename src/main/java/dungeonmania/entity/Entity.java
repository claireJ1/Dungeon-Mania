package dungeonmania.entity;

import dungeonmania.util.*;

import org.json.JSONObject;

import dungeonmania.Character;
import dungeonmania.Dungeon;

public abstract class Entity {
    private Position position;
    private Dungeon dungeon;
    private boolean isInteractable;
    private String id;
    private String type;
    private Character player;

    public abstract JSONObject toJson();

    public Entity(Position pos, Dungeon dun, String id, String type) {
        this.position = pos;
        this.dungeon = dun;
        this.id = id;
        this.type = type;
        this.isInteractable = false;
        this.player = this.dungeon.getCharacter();

    }

    public Entity(Position pos, Dungeon dun, String id, String type, Boolean interactable) {
        this.position = pos;
        this.dungeon = dun;
        this.id = id;
        this.type = type;
        this.isInteractable = interactable;
        this.player = this.dungeon.getCharacter();
    }

    public String getId() { return this.id; }
    public String getType() { return this.type; }
    public void setType(String type) { this.type = type; }
    public Dungeon getDungeon() { return this.dungeon; }
    public Character getCharacter() { return dungeon.getCharacter(); }
    public Position getPosition() { return this.position; }
    public void setPosition(Position p) { this.position = p; }
    public boolean getIsInteractable() {return isInteractable; }
    public int getXPosition() { return position.getX(); }
    public int getYPosition() { return position.getY(); }
    public Position getCharacterPos() { return player.getPosition(); }
}
