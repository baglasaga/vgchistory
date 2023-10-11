package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {

    private MatchHistory mh;
    private PokemonFinder pf;
    private Match m1;
    private Match m2;
    private Match m3;
    private Pokemon p1;
    private Pokemon p2;

    @BeforeEach
    public void setup() {
        mh = new MatchHistory();
        pf = new PokemonFinder();

        m1 = new Match();
        m2 = new Match();
        m3 = new Match();

        p1 = new Pokemon("Flutter Mane");
        p2 = new Pokemon("Iron Hands");
    }

    @Test
    public void testConstructor() {

        assertTrue(m1.getId() > 0);
        assertTrue(m1.getMyTeam().isEmpty());
        assertTrue(m1.getEnemyTeam().isEmpty());


    }

    @Test
    public void testSetWinLoss() {
        m1.setWin();
        m2.setLoss();

        assertTrue(m1.getWinStatus());
        assertFalse(m2.getWinStatus());
    }

    @Test
    public void testGetTeamNames() {
        m1.setWin();

        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.USER, mh);

        m1.addPokemonByName(p1.getName(), TeamSelector.OPPONENT, mh);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);

        List<String> userTeamNames = m1.getTeamNames(TeamSelector.USER);
        List<String> opponentTeamNames = m1.getTeamNames(TeamSelector.OPPONENT);

        assertEquals(2, userTeamNames.size());
        assertEquals(2, opponentTeamNames.size());

        assertTrue(userTeamNames.contains(p1.getName()));
        assertTrue(userTeamNames.contains(p2.getName()));

        assertTrue(opponentTeamNames.contains(p1.getName()));
        assertTrue(opponentTeamNames.contains(p2.getName()));
    }

    @Test
    public void testAddPokemonByNameNewPokemon() {
        m1.setWin();
        m1.addPokemonByName("Chi-Yu", TeamSelector.USER, mh);
        assertEquals(1, m1.getMyTeam().size());
        assertEquals("Chi-Yu", m1.getMyTeam().get(0).getName());
    }

    @Test
    public void testAddPokemonByNameAlreadyExistsInMatchHistory() {
        addUniquePokemon(p1, mh);
        addUniquePokemon(p2, mh);

        m1.setWin();
        m1.addPokemonByName("Flutter Mane", TeamSelector.USER, mh);
        assertEquals(1, m1.getMyTeam().size());
        assertTrue(m1.getMyTeam().contains(pf.findPokemon(p1.getName(), mh.getPokemonList())));

        m1.addPokemonByName("Iron Hands", TeamSelector.OPPONENT, mh);
        assertEquals(1, m1.getEnemyTeam().size());
        assertTrue(m1.getEnemyTeam().contains(pf.findPokemon(p2.getName(), mh.getPokemonList())));
    }

    @Test
    public void testAddPokemonByNameAlreadyExistsInMatch() {
        m1.setWin();
        m1.addPokemonByName("Chi-Yu", TeamSelector.USER, mh);
        assertEquals(1, m1.getMyTeam().size());
        assertEquals("Chi-Yu", m1.getMyTeam().get(0).getName());

        m1.addPokemonByName("Chi-Yu", TeamSelector.OPPONENT, mh);
        assertEquals(1, m1.getEnemyTeam().size());
        assertEquals(m1.getMyTeam().get(0), m1.getEnemyTeam().get(0));
    }

    @Test
    public void testAddOneAllyPokemonToLostMatch() {
        m1.setLoss();

        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        Pokemon firstPokemon = m1.getMyTeam().get(0);
        assertEquals(1, m1.getMyTeam().size());
        assertEquals(p1.getName(), firstPokemon.getName());

        assertEquals(1, firstPokemon.getAlliedMatches().size());
        assertTrue(firstPokemon.getAlliedMatches().contains(m1));
        assertEquals(0, firstPokemon.getAlliedWins());

        assertEquals(0, firstPokemon.getEnemyMatches().size());
        assertEquals(0, firstPokemon.getEnemyWins());

    }

    @Test
    public void testAddOnePokemonEachTeamToLostMatch() {
        m1.setLoss();

        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        Pokemon firstPokemon = m1.getMyTeam().get(0);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);
        Pokemon secondPokemon = m1.getEnemyTeam().get(0);

        assertEquals(1, m1.getMyTeam().size());
        assertEquals(p1.getName(), firstPokemon.getName());

        assertEquals(1, m1.getEnemyTeam().size());
        assertEquals(p2.getName(), secondPokemon.getName());

        assertEquals(1, firstPokemon.getAlliedMatches().size());
        assertTrue(firstPokemon.getAlliedMatches().contains(m1));
        assertEquals(0, firstPokemon.getAlliedWins());

        assertEquals(1, secondPokemon.getEnemyMatches().size());
        assertTrue(secondPokemon.getEnemyMatches().contains(m1));
        assertEquals(0, secondPokemon.getEnemyWins());
    }

    @Test
    public void testAddOneAllyPokemonToWonMatch() {
        m1.setWin();

        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        Pokemon firstPokemon = m1.getMyTeam().get(0);
        assertEquals(1, m1.getMyTeam().size());
        assertEquals(p1.getName(), firstPokemon.getName());

        assertEquals(1, firstPokemon.getAlliedMatches().size());
        assertTrue(firstPokemon.getAlliedMatches().contains(m1));
        assertEquals(1, firstPokemon.getAlliedWins());

        assertEquals(0, firstPokemon.getEnemyMatches().size());
        assertEquals(0, firstPokemon.getEnemyWins());
    }

    @Test
    public void testAddOnePokemonEachTeamToWonMatch() {
        m1.setWin();

        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        Pokemon firstPokemon = m1.getMyTeam().get(0);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);
        Pokemon secondPokemon = m1.getEnemyTeam().get(0);

        assertEquals(1, m1.getMyTeam().size());
        assertEquals(p1.getName(), firstPokemon.getName());

        assertEquals(1, m1.getEnemyTeam().size());
        assertEquals(p2.getName(), secondPokemon.getName());

        assertEquals(1, firstPokemon.getAlliedMatches().size());
        assertTrue(firstPokemon.getAlliedMatches().contains(m1));
        assertEquals(1, firstPokemon.getAlliedWins());

        assertEquals(1, secondPokemon.getEnemyMatches().size());
        assertTrue(secondPokemon.getEnemyMatches().contains(m1));
        assertEquals(1, secondPokemon.getEnemyWins());
    }

    @Test
    public void testAddMultiplePokemonEachTeamToLostMatch() {
        m1.setLoss();

        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        Pokemon firstAlly = m1.getMyTeam().get(0);
        m1.addPokemonByName(p2.getName(), TeamSelector.USER, mh);
        Pokemon secondAlly = m1.getMyTeam().get(1);

        assertEquals(2, m1.getMyTeam().size());
        assertEquals(p1.getName(), firstAlly.getName());
        assertEquals(p2.getName(), secondAlly.getName());

        m1.addPokemonByName(p1.getName(), TeamSelector.OPPONENT, mh);
        Pokemon firstEnemy = m1.getEnemyTeam().get(0);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);
        Pokemon secondEnemy = m1.getEnemyTeam().get(1);

        assertEquals(2, m1.getMyTeam().size());
        assertEquals(p1.getName(), firstEnemy.getName());
        assertEquals(p2.getName(), secondEnemy.getName());

        assertEquals(1, firstAlly.getAlliedMatches().size());
        assertTrue(firstAlly.getAlliedMatches().contains(m1));
        assertEquals(0, firstAlly.getAlliedWins());

        assertEquals(1, secondAlly.getAlliedMatches().size());
        assertTrue(secondAlly.getAlliedMatches().contains(m1));
        assertEquals(0, secondAlly.getAlliedWins());

        assertEquals(1, firstEnemy.getEnemyMatches().size());
        assertTrue(firstEnemy.getEnemyMatches().contains(m1));
        assertEquals(0, firstEnemy.getEnemyWins());

        assertEquals(1, secondEnemy.getEnemyMatches().size());
        assertTrue(secondEnemy.getEnemyMatches().contains(m1));
        assertEquals(0, secondEnemy.getEnemyWins());

        assertEquals(firstAlly, firstEnemy);
        assertEquals(secondAlly, secondEnemy);

    }

    @Test
    public void testAddMultiplePokemonEachTeamToWonMatch() {
        m1.setWin();

        m1.addPokemonByName(p1.getName(), TeamSelector.USER, mh);
        Pokemon firstAlly = m1.getMyTeam().get(0);
        m1.addPokemonByName(p2.getName(), TeamSelector.USER, mh);
        Pokemon secondAlly = m1.getMyTeam().get(1);

        assertEquals(2, m1.getMyTeam().size());
        assertEquals(p1.getName(), firstAlly.getName());
        assertEquals(p2.getName(), secondAlly.getName());

        m1.addPokemonByName(p1.getName(), TeamSelector.OPPONENT, mh);
        Pokemon firstEnemy = m1.getEnemyTeam().get(0);
        m1.addPokemonByName(p2.getName(), TeamSelector.OPPONENT, mh);
        Pokemon secondEnemy = m1.getEnemyTeam().get(1);

        assertEquals(2, m1.getMyTeam().size());
        assertEquals(p1.getName(), firstEnemy.getName());
        assertEquals(p2.getName(), secondEnemy.getName());

        assertEquals(1, firstAlly.getAlliedMatches().size());
        assertTrue(firstAlly.getAlliedMatches().contains(m1));
        assertEquals(1, firstAlly.getAlliedWins());

        assertEquals(1, secondAlly.getAlliedMatches().size());
        assertTrue(secondAlly.getAlliedMatches().contains(m1));
        assertEquals(1, secondAlly.getAlliedWins());

        assertEquals(1, firstEnemy.getEnemyMatches().size());
        assertTrue(firstEnemy.getEnemyMatches().contains(m1));
        assertEquals(1, firstEnemy.getEnemyWins());

        assertEquals(1, secondEnemy.getEnemyMatches().size());
        assertTrue(secondEnemy.getEnemyMatches().contains(m1));
        assertEquals(1, secondEnemy.getEnemyWins());

        assertEquals(firstAlly, firstEnemy);
        assertEquals(secondAlly, secondEnemy);

    }

    public void addUniquePokemon(Pokemon p, MatchHistory mh) {

        m3.setWin();
        m3.addPokemonByName(p.getName(), TeamSelector.USER, mh);
        mh.addMatch(m3);
    }

}
