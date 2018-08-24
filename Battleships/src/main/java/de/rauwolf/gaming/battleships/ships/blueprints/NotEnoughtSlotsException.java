package de.rauwolf.gaming.battleships.ships.blueprints;

public class NotEnoughtSlotsException extends Exception {

    private static final long serialVersionUID = 1L;

    public NotEnoughtSlotsException(String string) {
        super(string);
    }

}
