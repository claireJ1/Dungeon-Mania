package dungeonmania.game;

import dungeonmania.Dungeon;

public interface GameMode {
    /**
     * Apply the current game mode to the given dungeon
     * @param dungeon the given dungeon to set game mode
     */
    public void apply(Dungeon dungeon);
    public String modeString();
}
