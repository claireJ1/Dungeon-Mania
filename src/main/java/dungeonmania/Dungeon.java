package dungeonmania;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.entity.*;
import dungeonmania.entity.staticEntity.*;
import dungeonmania.entity.movingEntity.*;
import dungeonmania.entity.collectableEntity.*;
import dungeonmania.exceptions.*;
import dungeonmania.goal.*;
import dungeonmania.response.models.*;
import dungeonmania.util.*;

public class Dungeon {
    private String dungeonId;
    private String dungeonName;
    private int height;
    private int width;
    private List<Entity> entities = new ArrayList<>();
    private Character player;
    private List<Position> spawnPoints = new ArrayList<>();
    private GoalComponent goal;
    private String goalString;

    private final int spiderLimit = 4;

    public Dungeon(String name) {
        this.dungeonId = name + " (" + LocalDateTime.now().toString() + ")";
        this.dungeonName = name;
        this.player = new Character(this);
        // Dungeon - read map
        JsonObject json = loadRaw(name);
        this.entities = loadEntity(json);
        groupEntity();
        // Dungeon - read dimension
        loadDimension(json);
        // Dungeon - player
        loadPlayer(json);
        // Dungeon - goal
        this.goal = loadGoal(json);
        notifyGoal();
        // update logic entities
        List<LogicEntity> elogic = new ArrayList<>();
        for (Entity e : entities) {
            if (e instanceof LogicEntity)
                elogic.add((LogicEntity) e);
        }
        for (LogicEntity e : elogic)
            e.update();
    }

    public Dungeon(String id, String name, List<Entity> entities, Character c, GoalComponent goal) {
        this.dungeonId = id;
        this.dungeonName = name;
        this.entities = entities;
        this.player = c;
        this.goal = goal;
        this.player.setDungeon(this);
        this.goal = goal;
        spawnPoints.add(c.getPosition());
    }

    public Dungeon(int w, int h, Position start, Position end) {
        this.dungeonId = "random_Prim" + " (" + LocalDateTime.now().toString() + ")";
        this.dungeonName = "random_prim";

        setRandomPrim(w, h, start, end);
        spawnPoints.add(start);
        notifyGoal();
    }

    public Dungeon(int w, int h, List<Entity> entities, String name, String id, JsonObject goal, Character c) {
        height = h;
        width = w;
        this.entities = entities;
        dungeonId = id;
        dungeonName = name;
        this.goal = loadGoal(goal);
        player = c;
        notifyGoal();
    }

    public String getId() {
        return dungeonId;
    }

    public String getName() {
        return dungeonName;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Character getCharacter() {
        return player;
    }

    public void setCharacter(Character player) {
        this.player = player;
    }

    public List<Position> getSpawnPoints() {
        return spawnPoints;
    }

    public void setPlayer(Character ch) {
        this.player = ch;
    }

    public void setGoalString(String s) {
        this.goalString = s;
    }

    /**
     * Add entity to this dungeon
     * 
     * @param item
     */
    public void addEntity(Entity item) {
        entities.add(item);
    }

    /**
     * Remove entity to this dungeon
     * 
     * @param item
     */
    public void removeEntity(Entity item) {
        entities.remove(item);
    }

    // ########## DATA LOADING SECTION #########
    /**
     * load map file using FileLoader
     * 
     * @param fileName
     * @throws IOException
     */
    private JsonObject loadRaw(String fileName) {
        try {
            if (!FileLoader.listFileNamesInResourceDirectory("dungeons").contains(fileName)) {
                throw new IllegalArgumentException();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String raw;
        try {
            raw = FileLoader.loadResourceFile("dungeons/" + fileName + ".json");
            return JsonParser.parseString(raw).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

    }

    /**
     * load entities from loadRaw() returned json
     * 
     * @param json
     * @return list of loaded entities
     */
    private List<Entity> loadEntity(JsonObject json) {

        List<Entity> ret = new ArrayList<Entity>();
        JsonArray jEntities = json.get("entities").getAsJsonArray();
        for (JsonElement e : jEntities) {
            if (!e.getAsJsonObject().get("type").getAsString().equals("player")) {
                Position pos = new Position(e.getAsJsonObject().get("x").getAsInt(),
                        e.getAsJsonObject().get("y").getAsInt());
                // load each object
                switch (e.getAsJsonObject().get("type").getAsString()) {
                case "wall":
                    ret.add(new Wall(pos, this));
                    break;
                case "exit":
                    ret.add(new Exit(pos, this));
                    break;
                case "boulder":
                    ret.add(new Boulder(pos, this));
                    break;
                case "switch":
                    ret.add(new FloorSwitch(pos, this));
                    break;
                case "door":
                    if (e.getAsJsonObject().has("logic"))
                        ret.add(new LogicDoor(pos, this, e.getAsJsonObject().get("logic").getAsString()));
                    else
                        ret.add(new Door(pos, this, e.getAsJsonObject().get("key").getAsString()));
                    break;
                case "switch_door":
                    ret.add(new LogicDoor(pos, this, e.getAsJsonObject().get("logic").getAsString()));
                    break;
                case "portal":
                    ret.add(new Portal(pos, this, e.getAsJsonObject().get("colour").getAsString()));
                    break;
                case "zombie_toast_spawner":
                    ret.add(new ZombieToastSpawner(pos, this));
                    break;
                case "swamp_tile":
                    ret.add(new SwampTile(pos, this, e.getAsJsonObject().get("movement_factor").getAsInt()));
                    break;
                case "light_bulb_off":
                    ret.add(new LightBulb(pos, this, e.getAsJsonObject().get("logic").getAsString()));
                    break;
                case "light_bulb_on":
                    ret.add(new LightBulb(pos, this, e.getAsJsonObject().get("logic").getAsString()));
                    break;
                case "wire":
                    ret.add(new Wire(pos, this));
                    break;

                case "spider":
                    ret.add(new Spider(pos, this));
                    break;
                case "zombie_toast":
                    ret.add(new ZombieToast(pos, this));
                    break;
                case "mercenary":
                    ret.add(new Mercenary(pos, this));
                    break;
                case "assassin":
                    ret.add(new Assassin(pos, this));
                    break;
                case "hydra":
                    ret.add(new Hydra(pos, this));
                    break;

                case "treasure":
                    ret.add(new Treasure(pos, this));
                    break;
                case "key":
                    ret.add(new Key(pos, this, e.getAsJsonObject().get("key").getAsString()));
                    break;
                case "health_potion":
                    ret.add(new HealthPotion(pos, this));
                    break;
                case "invincibility_potion":
                    ret.add(new InvincibilityPotion(pos, this));
                    break;
                case "invisibility_potion":
                    ret.add(new InvisibilityPotion(pos, this));
                    break;
                case "wood":
                    ret.add(new Wood(pos, this));
                    break;
                case "arrow":
                    ret.add(new Arrows(pos, this));
                    break;
                case "bomb":
                    if (e.getAsJsonObject().has("logic"))
                        ret.add(new Bomb(pos, this, e.getAsJsonObject().get("logic").getAsString()));
                    else
                        ret.add(new Bomb(pos, this));
                    break;

                case "sword":
                    ret.add(new Sword(pos, this));
                    break;
                case "armour":
                    ret.add(new Armour(pos, this));
                    break;
                case "anduril":
                    ret.add(new Anduril(pos, this));
                    break;

                case "one_ring":
                    ret.add(new TheOneRing(pos, this));
                    break;
                case "sun_stone":
                    ret.add(new SunStone(pos, this));
                    break;
                
                }
            }
        }
        return ret;
    }

    /**
     * load entities pairs if needed
     */
    private void groupEntity() {
        // Door - Key
        for (Entity e : entities) {
            if (e instanceof Door) {
                for (Entity item : entities) {
                    if (item instanceof Key) {
                        Door door = (Door) e;
                        Key key = (Key) item;
                        if (door.getGroup().equals(key.getGroup())) {
                            door.setKey(key);
                            key.setDoor(door);
                        }
                    }
                }
            }
        }
        // Portal - Portal
        for (Entity e : entities) {
            if (e instanceof Portal) {
                for (Entity item : entities) {
                    if (item instanceof Portal) {
                        Portal from = (Portal) e;
                        Portal to = (Portal) item;
                        if (from.getGroup().equals(to.getGroup()) && !from.getId().equals(to.getId())) {
                            from.setCorresponding(to);
                            to.setCorresponding(from);
                        }
                    }
                }
            }
        }
    }

    /**
     * load width x height if exist from loadRaw() returned json
     * 
     * @param json
     */
    public void loadDimension(JsonObject json) {
        if (json != null && json.has("height")) {
            this.height = json.get("height").getAsInt();
        } else {
            this.height = entities.stream().mapToInt(Entity::getYPosition).max().orElse(0);
            this.height++;
        }
        if (json != null && json.has("width")) {
            this.width = json.get("width").getAsInt();
        } else {
            this.width = entities.stream().mapToInt(Entity::getXPosition).max().orElse(0);
            this.width++;
        }
    }

    /**
     * load player spawn point sets from loadRaw() returned json
     * 
     * @param json
     */
    private void loadPlayer(JsonObject json) {
        List<Position> ret = new ArrayList<Position>();
        JsonArray jEntities = json.get("entities").getAsJsonArray();
        for (JsonElement e : jEntities) {
            if (e.getAsJsonObject().get("type").getAsString().equals("player")) {
                ret.add(new Position(e.getAsJsonObject().get("x").getAsInt(), e.getAsJsonObject().get("y").getAsInt()));
            }
        }
        this.spawnPoints = ret;
        spawnPlayer();
    }

    /**
     * load goals from loadRaw() returned json
     * 
     * @param json
     * @return GoalComponent
     */
    private GoalComponent loadGoal(JsonObject json) {
        if (!json.has("goal-condition"))
            return new GoalNone();
        JsonObject jGoal = json.get("goal-condition").getAsJsonObject();
        return recurGoal(jGoal);
    }

    /**
     * the recursion method called by loadGoal()
     * 
     * @param json
     * @return GoalComponent
     */
    private GoalComponent recurGoal(JsonObject json) {
        switch (json.get("goal").getAsString()) {
        case "AND":
            return new GoalConjunction(recurGoal(json.get("subgoals").getAsJsonArray().get(0).getAsJsonObject()),
                    recurGoal(json.get("subgoals").getAsJsonArray().get(1).getAsJsonObject()));
        case "OR":
            return new GoalDisjunction(recurGoal(json.get("subgoals").getAsJsonArray().get(0).getAsJsonObject()),
                    recurGoal(json.get("subgoals").getAsJsonArray().get(1).getAsJsonObject()));
        case "exit":
            return new GoalExit();
        case "enemies":
            return new GoalEnemy();
        case "treasure":
            return new GoalTreasure();
        case "boulders":
            return new GoalSwitch();
        }
        return new GoalNone();
    }

    // ########## ACTIONS ##########
    /**
     * mirror of tick() in controller
     * 
     * @param itemUsed
     * @param direction
     * @param tickNum
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
    public void tick(String itemUsed, Direction direction, int tickNum)
            throws IllegalArgumentException, InvalidActionException {
        if (player != null) {
            // player action
            player.useItem(itemUsed);
            player.move(direction);
            // update logic entities
            List<LogicEntity> elogic = new ArrayList<>();
            for (Entity e: entities) { if (e instanceof LogicEntity) elogic.add((LogicEntity)e); }
            for (LogicEntity e: elogic) e.update();
            // player collect item
            for (Entity e: getPositionEntity(player.getPosition())) {
                if (e instanceof CollectableEntity) player.addItem((CollectableEntity)e);
            }
            // battle
            for (MovingEntity enemy : getEnemiesOnPlayer()) {
                if (player == null) {
                    return;
                } else {
                    player.battle(enemy);
                }
            }
            if (player == null) {
                return;
            }
            // mob action
            entities.stream().filter(e -> e instanceof MovingEntity).map(e -> (MovingEntity) e).forEach(m -> m.move());
            // battle
            if (!getEnemiesOnPlayer().isEmpty()) {
                for (MovingEntity enemy : getEnemiesOnPlayer()) {
                    if (player == null) {
                        return;
                    } else {
                        player.battle(enemy);
                    }
                }
            }
            if (player == null) {
                return;
            }
            // spawn mobs
            spawnSpider(tickNum);
            spawnZombie(tickNum);
            spawnMercenary(tickNum);
            // is player dead
            if (player == null) {
                return;
            }
            // notify goal
            notifyGoal();
            // consume potion lifespan
            player.consumePotion();
        }
        
    }

    /**
     * mirror of interact() in controller
     * 
     * @param entityId
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
    public void interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        Entity target = getEntityById(entityId);
        if (target != null) {
            if (target instanceof Mercenary) {
                ((Mercenary) target).setToAlly(true);
            } else if (target instanceof ZombieToastSpawner) {
                ((ZombieToastSpawner) target).destroy();
            }
        } else
            throw new IllegalArgumentException("entityId is not a valid entity ID");
    }

    /**
     * mirror of build() in controller
     * 
     * @param buildable
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
    public void build(String buildable) throws IllegalArgumentException, InvalidActionException {
        player.build(buildable);
    }

    /**
     * generate player by selecting a spawn point
     */
    public void spawnPlayer() {
        Random rand = new Random();
        int pick = rand.nextInt(spawnPoints.size());
        if (player != null)
            player.setPosition(spawnPoints.get(pick).asLayer(2));
        else
            this.player = new Character(spawnPoints.get(pick).asLayer(2), this);
    }

    /**
     * generate spider random
     */
    public void spawnSpider(int tickNum) {
        if (getEntityCountByType("spider") >= spiderLimit) {
            return;
        }
            
        if (tickNum % 20 == 0) {
            Random random = new Random();
            Position pos = new Position(random.nextInt(width), random.nextInt(height));
            addEntity(new Spider(pos, this));
        }
    }

    /**
     * generate zombie calling every toaster
     * 
     * @param tickNum
     */
    public void spawnZombie(int tickNum) {
        List<ZombieToastSpawner> toasters = new ArrayList<ZombieToastSpawner>();
        for (Entity e : entities) {
            if (e instanceof ZombieToastSpawner)
                toasters.add(((ZombieToastSpawner) e));
        }
        toasters.stream().forEach(t -> t.spawnZombie(tickNum));
    }

    /**
     * Spawn hydra every 50 ticks in hard mode
     * 
     * @param tickNum the current tick number
     */
    public void spawnHydra(int tickNum) {
        if (tickNum % 50 == 0) {
            Random random = new Random();
            Position pos = new Position(random.nextInt(width), random.nextInt(height));
            addEntity(new Hydra(pos, this));
        }
    }

    /**
     * update goal observers
     */
    public void notifyGoal() {
        goal.update(this);
    }

    /**
     * given id get object
     * 
     * @param id
     * @return entity or null if not found
     */
    public Entity getEntityById(String id) {
        for (Entity e : entities) {
            if (e.getId().equals(id))
                return e;
        }
        return null;
    }

    /**
     * given type get numbers
     * 
     * @param type
     * @return count
     */
    public int getEntityCountByType(String type) {
        int count = 0;
        for (Entity e : entities) {
            if (e.getType().equals(type))
                count++;
        }
        return count;
    }

    /**
     * given type get objects
     * 
     * @param type
     * @return entities
     */
    public List<Entity> getEntityByType(String type) {
        List<Entity> ret = new ArrayList<Entity>();
        for (Entity e : entities) {
            if (e.getType().equals(type))
                ret.add(e);
        }
        return ret;
    }

    /**
     * given position and layer get object
     * 
     * @param pos
     * @param layer
     * @return entity or null if not found
     */
    public Entity existEntityByPositionLayer(Position pos, int layer) {
        for (Entity e : entities) {
            if (e.getPosition().equals(pos) && e.getPosition().getLayer() == layer)
                return e;
        }
        return null;
    }

    /**
     * what monster(s) I'm facing?
     * 
     * @return a list of mobs
     */
    public List<MovingEntity> getEnemiesOnPlayer() {
        List<MovingEntity> mobs = new ArrayList<MovingEntity>();
        if (player == null) {
            return mobs;
        }
        for (Entity e : entities) {
            if (e.getPosition().equals(player.getPosition()) && (e instanceof MovingEntity))
                mobs.add((MovingEntity) e);
        }
        return mobs;
    }

    public List<Entity> getPositionEntity(Position position) {
        List<Entity> result = new ArrayList<>();
        for (Entity e : entities) {
            if (e.getPosition().equals(position)) {
                result.add(e);
            }
        }
        return result;
    }

    public List<LogicEntity> getAdjacentLogics(Position position) {
        List<LogicEntity> ret = new ArrayList<>();
        for (Position adj : position.getCardinallyAdjacentPositions()) {
            for (Entity e : getPositionEntity(adj)) {
                if (e instanceof LogicEntity)
                    ret.add((LogicEntity) e);
            }
        }
        return ret;
    }

    /**
     * toDungeonResponse()
     * 
     * @return DungeonResponse
     */
    public DungeonResponse getInfo() {
        List<EntityResponse> rEntities = new ArrayList<EntityResponse>();
        entities.stream().forEach(
                e -> rEntities.add(new EntityResponse(e.getId(), e.getType(), e.getPosition(), e.getIsInteractable())));
        if (player != null) {
            rEntities.add(new EntityResponse("player_1", "player", getCharacter().getPosition(), false));
            return new DungeonResponse(getId(), getName(), rEntities, player.getItemInfoInInventory(), player.getBuildableItems(), goalString);
        } else {
            return (new DungeonResponse(getId(), getName(), rEntities, new ArrayList<>(), new ArrayList<>(), goalString));
        }

    }

    private void spawnMercenary(int numTick) {
        List<Entity> enemies = entities.stream().filter(e -> e instanceof MovingEntity).collect(Collectors.toList());

        if ((numTick % 60 == 0) && (enemies.size() > 0)) {
            Random random = new Random();
            int value = random.nextInt(10);
            if (value < 2) {
                entities.add(new Assassin(spawnPoints.get(0), this));
            } else {
                entities.add(new Mercenary(spawnPoints.get(0), this));
            }
        }
    }

    public GoalComponent getGoal() {
        return this.goal;
    }

    /**
     * Find all mercenaries that are 5 far away from the character
     * 
     * @param cur the current position of the character
     * @return a list of mercenaries within range
     */
    public List<Mercenary> findMercenaryWithinRange(Position cur) {
        List<Position> adjacent = cur.getAdjacentPositions();
        List<Position> allAdja = new ArrayList<>();
        List<Mercenary> result = new ArrayList<>();
        // find positions within range
        for (Position pos : adjacent) {
            allAdja.addAll(pos.getAdjacentPositions());
        }
        adjacent.addAll(allAdja);
        // find all Mercenaries
        for (Position pos : adjacent) {
            List<Entity> entities = this.getPositionEntity(pos);
            entities.stream().filter(e -> !pos.equals(cur) && e instanceof Mercenary)
                    .map(e -> result.add((Mercenary) e));
        }
        // remove duplicates
        List<Mercenary> resultNoDuplicate = result.stream().distinct().collect(Collectors.toList());
        return resultNoDuplicate;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    /**
     * 
     * @param position
     * @return whether there is wall
     */
    public boolean isWall(Position position) {
        if (existEntityByPositionLayer(position, 2) == null) {
            return false;
        }
        if (existEntityByPositionLayer(position, 2).getType() == "wall") {
            return true;
        }
        return false;
    }

    /**
     * 
     * @param position
     * @return whether there is Boulder
     */
    public boolean isBoulder(Position position) {
        if (existEntityByPositionLayer(position, 2) == null) {
            return false;
        }
        if (existEntityByPositionLayer(position, 2).getType() == "boulder") {
            return true;
        }
        return false;
    }

    /**
     * 
     * @param position
     * @return whether there is door
     */
    public boolean isDoor(Position position) {

        if (existEntityByPositionLayer(position, 2) == null) {
            return false;
        }
        if (existEntityByPositionLayer(position, 2).getType().contains("door")) {
            return true;
        }
        return false;
    }

    /**
     * 
     * @param entity
     * @return get the portal
     */
    public Portal getPortal(Position entity) {
        Entity portal = entities.stream().filter(e -> e instanceof Portal && e.getPosition().equals(entity)).findAny()
                .orElse(null);

        return (Portal) portal;
    }

    /**
     * 
     * @param position
     * @return whether there is boom used
     */
    public boolean isUsedBoom(Position position) {

        if (existEntityByPositionLayer(position, 2) == null) {
            return false;
        }
        if (existEntityByPositionLayer(position, 2) instanceof StaticBomb){
            return true;
        }
        return false;
    }

    /**
     * 
     * @param position
     * @return whether there is ZombieToastSpawner
     */
    public boolean isZombieToastSpawner(Position position) {

        if (existEntityByPositionLayer(position, 2) == null) {
            return false;
        }
        if (existEntityByPositionLayer(position, 2).getType() == "zombie_toast_spawner") {
            return true;
        }
        return false;
    }

    public void setRandomPrim(int w, int h, Position start, Position end) {
        boolean[][] maze = RandomPrim(w, h, start, end);
        player = new Character(start, this);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (!maze[i][j]) {
                    entities.add(new Wall(new Position(i, j), this));
                }
            }
        }

        entities.add(new Exit(end, this));
        goal = new GoalExit();
        width = w;
        height = h;
    }

    public boolean[][] RandomPrim(int w, int h, Position start, Position end) {

        // let maze be a 2D array of booleans 
        // (of size width and height) default false
        boolean[][] maze = new boolean[w][h];
        for (boolean[] row: maze) {
            Arrays.fill(row, false);
        }
        maze[start.getX()][start.getY()] = true;

        List<Position> options = new ArrayList<>();

        // add to options all neighbours of 'start' not on boundary that are of distance 2 away and are walls
        List<Position> validPos = positionsDistTwo(start, w, h);
        for (Position p : validPos) {
            options.add(p);
        }

        while (!options.isEmpty()) {

            Random rand = new Random();
            int nextIndex = rand.nextInt(options.size());
            Position next = options.get(nextIndex);
            options.remove(nextIndex);

            List<Position> neighbours  = positionsDistTwo(next, w, h).stream().filter(e -> maze[e.getX()][e.getY()])
                                                                              .collect(Collectors.toList());
            
            if (!neighbours.isEmpty()) {
                Random rand2 = new Random();
                int neIndex = rand2.nextInt(neighbours.size());
                Position neightbour = neighbours.get(neIndex);
                maze[next.getX()][next.getY()] = true;

                Position between = posBetween(neightbour, next);
                maze[between.getX()][between.getY()] = true;
                maze[neightbour.getX()][neightbour.getY()]  = true;
            }

            for (Position p : positionsDistTwo(next, w, h)) {
                if (!maze[p.getX()][p.getY()]) {
                    options.add(p);
                }
            }
            /** 
            positionsDistTwo(next, w, h).stream().filter(e -> !maze[e.getX()][e.getY()])
                                                 .forEach(e -> options.add(e));*/
            
        }

        if (!maze[end.getX()][end.getY()]) {
            maze[end.getX()][end.getY()] = true;

            List<Position> validPosOne = end.getCardinallyAdjacentPositions().stream()
                                                                             .filter(e -> e.getX() > 0 && e.getX() < (width - 1) && 
                                                                             e.getY() > 0 && e.getY() < (height - 1))
                                                                             .collect(Collectors.toList());
            
            Position posEmpty = validPosOne.stream().filter(e -> maze[e.getX()][e.getY()])
                                                    .findAny()
                                                    .orElse(null);

            if (posEmpty == null) {
                Random rand3 = new Random();
                int neighIndex = rand3.nextInt(validPosOne.size());
                Position adj = validPosOne.get(neighIndex);
                maze[adj.getX()][adj.getY()] = true;
            }
        }

        return maze;

    }

    private Position posBetween(Position p1, Position p2) {
        if (Math.abs(p1.getX() - p2.getX()) == 2) {
            return new Position(Math.min(p1.getX(), p2.getX()) + 1, p1.getY());

        } else if (Math.abs(p1.getY() - p2.getY()) == 2) {
            return new Position(p1.getX(), Math.min(p1.getY(), p2.getY()) + 1);

        } /**else if (p1.getX() > p2.getX() && p1.getY() < p2.getY()) {
            return new Position(p1.getX(), p2.getY());

        } else if (p1.getX() < p2.getX() && p1.getY() < p2.getY()) {
            return new Position(p2.getX(), p1.getY());

        } else if (p1.getX() < p2.getX() && p1.getY() > p2.getY()) {
            return new Position(p1.getX(), p2.getY());

        } else if (p1.getX() > p2.getX() && p1.getY() > p2.getY()) {
            return new Position(p2.getX(), p1.getY());
        }*/

        return null;
    }

    private List<Position> positionsDistTwo(Position p, int width, int height) {
        List<Position> posTwoAway = new ArrayList<>();

        int x = p.getX();
        int y = p.getY();
        /**posTwoAway.add(new Position(x+1, y+1));
        posTwoAway.add(new Position(x-1, y+1));
        posTwoAway.add(new Position(x+1, y-1));
        posTwoAway.add(new Position(x-1, y-1));*/
        posTwoAway.add(new Position(x, y+2));
        posTwoAway.add(new Position(x, y-2));
        posTwoAway.add(new Position(x+2, y));
        posTwoAway.add(new Position(x-2, y));

        List<Position> result = posTwoAway.stream().filter(e -> e.getX() > 0 && e.getX() < (width - 1) && 
                                                           e.getY() > 0 && e.getY() < (height - 1))
                                                   .collect(Collectors.toList());
        return result;
    }

    public JSONObject toJson() {
        JSONObject dungeon = new JSONObject();
        JSONArray entities = new JSONArray();
        for (Entity entity : this.entities) {
            entities.put(entity.toJson()) ;
        }
        dungeon.put("id", dungeonId);
        dungeon.put("name", dungeonName);
        dungeon.put("height", height);
        dungeon.put("width", width);
        if (player != null) {
            dungeon.put("character", player.toJson());
        }
        dungeon.put("entities", entities);
        dungeon.put("goal-condition", goal.toJson());

        return dungeon;
    }
}