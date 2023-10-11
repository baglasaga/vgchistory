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
    public void testAddLostMatchNoEloChange() {
        m1.setLoss();
        m1.setEloChange(0);
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);

        mh.addMatch(m1);

        assertEquals(1, mh.getMatches().size());
        assertEquals(1000, mh.getElo());
        assertEquals(0, mh.getWins());

        assertEquals(2, mh.getPokemonList().size());
        assertTrue(mh.getPokemonList().contains(m1.getMyTeam().get(0)));
        assertTrue(mh.getPokemonList().contains(m1.getEnemyTeam().get(0)));
   }

    @Test
    public void testAddMatchAddSamePokemonTwice() {
        m1.setLoss();
        m1.setEloChange(0);
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p1.getName(), TeamSelector.OPPONENT, mh);

        mh.addMatch(m1);

        assertEquals(1, mh.getPokemonList().size());
        assertTrue(mh.getPokemonList().contains(m1.getMyTeam().get(0)));
    }

    @Test
    public void testAddMatchAddSamePokemonMultipleTimes() {
        m1.setLoss();
        m1.setEloChange(0);
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p1.getName(), TeamSelector.OPPONENT, mh);

        mh.addMatch(m1);

        m2.setLoss();
        m2.setEloChange(0);
        m2.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m2.addPokemonByName(p1.getName(), TeamSelector.OPPONENT, mh);

        mh.addMatch(m2);

        assertEquals(1, mh.getPokemonList().size());
        assertTrue(mh.getPokemonList().contains(m1.getMyTeam().get(0)));
    }

    @Test
    public void testAddMatchAddMultiplePokemonWithOverlap() {
        m1.setLoss();
        m1.setEloChange(0);
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);

        mh.addMatch(m1);

        m2.setLoss();
        m2.setEloChange(0);
        m2.addPokemonByName(p2.getName(), TeamSelector.USER, mh);
        m2.addPokemonByName(p3.getName(), TeamSelector.OPPONENT, mh);

        mh.addMatch(m2);

        assertEquals(3, mh.getPokemonList().size());
        assertTrue(mh.getPokemonList().contains(m1.getMyTeam().get(0)));
        assertTrue(mh.getPokemonList().contains(m1.getEnemyTeam().get(0)));
        assertTrue(mh.getPokemonList().contains(m2.getEnemyTeam().get(0)));
    }

    @Test
    public void testAddWonMatchWithPositiveEloChange() {
        m1.setWin();
        m1.setEloChange(13);
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);

        mh.addMatch(m1);

        assertEquals(1, mh.getMatches().size());
        assertEquals(1013, mh.getElo());
        assertEquals(1, mh.getWins());

        assertEquals(2, mh.getPokemonList().size());

        assertEquals(100.0, mh.getWinRate());
    }

    @Test
    public void testAddLostMatchWithNegativeEloChange() {
        m1.setLoss();
        m1.setEloChange(-22);
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);

        mh.addMatch(m1);

        assertEquals(1, mh.getMatches().size());
        assertEquals(1000-22, mh.getElo());
        assertEquals(0, mh.getWins());

        assertEquals(2, mh.getPokemonList().size());

        assertEquals(0, mh.getWinRate());
    }

    @Test
    public void testAddMultipleWonMatches() {
        m1.setWin();
        m1.setEloChange(13);
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);

        mh.addMatch(m1);

        assertEquals(1, mh.getMatches().size());
        assertEquals(1013, mh.getElo());
        assertEquals(1, mh.getWins());

        assertEquals(2, mh.getPokemonList().size());

        m2.setWin();
        m2.setEloChange(12);
        m2.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m2.addPokemonByName(p3.getName(), TeamSelector.OPPONENT, mh);

        mh.addMatch(m2);

        assertEquals(2, mh.getMatches().size());
        assertEquals(1025, mh.getElo());
        assertEquals(2, mh.getWins());

        assertEquals(3, mh.getPokemonList().size());

        assertEquals(100.0, mh.getWinRate());
    }

    @Test
    public void testAddMultipleLostMatches() {
        m1.setLoss();
        m1.setEloChange(-13);
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);

        mh.addMatch(m1);

        assertEquals(1, mh.getMatches().size());
        assertEquals(1000-13, mh.getElo());
        assertEquals(0, mh.getWins());

        assertEquals(2, mh.getPokemonList().size());

        m2.setLoss();
        m2.setEloChange(-12);
        m2.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m2.addPokemonByName(p3.getName(), TeamSelector.OPPONENT, mh);

        mh.addMatch(m2);

        assertEquals(2, mh.getMatches().size());
        assertEquals(1000-25, mh.getElo());
        assertEquals(0, mh.getWins());

        assertEquals(3, mh.getPokemonList().size());

        assertEquals(0, mh.getWinRate());
    }

    @Test
    public void testAddMatchOneWinOneLoss() {
        m1.setWin();
        m1.setEloChange(13);
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);

        mh.addMatch(m1);

        assertEquals(1, mh.getMatches().size());
        assertEquals(1013, mh.getElo());
        assertEquals(1, mh.getWins());

        assertEquals(2, mh.getPokemonList().size());

        m2.setLoss();
        m2.setEloChange(-12);
        m2.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m2.addPokemonByName(p3.getName(), TeamSelector.OPPONENT, mh);

        mh.addMatch(m2);

        assertEquals(2, mh.getMatches().size());
        assertEquals(1001, mh.getElo());
        assertEquals(1, mh.getWins());

        assertEquals(3, mh.getPokemonList().size());

        assertEquals(50.0, mh.getWinRate());
    }

    @Test
    public void testGetHighestWinRatesOnePokemon() {
        m1.setWin();
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p1.getName(), TeamSelector.OPPONENT, mh);

        mh.addMatch(m1);

        List<Pokemon> pokemonList = mh.getHighestWinRates(1, TeamSelector.USER);
        assertEquals(1, pokemonList.size());
        assertTrue(pokemonList.contains(m1.getMyTeam().get(0)));

        List<Pokemon> pokemonList2 = mh.getHighestWinRates(1, TeamSelector.OPPONENT);
        assertEquals(1, pokemonList2.size());
        assertTrue(pokemonList2.contains(m1.getEnemyTeam().get(0)));
    }

    @Test
    public void testGetLowestWinRatesOnePokemon() {
        m1.setLoss();
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        Pokemon firstPokemon = m1.getMyTeam().get(0);
        m1.addPokemonByName(p1.getName(), TeamSelector.OPPONENT, mh);
        assertTrue(m1.getEnemyTeam().contains(firstPokemon));
        mh.addMatch(m1);

        firstPokemon.setAlliedWinRate(100);
        firstPokemon.setEnemyWinRate(100);

        List<Pokemon> pokemonList = mh.getLowestWinRates(1, TeamSelector.USER);
        assertEquals(1, pokemonList.size());
        assertTrue(pokemonList.contains(firstPokemon));

        List<Pokemon> pokemonList2 = mh.getLowestWinRates(1, TeamSelector.OPPONENT);
        assertEquals(1, pokemonList2.size());
        assertTrue(pokemonList2.contains(firstPokemon));
    }

    @Test
    public void testGetHighestOneWinRateFirstPokemonHighest() {
        m1.setLoss();
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p3.getName(), TeamSelector.USER, mh);
        mh.addMatch(m1);

        Pokemon firstPokemon = m1.getMyTeam().get(0);
        Pokemon secondPokemon = m1.getMyTeam().get(1);
        Pokemon thirdPokemon = m1.getMyTeam().get(2);

        firstPokemon.setAlliedWinRate(100);
        secondPokemon.setAlliedWinRate(50);
        thirdPokemon.setAlliedWinRate(33.333);

        List<Pokemon> pokemonList = mh.getHighestWinRates(1, TeamSelector.USER);
        assertEquals(1, pokemonList.size());
        assertTrue(pokemonList.contains(firstPokemon));
    }

    @Test
    public void testGetHighestOneWinRateLastPokemonHighest() {
        m1.setLoss();
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p3.getName(), TeamSelector.USER, mh);
        mh.addMatch(m1);

        Pokemon firstPokemon = m1.getMyTeam().get(0);
        Pokemon secondPokemon = m1.getMyTeam().get(1);
        Pokemon thirdPokemon = m1.getMyTeam().get(2);

        firstPokemon.setAlliedWinRate(33.333);
        secondPokemon.setAlliedWinRate(50);
        thirdPokemon.setAlliedWinRate(100);



        List<Pokemon> pokemonList = mh.getHighestWinRates(1, TeamSelector.USER);
        assertEquals(1, pokemonList.size());
        assertTrue(pokemonList.contains(thirdPokemon));
    }

    @Test
    public void testGetHighestOneWinRateWithTie() {
        m1.setLoss();
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p3.getName(), TeamSelector.USER, mh);
        mh.addMatch(m1);

        Pokemon firstPokemon = m1.getMyTeam().get(0);
        Pokemon secondPokemon = m1.getMyTeam().get(1);
        Pokemon thirdPokemon = m1.getMyTeam().get(2);

        firstPokemon.setAlliedWinRate(33.333);
        secondPokemon.setAlliedWinRate(50);
        thirdPokemon.setAlliedWinRate(50);

        List<Pokemon> pokemonList = mh.getHighestWinRates(1, TeamSelector.USER);
        assertEquals(1, pokemonList.size());
        assertTrue(pokemonList.contains(secondPokemon));
    }

    @Test
    public void testGetHighestAllWinRates() {
        m1.setLoss();
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p3.getName(), TeamSelector.USER, mh);
        mh.addMatch(m1);

        Pokemon firstPokemon = m1.getMyTeam().get(0);
        Pokemon secondPokemon = m1.getMyTeam().get(1);
        Pokemon thirdPokemon = m1.getMyTeam().get(2);

        firstPokemon.setAlliedWinRate(100);
        secondPokemon.setAlliedWinRate(50);
        thirdPokemon.setAlliedWinRate(75);


        List<Pokemon> pokemonList = mh.getHighestWinRates(3, TeamSelector.USER);
        assertEquals(3, pokemonList.size());
        assertEquals(firstPokemon, pokemonList.get(0));
        assertEquals(thirdPokemon, pokemonList.get(1));
        assertEquals(secondPokemon, pokemonList.get(2));
    }

    @Test
    public void testGetHighestPokemonNoPokemonWithUsage() {
        m1.setLoss();
        m1.addPokemonByName(p1.getName(), TeamSelector.OPPONENT, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);
        m1.addPokemonByName(p3.getName(), TeamSelector.OPPONENT, mh);
        mh.addMatch(m1);


        List<Pokemon> result = mh.getHighestWinRates(3, TeamSelector.USER);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetHighestPokemonOnePokemonWithUsage() {
        m1.setLoss();
        m1.addPokemonByName(p1.getName(), TeamSelector.OPPONENT, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);
        m1.addPokemonByName(p3.getName(), TeamSelector.USER, mh);
        mh.addMatch(m1);

        Pokemon firstPokemon = m1.getEnemyTeam().get(0);
        Pokemon secondPokemon = m1.getEnemyTeam().get(1);
        Pokemon thirdPokemon = m1.getMyTeam().get(0);

        List<Pokemon> result = mh.getHighestWinRates(3, TeamSelector.USER);

        assertEquals(1, result.size());
        assertEquals(thirdPokemon, result.get(0));
        assertFalse(result.contains(firstPokemon));
        assertFalse(result.contains(secondPokemon));
    }

    @Test
    public void testGetLowestOneWinRateFirstPokemonLowest() {
        m1.setLoss();
        m1.addPokemonByName(p1.getName(), TeamSelector.OPPONENT, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);
        m1.addPokemonByName(p3.getName(), TeamSelector.OPPONENT, mh);
        mh.addMatch(m1);

        Pokemon firstPokemon = m1.getEnemyTeam().get(0);
        Pokemon secondPokemon = m1.getEnemyTeam().get(1);
        Pokemon thirdPokemon = m1.getEnemyTeam().get(2);

        firstPokemon.setEnemyWinRate(25);
        secondPokemon.setEnemyWinRate(50);
        thirdPokemon.setEnemyWinRate(33.333);


        List<Pokemon> pokemonList = mh.getLowestWinRates(1, TeamSelector.OPPONENT);
        assertEquals(1, pokemonList.size());
        assertTrue(pokemonList.contains(firstPokemon));
    }

    @Test
    public void testGetLowestOneWinRateLastPokemonLowest() {
        m1.setLoss();
        m1.addPokemonByName(p1.getName(), TeamSelector.OPPONENT, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);
        m1.addPokemonByName(p3.getName(), TeamSelector.OPPONENT, mh);
        mh.addMatch(m1);

        Pokemon firstPokemon = m1.getEnemyTeam().get(0);
        Pokemon secondPokemon = m1.getEnemyTeam().get(1);
        Pokemon thirdPokemon = m1.getEnemyTeam().get(2);

        firstPokemon.setEnemyWinRate(33.333);
        secondPokemon.setEnemyWinRate(50);
        thirdPokemon.setEnemyWinRate(25);

        List<Pokemon> pokemonList = mh.getLowestWinRates(1, TeamSelector.OPPONENT);
        assertEquals(1, pokemonList.size());
        assertTrue(pokemonList.contains(thirdPokemon));
    }

    @Test
    public void testGetLowestOneWinRateWithTie() {
        m1.setLoss();
        m1.addPokemonByName(p1.getName(), TeamSelector.OPPONENT, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);
        m1.addPokemonByName(p3.getName(), TeamSelector.OPPONENT, mh);
        mh.addMatch(m1);

        Pokemon firstPokemon = m1.getEnemyTeam().get(0);
        Pokemon secondPokemon = m1.getEnemyTeam().get(1);
        Pokemon thirdPokemon = m1.getEnemyTeam().get(2);

        firstPokemon.setEnemyWinRate(33.333);
        secondPokemon.setEnemyWinRate(25);
        thirdPokemon.setEnemyWinRate(25);

        List<Pokemon> pokemonList = mh.getLowestWinRates(1, TeamSelector.OPPONENT);
        assertEquals(1, pokemonList.size());
        assertTrue(pokemonList.contains(secondPokemon));
    }

    @Test
    public void testGetLowestAllWinRates() {
        m1.setLoss();
        m1.addPokemonByName(p1.getName(), TeamSelector.OPPONENT, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);
        m1.addPokemonByName(p3.getName(), TeamSelector.OPPONENT, mh);
        mh.addMatch(m1);

        Pokemon firstPokemon = m1.getEnemyTeam().get(0);
        Pokemon secondPokemon = m1.getEnemyTeam().get(1);
        Pokemon thirdPokemon = m1.getEnemyTeam().get(2);

        firstPokemon.setEnemyWinRate(100);
        secondPokemon.setEnemyWinRate(50);
        thirdPokemon.setEnemyWinRate(75);

        List<Pokemon> pokemonList = mh.getLowestWinRates(3, TeamSelector.OPPONENT);
        assertEquals(3, pokemonList.size());
        assertEquals(secondPokemon, pokemonList.get(0));
        assertEquals(thirdPokemon, pokemonList.get(1));
        assertEquals(firstPokemon, pokemonList.get(2));
    }

    @Test
    public void testGetLowestPokemonNoPokemonWithUsage() {
        m1.setLoss();
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p3.getName(), TeamSelector.USER, mh);
        mh.addMatch(m1);

        List<Pokemon> result = mh.getLowestWinRates(3, TeamSelector.OPPONENT);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetLowestPokemonOnePokemonWithUsage() {
        m1.setLoss();
        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p3.getName(), TeamSelector.OPPONENT, mh);
        mh.addMatch(m1);

        Pokemon firstPokemon = m1.getMyTeam().get(0);
        Pokemon secondPokemon = m1.getMyTeam().get(1);
        Pokemon thirdPokemon = m1.getEnemyTeam().get(0);

        List<Pokemon> result = mh.getLowestWinRates(3, TeamSelector.OPPONENT);

        assertEquals(1, result.size());
        assertEquals(thirdPokemon, result.get(0));
        assertFalse(result.contains(firstPokemon));
        assertFalse(result.contains(secondPokemon));
    }

}
