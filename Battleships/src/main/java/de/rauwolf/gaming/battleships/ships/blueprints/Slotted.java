package de.rauwolf.gaming.battleships.ships.blueprints;

import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.SlotException;

public interface Slotted<T> {
    public void setSlottedElement(T element) throws SlotException;

    public T getSlottedElement();

    public void unsetSlottedElement();

    public boolean isEmpty();
}
