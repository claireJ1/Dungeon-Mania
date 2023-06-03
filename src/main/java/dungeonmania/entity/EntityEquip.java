package dungeonmania.entity;

import dungeonmania.Character;

public interface EntityEquip {
    public void equip(Character c);

    public void use(Character c);

    public int getDurability();

}

