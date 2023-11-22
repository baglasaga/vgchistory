package ui;

import model.MatchHistory;
import ui.actions.AddMatchAction;
import ui.actions.LoadMatchHistoryAction;
import ui.actions.SaveMatchHistoryAction;
import ui.actions.ViewMatchHistoryAction;

import javax.swing.*;
import java.awt.*;

public class MatchHistoryViewerGUI extends JFrame {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;

    private MatchHistory mh;
    private JTabbedPane sidebar;
    private JPanel panel;

    public MatchHistoryViewerGUI() {
        super("VGC Match History Viewer");
        mh = new MatchHistory();
        panel = new JPanel();
        // TODO: fix the sizing dont use this
//        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        addButtons();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: adds necessary buttons to panel and adds panel to this, resetting panel before doing so
    private void addButtons() {
        panel.removeAll();
        panel.add(new JButton(new AddMatchAction(mh, panel)));
        panel.add(new JButton(new ViewMatchHistoryAction(mh, panel, this)));
        panel.add(new JButton(new SaveMatchHistoryAction(mh, panel)));
        panel.add(new JButton(new LoadMatchHistoryAction(mh, panel, this)));
        add(panel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: sets match history to given match history and recreates buttons with updated match history
    public void setMatchHistory(MatchHistory mh) {
        this.mh = mh;
        remove(panel);
        addButtons();
        revalidate();
        repaint();
    }
}
