package main.java.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.Random;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ShipDesignerGUI_Try1 extends JFrame {

    class Surface extends JPanel {
        private static final long serialVersionUID = 1L;

        private void doDrawing(Graphics g) {
            // Ex1
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawString("Java 2D", 50, 50);

            // Ex2
            g2d.setColor(Color.blue);

            Dimension size = getSize();
            Insets insets = getInsets();

            int w = size.width - insets.left - insets.right;
            int h = size.height - insets.top - insets.bottom;

            Random r = new Random();

            for (int i = 0; i < 1000; i++) {
                int x = Math.abs(r.nextInt()) % w;
                int y = Math.abs(r.nextInt()) % h;
                g2d.drawLine(x, y, x, y);
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            doDrawing(g);
        }
    }

    class LeftPanel_NeedsToBeNamedAppropriately extends JPanel {
        public LeftPanel_NeedsToBeNamedAppropriately() {
            JComboBox<String> shipTypeSelectBox = new JComboBox<String>();
            add(shipTypeSelectBox);
            setMinimumSize(new Dimension(200, 500));
        }

        private static final long serialVersionUID = 1L;

        private void doDrawing(Graphics g) {}

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            doDrawing(g);
        }
    }

    private static final long serialVersionUID = 1L;

    public ShipDesignerGUI_Try1() {
        initUI();
    }

    private void initUI() {
        setTitle("Ship Designer");
        setLayout(new BorderLayout(1, 1));

        add(new LeftPanel_NeedsToBeNamedAppropriately(), BorderLayout.WEST);
        add(new Surface(), BorderLayout.CENTER);

        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                ShipDesignerGUI_Try1 sk = new ShipDesignerGUI_Try1();
                sk.setSize(1280, 960);
                sk.setResizable(false);
                sk.setLocation(300, 50);
                sk.setVisible(true);
            }
        });
    }
}