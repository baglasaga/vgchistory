package ui;

import model.Match;
import model.MatchHistory;
import model.TeamSelector;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AddMatchAction extends AbstractAction {
    private JTextField textField;
    private final String enterOption = "Enter";
    private final String cancelOption = "Cancel";
    private static final int MAX_POKEMON_PER_TEAM = 4;
    private MatchHistory mh;
    private JComponent panel;

    AddMatchAction(MatchHistory mh, JComponent panel) {
        super("Add Match");
        this.textField = new JTextField(0);
        this.mh = mh;
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Match match = new Match();
//        Object[] eloDialogue = {"Enter elo gained/lost:", textField};
//        Object[] options = {enterOption, cancelOption};
        int eloChange = Integer.parseInt((String) JOptionPane.showInputDialog(panel,
                "Enter elo gained/lost:",
                "Elo Change",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                ""));
        match.setEloChange(eloChange);
        processWinStatus(match);
        addTeam(match, TeamSelector.USER);
        addTeam(match, TeamSelector.OPPONENT);
        mh.addMatch(match);
        JOptionPane.showMessageDialog(panel, "Success! New match created with id " + match.getId());
    }

    private void processWinStatus(Match match) {
        Object[] winOptions = {"Win", "Lose"};
        String winStatus = (String)JOptionPane.showInputDialog(panel,
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
    }

    private void addTeam(Match match, TeamSelector team) {
        String teamString;
        if (team == TeamSelector.USER) {
            teamString = "your";
        } else {
            teamString = "the enemy's";
        }
        String prompt = "Add a Pokemon on " + teamString + " team (case sensitive):";
        for (int i = 0; i < MAX_POKEMON_PER_TEAM; i++) {
            String name = JOptionPane.showInputDialog(panel, prompt, "");
            match.addPokemonByName(name, team, mh);
        }
    }

}
