package model;

import java.util.List;

// handles methods involving searching for and returning Pokemon from a list
public class PokemonFinder {

    // EFFECTS: constructs a PokemonFinder
    public PokemonFinder() {

    }

    // EFFECTS: returns true if given name can be found as a Pokemon's name in
    //          the given list; false otherwise; ignores case of either name and trims spaces before comparing
    public boolean canFindName(String name, List<Pokemon> list) {
        return findPokemon(name, list) != null;
    }

    // EFFECTS: returns Pokemon of given name from this.pokemonList; null if Pokemon can't be found;
    //          ignores case of either name and trims spaces before comparing
    public Pokemon findPokemon(String name, List<Pokemon> list) {
        String nameIgnoreCase = name.toLowerCase().trim();
        for (Pokemon p : list) {
            if (p.getName().toLowerCase().trim().equals(nameIgnoreCase)) {
                EventLog.getInstance().logEvent(new Event("Found Pokemon named " + p.getName()));
                return p;
            }
        }
        return null;
    }

}
