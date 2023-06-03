package dungeonmania.game;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import dungeonmania.*;
import dungeonmania.Character;
import dungeonmania.entity.Entity;
import dungeonmania.entity.LogicEntity;
import dungeonmania.entity.buildableEntity.Bow;
import dungeonmania.entity.buildableEntity.BuildableEntity;
import dungeonmania.entity.buildableEntity.MidnightArmour;
import dungeonmania.entity.buildableEntity.Sceptre;
import dungeonmania.entity.buildableEntity.Shield;
import dungeonmania.entity.collectableEntity.Armour;
import dungeonmania.entity.collectableEntity.Arrows;
import dungeonmania.entity.collectableEntity.Bomb;
import dungeonmania.entity.collectableEntity.CollectableEntity;
import dungeonmania.entity.collectableEntity.HealthPotion;
import dungeonmania.entity.collectableEntity.InvincibilityPotion;
import dungeonmania.entity.collectableEntity.InvisibilityPotion;
import dungeonmania.entity.collectableEntity.Key;
import dungeonmania.entity.collectableEntity.Sword;
import dungeonmania.entity.collectableEntity.TheOneRing;
import dungeonmania.entity.collectableEntity.Treasure;
import dungeonmania.entity.collectableEntity.Wood;
import dungeonmania.entity.movingEntity.Assassin;
import dungeonmania.entity.movingEntity.Mercenary;
import dungeonmania.entity.movingEntity.Spider;
import dungeonmania.entity.movingEntity.ZombieToast;
import dungeonmania.entity.staticEntity.Boulder;
import dungeonmania.entity.staticEntity.Door;
import dungeonmania.entity.staticEntity.Exit;
import dungeonmania.entity.staticEntity.FloorSwitch;
import dungeonmania.entity.staticEntity.LightBulb;
import dungeonmania.entity.staticEntity.LogicDoor;
import dungeonmania.entity.staticEntity.Portal;
import dungeonmania.entity.staticEntity.StaticBomb;
import dungeonmania.entity.staticEntity.SwampTile;
import dungeonmania.entity.staticEntity.Wall;
import dungeonmania.entity.staticEntity.Wire;
import dungeonmania.entity.staticEntity.ZombieToastSpawner;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Game {
    private Dungeon dungeon;
    private GameMode mode;
    private int tickNum = 0;

    public Game(String dungeon, GameMode mode) {
        this.dungeon = new Dungeon(dungeon);
        this.mode = mode;
        this.mode.apply(this.dungeon);
    }

    public Game(int xStart, int yStart, int xEnd, int yEnd, GameMode mode) {
        this.dungeon = new Dungeon(50, 50, new Position(xStart, yStart), new Position(xEnd, yEnd));
        this.mode = mode;
        this.mode.apply(this.dungeon);
    }

    public Game(JsonObject json, GameMode mode) {
        JsonObject dungeon = json.get("dungeon").getAsJsonObject();
        Character c = null;
        if (dungeon.has("character")) {
            c = loadCharacter(dungeon.get("character").getAsJsonObject(), loadInventory(dungeon.get("character").getAsJsonObject().get("inventory").getAsJsonObject()));
        }
        this.dungeon = new Dungeon(dungeon.get("width").getAsInt(), dungeon.get("height").getAsInt(), new ArrayList<>(), dungeon.get("name").getAsString(), dungeon.get("id").getAsString(), dungeon.get("goal-condition").getAsJsonObject(), c);

        List<Entity> entities = loadEntity(dungeon.get("entities").getAsJsonArray());
        // update logic entities
        List<LogicEntity> elogic = new ArrayList<>();
        for (Entity e : entities) {
            if (e instanceof LogicEntity)
                elogic.add((LogicEntity) e);
        }
        for (LogicEntity e : elogic) {
            e.update();
        }
        c.setDungeon(this.dungeon);
        this.dungeon.setEntities(entities);
        this.dungeon.notifyGoal();
        this.mode = mode;
        this.mode.apply(this.dungeon);
        // add allies
        if (c != null) {
            List<Mercenary> allies = entities.stream().filter(e->e instanceof Mercenary).map(e-> (Mercenary)e).filter(e->e.getIsAlly()).collect(Collectors.toList());
            c.setAllies(allies);
        }
        
    }

    public void doTick(String itemUsed, Direction dirt) throws IllegalArgumentException, InvalidActionException {
        tickNum++;
        dungeon.tick(itemUsed, dirt, tickNum);
        if (mode instanceof GameModeHard) {
            dungeon.spawnHydra(tickNum);
        }
    }
    
    /**
     * Generate info for this game at the current state
     * @return response generated
     */
    public DungeonResponse getInfo() {
        return this.dungeon.getInfo();
    }

    public DungeonResponse doInteract(String entityId) throws IllegalArgumentException, InvalidActionException {
        this.dungeon.interact(entityId);
        return this.dungeon.getInfo();
    }

    public DungeonResponse doBuild(String buildable) throws IllegalArgumentException, InvalidActionException {
        this.dungeon.build(buildable);
        return this.dungeon.getInfo();
    }

    public DungeonResponse saveGame(String id) {
        try {
            Files.createDirectories(Paths.get("savedGames"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter gameSaved = new FileWriter("savedGames/" + id + ".json", false);
            gameSaved.write(toJson().toString(2));
            gameSaved.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getInfo();
    }

    private JSONObject toJson() {
        JSONObject game = new JSONObject();
        JSONObject dungeon = this.dungeon.toJson();
        game.put("mode", mode.modeString());
        game.put("dungeon", dungeon);
        return game;
    }

    private List<Entity> loadEntity(JsonArray entities) {
        List<Entity> ret = new ArrayList<Entity>();
        for (JsonElement e : entities) {
            Position pos = new Position(e.getAsJsonObject().get("x").getAsInt(),
                    e.getAsJsonObject().get("y").getAsInt(), e.getAsJsonObject().get("z").getAsInt());
            // load each object
            switch (e.getAsJsonObject().get("type").getAsString()) {
                case "wall":
                    ret.add(new Wall(pos, this.dungeon));
                    break;
                case "exit":
                    ret.add(new Exit(pos, this.dungeon));
                    break;
                case "boulder":
                    ret.add(new Boulder(pos, this.dungeon));
                    break;
                case "switch":
                    ret.add(new FloorSwitch(pos, this.dungeon, e.getAsJsonObject().get("lastActivation").getAsInt()));
                    break;
                case "door":
                    if (e.getAsJsonObject().has("logic"))
                        ret.add(new LogicDoor(pos, this.dungeon, e.getAsJsonObject().get("logic").getAsString(), e.getAsJsonObject().get("isOpen").getAsBoolean()));
                    else
                        ret.add(new Door(pos, this.dungeon, e.getAsJsonObject().get("colour").getAsString(), e.getAsJsonObject().get("isOpen").getAsBoolean()));
                    break;
                case "portal":
                    ret.add(new Portal(pos, this.dungeon, e.getAsJsonObject().get("group").getAsString()));
                    break;
                case "zombie_toast_spawner":
                    ret.add(new ZombieToastSpawner(pos, this.dungeon));
                    break;
                case "swamp_tile":
                    ret.add(new SwampTile(pos, this.dungeon, e.getAsJsonObject().get("movement_factor").getAsInt()));
                    break;
                case "light_bulb_off":
                    ret.add(new LightBulb(pos, this.dungeon, e.getAsJsonObject().get("logic").getAsString()));
                    break;
                case "light_bulb_on":
                    ret.add(new LightBulb(pos, this.dungeon, e.getAsJsonObject().get("logic").getAsString()));
                    break;
                case "wire":
                    ret.add(new Wire(pos, this.dungeon, e.getAsJsonObject().get("lastActivation").getAsInt()));
                    break;
                case "static_bomb":
                    if (e.getAsJsonObject().has("logic"))
                        ret.add(new StaticBomb(pos, this.dungeon, e.getAsJsonObject().get("logic").getAsString()));
                    else
                        ret.add(new StaticBomb(pos, this.dungeon));
                    break;
                // moving entities
                case "spider":
                    ret.add(new Spider(pos, this.dungeon, e.getAsJsonObject().get("hp").getAsInt()));
                    break;
                case "zombie_toast":
                    ret.add(new ZombieToast(pos, this.dungeon, e.getAsJsonObject().get("hp").getAsInt()));
                    break;
                case "mercenary":
                    ret.add(new Mercenary(pos, this.dungeon, e.getAsJsonObject().get("hp").getAsInt(), e.getAsJsonObject().get("sceptre_period").getAsInt(), e.getAsJsonObject().get("isAlly").getAsBoolean()));
                    break;
                case "assassin":
                    ret.add(new Assassin(pos, this.dungeon, e.getAsJsonObject().get("hp").getAsInt(), e.getAsJsonObject().get("sceptre_period").getAsInt(), e.getAsJsonObject().get("isAlly").getAsBoolean()));
                    break;
                // collectable entities
                case "treasure":
                    ret.add(new Treasure(pos, this.dungeon));
                    break;
                case "key":
                    ret.add(new Key(pos, this.dungeon, e.getAsJsonObject().get("colour").getAsString()));
                    break;
                case "health_potion":
                    ret.add(new HealthPotion(pos, this.dungeon));
                    break;
                case "invincibility_potion":
                    ret.add(new InvincibilityPotion(pos, this.dungeon));
                    break;
                case "invisibility_potion":
                    ret.add(new InvisibilityPotion(pos, this.dungeon));
                    break;
                case "wood":
                    ret.add(new Wood(pos, this.dungeon));
                    break;
                case "arrow":
                    ret.add(new Arrows(pos, this.dungeon));
                    break;
                case "bomb": 
                    if (e.getAsJsonObject().has("logic"))
                        ret.add(new Bomb(pos, this.dungeon, e.getAsJsonObject().get("logic").getAsString()));
                    else
                        ret.add(new Bomb(pos, this.dungeon));
                    break;
                case "sword":
                    ret.add(new Sword(pos, this.dungeon, e.getAsJsonObject().get("durability").getAsInt()));
                    break;
                case "armour":
                    ret.add(new Armour(pos, this.dungeon, e.getAsJsonObject().get("durability").getAsInt()));
                    break;

                case "one_ring":
                    ret.add(new TheOneRing(pos, this.dungeon));
                    break;
                // buildable entities
                case "bow":
                    ret.add(new Bow(pos, this.dungeon, e.getAsJsonObject().get("durability").getAsInt()));
                case "midnight_armour":
                    ret.add(new MidnightArmour(pos, this.dungeon, e.getAsJsonObject().get("durability").getAsInt()));
                case "sceptre":
                    ret.add(new Sceptre(pos, this.dungeon, e.getAsJsonObject().get("durability").getAsInt()));
                case "shield":
                    ret.add(new Shield(pos, this.dungeon,  e.getAsJsonObject().get("durability").getAsInt()));
            }
        }
        return ret;
    }

    private Character loadCharacter(JsonObject ch, Inventory inv) {
        Position pos = new Position(ch.getAsJsonObject().get("x").getAsInt(),
                    ch.getAsJsonObject().get("y").getAsInt(), ch.getAsJsonObject().get("z").getAsInt());
        Character character = new Character(pos, null, ch.get("hp").getAsInt(), inv);
        inv.setCharacter(character);
        return character;
    }

    private Inventory loadInventory(JsonObject inv) {
        List<BuildableEntity> buildable = loadEntity(inv.get("buildable").getAsJsonArray()).stream().map(e->(BuildableEntity)e).collect(Collectors.toList());

        List<CollectableEntity> item = loadEntity(inv.get("item").getAsJsonArray()).stream().map(e->(CollectableEntity)e).collect(Collectors.toList());
        
        return new Inventory(item, buildable);
    }
}
