package de.rauwolf.gaming.battleships.gui.blueprintDisplay;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import de.rauwolf.gaming.battleships.gui.blueprintToText.BlueprintToDetailText;
import de.rauwolf.gaming.battleships.gui.uiElements.BsJLabel;
import de.rauwolf.gaming.battleships.gui.uiElements.BsJPanel;
import de.rauwolf.gaming.battleships.gui.uiElements.HighlightableBsJPanel;
import de.rauwolf.gaming.battleships.gui.uiElements.SlottedBsJTextArea;
import de.rauwolf.gaming.battleships.gui.uiElements.SlottedHighlightableBsJPanel;
import de.rauwolf.gaming.battleships.ships.blueprints.Blueprint;
import de.rauwolf.gaming.battleships.ships.blueprints.ShipBlueprint;
import de.rauwolf.gaming.battleships.ships.blueprints.Slotted;
import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.SlotException;

class BlueprintDesignPanel extends BsJPanel {
    private static final long           serialVersionUID                 = -8178574393202535053L;

    private static final int            COLS_OF_ENTRIES                  = 2;

    private static final String         SHIPNAME_LABEL_LAYOUT            = "span " + (COLS_OF_ENTRIES + 1) + " 1, w 100%, h 5%";
    private static final String         SECTION_LABEL_LAYOUT             = "span " + (COLS_OF_ENTRIES + 1) + " 1, w 100%, h 5%";

    private static final String         WEAPON_ENTRY_CONTAINER_LAYOUT    = "span 1 1, w 60%, h 50%";
    private static final String         COMPONENT_ENTRY_CONTAINER_LAYOUT = "span 1 1, w 60%, h 50%, top";
    private static final String         DETAILS_PANEL_LAYOUT             = "span 1 16, w 40%, h 40%, top";

    private static final String         ENTRY_LAYOUT                     = "span 1 1, w 50%, h 34px";

    private final BsJLabel              shipnameLabel;
    private final BsJPanel              weaponEntriesContainer;
    private final BsJPanel              componentEntriesContainer;

    private final SlottedBsJTextArea    detailsPanel;
    private final BlueprintToDetailText blueprintToDetailText;

    private HighlightableBsJPanel       highlightedElement;

    BlueprintDesignPanel() {
        shipnameLabel = new BsJLabel();
        shipnameLabel.setTitleCase();

        BsJLabel weaponSectionLabel = new BsJLabel("Weapons");
        weaponSectionLabel.setSubtitleCase();
        BsJLabel componentSectionLabel = new BsJLabel("Components");
        componentSectionLabel.setSubtitleCase();

        weaponEntriesContainer = new BsJPanel();
        componentEntriesContainer = new BsJPanel();

        blueprintToDetailText = new BlueprintToDetailText();
        detailsPanel = new SlottedBsJTextArea(blueprintToDetailText);

        addAndWrap(shipnameLabel, SHIPNAME_LABEL_LAYOUT);

        addAndWrap(weaponSectionLabel, SECTION_LABEL_LAYOUT);
        add(weaponEntriesContainer, WEAPON_ENTRY_CONTAINER_LAYOUT);
        addAndWrap(detailsPanel, DETAILS_PANEL_LAYOUT);

        addAndWrap(componentSectionLabel, SECTION_LABEL_LAYOUT);
        addAndWrap(componentEntriesContainer, COMPONENT_ENTRY_CONTAINER_LAYOUT);
    }

    public void setShipBlueprint(ShipBlueprint blueprint) throws SlotException {
        weaponEntriesContainer.removeAll();
        componentEntriesContainer.removeAll();

        shipnameLabel.setText(blueprint.getName());
        detailsPanel.unsetSlottedElement();
        blueprintToDetailText.setCurrentShipBlueprint(blueprint);

        setEntriesForContainer(blueprint.getWeaponSlots(), weaponEntriesContainer, "Size ");
        setEntriesForContainer(blueprint.getComponentSlots(), componentEntriesContainer, "Type ");
    }

    private <X, Y extends Slotted<? extends Blueprint>> void setEntriesForContainer(Map<X, List<Y>> slottedElements, BsJPanel containerToUse,
            String subsectionDisclaimer) throws SlotException {

        for (X slotType : slottedElements.keySet()) {
            int i = 0;
            List<Y> compomentListForType = slottedElements.get(slotType);

            if (!compomentListForType.isEmpty()) {
                BsJLabel subsectionLabel = new BsJLabel(subsectionDisclaimer + slotType);
                containerToUse.wrapAndAdd(subsectionLabel, SECTION_LABEL_LAYOUT);

                for (Y blueprintSlot : compomentListForType) {
                    SlottedHighlightableBsJPanel componentEntryPanel = addEntryPanel(containerToUse, (i % COLS_OF_ENTRIES) == (COLS_OF_ENTRIES - 1));
                    componentEntryPanel.setSlottedElement(blueprintSlot);
                    i++;
                }
            }
        }
    }

    private SlottedHighlightableBsJPanel addEntryPanel(BsJPanel containerToUse, boolean doWrap) {
        SlottedHighlightableBsJPanel componentEntryPanel = new SlottedHighlightableBsJPanel();
        componentEntryPanel.setBackground(Color.ORANGE);

        if (doWrap) {
            containerToUse.addAndWrap(componentEntryPanel, ENTRY_LAYOUT);
        } else {
            containerToUse.add(componentEntryPanel, ENTRY_LAYOUT);
        }

        return componentEntryPanel;
    }

    protected void setHighlighted(SlottedHighlightableBsJPanel wep) {
        if (wep.getSlottedElement() != null && wep.getSlottedElement().getSlottedElement() != null) {
            try {
                detailsPanel.setSlottedElement(wep.getSlottedElement());
            } catch (SlotException e) {
                e.printStackTrace();
                return;
            }
        } else {
            detailsPanel.unsetSlottedElement();
        }

        if (highlightedElement != null) {
            highlightedElement.setHightlighted(false);
        }

        wep.setHightlighted(true);
        highlightedElement = wep;

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() instanceof SlottedHighlightableBsJPanel) {
            SlottedHighlightableBsJPanel slottedPanel = (SlottedHighlightableBsJPanel) e.getSource();
            setHighlighted(slottedPanel);
        }
        super.mouseReleased(e);
    }
}
