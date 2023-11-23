package ui.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

// represents an action to return to the main menu in the GUI
public class ReturnToMenuAction extends AbstractAction {
    private JPanel mainPanel;
    private JFrame frame;

    // EFFECTS: constructs a return to menu action with given panel and frame
    public ReturnToMenuAction(JPanel panel, JFrame frame) {
        super("Return to Menu");
        this.mainPanel = panel;
        this.frame = frame;
    }

    // MODIFIES: this
    // EFFECTS: returns the frame's contents back to the main menu
    @Override
    public void actionPerformed(ActionEvent e) {
        frame.setContentPane(mainPanel);
        frame.repaint();
        frame.revalidate();
    }
}
