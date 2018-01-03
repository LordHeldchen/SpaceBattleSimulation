package main.java.gui.fleetConfigurator;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.java.ships.Fleet;
import main.java.ships.blueprints.ShipBlueprint;

public class FleetPanel extends JPanel {
    private static final long serialVersionUID = -8754796328572565344L;
    
    private List<ShipPanel> shipClassSelectorList;
    private boolean showFleet;
    private Integer fleetOwnerId;

    FleetPanel(int fleetNum) {
        super(new GridLayout(17,1));
        this.shipClassSelectorList = new LinkedList<ShipPanel>();
        this.showFleet = false;
        this.fleetOwnerId = fleetNum;
        
        JLabel shipLabel = new JLabel("Ships of Fleet " + fleetNum);
        this.add(shipLabel);
        
        for (int i = 0; i < 15; i++) {
            ShipPanel shipClassSelectorPanel = new ShipPanel();
            shipClassSelectorPanel.setVisible(false);
            shipClassSelectorList.add(shipClassSelectorPanel);
            this.add(shipClassSelectorPanel);
        }
        
        final JButton createNewFleetButton = new JButton("Create Fleet");
        createNewFleetButton.addActionListener(new ToggleFleetHandler(createNewFleetButton, fleetNum));
        this.add(createNewFleetButton);
    }
    
    Fleet getFleet() {
        Fleet fleet = new Fleet(fleetOwnerId);
        for (ShipPanel shipPanel : shipClassSelectorList) {
            ShipBlueprint selectedShipBlueprint = shipPanel.getSelectedShipBlueprint();
            if (selectedShipBlueprint != null) {
                fleet.add(selectedShipBlueprint, shipPanel.getSelectedNum());
            }
        }
        return fleet;
    }

    private class ToggleFleetHandler implements ActionListener {
        private JButton createNewFleetButton;

        private ToggleFleetHandler(JButton createNewFleetButton, int fleetNum) {
            this.createNewFleetButton = createNewFleetButton;
        }
        
        public void actionPerformed(ActionEvent event) {
            showFleet = !showFleet;
            createNewFleetButton.setText(showFleet ? "Delete Fleet" : "Create Fleet");

            for (ShipPanel shipClassSelector : shipClassSelectorList) {
                shipClassSelector.setActive(false);
                shipClassSelector.setVisible(showFleet);
            }
        }
    }
}
