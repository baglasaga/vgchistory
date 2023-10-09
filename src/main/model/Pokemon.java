package model;

import java.util.ArrayList;
import java.util.List;

// represents a Pokemon used in a Match, with a name, a list of Matches participated in on either the user's or
// opponent's team, and a win rate for this Pokemon's usage on the user's team, and the opponent's team
public class Pokemon {

    private final String name;
    private final List<Match> alliedMatches;
    private final List<Match> enemyMatches;
    private int alliedWins;
    private int enemyWins;
    private double alliedWinRate;
    private double enemyWinRate;

    // EFFECTS: constructs a Pokemon with given name, no allied or enemy matches, no wins on allied or enemy teams,
    // and a 0% win-rate on each team
    public Pokemon(String nm) {
        this.name = nm;

        this.alliedMatches = new ArrayList<>();
        this.enemyMatches = new ArrayList<>();

        this.alliedWins = 0;
        this.enemyWins = 0;

        this.alliedWinRate = 0;
        this.enemyWinRate = 0;
    }

    // getters and setters

    public String getName() {
        return this.name;
    }

    public List<Match> getAlliedMatches() {
        return this.alliedMatches;
    }

    public List<Match> getEnemyMatches() {
        return this.enemyMatches;
    }

    public int getAlliedWins() {
        return this.alliedWins;
    }

    public int getEnemyWins() {
        return this.enemyWins;
    }

    public double getAlliedWinRate() {
        return this.alliedWinRate;
    }

    public double getEnemyWinRate() {
        return this.enemyWinRate;
    }

    public void setAlliedWinRate(double wr) {
        this.alliedWinRate = wr;
    }

    public void setEnemyWinRate(double wr) {
        this.enemyWinRate = wr;
    }

    public void addWin(TeamSelector team) {
        if (team == TeamSelector.USER) {
            this.alliedWins++;
        } else {
            this.enemyWins++;
        }
    }

    // MODIFIES: this
    // EFFECTS: adds match to given team's matches
    public void addMatch(Match match, TeamSelector team) {
        if (team == TeamSelector.USER) {
            this.alliedMatches.add(match);
        } else {
            this.enemyMatches.add(match);
        }
    }

    // REQUIRES: list of matches for given team must not be empty
    // MODIFIES: this
    // EFFECTS: updates percentage of matches in given team's matches that are won
    public void updateWinRate(TeamSelector team) {
        int wins;
        List<Match> matches;
        if (team == TeamSelector.USER) {
            wins = this.alliedWins;
            matches = this.alliedMatches;
        } else {
            wins = this.enemyWins;
            matches = this.enemyMatches;
        }
        double rawWinRate = (double) wins / matches.size();
        if (team == TeamSelector.USER) {
            this.alliedWinRate = rawWinRate * 100;
        } else {
            this.enemyWinRate = rawWinRate * 100;
        }
    }

}
