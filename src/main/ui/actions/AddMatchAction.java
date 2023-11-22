package ui.actions;

import model.Match;
import model.MatchHistory;
import model.TeamSelector;

import javax.swing.*;
import java.awt.event.ActionEvent;

// represents an action to add matches in the GUI
public class AddMatchAction extends AbstractAction {
    private static final int MAX_POKEMON_PER_TEAM = 4;
    private MatchHistory mh;
    private JComponent panel;

    // constructs an add match action with given match history and given parent
    public AddMatchAction(MatchHistory mh, JComponent panel) {
        super("Add Match");
        this.mh = mh;
        this.panel = panel;
    }

    // MODIFIES: this, match
    // EFFECTS: creates new match and modifies it based on user inputs to given prompts
    @Override
    public void actionPerformed(ActionEvent evt) {
        Match match = new Match();
        processEloChange(match);
        processWinStatus(match);
        addTeam(match, TeamSelector.USER);
        addTeam(match, TeamSelector.OPPONENT);
        mh.addMatch(match);
        JOptionPane.showMessageDialog(panel, "Success! New match created with id " + match.getId());
    }

    // MODIFIES: match
    // EFFECTS: prompts user to input integer representing elo gain/loss, if non-integer or nothing is inputted,
    //          prompts user again
    private void processEloChange(Match match) {
        int eloChange = 0;
        boolean choosing = true;
        while (choosing) {
            try {
                String eloChangeString = (String) JOptionPane.showInputDialog(panel,
                        "Enter elo gained/lost:",
                        "Elo Change",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "");
                eloChange = Integer.parseInt(eloChangeString);
                choosing = false;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(panel, "Please input a number!");
            }
        }
        match.setEloChange(eloChange);
    }

    // MODIFIES: match
    // EFFECTS: prompts user to select if they won or lost, if the prompt window is closed or cancel is clicked,
    //          prompts user again
    private void processWinStatus(Match match) {
        Object[] winOptions = {"Win", "Lose"};
        boolean choosing = true;
        while (choosing) {
            try {
                String winStatus = (String) JOptionPane.showInputDialog(panel,
                        "Select match outcome:",
                        "Match outcome",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        winOptions,
                        "Win");
                if (winStatus.equals("Win")) {
                    match.setWin();
                } else {
                    match.setLoss();
                }
                choosing = false;
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(panel, "Please pick a choice.");
            }
        }

    }

    // MODIFIES: match
    // EFFECTS: adds a team to processed match based on user input, if window is closed or 'cancel' is clicked
    //          then prompts user again
    private void addTeam(Match match, TeamSelector team) {
        String teamString;
        if (team == TeamSelector.USER) {
            teamString = "your";
        } else {
            teamString = "the enemy's";
        }
        String prompt = "Add a Pokemon on " + teamString + " team (case sensitive):";
        for (int i = 0; i < MAX_POKEMON_PER_TEAM; i++) {
            boolean choosing = true;
            while (choosing) {

                String name = JOptionPane.showInputDialog(panel, prompt, null);
                if (name != null) {
                    match.addPokemonByName(name.trim(), team, mh);
                    choosing = false;
                } else {
                    JOptionPane.showMessageDialog(panel, "Please enter a name.");
                }


            }
        }
    }
}
