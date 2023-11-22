package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PokemonFinderTest {

    PokemonFinder pf;

    Pokemon p1;
    Pokemon p2;
    Pokemon p3;
    List<Pokemon> pokemonList;

    @BeforeEach
    public void setup() {
        pf = new PokemonFinder();
        p1 = new Pokemon("Snorlax");
        p2 = new Pokemon("Sinistcha");
        p3 = new Pokemon("Clefairy");
        pokemonList = new ArrayList<>();
    }

    @Test
    public void testCanFindNameOneNameInList() {
        pokemonList.add(p1);
        assertTrue(pf.canFindName("Snorlax", pokemonList));
    }

    @Test
    public void testCanFindNameMultipleInList() {
        pokemonList.add(p1);
        pokemonList.add(p2);
        pokemonList.add(p3);
        assertTrue(pf.canFindName("Clefairy", pokemonList));
    }

    @Test
    public void testCantFindName() {
        assertFalse(pf.canFindName("Snorlax", pokemonList));
    }

    @Test
    public void testCanFindNameIgnoringCase() {
        pokemonList.add(p1);
        assertTrue(pf.canFindName("snorlax", pokemonList));
        assertTrue(pf.canFindName("sNorLax", pokemonList));
        assertTrue(pf.canFindName("SNORLAX", pokemonList));
    }

    @Test
    public void testCanFindNameIgnoringSpaces() {
        pokemonList.add(p1);
        assertTrue(pf.canFindName(" snorlax", pokemonList));
        assertTrue(pf.canFindName("sNorLax ", pokemonList));
        assertTrue(pf.canFindName("      SNORLAX   ", pokemonList));
    }

    @Test
    public void testFindPokemonOneInList() {
        pokemonList.add(p1);
        assertEquals(p1, pf.findPokemon("Snorlax", pokemonList));
    }

    @Test
    public void testFindPokemonMultiplePokemonInList() {
        pokemonList.add(p1);
        pokemonList.add(p2);
        pokemonList.add(p3);
        assertEquals(p3, pf.findPokemon("Clefairy", pokemonList));
    }

    @Test
    public void testCantFindPokemon() {
        assertNull(pf.findPokemon("Kommo-o", pokemonList));
    }

    @Test
    public void testFindPokemonIgnoringCase() {
        pokemonList.add(p1);

        assertEquals(p1, pf.findPokemon("snorlax", pokemonList));
        assertEquals(p1, pf.findPokemon("sNorLax", pokemonList));
        assertEquals(p1, pf.findPokemon("SNORLAX", pokemonList));
    }


}
