package ui;

import model.Event;
import model.EventLog;
import model.MatchHistory;
import ui.actions.AddMatchAction;
import ui.actions.LoadMatchHistoryAction;
import ui.actions.SaveMatchHistoryAction;
import ui.actions.ViewMatchHistoryAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;

// Match History Viewer app GUI
public class MatchHistoryViewerGUI extends JFrame {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;
    private MatchHistory mh;
    private JPanel buttonPanel;
    private JPanel buttonContainer;

    // EFFECTS: constructs match history viewer GUI and initializes match history, buttons, and necessary panels
    //          and frame with correct layout
    public MatchHistoryViewerGUI() {
        super("VGC Match History Viewer");
        mh = new MatchHistory();
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1));
        buttonContainer = new JPanel();
        buttonContainer.setLayout(new GridLayout(0, 1));
        add(buttonContainer);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        addButtons();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
//                super.windowClosing(e);
                Iterator<model.Event> iterator = EventLog.getInstance().iterator();
                while (iterator.hasNext()) {
                    Event event = iterator.next();
                    System.out.println(event.toString());
                }
            }
        });
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: adds necessary buttons to panel and adds panel to this, resetting panel before doing so
    private void addButtons() {
        buttonPanel.removeAll();
        buttonPanel.add(new JButton(new AddMatchAction(mh, buttonContainer)));
        buttonPanel.add(new JButton(new ViewMatchHistoryAction(mh, buttonContainer, this)));
        buttonPanel.add(new JButton(new SaveMatchHistoryAction(mh, buttonContainer)));
        buttonPanel.add(new JButton(new LoadMatchHistoryAction(mh, buttonContainer, this)));
        buttonContainer.add(buttonPanel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: sets match history to given match history and recreates buttons with updated match history
    public void setMatchHistory(MatchHistory mh) {
        this.mh = mh;
        buttonContainer.remove(buttonPanel);
        addButtons();
        revalidate();
        repaint();
    }
}
