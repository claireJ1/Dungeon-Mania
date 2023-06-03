package dungeonmania.entity.movingEntity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.collectableEntity.*;
import dungeonmania.entity.staticEntity.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.*;

public class Mercenary extends MovingEntity {
    public static int idNum = 1;
    private boolean isAlly;
    private int healthPoint = 16;
    public static int attackDamage = 3;
    private Armour armour;
    public Direction move;
    private int sceptrePeriod = -1;

    /**
     * Constructor for mercenary
     * @param pos
     * @param dun
     */
    public Mercenary(Position pos, Dungeon dun) {
        super(pos, dun, "mercenary_" + Integer.toString(idNum), "mercenary", true);
        this.armour = generateArmour();
        idNum++;
    }

    public Mercenary(Position pos, Dungeon dun, int hp, int sceptrePeriod, boolean ally) {
        super(pos, dun, "mercenary_" + Integer.toString(idNum), "mercenary", true);
        isAlly = ally;
        this.armour = generateArmour();
        healthPoint = hp;
        this.sceptrePeriod = sceptrePeriod;
        idNum++;
    }

    /**
     * Constructor for mercenary
     * @param pos
     * @param dun
     * @param name
     * @param type
     */
    public Mercenary(Position pos, Dungeon dun, String name, String type) {
        super(pos, dun, name, type, true);
        this.armour = generateArmour();
    }


    @Override
    public Armour getArmour() { return this.armour;}

    @Override
    public int getHealthPoint() { return healthPoint; }

    @Override
    public int getAttackDamage() { return attackDamage; }

    public int getSceptrePeriod() { return sceptrePeriod;}

    @Override
    public void setHealthPoint(int curHealth) { this.healthPoint = curHealth; }

    public void setSceptrePeriod(int period) { this.sceptrePeriod = period; }

    public void setIsAlly(boolean isAlly) {
        this.isAlly = isAlly;
    }

    @Override
    public double getDefenseCoefficient() {
        if (this.armour != null) {
            return 0.5;
        }
        return 1.0;
    }

    public boolean getIsAlly() { return isAlly; }


    /**
     * Move mercenary according to position and state of the character
     */
    @Override
    public void move() {

        Position charPos = super.getCharacterPos();
        Position mercPos = getPosition();
        allyStateCheck();
        swampMove(getPosition());

        if (getFrozen() > 0) {
            return;
        }

        if (super.getCharacter().getInvisible() > 0 && !isAlly) {
            return;
        } else if (super.getCharacter().getInvincible() > 0 && !isAlly) {
    
            this.setPosition(checkPortal(furthestNextPosFromChar()).asLayer(2));

        } else {
            Position p = nextPos(new Position(mercPos.getX(), mercPos.getY()), 
                                 new Position(charPos.getX(), charPos.getY()));
            this.setPosition(checkPortal(p).asLayer(2));
        }
        
    }

    /**
     * Helper function to determine the mercenary's ally state
     */
    private void allyStateCheck() {

        // if program reaches 10 tick 
        if (this.getSceptrePeriod() == 0) {
            this.setSceptrePeriod(-1);
            getCharacter().removeAlly(this);
            this.isAlly = false;
        }

        // if mercenary is ally with limitation
        if (this.getIsAlly() && (this.sceptrePeriod != -1)) {
            this.sceptrePeriod--;
        } 
    }

    /**
     * Helper method to check portal exists and get mercenary correct position 
     * @param current
     * @return
     */
    private Position checkPortal(Position current) {
        Portal p = super.getDungeon().getPortal(current);

        // check if portal exists at this location and is valid 
        if (p != null) {
            current = p.getCorrespondingPos(move, getPosition());
        } 
        
        return current;
    }

    /**
     * Set mercenary to ally
     * @preconditions b != null
     * @param b
     * @throws InvalidActionException if player is not in range or player doesn't have treasure
     */
    public void setToAlly(Boolean b) throws InvalidActionException {

        if (isAlly || !b) {
            isAlly = b;
            return;
        }

        // Check Mercenary is in range
        Position diff = Position.calculatePositionBetween(this.getPosition(), super.getCharacterPos());
        if ((Math.abs(diff.getX()) + Math.abs(diff.getY())) > 2) {
            throw new InvalidActionException("Player is not within 2 cardinal tiles to the mercenary");
        }

        // if character has treasure to bribe
        bribeAction();
        this.isAlly = b;
    }

    public void bribeAction() throws InvalidActionException {
        super.getCharacter().bribeMercenary(this);
    }

    /**
     * Find the furthest adjacent position away from character
     * @return
     */
    public Position furthestNextPosFromChar() {
       
        // Get a list of adjacent position and 
        // determine if it is a valid position to walk on
        // Merge With Dungeon convertDungeonToMap();
        List<Position> validCells = this.getPosition().getCardinallyAdjacentPositions().stream()
                                                                                       .filter(e -> validPos(e))
                                                                                       .collect(Collectors.toList());
        
        if (validCells.isEmpty()) {
            return getPosition();
        }
        // Find furthest next position from character
        Position player = this.getCharacterPos();
        Position furthest = validCells.stream().sorted(Comparator.comparingInt(e -> distanceBetweenTwoPos(e, player)))
                                               .reduce((first, second) -> second)
                                               .orElse(null);

        return furthest;
    }

    /**
     * Helper method to generate armour randomly
     * @return
     */
    private Armour generateArmour() {
        Random random = new Random();
        int value = random.nextInt(10 - 1 + 1) + 1;

        if (value % 3 == 0) {
            return new Armour(new Position(-1, -1), super.getDungeon());
        }

        return null;
    }

    /**
     * Helper method to calculate the distance between to position
     * @param p1
     * @param p2
     * @return
     */
    private int distanceBetweenTwoPos(Position p1, Position p2) {
        Position diff = Position.calculatePositionBetween(p1, p2);
        return Math.abs(diff.getX()) + Math.abs(diff.getY());
    }

    /**
     * Given two position, find a position such that it is 
     * the first motion required to move form one position to another
     * @param from
     * @param to
     * @return
     */
    public Position nextPos(Position from, Position to) {
        // merge with master convertDungeonToMap();
        List<Position> output = new ArrayList<>();
        for (int i = 0; i < getDungeon().getWidth(); i++) {
            for (int j = 0; j < getDungeon().getHeight(); j++) {
                Position pos = new Position(i, j);
                if (validPos(pos)) {
                    output.add(pos);
                }
            }
        }
        return dijAlgo(output, from, to);
    }
 

    /**
     * Helper function to find the next position of the shorest path 
     * to the character 
     * @param validPositions
     * @param entity
     * @param charac
     * @return
     */
    private Position dijAlgo(List<Position> validPositions, Position entity, Position charac) {

        // check entity is not at the same position as charac
        if (entity.equals(charac)) {
            this.move = Direction.NONE;
            return entity;
        }

        Map<Position, Integer> dist = new HashMap<>();
        Map<Position, Position> prev = new HashMap<>();
        Queue<Position> q = new ArrayDeque<>();

        q.add(entity);
        for (Position p : validPositions) {
            dist.put(p, Integer.MAX_VALUE);
            prev.put(p, null);
        }
        dist.replace(entity, 0);

        while (!q.isEmpty()) {
            Position u = q.poll();
            List<Position> neighbours = u.getCardinallyAdjacentPositions();
            List<Position> validNeighbours = neighbours.stream().filter(e -> validPositions.contains(e)).collect(Collectors.toList());
            for (Position v : validNeighbours) {
                if ((dist.get(u) + costFromAtoB(u, v)) < dist.get(v)) {
                    dist.replace(v, dist.get(u) + costFromAtoB(u, v));
                    prev.replace(v, u);
                    q.add(v);
                }
            }
        }

        // Trace back
        if (prev.get(charac) == null) {
            move = Direction.NONE;
            return getPosition();
        }

        Position prevPos = prev.get(charac);
        Position nextPos = charac;
        while (!prevPos.equals(entity)) {
            nextPos = prevPos;
            prevPos = prev.get(nextPos);
        }

        // Find out the direction mercnary is heading 
        Position mercMove = Position.calculatePositionBetween(entity, nextPos);
        for (Direction dir : Direction.values()) {
            if (mercMove.equals(dir.getOffset())) {
                this.move = dir;
            }
        }

        return nextPos;
    }

    private int costFromAtoB(Position a, Position b) {
        SwampTile ent = getDungeon().getPositionEntity(b).stream()
                                                      .filter(e -> (e instanceof SwampTile))
                                                      .map(e -> (SwampTile) e)
                                                      .findAny()
                                                      .orElse(null);

        if (ent == null) {
            return 1;
        } else {
            return ent.getMovementFactor();
        } 

    }

    /**
     * Helper method to determine the position is valid for mercenary to walk
     * @param p
     * @return
     */
    private boolean validPos(Position p) {

        List<Entity> entityInDun = getDungeon().getEntities();
        for (Entity e : entityInDun) {
            if (!isValidEntity(e) && e.getPosition().equals(p) && (!p.equals(getCharacterPos()))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determine the entity is a contraint or not
     * @param e
     * @return
     */
    private boolean isValidEntity(Entity e) {
        if (e instanceof StaticEntity && !(e instanceof FloorSwitch) 
            && !(e instanceof Exit) && !(e instanceof Portal) && !(e instanceof SwampTile)) {
                return false;
        }

        return true;
    }

    @Override
    public JSONObject toJson() {
        
        JSONObject j = new JSONObject();
        j.put("x", getXPosition());
        j.put("y", getYPosition());
        j.put("z", getPosition().getLayer());
        j.put("hp", healthPoint);
        j.put("sceptre_period", sceptrePeriod);
        j.put("type", "mercenary");
        j.put("isAlly", isAlly);
        return j;
    }
    
}



