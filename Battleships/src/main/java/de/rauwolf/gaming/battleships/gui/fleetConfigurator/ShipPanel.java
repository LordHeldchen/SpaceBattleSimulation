package de.rauwolf.gaming.battleships.gui.fleetConfigurator;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import de.rauwolf.gaming.battleships.gui.uiElements.StyledComboBox;
import de.rauwolf.gaming.battleships.ships.blueprints.NotEnoughtSlotsException;
import de.rauwolf.gaming.battleships.ships.blueprints.ShipBlueprint;
import de.rauwolf.gaming.battleships.ships.blueprints.StandardConfigurationResourceLoader;
import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.WrongSizeForSlotException;
import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.WrongTypeForSlotException;

public class ShipPanel extends JPanel {
    private static final long             serialVersionUID    = -8754796328572565344L;

    private StyledComboBox<ShipBlueprint> selectConfigBox;
    private JFormattedTextField           numField;
    private JButton                       toggleButton;

    private final String                  addShipClassText    = "+";
    private final String                  deleteShipClassText = "X";

    private class ToggleShipHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            setActive(!selectConfigBox.isVisible());
        }
    }

    boolean isActive() {
        return selectConfigBox.isVisible();
    }

    void setActive(boolean active) {
        toggleButton.setText(active ? deleteShipClassText : addShipClassText);
        selectConfigBox.setVisible(active);
        numField.setVisible(active);
        if (!active) {
            selectConfigBox.setSelectedIndex(-1);
            numField.setText("");
        }
    }

    ShipPanel() throws NumberFormatException, WrongSizeForSlotException, WrongTypeForSlotException {
        super(new GridLayout(1, 3));

        toggleButton = new JButton(addShipClassText);
        toggleButton.addActionListener(new ToggleShipHandler());

        try {
            selectConfigBox = new StyledComboBox<ShipBlueprint>(StandardConfigurationResourceLoader.getAvailableBlueprints());
            selectConfigBox.setVisible(false);
            selectConfigBox.setSelectedIndex(-1);
        } catch (InstantiationException | NotEnoughtSlotsException | IOException e) {
            throw new RuntimeException("Error in StandardConfigs of Ships!");
        }

        NumberFormat integerFormatter = NumberFormat.getIntegerInstance();
        integerFormatter.setMaximumIntegerDigits(5);
        numField = new JFormattedTextField(integerFormatter);
        numField.setVisible(false);
        numField.setText("");

        this.add(toggleButton);
        this.add(selectConfigBox);
        this.add(numField);
    }

    ShipBlueprint getSelectedShipBlueprint() {
        if (selectConfigBox.getSelectedIndex() != -1) {
            return (ShipBlueprint) selectConfigBox.getSelectedItem();
        } else {
            return null;
        }
    }

    int getSelectedNum() {
        if (!numField.getText().isEmpty()) {
            return Integer.parseInt(numField.getText().replaceAll("\\.", ""));
        } else {
            return 0;
        }
    }
}
