package ui;

import model.MatchHistory;
import persistence.JsonReader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class LoadMatchHistoryAction extends AbstractAction {
    private static final String JSON_LOCATION = "./data/matchhistory.json";
    private JsonReader jsonReader;
    private MatchHistory mh;
    private JPanel panel;

    public LoadMatchHistoryAction(MatchHistory mh, JPanel panel) {
        super("Load match history");
        this.mh = mh;
        this.panel = panel;
        this.jsonReader = new JsonReader(JSON_LOCATION);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // TODO: problem: this is only updating it within this class, but not within everything
            this.mh = jsonReader.read();
            JOptionPane.showMessageDialog(panel, "Successfully loaded " + JSON_LOCATION);
            // TODO: return this value, then add a call to the main gui class that sets the new match history
            // EZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(panel, "Unable to load file " + JSON_LOCATION);
        }
    }
}
