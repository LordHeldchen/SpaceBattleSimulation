package de.rauwolf.gaming.battleships.characters;

public class Character {
    private String name;
    
    public Character(String name) {
        super();
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
