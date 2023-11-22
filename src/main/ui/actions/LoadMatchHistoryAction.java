package ui.actions;

import model.MatchHistory;
import persistence.JsonReader;
import ui.MatchHistoryViewerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

// represents an action to load a match history from file in GUI
public class LoadMatchHistoryAction extends AbstractAction {
    private static final String JSON_LOCATION = "./data/matchhistory.json";
    private JsonReader jsonReader;
    private MatchHistory mh;
    private JPanel parentContainerPanel;
    private MatchHistoryViewerGUI mainGUI;

    // EFFECTS: constructs load match history action with given match history, parent container panel,
    //          and main GUI, and initializes jsonReader
    public LoadMatchHistoryAction(MatchHistory mh, JPanel panel, MatchHistoryViewerGUI mainGUI) {
        super("Load match history");
        this.mh = mh;
        this.parentContainerPanel = panel;
        this.jsonReader = new JsonReader(JSON_LOCATION);
        this.mainGUI = mainGUI;
    }

    // MODIFIES: this, mainGUI
    // EFFECTS: loads match history from file and updates it in main GUI
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            this.mh = jsonReader.read();
            mainGUI.setMatchHistory(this.mh);
            JOptionPane.showMessageDialog(parentContainerPanel, "Successfully loaded " + JSON_LOCATION);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parentContainerPanel, "Unable to load file " + JSON_LOCATION);
        }
    }
}
