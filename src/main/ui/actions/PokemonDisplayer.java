package ui.actions;

import model.Pokemon;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PokemonDisplayer {

    public PokemonDisplayer() {

    }

    // MODIFIES: this
    // EFFECTS: displays list of Pokemon onto panel
    public void displayPokemonList(List<Pokemon> pokemonList, JPanel panel) {

        for (Pokemon p : pokemonList) {
            displayPokemon(p, panel);
        }
    }

    // MODIFIES: this
    // EFFECTS: displays given Pokemon onto panel
    public void displayPokemon(Pokemon p, JPanel panel) {
        String pokemonInfo = "<html>[" + p.getName() + "]<br>"
                           + "<br> Win-rate with: " + p.getAlliedWinRate()
                           + "<br> Win-rate against: " + p.getEnemyWinRate()
                           + "<br> Number of wins with: " + p.getAlliedWins()
                           + "<br> Number of wins against: " + p.getEnemyWins()
                           + "<br> Games played with: " + p.getAlliedMatches().size()
                           + "<br> Games played against: " + p.getEnemyMatches().size();
        JLabel pokemonInfoLabel = new JLabel(pokemonInfo);
        pokemonInfoLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(pokemonInfoLabel);
    }
}
