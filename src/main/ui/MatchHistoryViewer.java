package ui;

import model.Match;
import model.MatchHistory;
import model.TeamSelector;
import model.Pokemon;

import java.util.List;
import java.util.Scanner;

public class MatchHistoryViewer {
    private static final int MAX_POKEMON_PER_TEAM = 4;
    private MatchHistory mh;
    private Scanner input;

    public MatchHistoryViewer() {
        runMatchHistory();
    }

    public void runMatchHistory() {
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

    public void start() {
        mh = new MatchHistory();
        input = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("\nSelect an option:");
        System.out.println("\na -> add a match");
        System.out.println("\nv -> view match history");
        System.out.println("\np -> view Pokemon statistics");
        System.out.println("\ns -> search Pokemon");
        System.out.println("\nq -> quit");
    }

    public void processCommand(String command) {
        if (command.equals("a")) {
            addMatch();
        } else if (command.equals("v")) {
            displayMatches();
        } else if (command.equals("p")) {
            pokemonStatsOptions();
        } else if (command.equals("s")) {
            searchPokemon();
        } else {
            System.out.println("Invalid input! Try again:");
        }
    }

    public void addMatch() {
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

    public void processWinStatus(Match match) {
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

    public void addTeam(Match match, TeamSelector team) {
        String prompt;
        if (team == TeamSelector.USER) {
            prompt = "Add a Pokemon on your team (case sensitive):\n";
        } else {
            prompt = "Add a Pokemon on the enemy's team (case sensitive):\n";
        }
        for (int i = 0; i < MAX_POKEMON_PER_TEAM; i++) {
            System.out.println(prompt);
            String command = input.nextLine();
            match.addPokemonByName(command, team, mh);
        }
    }

    public void displayMatches() {
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

    public void pokemonStatsOptions() {
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

    private void showNumberOptions(TeamSelector team) {
        System.out.println("\nHow many Pokemon would you like to be displayed?");
        System.out.println("\na -> all");
        System.out.println("\nc -> custom amount");
        chooseNumberPokemon(team);
    }

    public void chooseNumberPokemon(TeamSelector team) {
        String command;
        boolean choosing = true;
        int num = 0;
        while (choosing) {
            command = input.next();
            command = command.toLowerCase();
            if (command.equals("a")) {
                num = mh.getPokemonList().size();
                choosing = false;
            } else if (command.equals("c")) {
                System.out.println("Enter custom amount:");
                int picked = input.nextInt();
                if (picked <= mh.getPokemonList().size()) {
                    num = input.nextInt();
                    choosing = false;
                } else {
                    System.out.println("Too big of a number!");
                }
            } else {
                System.out.println("Invalid choice!");
            }
        }
        chooseHighestOrLowest(team, num);
    }

    public void chooseHighestOrLowest(TeamSelector team, int num) {
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

    public void displayPokemonList(List<Pokemon> list) {
        for (Pokemon p : list) {
            displayPokemon(p);
        }
    }

    public void displayPokemon(Pokemon p) {
        System.out.println("\n[" + p.getName() + "]");
        System.out.println("\n Win-rate with: " + p.getAlliedWinRate());
        System.out.println("\n Win-rate against: " + p.getEnemyWinRate());
        System.out.println("\n Number of wins with: " + p.getAlliedWins());
        System.out.println("\n Number of wins against: " + p.getEnemyWins());
        System.out.println("\n Games played with: " + p.getAlliedMatches().size());
        System.out.println("\n Games played against: " + p.getEnemyMatches().size());
    }

    private void searchPokemon() {
        if (mh.getPokemonList().isEmpty()) {
            System.out.println("There are no Pokemon here! Add some matches first.");
        } else {
            System.out.println("Search for Pokemon name: ");
            input.nextLine();
            String command = input.nextLine();
            if (mh.canFindName(command)) {
                displayPokemon(mh.findPokemon(command));
            } else {
                System.out.println("Can't find Pokemon named " + command + "!");
            }
        }
    }

}
