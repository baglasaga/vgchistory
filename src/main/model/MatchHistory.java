package model;

import java.util.ArrayList;
import java.util.List;

// represents a list of Matches, with a number of wins, a win-rate, a list of unique Pokemon used over every Match,
// and the user's overall elo (rating)
public class MatchHistory {

    private final List<Match> matches;
    private final List<Pokemon> pokemonList;
    private int wins;
    private double winRate;
    private int elo;

    // EFFECTS: constructs a MatchHistory with an empty list of matches, an empty list of Pokemon,
    //          no wins, a 0% win rate, and an elo of 1000
    public MatchHistory() {
        this.matches = new ArrayList<>();
        this.pokemonList = new ArrayList<>();
        this.wins = 0;
        this.winRate = 0;
        this.elo = 1000;
    }

    // getters
    public List<Match> getMatches() {
        return this.matches;
    }

    public List<Pokemon> getPokemonList() {
        return this.pokemonList;
    }

    public int getWins() {
        return this.wins;
    }

    public double getWinRate() {
        return this.winRate;
    }

    public int getElo() {
        return this.elo;
    }

    // MODIFIES: this
    // EFFECTS: if p is not already in this.pokemonList, adds it to the list
    public void addUniquePokemon(Pokemon p) {
        if (!this.pokemonList.contains(p)) {
            this.pokemonList.add(p);
        }
    }

    // MODIFIES: this
    // EFFECTS: updates this.pokemonList using given team in match
    public void updatePokemonList(Match match, TeamSelector team) {
        List<Pokemon> pokemonTeam;
        if (team == TeamSelector.USER) {
            pokemonTeam = match.getMyTeam();
        } else {
            pokemonTeam = match.getEnemyTeam();
        }

        for (Pokemon p : pokemonTeam) {
            addUniquePokemon(p);
        }
    }

    // REQUIRES: match.getWin() !== null (setWin() or setLoss() must be called first)
    // MODIFIES: this
    // EFFECTS: adds match to this.matches, adds 1 win to this if the match is won, adds the match's elo change to
    //          this.elo, and updates this.pokemonList with each team's pokemon in match
    public void addMatch(Match match) {
        this.matches.add(match);
        this.elo += match.getEloChange();
        if (match.getWinStatus()) {
            this.wins++;
        }
        updatePokemonList(match, TeamSelector.USER);
        updatePokemonList(match, TeamSelector.OPPONENT);
    }

    // REQUIRES: matches must not be empty
    // MODIFIES: this
    // EFFECTS: updates percentage of Matches in matches that are won
    public void updateWinRate() {
        double rawWinRate = (double) this.wins / this.matches.size();
        this.winRate = rawWinRate * 100;
    }

    // REQUIRES: n <= this.pokemonList.size(), n > 0
    // EFFECTS: returns list of the n highest win-rate Pokemon on given team, picks the first win-rate as higher in case
    //          of a tie
    public List<Pokemon> getHighestWinRates(int n, TeamSelector team) {
        List<Pokemon> pokemonList = new ArrayList<>(this.pokemonList);
        List<Pokemon> resultList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Pokemon highestPokemon = pokemonList.get(0);
            for (Pokemon p : pokemonList) {
                double wr;
                double highest;
                if (team == TeamSelector.USER) {
                    wr = p.getAlliedWinRate();
                    highest = highestPokemon.getAlliedWinRate();
                } else {
                    wr = p.getEnemyWinRate();
                    highest = highestPokemon.getEnemyWinRate();
                }
                if (highest < wr) {
                    highestPokemon = p;
                }
            }
            resultList.add(highestPokemon);
            pokemonList.remove(highestPokemon);
        }
        return resultList;
    }

    // REQUIRES: n <= this.pokemonList.size()
    // EFFECTS: returns list of the n lowest win-rate Pokemon on given team
    public List<Pokemon> getLowestWinRates(int n, TeamSelector team) {
        List<Pokemon> pokemonList = new ArrayList<>(this.pokemonList);
        List<Pokemon> resultList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Pokemon highestPokemon = pokemonList.get(0);
            for (Pokemon p : pokemonList) {
                double wr;
                double highest;
                if (team == TeamSelector.USER) {
                    wr = p.getAlliedWinRate();
                    highest = highestPokemon.getAlliedWinRate();
                } else {
                    wr = p.getEnemyWinRate();
                    highest = highestPokemon.getEnemyWinRate();
                }
                if (highest > wr) {
                    highestPokemon = p;
                }
            }
            resultList.add(highestPokemon);
            pokemonList.remove(highestPokemon);
        }
        return resultList;
    }

}
