package ui.actions;

import model.Match;
import model.MatchHistory;
import model.TeamSelector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// represents an action to display the match history in the GUI
public class ViewMatchHistoryAction extends AbstractAction {
    private MatchHistory mh;
    private JPanel mainPanel;
    private JPanel matchHistoryPanel;
    private JPanel scrollContainerPanel;
    private JFrame frame;
    private JScrollPane scrollPane;
    private JPanel scrollPanel;

    public ViewMatchHistoryAction(MatchHistory mh, JPanel panel, JFrame frame) {
        super("View Match History");
        this.mh = mh;
        this.mainPanel = panel;
        this.matchHistoryPanel = new JPanel();
        this.matchHistoryPanel.setLayout(new BoxLayout(matchHistoryPanel, BoxLayout.PAGE_AXIS));
        this.scrollContainerPanel = new JPanel();
        this.scrollContainerPanel.setLayout(new GridLayout(1, 0));
        this.frame = frame;
        this.scrollPanel = new JPanel();
        this.scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.PAGE_AXIS));
        this.scrollPane = new JScrollPane(scrollPanel);
        this.scrollPane.getVerticalScrollBar().setUnitIncrement(15);
    }

    // TODO: make it so that it can store the match history that was previously called and then just build off of the
    //       match history instead of having to make a new one every time
    // MODIFIES: this
    // EFFECTS: empties out match history panel, and adds all match history information to match history panel
    @Override
    public void actionPerformed(ActionEvent e) {
        ImageIcon img = new ImageIcon("data/incineroar.png", "Icon of Incineroar");
        matchHistoryPanel.removeAll();
        scrollPanel.removeAll();
        JLabel overallStats = new JLabel("<html>Elo: " + mh.getElo()
                                               + "<br> Win-rate: " + mh.getWinRate()
                                               + "<br> Games played: " + mh.getMatches().size(), img,
                                               JLabel.LEFT);
        overallStats.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        matchHistoryPanel.add(overallStats);
        addButtons();
        for (Match m : mh.getMatches()) {
            displayMatch(m);
        }
        scrollContainerPanel.add(scrollPane);
        matchHistoryPanel.add(scrollContainerPanel);
        frame.setContentPane(matchHistoryPanel);
        frame.repaint();
        frame.revalidate();
    }

    // MODIFIES: this
    // EFFECTS: adds necessary buttons to match history panel
    private void addButtons() {
        matchHistoryPanel.add(new JButton(new ReturnToMenuAction(mainPanel, frame)));
        matchHistoryPanel.add(new JButton(new FilterPokemonAction(mh, scrollContainerPanel)));
        matchHistoryPanel.add(new JButton(new SearchPokemonAction(mh, scrollContainerPanel)));
    }

    // MODIFIES: this
    // EFFECTS: displays given match on scrollPanel as a panel
    private void displayMatch(Match m) {
        JPanel matchPanel = new JPanel(new BorderLayout());
        String winLoss;
        String eloPrefix;
        if (m.getWinStatus()) {
            matchPanel.setBackground(new Color(83, 131, 232));
            winLoss = "W";
            // TODO: make it so that there is an image based on whether u win or lose (probably next to the match info)
        } else {
            matchPanel.setBackground(new Color(232, 64, 87));
            winLoss = "L";
        }
        if (m.getEloChange() > 0) {
            eloPrefix = "+";
        } else {
            eloPrefix = "";
        }
        String matchStats = "[ID: " + m.getId() + "]   "
                                           + winLoss + " (" + eloPrefix + m.getEloChange() + ")";
        String myTeam = "<html>My team: <br>" + m.getTeamNames(TeamSelector.USER);
        String enemyTeam = "<html>My team: <br>" + m.getTeamNames(TeamSelector.OPPONENT);
        matchPanel.add(new JLabel("<html>" + matchStats + "<br>" + myTeam + "<br>" + enemyTeam));
        matchPanel.setPreferredSize(new Dimension(200, 250));
        scrollPanel.add(matchPanel, 0);
    }



}
