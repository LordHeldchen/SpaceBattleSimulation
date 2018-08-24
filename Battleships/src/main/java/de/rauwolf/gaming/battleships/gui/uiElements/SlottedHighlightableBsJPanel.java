package de.rauwolf.gaming.battleships.gui.uiElements;

import de.rauwolf.gaming.battleships.ships.blueprints.Blueprint;
import de.rauwolf.gaming.battleships.ships.blueprints.Slotted;
import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.SlotException;

public class SlottedHighlightableBsJPanel extends HighlightableBsJPanel implements Slotted<Slotted<? extends Blueprint>> {
    private static final long serialVersionUID = -3775766461595274451L;

    private Slotted<? extends Blueprint> slottedElement;
    private final BsJLabel               slotLabel;

    public SlottedHighlightableBsJPanel() {
        slotLabel = new BsJLabel();
        add(slotLabel);
    }

    @Override
    public void setSlottedElement(Slotted<? extends Blueprint> element) throws SlotException {
        this.slottedElement = element;
        if (element.getSlottedElement() != null) {
            slotLabel.setText(element.getSlottedElement().getName());
        }
    }

    @Override
    public Slotted<? extends Blueprint> getSlottedElement() {
        return slottedElement;
    }

    @Override
    public boolean isEmpty() {
        return slottedElement.isEmpty();
    }

    @Override
    public void unsetSlottedElement() {
        slottedElement = null;
        slotLabel.setText("");
    }
}
