package de.rauwolf.gaming.battleships.gui;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import de.rauwolf.gaming.battleships.gui.blueprintDisplay.BlueprintDesignerGUI;
import de.rauwolf.gaming.battleships.gui.blueprintDisplay.GUIPreferences;
import de.rauwolf.gaming.battleships.gui.keyBindings.KeyToActionDispatcher;
import de.rauwolf.gaming.battleships.gui.menubars.MainMenuBar;
import de.rauwolf.gaming.battleships.ships.blueprints.NotEnoughtSlotsException;
import de.rauwolf.gaming.battleships.ships.blueprints.ShipBlueprint;
import de.rauwolf.gaming.battleships.ships.blueprints.StandardConfigurationResourceLoader;
import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.SlotException;
import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.WrongSizeForSlotException;
import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.WrongTypeForSlotException;

public class Battleships_Main {

    private static MainMenuBar           mainMenuBar;
    private static JFrame                mainFrame;

    private static BlueprintDesignerGUI  blueprintDesignerGUI;
    private static KeyToActionDispatcher keyToActionDispatcher;

    private static GUIPreferences        guiPreferences;
    private static ShipBlueprint         fleetCruiserBP;

    public static void main(String[] args)
            throws InstantiationException, NotEnoughtSlotsException, IOException, NumberFormatException, WrongSizeForSlotException, WrongTypeForSlotException {

        guiPreferences = GUIPreferences.getInstance();
        guiPreferences.setDisplayWidth(1200);
        guiPreferences.setDisplayHeigth(900);
        fleetCruiserBP = StandardConfigurationResourceLoader.getBlueprint("fcrui");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    initializeComponents();
                } catch (SlotException e) {
                    e.printStackTrace();
                    return;
                }
                createAndRunGUI();
            }
        });
    }

    private static void initializeComponents() throws SlotException {
        mainMenuBar = new MainMenuBar(mainFrame);
        keyToActionDispatcher = new KeyToActionDispatcher();
        blueprintDesignerGUI = new BlueprintDesignerGUI(keyToActionDispatcher);

        blueprintDesignerGUI.setShipBlueprint(fleetCruiserBP);
    }

    private static void createAndRunGUI() {
        mainFrame = new JFrame("Ship Designer");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setPreferredSize(new Dimension(guiPreferences.getDisplayWidth(), guiPreferences.getDisplayHeigth()));
        mainFrame.setResizable(false);

        mainFrame.setJMenuBar(mainMenuBar);

        mainFrame.add(blueprintDesignerGUI);
        blueprintDesignerGUI.setVisible(true);

        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}