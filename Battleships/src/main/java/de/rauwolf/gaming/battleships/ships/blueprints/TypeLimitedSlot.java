package de.rauwolf.gaming.battleships.ships.blueprints;

import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.WrongTypeForSlotException;
import de.rauwolf.gaming.battleships.ships.components.Typed;
import de.rauwolf.gaming.battleships.ships.shipHulls.ComponentType;

public class TypeLimitedSlot<T extends Typed> implements Slotted<T> {
    private T element;
    private final ComponentType type;

    public TypeLimitedSlot(ComponentType type) {
        this.type = type;
    }

    public void setSlottedElement(T element) throws WrongTypeForSlotException {
        if (element.getType().equals(this.type)) {
            this.element = element;
        } else {
            throw new WrongTypeForSlotException();
        }
    }

    public T getSlottedElement() {
        return this.element;
    }

    public boolean isEmpty() {
        return this.element == null;
    }

    @Override
    public void unsetSlottedElement() {
        element = null;
    }
}
