package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// represents a list of Matches, with a number of wins, a win-rate, a list of unique Pokemon used over every Match,
// and the user's overall elo (rating)
public class MatchHistory implements Writable {

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
        EventLog.getInstance().logEvent(new Event("Match History displayed"));
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
    private void addUniquePokemon(Pokemon p) {
        if (!this.pokemonList.contains(p)) {
            this.pokemonList.add(p);
            EventLog.getInstance().logEvent(new Event("Added unique Pokemon "
                                            + p.getName() + " to unique Pokemon list"));
        }
    }

    // MODIFIES: this
    // EFFECTS: updates this.pokemonList using given team in match
    private void updatePokemonList(Match match, TeamSelector team) {
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
        updateWinRate();
        EventLog.getInstance().logEvent(new Event("Added a Match with id " + match.getId()));
    }

    // REQUIRES: matches must not be empty
    // MODIFIES: this
    // EFFECTS: updates percentage of Matches in matches that are won
    private void updateWinRate() {
        double rawWinRate = (double) this.wins / this.matches.size();
        this.winRate = rawWinRate * 100;
    }

    // EFFECTS: return true if given Pokemon has at least one match played on given team
    private boolean usedOnTeam(Pokemon p, TeamSelector team) {
        List<Match> matches;
        if (team == TeamSelector.USER) {
            matches = p.getAlliedMatches();
        } else {
            matches = p.getEnemyMatches();
        }

        return matches.size() > 0;
    }

    // EFFECTS: filters out all Pokemon in given list that have 0 matches on the given team, and returns
    //          the filtered list
    private List<Pokemon> getUsedOnTeam(List<Pokemon> list, TeamSelector team) {
        List<Pokemon> result = new ArrayList<>();
        for (Pokemon p : list) {
            if (usedOnTeam(p, team)) {
                result.add(p);
            }
        }
        return result;
    }

    // REQUIRES: !pokemonList.isEmpty()
    // EFFECTS: returns Pokemon in given list with the highest win-rate on given team
    private Pokemon returnHighestWinRate(List<Pokemon> pokemonList, TeamSelector team) {
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
        return highestPokemon;
    }

    // REQUIRES: !pokemonList.isEmpty()
    // EFFECTS: returns Pokemon in given list with the lowest win-rate on given team
    private Pokemon returnLowestWinRate(List<Pokemon> pokemonList, TeamSelector team) {
        Pokemon highestPokemon = pokemonList.get(0);
        for (Pokemon p : pokemonList) {
            double wr;
            double lowest;
            if (team == TeamSelector.USER) {
                wr = p.getAlliedWinRate();
                lowest = highestPokemon.getAlliedWinRate();
            } else {
                wr = p.getEnemyWinRate();
                lowest = highestPokemon.getEnemyWinRate();
            }
            if (lowest > wr) {
                highestPokemon = p;
            }
        }

        return highestPokemon;
    }

    // REQUIRES: n <= this.pokemonList.size(), n > 0
    // EFFECTS: returns list of the n highest win-rate Pokemon that have usage on given team,
    //          picks the first win-rate as higher in case of a tie
    public List<Pokemon> getHighestWinRates(int n, TeamSelector team) {
        List<Pokemon> pokemonList = new ArrayList<>(this.pokemonList);
        List<Pokemon> resultList = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Pokemon highestPokemon = returnHighestWinRate(pokemonList, team);
            resultList.add(highestPokemon);
            pokemonList.remove(highestPokemon);
        }
        String teamString;
        if (team == TeamSelector.USER) {
            teamString = "my team";
        } else {
            teamString = "the enemy's team";
        }
        EventLog.getInstance().logEvent(new Event("filtered " + n + " highest win-rate Pokemon found on "
                                                  + teamString));
        return getUsedOnTeam(resultList, team);
    }

    // REQUIRES: n <= this.pokemonList.size(), n > 0
    // EFFECTS: returns list of the n lowest win-rate Pokemon on given team that
    //          have usage on that team, picks the first win-rate to be the lowest in case of a tie
    public List<Pokemon> getLowestWinRates(int n, TeamSelector team) {
        List<Pokemon> pokemonList = new ArrayList<>(getUsedOnTeam(this.pokemonList, team));
        List<Pokemon> resultList = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (pokemonList.isEmpty()) {
                break;
            }
            Pokemon lowestPokemon = returnLowestWinRate(pokemonList, team);
            resultList.add(lowestPokemon);
            pokemonList.remove(lowestPokemon);
        }
        String teamString;
        if (team == TeamSelector.USER) {
            teamString = "my team";
        } else {
            teamString = "the enemy's team";
        }
        EventLog.getInstance().logEvent(new Event("filtered " + n + "lowest win-rate Pokemon found on "
                                                  + teamString));
        return resultList;
    }

    // EFFECTS: returns this as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("matches", matchesToJson());
        return json;
    }

    // EFFECTS: returns matches in this match history as a JSON array
    private JSONArray matchesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Match m : this.matches) {
            jsonArray.put(m.toJson());
        }

        return jsonArray;
    }
}
