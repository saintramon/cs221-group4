package prelim;

import prelim.view.GUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final GUI gui = new GUI();
                gui.setVisible(true);
            }
        });
    }
}