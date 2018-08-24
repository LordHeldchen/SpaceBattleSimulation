package de.rauwolf.gaming.battleships.gui.blueprintDisplay;

import java.awt.Color;
import java.awt.Cursor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;

import de.rauwolf.gaming.battleships.gui.keyBindings.KeyToActionDispatcher;
import de.rauwolf.gaming.battleships.gui.uiElements.BsJPanel;
import de.rauwolf.gaming.battleships.ships.blueprints.ShipBlueprint;
import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.SlotException;

public class BlueprintDesignerGUI extends BsJPanel {
    private static final long          serialVersionUID             = 3913466735610109147L;

    public static final String         VIEW_MEK_DISPLAY             = "viewMekDisplay";
    public static final String         VIEW_MINI_MAP                = "viewMiniMap";
    public static final String         VIEW_LOS_SETTING             = "viewLOSSetting";
    public static final String         VIEW_UNIT_OVERVIEW           = "viewUnitOverview";
    public static final String         VIEW_ZOOM_IN                 = "viewZoomIn";
    public static final String         VIEW_ZOOM_OUT                = "viewZoomOut";
    public static final String         VIEW_TOGGLE_ISOMETRIC        = "viewToggleIsometric";
    public static final String         VIEW_TOGGLE_FOV_DARKEN       = "viewToggleFovDarken";
    public static final String         VIEW_TOGGLE_FOV_HIGHLIGHT    = "viewToggleFovHighlight";
    public static final String         VIEW_TOGGLE_FIRING_SOLUTIONS = "viewToggleFiringSolutions";
    public static final String         VIEW_MOVE_ENV                = "viewMovementEnvelope";
    public static final String         VIEW_MOVE_MOD_ENV            = "viewMovModEnvelope";

    public final KeyToActionDispatcher keyToActionDispatcher;

    private File                       curfileBoardImage;
    private File                       curfileBoard;

    private final BlueprintDesignPanel blueprintDesignArea;
    private final BsJPanel             componentChoiceView;
    private final BsJPanel             shipStatView;

    private HashMap<String, String>    secondaryNames               = new HashMap<String, String>();

    public BlueprintDesignerGUI(KeyToActionDispatcher keyToActionDispatcher) {
        blueprintDesignArea = new BlueprintDesignPanel();
        componentChoiceView = new BsJPanel();
        shipStatView = new BsJPanel();

        this.keyToActionDispatcher = keyToActionDispatcher;

        shipStatView.setBackground(Color.CYAN);
        blueprintDesignArea.setBackground(Color.LIGHT_GRAY);
        componentChoiceView.setBackground(Color.WHITE);

        add(blueprintDesignArea, "w 70%, h 100%");
        addAndWrap(shipStatView, "w 30%, h 100%");
        // add(componentChoiceView, "span 2 1, w 100%, h 25%");
    }

    void switchPanel(String blueprintName) {
        String name = String.valueOf(blueprintName);
        String secondaryToShow = secondaryNames.get(name);

        if (secondaryToShow != null) {
            componentChoiceView.setVisible(true);
        } else {
            componentChoiceView.setVisible(false);
        }
    }

    public void setShipBlueprint(ShipBlueprint blueprint) throws SlotException {
        blueprintDesignArea.setShipBlueprint(blueprint);
    }

    /**
     * Loads a preview image of the unit into the BufferedPanel.
     *
     * @param bp
     * @param entity
     */
    public void loadPreviewImage(JLabel bp, ShipBlueprint entity) {
        // Image camo = null;
        // if (entity.getCamoFileName() != null) {
        // camo = bv.getTilesetManager().getEntityCamo(entity);
        // }
        // int tint = PlayerColors.getColorRGB(player.getColorIndex());
        // bp.setIcon(new
        // ImageIcon(bv.getTilesetManager().loadPreviewImage(entity, camo, tint,
        // bp)));
    }

    /**
     * Checks to see if there is already a path and name stored; if not, calls "save as"; otherwise, saves the board to the specified file.
     */
    private void boardSave() {
        if (curfileBoard == null) {
            boardSaveAs();
            return;
        }

        try (OutputStream os = new FileOutputStream(curfileBoard)) {
        } catch (IOException ex) {
            System.err.println("error opening file to save!");
            System.err.println(ex);
        }
    }

    /**
     * Saves the board in PNG image format.
     */
    private void boardSaveImage() {
        if (curfileBoardImage == null) {
            boardSaveAsImage();
            return;
        }
        JDialog waitD = new JDialog((JFrame) getParent(), "BoardEditor.waitDialog.title");
        waitD.add(new JLabel("BoardEditor.waitDialog.message"));
        waitD.setSize(250, 130);
        // move to middle of screen
        waitD.setLocation((getParent().getSize().width / 2) - (waitD.getSize().width / 2), (getParent().getSize().height / 2) - (waitD.getSize().height / 2));
        waitD.setVisible(true);
        getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        waitD.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        waitD.setVisible(false);
        getParent().setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Opens a file dialog box to select a file to save as; saves the board to the file.
     */
    private void boardSaveAs() {
        JFileChooser fc = new JFileChooser("data" + File.separator + "boards");
        fc.setLocation(getParent().getLocation().x + 150, getParent().getLocation().y + 100);
        fc.setDialogTitle("BoardEditor.saveBoardAs");
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File dir) {
                return ((null != dir.getName()) && (dir.getName().endsWith(".board") || dir.isDirectory()));
            }

            @Override
            public String getDescription() {
                return "*.board";
            }
        });

        int returnVal = fc.showSaveDialog(getParent());
        if ((returnVal != JFileChooser.APPROVE_OPTION) || (fc.getSelectedFile() == null)) {
            return;
        }
        curfileBoard = fc.getSelectedFile();

        // make sure the file ends in board
        if (!curfileBoard.getName().toLowerCase().endsWith(".board")) {
            try {
                curfileBoard = new File(curfileBoard.getCanonicalPath() + ".board");
            } catch (IOException ie) {
                return;
            }
        }
        boardSave();
    }

    /**
     * Opens a file dialog box to select a file to save as; saves the board to the file as an image. Useful for printing boards.
     */
    private void boardSaveAsImage() {
        JFileChooser fc = new JFileChooser(".");
        fc.setLocation(getParent().getLocation().x + 150, getParent().getLocation().y + 100);
        fc.setDialogTitle("BoardEditor.saveAsImage");
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File dir) {
                return (null != dir.getName()) && (dir.getName().endsWith(".png") || dir.isDirectory());
            }

            @Override
            public String getDescription() {
                return ".png";
            }
        });
        int returnVal = fc.showSaveDialog(getParent());
        if ((returnVal != JFileChooser.APPROVE_OPTION) || (fc.getSelectedFile() == null)) {
            // I want a file, y'know!
            return;
        }
        curfileBoardImage = fc.getSelectedFile();

        // make sure the file ends in png
        if (!curfileBoardImage.getName().toLowerCase().endsWith(".png")) {
            try {
                curfileBoardImage = new File(curfileBoardImage.getCanonicalPath() + ".png");
            } catch (IOException ie) {
                // failure!
                return;
            }
        }
        boardSaveImage();
    }
}
