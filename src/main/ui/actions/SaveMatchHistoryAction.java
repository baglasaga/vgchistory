package ui.actions;

import model.MatchHistory;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

public class SaveMatchHistoryAction extends AbstractAction {
    private static final String JSON_LOCATION = "./data/matchhistory.json";
    private JsonWriter jsonWriter;
    private MatchHistory mh;
    private JPanel panel;

    public SaveMatchHistoryAction(MatchHistory mh, JPanel panel) {
        super("Save match history");
        this.mh = mh;
        this.jsonWriter = new JsonWriter(JSON_LOCATION);
        this.panel = panel;
    }

    // EFFECTS: saves match history to file
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            jsonWriter.open();
            jsonWriter.write(mh);
            jsonWriter.close();
            System.out.println("Saved to " + JSON_LOCATION);
            JOptionPane.showMessageDialog(panel, "Saved to " + JSON_LOCATION);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(panel, "Unable to write to file " + JSON_LOCATION);
        }
    }
}
