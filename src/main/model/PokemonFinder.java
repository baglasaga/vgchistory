package model;

import java.util.List;

// handles methods involving searching for and returning Pokemon from a list
public class PokemonFinder {

    // EFFECTS: constructs a PokemonFinder
    public PokemonFinder() {

    }

    // EFFECTS: returns true if given name can be found as a Pokemon's name in
    //          the given list
    public boolean canFindName(String name, List<Pokemon> list) {
        for (Pokemon p : list) {
            if (p.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns Pokemon of given name from this.pokemonList; null if Pokemon can't be found
    public Pokemon findPokemon(String name, List<Pokemon> list) {
        for (Pokemon p : list) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

}
