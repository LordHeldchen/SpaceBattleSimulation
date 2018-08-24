package de.rauwolf.gaming.battleships.ships.blueprints;

import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.WrongSizeForSlotException;
import de.rauwolf.gaming.battleships.ships.weapons.Sized;

public class SizeLimitedSlot<T extends Sized> implements Slotted<T> {
    private SizeClass size;
    private T         element;

    public SizeLimitedSlot(SizeClass sizeClass) {
        this.size = sizeClass;
    }

    public void setSlottedElement(T element) throws WrongSizeForSlotException {
        if (element.getSize().equals(this.size)) {
            this.element = element;
        } else {
            throw new WrongSizeForSlotException();
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
        this.element = null;
    }
}
