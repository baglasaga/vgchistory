package ui.actions;

import model.MatchHistory;
import model.Pokemon;
import model.TeamSelector;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

// represents an action to filter and display Pokemon depending on user input in GUI
public class FilterPokemonAction extends AbstractAction {
    private MatchHistory mh;
    private JPanel parentContainerPanel;
    private JPanel scrollPanel;
    private JScrollPane scrollPane;
    private PokemonDisplayer pd;

    // EFFECTS: constructs filter pokemon action with given match history and parent container panel
    //          with necessary display panels and scroll pane
    public FilterPokemonAction(MatchHistory mh, JPanel panel) {
        super("Filter Pokemon");
        this.mh = mh;
        this.parentContainerPanel = panel;
        this.scrollPanel = new JPanel();
        this.scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.PAGE_AXIS));
        this.scrollPane = new JScrollPane(scrollPanel);
        this.scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        this.pd = new PokemonDisplayer();
    }

    // MODIFIES: this
    // EFFECTS: clears scrollPanel, prompts user on how they would like to filter Pokemon
    @Override
    public void actionPerformed(ActionEvent e) {
        scrollPanel.removeAll();
        Object[] sortOptions = {"Sort by win-rates of Pokemon used on my team",
                                "Sort by win-rates of Pokemon used on the enemy team"};
        String sortChoice = (String)JOptionPane.showInputDialog(parentContainerPanel,
                                                                "What would you like to filter by?",
                                                                "Filter Options",
                                                                JOptionPane.PLAIN_MESSAGE,
                                                                null,
                                                                sortOptions,
                                                                "");
        if (sortChoice.equals(sortOptions[0])) {
            numSelector(TeamSelector.USER);
        } else {
            numSelector(TeamSelector.OPPONENT);
        }
    }

    // EFFECTS: prompts user on how many Pokemon they would like to display, being prompted additionally
    //          to enter a custom number amount
    private void numSelector(TeamSelector team) {
        Object[] numberOptions = {"All", "Custom amount"};
        String numberChoice = (String)JOptionPane.showInputDialog(parentContainerPanel,
                "What would you like to filter by?",
                "Filter Options",
                JOptionPane.PLAIN_MESSAGE,
                null,
                numberOptions, "");
        int num;
        if (numberChoice.equals(numberOptions[0])) {
            num = mh.getPokemonList().size();
        } else {
            num = Integer.parseInt((String) JOptionPane.showInputDialog(parentContainerPanel,
                    "Enter number of Pokemon to filter",
                    "Number of Pokemon to Filter",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    ""));
            if (num > mh.getPokemonList().size()) {
                num = mh.getPokemonList().size();
            }
        }
        filterHighestOrLowest(team, num);
    }

    // MODIFIES: this
    // EFFECTS: displays the filtered list of Pokemon based on the user's inputs
    private void filterHighestOrLowest(TeamSelector team, int num) {
        Object[] filterOptions = {"Highest", "Lowest"};
        String filterChoice = (String)JOptionPane.showInputDialog(parentContainerPanel,
                "Sort by highest or lowest win-rates?",
                "Filter Options",
                JOptionPane.PLAIN_MESSAGE,
                null,
                filterOptions,
                "");
        List<Pokemon> pokemonList;
        if (filterChoice.equals(filterOptions[0])) {
            pokemonList = mh.getHighestWinRates(num, team);
        } else {
            pokemonList = mh.getLowestWinRates(num, team);
        }
        pd.displayPokemonList(pokemonList, scrollPanel);
        parentContainerPanel.add(scrollPane);
        parentContainerPanel.repaint();
        parentContainerPanel.revalidate();
    }


}
