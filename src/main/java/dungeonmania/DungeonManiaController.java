package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.game.Game;
import dungeonmania.game.GameModeHard;
import dungeonmania.game.GameModePeaceful;
import dungeonmania.game.GameModeStandard;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public class DungeonManiaController {
    private Game curGame;

    public DungeonManiaController() {
    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    public List<String> getGameModes() {
        return Arrays.asList("Standard", "Peaceful", "Hard");
    }

    /**
     * /dungeons
     * 
     * Done for you.
     */
    public static List<String> dungeons() {
        try {
            return FileLoader.listFileNamesInResourceDirectory("/dungeons");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public DungeonResponse newGame(String dungeonName, String gameMode) throws IllegalArgumentException {
        Game game;
        switch (gameMode.toLowerCase()) {
        case "peaceful":
            game = new Game(dungeonName, new GameModePeaceful());
            break;
        case "standard":
            game = new Game(dungeonName, new GameModeStandard());
            break;
        case "hard":
            game = new Game(dungeonName, new GameModeHard());
            break;
        default:
            throw new IllegalArgumentException("Invalid game mode.");

        }
        this.curGame = game;
        return this.curGame.getInfo();
    }

    public DungeonResponse saveGame(String id) {
        return curGame.saveGame(id);
    }

    public DungeonResponse loadGame(String id) throws IllegalArgumentException {

        try {
            if (!FileLoader.listFileNamesInDirectoryOutsideOfResources("savedGames").contains(id)) {
                throw new IllegalArgumentException("Invalid game id");
            } else {
                Gson gson = new Gson();
                try {
                    JsonReader reader = new JsonReader(new FileReader("savedGames/" + id + ".json"));
                    JsonObject json = gson.fromJson(reader, JsonObject.class);
                    String mode = json.get("mode").getAsString();
                    switch (mode.toLowerCase()) {
                        case "peaceful":
                            curGame = new Game(json, new GameModePeaceful());
                            break;
                        case "standard":
                            curGame = new Game(json, new GameModeStandard());
                            break;
                        case "hard":
                            curGame = new Game(json, new GameModeHard());
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid game mode.");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return curGame.getInfo();
    }

    public List<String> allGames() {
        try {
            return FileLoader.listFileNamesInDirectoryOutsideOfResources("savedGames");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public DungeonResponse tick(String itemUsed, Direction movementDirection)
            throws IllegalArgumentException, InvalidActionException {
        curGame.doTick(itemUsed, movementDirection);
        return this.curGame.getInfo();
    }

    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        curGame.doInteract(entityId);
        return this.curGame.getInfo();
    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        curGame.doBuild(buildable);
        return this.curGame.getInfo();
    }

    public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd, String gameMode) throws IllegalArgumentException {
        Game game;
        switch (gameMode.toLowerCase()) {
        case "peaceful":
            game = new Game(xStart, yStart, xEnd, yEnd, new GameModePeaceful());
            break;
        case "standard":
            game = new Game(xStart, yStart, xEnd, yEnd, new GameModeStandard());
            break;
        case "hard":
            game = new Game(xStart, yStart, xEnd, yEnd, new GameModeHard());
            break;
        default:
            throw new IllegalArgumentException("Invalid game mode.");

        }
        this.curGame = game;
        return this.curGame.getInfo();
    }

}
