package ui.actions;

import model.MatchHistory;
import model.PokemonFinder;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SearchPokemonAction extends AbstractAction {
    private MatchHistory mh;
    private JPanel parentContainerPanel;
    private JPanel pokemonPanel;
    private PokemonDisplayer pd;
    private PokemonFinder pf;

    public SearchPokemonAction(MatchHistory mh, JPanel panel) {
        super("Search Pokemon");
        this.mh = mh;
        this.parentContainerPanel = panel;
        this.pokemonPanel = new JPanel();
        this.pd = new PokemonDisplayer();
        this.pf = new PokemonFinder();
    }

    // MODIFIES: this
    // EFFECTS: displays Pokemon of user-inputted name, gives message if couldn't
    //          find the name
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = (String) JOptionPane.showInputDialog(parentContainerPanel,
                    "Search Pokemon:",
                    "Search",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "");
        if (pf.canFindName(name, mh.getPokemonList())) {
            pd.displayPokemon(pf.findPokemon(name, mh.getPokemonList()), pokemonPanel);
        } else {
            JOptionPane.showMessageDialog(parentContainerPanel, "Couldn't find " + name);
        }
        parentContainerPanel.add(pokemonPanel);
        parentContainerPanel.revalidate();
        parentContainerPanel.repaint();
    }
}