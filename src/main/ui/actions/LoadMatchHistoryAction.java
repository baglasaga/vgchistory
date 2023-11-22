package ui.actions;

import model.MatchHistory;
import persistence.JsonReader;
import ui.MatchHistoryViewerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class LoadMatchHistoryAction extends AbstractAction {
    private static final String JSON_LOCATION = "./data/matchhistory.json";
    private JsonReader jsonReader;
    private MatchHistory mh;
    private JPanel panel;
    private MatchHistoryViewerGUI frame;

    public LoadMatchHistoryAction(MatchHistory mh, JPanel panel, MatchHistoryViewerGUI frame) {
        super("Load match history");
        this.mh = mh;
        this.panel = panel;
        this.jsonReader = new JsonReader(JSON_LOCATION);
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // TODO: problem: this is only updating it within this class, but not within everything
            this.mh = jsonReader.read();
            frame.setMatchHistory(this.mh);
            JOptionPane.showMessageDialog(panel, "Successfully loaded " + JSON_LOCATION);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(panel, "Unable to load file " + JSON_LOCATION);
        }
    }
}
