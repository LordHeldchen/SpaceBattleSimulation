package de.rauwolf.gaming.battleships.gui.fleetConfigurator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.rauwolf.gaming.battleships.battle.SingleBattle;
import de.rauwolf.gaming.battleships.ships.Fleet;
import de.rauwolf.gaming.battleships.ships.blueprints.NotEnoughtSlotsException;
import de.rauwolf.gaming.battleships.ships.blueprints.StandardConfigurationResourceLoader;
import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.WrongSizeForSlotException;
import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.WrongTypeForSlotException;

class FleetConfigurator extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private List<FleetPanel> fleetCreatorPanels = new LinkedList<FleetPanel>();
    
    private FleetConfigurator()
            throws InstantiationException, NotEnoughtSlotsException, IOException, NumberFormatException, WrongSizeForSlotException, WrongTypeForSlotException {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Battle Simulator");
        this.setSize(1200, 600);
        this.setLocationRelativeTo(null);

        BorderLayout frameGrid = new BorderLayout();

        this.setLayout(frameGrid);

        this.add(createFleetPanel(), BorderLayout.CENTER);
        this.add(createButtonPanel(), BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private JPanel createFleetPanel()
            throws InstantiationException, NotEnoughtSlotsException, IOException, NumberFormatException, WrongSizeForSlotException, WrongTypeForSlotException {

        final JPanel fleetPanel = new JPanel(new GridLayout(1,4));
        
        for (int i = 0; i < 3; i++) {
            FleetPanel fleetCreatorPanel = new FleetPanel(i + 1);
            fleetCreatorPanels.add(fleetCreatorPanel);
            fleetPanel.add(fleetCreatorPanel);
        }
        
        return fleetPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        
        JButton runSimulation = new JButton("Run Simulation");

        runSimulation.addActionListener(actionEvent -> {
            Set<Fleet> allFleets = fleetCreatorPanels.stream().map(p -> p.getFleet()).collect(Collectors.toSet());
            SingleBattle battle = new SingleBattle(allFleets);

            battle.checkSetup();
            battle.fight();
            battle.endBattle();
            
            for (Fleet fleet: allFleets) {
                fleet.getTypesOfShipsInFleet().stream().forEach(bp -> StandardConfigurationResourceLoader.saveBlueprint(bp));
            }
        });
        buttonPanel.add(runSimulation, BorderLayout.SOUTH);
        return buttonPanel;
    }
}
