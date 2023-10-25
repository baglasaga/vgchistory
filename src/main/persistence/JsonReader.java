package persistence;

import model.Match;
import model.MatchHistory;
import model.TeamSelector;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// NOTE: code influenced by the JsonSerializationDemo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

// represents a reader that reads match history from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads match history from file and returns it;
    // throws IOException if an error occurs reading data from file
    public MatchHistory read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMatchHistory(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // parses match history from JSON object and returns it
    private MatchHistory parseMatchHistory(JSONObject jsonObject) {
        MatchHistory mh = new MatchHistory();
        addMatches(mh, jsonObject);
        return mh;
    }

    // MODIFIES: mh
    // EFFECTS: parses matches from JSON object and adds them to workroom
    private void addMatches(MatchHistory mh, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("matches");
        for (Object json : jsonArray) {
            JSONObject nextMatch = (JSONObject) json;
            addMatch(mh, nextMatch);
        }
    }

    // MODIFIES: mh
    // EFFECTS: parses match from JSON object and adds it to match history
    private void addMatch(MatchHistory mh, JSONObject jsonObject) {
        boolean win = jsonObject.getBoolean("win");
        int eloChange = jsonObject.getInt("eloChange");
        JSONArray allies = jsonObject.getJSONArray("myTeam");
        JSONArray enemies = jsonObject.getJSONArray("enemyTeam");
        Match m = new Match();
        if (win) {
            m.setWin();
        } else {
            m.setLoss();
        }
        m.setEloChange(eloChange);
        addAllPokemon(m, allies, TeamSelector.USER, mh);
        addAllPokemon(m, enemies, TeamSelector.OPPONENT, mh);
        mh.addMatch(m);
    }

    // MODIFIES: m
    // EFFECTS: parses list of pokemon from JSON array and adds them to match on given team
    private void addAllPokemon(Match m, JSONArray jsonArray, TeamSelector team, MatchHistory mh) {
        for (Object json : jsonArray) {
            JSONObject nextPokemon = (JSONObject) json;
            addPokemon(m, nextPokemon, team, mh);
        }
    }

    // MODIFIES: m
    // EFFECTS: parses pokemon from JSON object and adds it to match on given team
    private void addPokemon(Match m, JSONObject jsonObject, TeamSelector team, MatchHistory mh) {
        String name = jsonObject.getString("name");
        m.addPokemonByName(name, team, mh);
    }

}
