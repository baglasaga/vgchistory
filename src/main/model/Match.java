package model;

import java.util.ArrayList;
import java.util.List;

// represents a Pokemon match, with a match ID, whether the match was won or lost, the Pokemon used on the user's
// team and their opponent's team, and the elo gained or lost from the match
public class Match {

    private static int nextMatchId = 0;

    private final int id;
    private boolean win;
    private final List<Pokemon> myTeam;
    private final List<Pokemon> enemyTeam;
    private int eloChange;

    // EFFECTS: creates a Match with a new id, 0 elo change, and empty team and enemy team
    public Match() {
        this.id  = nextMatchId++;
        this.eloChange = 0;
        this.myTeam = new ArrayList<>();
        this.enemyTeam = new ArrayList<>();
    }

    // getters and setters

    public int getId() {
        return this.id;
    }

    public boolean getWinStatus() {
        return this.win;
    }

    public List<Pokemon> getMyTeam() {
        return this.myTeam;
    }

    public List<Pokemon> getEnemyTeam() {
        return this.enemyTeam;
    }

    public int getEloChange() {
        return this.eloChange;
    }

    public void setWin() {
        this.win = true;
    }

    public void setLoss() {
        this.win = false;
    }

    public void setEloChange(int elo) {
        this.eloChange = elo;
    }

    // EFFECTS: returns list of the names of every Pokemon on given team
    public List<String> getTeamNames(TeamSelector team) {
        List<Pokemon> t;
        List<String> result = new ArrayList<>();
        if (team == TeamSelector.USER) {
            t = this.getMyTeam();
        } else {
            t = this.getEnemyTeam();
        }
        for (Pokemon p : t) {
            result.add(p.getName());
        }
        return result;
    }


    // EFFECTS: searches both teams in this and returns Pokemon of given name
    private Pokemon findPokemonOnEitherTeam(String name, PokemonFinder pf) {
        List<Pokemon> bothTeams = new ArrayList<>();
        bothTeams.addAll(this.myTeam);
        bothTeams.addAll(this.enemyTeam);
        return pf.findPokemon(name, bothTeams);
    }

    // REQUIRES: this.win !== null (setWin() or setLoss() must be called first), Pokemon of given name is not already
    //           on given team
    // MODIFIES: this
    // EFFECTS: if Pokemon of given name is in mh.getPokemonList() or already in this match,
    //          adds that Pokemon to the given team in the match. otherwise, creates new Pokemon with given name
    //          and adds to the given team in the match
    public void addPokemonByName(String name, TeamSelector team, MatchHistory mh) {
        PokemonFinder pf = new PokemonFinder();
        if (pf.canFindName(name, mh.getPokemonList())) {
            addPokemon(pf.findPokemon(name, mh.getPokemonList()), team);
        } else if (findPokemonOnEitherTeam(name, pf) != null) {
            addPokemon(findPokemonOnEitherTeam(name, pf), team);
        } else {
            addPokemon(new Pokemon(name), team);
        }
    }

    // REQUIRES: this.win !== null (setWin() or setLoss() must be called first), p is not already on given team
    // MODIFIES: this, p
    // EFFECTS: adds p to given team, and adds this to p's matches on that team, adding a win for p on that team if the
    // match is won
    private void addPokemon(Pokemon p, TeamSelector team) {

        if (team == TeamSelector.USER) {
            this.myTeam.add(p);
        } else {
            this.enemyTeam.add(p);
        }

        if (this.win) {
            p.addWin(team);
        }
        p.addMatch(this, team);
    }
}
