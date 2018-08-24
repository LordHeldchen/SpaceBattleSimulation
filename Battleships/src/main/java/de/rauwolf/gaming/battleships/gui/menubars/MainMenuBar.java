package de.rauwolf.gaming.battleships.gui.menubars;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainMenuBar extends JMenuBar {
    private static final long serialVersionUID = 1L;

    public static final String COMMON_MENU_BAR_FILE_MENU_ID = "CommonMenuBar.FileMenu";
    public static final String COMMON_MENU_BAR_UNIT_LIST_MENU_ID = "CommonMenuBar.UnitListMenu";

    public MainMenuBar(JFrame parent) {
        this();
    }

    public MainMenuBar() {
        JMenu menu;

        menu = new JMenu("CommonMenuBar.FileMenu");
        menu.setMnemonic(KeyEvent.VK_F);
        add(menu);

        addFleetConfiguratorSubMenu(menu);
    }

    private void addFleetConfiguratorSubMenu(JMenu menu) {
        JMenu submenu = new JMenu(COMMON_MENU_BAR_UNIT_LIST_MENU_ID);
        addMenuItem(submenu, ActionCommandConstants.SHOW_FLEET_CONFIGURATOR);
        menu.add(submenu);
    }

    private void addMenuItem(JMenu menu, ActionCommand action) {
        JMenuItem showFleetConfiguration = new JMenuItem(action.getLabel());
        showFleetConfiguration.addActionListener(action.getActionListener());
        menu.add(showFleetConfiguration);
    }
}
