package main.java.gui.uiElements;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

public class StyledComboBox<T> extends JComboBox<T> {
    class StyledComboBoxUI extends BasicComboBoxUI {
        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox) {
                private static final long serialVersionUID = 5420875220145876302L;

                @Override
                protected Rectangle computePopupBounds(int px, int py, int pw, int ph) {
                    return super.computePopupBounds(px, py, Math.max(comboBox.getPreferredSize().width, pw), ph);
                }
            };
            popup.getAccessibleContext().setAccessibleParent(comboBox);
            return popup;
        }
    }

    private static final long serialVersionUID = 6695208567945069386L;

    public StyledComboBox(T[] content) {
        super(content);
        setUI(new StyledComboBoxUI());
        setBorder(new LineBorder(Color.BLACK));
    }
}
