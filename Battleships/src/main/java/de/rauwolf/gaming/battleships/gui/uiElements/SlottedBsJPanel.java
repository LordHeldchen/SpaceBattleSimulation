package de.rauwolf.gaming.battleships.gui.uiElements;

import de.rauwolf.gaming.battleships.gui.blueprintToText.BlueprintToText;
import de.rauwolf.gaming.battleships.ships.blueprints.Blueprint;
import de.rauwolf.gaming.battleships.ships.blueprints.Slotted;
import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.SlotException;

public class SlottedBsJPanel<X extends Blueprint> extends BsJPanel implements Slotted<Slotted<X>> {
    private static final long serialVersionUID = -3775766461595274451L;

    private Slotted<X>        slottedElement;
    private final BsJLabel    slotLabel;
    private BlueprintToText   blueprintToText;

    public SlottedBsJPanel(BlueprintToText blueprintToText) {
        slotLabel = new BsJLabel();
        add(slotLabel);
        this.blueprintToText = blueprintToText;
    }

    @Override
    public void setSlottedElement(Slotted<X> element) throws SlotException {
        this.slottedElement = element;
        if (element.getSlottedElement() != null) {
            slotLabel.setText(blueprintToText.toText(element.getSlottedElement()));
        }
    }

    @Override
    public Slotted<X> getSlottedElement() {
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
