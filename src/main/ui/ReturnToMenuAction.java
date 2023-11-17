package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ReturnToMenuAction extends AbstractAction {
    private JPanel panel;
    private JFrame frame;

    public ReturnToMenuAction(JPanel panel, JFrame frame) {
        super("Return to Menu");
        this.panel = panel;
        this.frame = frame;
    }

    // MODIFIES: this
    // EFFECTS: returns the frame's contents back to the main menu
    @Override
    public void actionPerformed(ActionEvent e) {
        frame.setContentPane(panel);
        frame.repaint();
        frame.revalidate();
    }
}
