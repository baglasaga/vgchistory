package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// Match History Viewer app
public class MatchHistoryViewer {
    private static final String JSON_LOCATION = "./data/matchhistory.json";
    private static final int MAX_POKEMON_PER_TEAM = 4;
    private MatchHistory mh;
    private PokemonFinder pf;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the app
    public MatchHistoryViewer() {
        runMatchHistory();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runMatchHistory() {
        boolean running = true;
        String command = null;

        start();

        while (running) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();
            if (command.equals("q")) {
                running = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("\nClosing...");
    }

    // EFFECTS: initializes Match History
    private void start() {
        mh = new MatchHistory();
        pf = new PokemonFinder();
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_LOCATION);
        jsonReader = new JsonReader(JSON_LOCATION);
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect an option:");
        System.out.println("\na -> add a match");
        System.out.println("\nv -> view match history");
        System.out.println("\np -> view Pokemon statistics");
        System.out.println("\nf -> find Pokemon");
        System.out.println("\ns -> save match history to file");
        System.out.println("\nl -> load match history from file");
        System.out.println("\nq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void processCommand(String command) {
        if (command.equals("a")) {
            addMatch();
        } else if (command.equals("v")) {
            displayMatches();
        } else if (command.equals("p")) {
            pokemonStatsOptions();
        } else if (command.equals("f")) {
            searchPokemon();
        } else if (command.equals("s")) {
            saveMatchHistory();
        } else if (command.equals("l")) {
            loadMatchHistory();
        } else {
            System.out.println("Invalid input! Try again:");
        }
    }



    // EFFECTS: saves match history to file
    private void saveMatchHistory() {
        try {
            jsonWriter.open();
            jsonWriter.write(mh);
            jsonWriter.close();
            System.out.println("Saved to " + JSON_LOCATION);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file " + JSON_LOCATION);
        }
    }

    // EFFECTS: loads match history from file
    private void loadMatchHistory() {
        try {
            mh = jsonReader.read();
            System.out.println("Successfully loaded " + JSON_LOCATION);
        } catch (IOException e) {
            System.out.println("Unable to load file " + JSON_LOCATION);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a match to the match history
    private void addMatch() {
        Match match = new Match();

        processWinStatus(match);

        System.out.println("Enter elo gained/lost:");
        int nextCommand = input.nextInt();
        input.nextLine();
        match.setEloChange(nextCommand);

        addTeam(match, TeamSelector.USER);
        addTeam(match, TeamSelector.OPPONENT);

        mh.addMatch(match);
        System.out.println("Success! New match added with id " + match.getId());

    }

    // MODIFIES: this
    // EFFECTS: processes if the processed match was won or lost
    private void processWinStatus(Match match) {
        System.out.println("Did you win (enter w) or lose (enter l)?\n");
        boolean choosing = true;
        while (choosing) {
            String command = input.next();
            command = command.toLowerCase();
            if (command.equals("w")) {
                match.setWin();
                choosing = false;
            } else if (command.equals("l")) {
                match.setLoss();
                choosing = false;
            } else {
                System.out.println("\nInvalid input!");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a team to the processed match
    private void addTeam(Match match, TeamSelector team) {
        String prompt;
        if (team == TeamSelector.USER) {
            prompt = "Add a Pokemon on your team:\n";
        } else {
            prompt = "Add a Pokemon on the enemy's team:\n";
        }
        for (int i = 0; i < MAX_POKEMON_PER_TEAM; i++) {
            System.out.println(prompt);
            String command = input.nextLine();
            match.addPokemonByName(command, team, mh);
        }
    }

    // EFFECTS: displays information about every match added to the match history
    private void displayMatches() {
        String winLoss;
        String eloPrefix;
        System.out.println("Elo: " + mh.getElo());
        System.out.println("Win-rate: " + mh.getWinRate());
        System.out.println("Games played: " + mh.getMatches().size());
        for (Match m : mh.getMatches()) {
            if (m.getWinStatus()) {
                winLoss = "W";
            } else {
                winLoss = "L";
            }
            if (m.getEloChange() > 0) {
                eloPrefix = "+";
            } else {
                eloPrefix = "";
            }
            System.out.println("\n\n[ID: " + m.getId() + "]   " + winLoss + " (" + eloPrefix + m.getEloChange() + ")");
            System.out.println("My team: ");
            System.out.print(m.getTeamNames(TeamSelector.USER));
            System.out.println("\nEnemy team: ");
            System.out.print(m.getTeamNames(TeamSelector.OPPONENT));
            System.out.println("\n");
        }
    }

    // EFFECTS: displays options to the user about how to sort the Pokemon statistics, and processes
    //          their input
    private void pokemonStatsOptions() {
        String command;
        boolean choosing = true;
        System.out.println("\nSelect an option:");
        System.out.println("\nm -> sort by win-rates of Pokemon used on my teams");
        System.out.println("\no -> sort by win-rates of Pokemon used on opponents' teams");
        while (choosing) {
            command = input.next();
            command = command.toLowerCase();
            if (command.equals("m")) {
                showNumberOptions(TeamSelector.USER);
                choosing = false;
            } else if (command.equals("o")) {
                showNumberOptions(TeamSelector.OPPONENT);
                choosing = false;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    // EFFECTS: displays choice on how many Pokemon to show
    private void showNumberOptions(TeamSelector team) {
        System.out.println("\nHow many Pokemon would you like to be displayed?");
        System.out.println("\na -> all");
        System.out.println("\nc -> custom amount");
        chooseNumberPokemon(team);
    }

    // EFFECTS: processes user input on how many Pokemon to show
    private void chooseNumberPokemon(TeamSelector team) {
        boolean choosing = true;
        String command;
        int num = 0;
        while (choosing) {
            command = input.next().toLowerCase();
            if (command.equals("a")) {
                num = mh.getPokemonList().size();
                choosing = false;
            } else if (command.equals("c")) {
                System.out.println("Enter custom amount:");
                int picked = input.nextInt();
                if (picked <= mh.getPokemonList().size()) {
                    num = picked;
                    choosing = false;
                } else {
                    System.out.println("Too big of a number! Choose an option again.");
                }
            } else {
                System.out.println("Invalid choice! Choose an option again.");
            }
        }
        chooseHighestOrLowest(team, num);
    }

    // EFFECTS: displays options of how to sort the displayed Pokemon, and processes their input
    private void chooseHighestOrLowest(TeamSelector team, int num) {
        String command;
        boolean choosing = true;
        System.out.println("\nWould you like to sort by the highest or lowest win-rates?");
        System.out.println("\nh -> highest");
        System.out.println("\nl -> lowest");
        while (choosing) {
            command = input.next();
            command = command.toLowerCase();
            if (command.equals("h")) {
                displayPokemonList(mh.getHighestWinRates(num, team));
                choosing = false;
            } else if (command.equals("l")) {
                displayPokemonList(mh.getLowestWinRates(num, team));
                choosing = false;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    // EFFECTS: displays the given list of Pokemon
    private void displayPokemonList(List<Pokemon> list) {
        for (Pokemon p : list) {
            displayPokemon(p);
        }
    }

    // EFFECTS: displays information about given Pokemon
    private void displayPokemon(Pokemon p) {
        System.out.println("\n[" + p.getName() + "]");
        System.out.println("\n Win-rate with: " + p.getAlliedWinRate());
        System.out.println("\n Win-rate against: " + p.getEnemyWinRate());
        System.out.println("\n Number of wins with: " + p.getAlliedWins());
        System.out.println("\n Number of wins against: " + p.getEnemyWins());
        System.out.println("\n Games played with: " + p.getAlliedMatches().size());
        System.out.println("\n Games played against: " + p.getEnemyMatches().size());
    }

    // EFFECTS: looks for and displays Pokemon of name inputted by user, if it exists
    private void searchPokemon() {
        if (mh.getPokemonList().isEmpty()) {
            System.out.println("There are no Pokemon here! Add some matches first.");
        } else {
            System.out.println("Search for Pokemon name: ");
            input.nextLine();
            String command = input.nextLine();
            if (pf.canFindName(command, mh.getPokemonList())) {
                displayPokemon(pf.findPokemon(command, mh.getPokemonList()));
            } else {
                System.out.println("Can't find Pokemon named " + command + "!");
            }
        }
    }

}
