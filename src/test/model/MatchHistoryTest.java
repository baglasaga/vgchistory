package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MatchHistoryTest {

    MatchHistory mh;
    Match m1;
    Match m2;
    Pokemon p1;
    Pokemon p2;
    Pokemon p3;
    Pokemon p4;

    @BeforeEach
    public void setup() {
        mh = new MatchHistory();

        m1 = new Match();
        m2 = new Match();

        p1 = new Pokemon("Ogerpon-Hearthflame");
        p2 = new Pokemon("Tornadus");
        p3 = new Pokemon("Kingambit");
        p4 = new Pokemon("Farigiraf");
    }

    @Test
    public void testConstructor() {
        assertTrue(mh.getMatches().isEmpty());
        assertTrue(mh.getPokemonList().isEmpty());

        assertEquals(0, mh.getWins());
        assertEquals(0, mh.getWinRate());
        assertEquals(1000, mh.getElo());

    }

    @Test
    public void testAddUniquePokemonOnePokemon() {
        mh.addUniquePokemon(p1);

        assertEquals(1, mh.getPokemonList().size());
        assertTrue(mh.getPokemonList().contains(p1));
    }

    @Test
    public void testAddUniquePokemonAlreadyInList() {
        mh.addUniquePokemon(p1);
        mh.addUniquePokemon(p1);

        assertEquals(1, mh.getPokemonList().size());
        assertTrue(mh.getPokemonList().contains(p1));
    }

    @Test
    public void testAddUniquePokemonAlreadyInListMultipleTimes() {
        mh.addUniquePokemon(p1);
        mh.addUniquePokemon(p1);
        mh.addUniquePokemon(p1);
        mh.addUniquePokemon(p1);

        assertEquals(1, mh.getPokemonList().size());
        assertTrue(mh.getPokemonList().contains(p1));
    }

    @Test
    public void testAddUniquePokemonMultipleTimes() {
        mh.addUniquePokemon(p1);
        mh.addUniquePokemon(p2);
        mh.addUniquePokemon(p3);
        mh.addUniquePokemon(p4);

        assertEquals(4, mh.getPokemonList().size());
        assertTrue(mh.getPokemonList().contains(p1));
        assertTrue(mh.getPokemonList().contains(p2));
        assertTrue(mh.getPokemonList().contains(p3));
        assertTrue(mh.getPokemonList().contains(p4));
    }

    @Test
    public void testUpdatePokemonListUserTeamOnePokemon() {
        m1.setWin();

        m1.addPokemon(p1, TeamSelector.USER);
        m1.addPokemon(p2, TeamSelector.OPPONENT);

        mh.updatePokemonList(m1, TeamSelector.USER);

        assertEquals(1, mh.getPokemonList().size());
        assertTrue(mh.getPokemonList().contains(p1));
        assertFalse(mh.getPokemonList().contains(p2));
    }

    @Test
    public void testUpdatePokemonListOpponentTeamOnePokemon() {
        m1.setWin();

        m1.addPokemon(p1, TeamSelector.USER);
        m1.addPokemon(p2, TeamSelector.OPPONENT);

        mh.updatePokemonList(m1, TeamSelector.OPPONENT);

        assertEquals(1, mh.getPokemonList().size());
        assertTrue(mh.getPokemonList().contains(p2));
        assertFalse(mh.getPokemonList().contains(p1));
    }

    @Test
    public void testUpdatePokemonListBothTeamsMultiplePokemon() {
        m1.setWin();

        m1.addPokemon(p1, TeamSelector.USER);
        m1.addPokemon(p3, TeamSelector.USER);
        m1.addPokemon(p2, TeamSelector.OPPONENT);
        m1.addPokemon(p4, TeamSelector.OPPONENT);

        mh.updatePokemonList(m1, TeamSelector.USER);
        assertEquals(2, mh.getPokemonList().size());
        assertTrue(mh.getPokemonList().contains(p1));
        assertTrue(mh.getPokemonList().contains(p3));
        assertFalse(mh.getPokemonList().contains(p2));
        assertFalse(mh.getPokemonList().contains(p4));

        mh.updatePokemonList(m1, TeamSelector.OPPONENT);
        assertEquals(4, mh.getPokemonList().size());
        assertTrue(mh.getPokemonList().contains(p2));
        assertTrue(mh.getPokemonList().contains(p4));
    }

    @Test
    public void testUpdatePokemonListBothTeamsMultiplePokemonWithOverlap() {
        m1.setWin();

        m1.addPokemon(p1, TeamSelector.USER);
        m1.addPokemon(p3, TeamSelector.USER);
        m1.addPokemon(p2, TeamSelector.OPPONENT);
        m1.addPokemon(p3, TeamSelector.OPPONENT);

        mh.updatePokemonList(m1, TeamSelector.USER);
        assertEquals(2, mh.getPokemonList().size());
        assertTrue(mh.getPokemonList().contains(p1));
        assertTrue(mh.getPokemonList().contains(p3));
        assertFalse(mh.getPokemonList().contains(p2));

        mh.updatePokemonList(m1, TeamSelector.OPPONENT);
        assertEquals(3, mh.getPokemonList().size());
        assertTrue(mh.getPokemonList().contains(p2));
    }

    @Test
    public void testAddLostMatchNoEloChange() {
        m1.setLoss();
        m1.setEloChange(0);
        m1.addPokemon(p1, TeamSelector.USER);
        m1.addPokemon(p2, TeamSelector.OPPONENT);

        mh.addMatch(m1);

        assertEquals(1, mh.getMatches().size());
        assertEquals(1000, mh.getElo());
        assertEquals(0, mh.getWins());

        assertEquals(2, mh.getPokemonList().size());
    }

    @Test
    public void testAddWonMatchWithPositiveEloChange() {
        m1.setWin();
        m1.setEloChange(13);
        m1.addPokemon(p1, TeamSelector.USER);
        m1.addPokemon(p2, TeamSelector.OPPONENT);

        mh.addMatch(m1);

        assertEquals(1, mh.getMatches().size());
        assertEquals(1013, mh.getElo());
        assertEquals(1, mh.getWins());

        assertEquals(2, mh.getPokemonList().size());
    }

    @Test
    public void testAddLostMatchWithNegativeEloChange() {
        m1.setLoss();
        m1.setEloChange(-22);
        m1.addPokemon(p1, TeamSelector.USER);
        m1.addPokemon(p2, TeamSelector.OPPONENT);

        mh.addMatch(m1);

        assertEquals(1, mh.getMatches().size());
        assertEquals(1000-22, mh.getElo());
        assertEquals(0, mh.getWins());

        assertEquals(2, mh.getPokemonList().size());
    }

    @Test
    public void testAddMultipleWonMatches() {
        m1.setWin();
        m1.setEloChange(13);
        m1.addPokemon(p1, TeamSelector.USER);
        m1.addPokemon(p2, TeamSelector.OPPONENT);

        mh.addMatch(m1);

        assertEquals(1, mh.getMatches().size());
        assertEquals(1013, mh.getElo());
        assertEquals(1, mh.getWins());

        assertEquals(2, mh.getPokemonList().size());

        m2.setWin();
        m2.setEloChange(12);
        m2.addPokemon(p1, TeamSelector.USER);
        m2.addPokemon(p3, TeamSelector.OPPONENT);

        mh.addMatch(m2);

        assertEquals(2, mh.getMatches().size());
        assertEquals(1025, mh.getElo());
        assertEquals(2, mh.getWins());

        assertEquals(3, mh.getPokemonList().size());
    }

    @Test
    public void testAddMultipleLostMatches() {
        m1.setLoss();
        m1.setEloChange(-13);
        m1.addPokemon(p1, TeamSelector.USER);
        m1.addPokemon(p2, TeamSelector.OPPONENT);

        mh.addMatch(m1);

        assertEquals(1, mh.getMatches().size());
        assertEquals(1000-13, mh.getElo());
        assertEquals(0, mh.getWins());

        assertEquals(2, mh.getPokemonList().size());

        m2.setLoss();
        m2.setEloChange(-12);
        m2.addPokemon(p1, TeamSelector.USER);
        m2.addPokemon(p3, TeamSelector.OPPONENT);

        mh.addMatch(m2);

        assertEquals(2, mh.getMatches().size());
        assertEquals(1000-25, mh.getElo());
        assertEquals(0, mh.getWins());

        assertEquals(3, mh.getPokemonList().size());
    }

    @Test
    public void testAddMatchOneWinOneLoss() {
        m1.setWin();
        m1.setEloChange(13);
        m1.addPokemon(p1, TeamSelector.USER);
        m1.addPokemon(p2, TeamSelector.OPPONENT);

        mh.addMatch(m1);

        assertEquals(1, mh.getMatches().size());
        assertEquals(1013, mh.getElo());
        assertEquals(1, mh.getWins());

        assertEquals(2, mh.getPokemonList().size());

        m2.setLoss();
        m2.setEloChange(-12);
        m2.addPokemon(p1, TeamSelector.USER);
        m2.addPokemon(p3, TeamSelector.OPPONENT);

        mh.addMatch(m2);

        assertEquals(2, mh.getMatches().size());
        assertEquals(1001, mh.getElo());
        assertEquals(1, mh.getWins());

        assertEquals(3, mh.getPokemonList().size());
    }

    @Test
    public void testUpdateWinRateNoWins() {
        m1.setLoss();
        mh.addMatch(m1);

        mh.updateWinRate();

        assertEquals(0, mh.getWinRate());
    }

    @Test
    public void testUpdateWinRateOneWin() {
        m1.setWin();
        mh.addMatch(m1);

        mh.updateWinRate();

        assertEquals(100.0, mh.getWinRate());
    }

    @Test
    public void testUpdateWinRateOneWinOneLoss() {
        m1.setWin();
        mh.addMatch(m1);
        m2.setLoss();
        mh.addMatch(m2);
        mh.updateWinRate();

        assertEquals(50.0, mh.getWinRate());
    }

    @Test
    public void testGetHighestWinRatesOnePokemon() {
        m1.setWin();
        p1.addMatch(m1, TeamSelector.USER);
        p1.updateWinRate(TeamSelector.USER);
        p1.addMatch(m1, TeamSelector.OPPONENT);
        p1.updateWinRate(TeamSelector.OPPONENT);
        mh.addUniquePokemon(p1);

        List<Pokemon> pokemonList = mh.getHighestWinRates(1, TeamSelector.USER);
        assertEquals(1, pokemonList.size());
        assertTrue(pokemonList.contains(p1));

        List<Pokemon> pokemonList2 = mh.getHighestWinRates(1, TeamSelector.OPPONENT);
        assertEquals(1, pokemonList2.size());
        assertTrue(pokemonList2.contains(p1));
    }

    @Test
    public void testGetLowestWinRatesOnePokemon() {
        p1.setAlliedWinRate(100);
        p1.setEnemyWinRate(100);
        mh.addUniquePokemon(p1);

        List<Pokemon> pokemonList = mh.getLowestWinRates(1, TeamSelector.USER);
        assertEquals(1, pokemonList.size());
        assertTrue(pokemonList.contains(p1));

        List<Pokemon> pokemonList2 = mh.getLowestWinRates(1, TeamSelector.OPPONENT);
        assertEquals(1, pokemonList2.size());
        assertTrue(pokemonList2.contains(p1));
    }

    @Test
    public void testGetHighestOneWinRateFirstPokemonHighest() {
        p1.setAlliedWinRate(100);
        p2.setAlliedWinRate(50);
        p3.setAlliedWinRate(33.333);

        mh.addUniquePokemon(p1);
        mh.addUniquePokemon(p2);
        mh.addUniquePokemon(p3);

        List<Pokemon> pokemonList = mh.getHighestWinRates(1, TeamSelector.USER);
        assertEquals(1, pokemonList.size());
        assertTrue(pokemonList.contains(p1));
    }

    @Test
    public void testGetHighestOneWinRateLastPokemonHighest() {
        p1.setAlliedWinRate(33.333);
        p2.setAlliedWinRate(50);
        p3.setAlliedWinRate(100);

        mh.addUniquePokemon(p1);
        mh.addUniquePokemon(p2);
        mh.addUniquePokemon(p3);

        List<Pokemon> pokemonList = mh.getHighestWinRates(1, TeamSelector.USER);
        assertEquals(1, pokemonList.size());
        assertTrue(pokemonList.contains(p3));
    }

    @Test
    public void testGetHighestOneWinRateWithTie() {
        p1.setAlliedWinRate(33.333);
        p2.setAlliedWinRate(50);
        p3.setAlliedWinRate(50);

        mh.addUniquePokemon(p1);
        mh.addUniquePokemon(p2);
        mh.addUniquePokemon(p3);

        List<Pokemon> pokemonList = mh.getHighestWinRates(1, TeamSelector.USER);
        assertEquals(1, pokemonList.size());
        assertTrue(pokemonList.contains(p2));
    }

    @Test
    public void testGetHighestAllWinRates() {
        p1.setAlliedWinRate(100);
        p2.setAlliedWinRate(50);
        p3.setAlliedWinRate(75);

        mh.addUniquePokemon(p1);
        mh.addUniquePokemon(p2);
        mh.addUniquePokemon(p3);

        List<Pokemon> pokemonList = mh.getHighestWinRates(3, TeamSelector.USER);
        assertEquals(3, pokemonList.size());
        assertEquals(p1, pokemonList.get(0));
        assertEquals(p3, pokemonList.get(1));
        assertEquals(p2, pokemonList.get(2));
    }

    @Test
    public void testGetLowestOneWinRateFirstPokemonLowest() {
        p1.setEnemyWinRate(25);
        p2.setEnemyWinRate(50);
        p3.setEnemyWinRate(33.333);

        mh.addUniquePokemon(p1);
        mh.addUniquePokemon(p2);
        mh.addUniquePokemon(p3);

        List<Pokemon> pokemonList = mh.getLowestWinRates(1, TeamSelector.OPPONENT);
        assertEquals(1, pokemonList.size());
        assertTrue(pokemonList.contains(p1));
    }

    @Test
    public void testGetLowestOneWinRateLastPokemonLowest() {
        p1.setEnemyWinRate(33.333);
        p2.setEnemyWinRate(50);
        p3.setEnemyWinRate(25);

        mh.addUniquePokemon(p1);
        mh.addUniquePokemon(p2);
        mh.addUniquePokemon(p3);

        List<Pokemon> pokemonList = mh.getLowestWinRates(1, TeamSelector.OPPONENT);
        assertEquals(1, pokemonList.size());
        assertTrue(pokemonList.contains(p3));
    }

    @Test
    public void testGetLowestOneWinRateWithTie() {
        p1.setEnemyWinRate(33.333);
        p2.setEnemyWinRate(25);
        p3.setEnemyWinRate(25);

        mh.addUniquePokemon(p1);
        mh.addUniquePokemon(p2);
        mh.addUniquePokemon(p3);

        List<Pokemon> pokemonList = mh.getLowestWinRates(1, TeamSelector.OPPONENT);
        assertEquals(1, pokemonList.size());
        assertTrue(pokemonList.contains(p2));
    }

    @Test
    public void testGetLowestAllWinRates() {
        p1.setEnemyWinRate(100);
        p2.setEnemyWinRate(50);
        p3.setEnemyWinRate(75);

        mh.addUniquePokemon(p1);
        mh.addUniquePokemon(p2);
        mh.addUniquePokemon(p3);

        List<Pokemon> pokemonList = mh.getLowestWinRates(3, TeamSelector.OPPONENT);
        assertEquals(3, pokemonList.size());
        assertEquals(p2, pokemonList.get(0));
        assertEquals(p3, pokemonList.get(1));
        assertEquals(p1, pokemonList.get(2));
    }

}
