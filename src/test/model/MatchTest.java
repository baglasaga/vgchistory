package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {

    private MatchHistory mh;
    private Match m1;
    private Match m2;
    private Pokemon p1;
    private Pokemon p2;

    @BeforeEach
    public void setup() {
        mh = new MatchHistory();

        m1 = new Match();
        m2 = new Match();

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

        m1.addPokemon(p1, TeamSelector.USER);
        m1.addPokemon(p2, TeamSelector.USER);

        m1.addPokemon(p1, TeamSelector.OPPONENT);
        m1.addPokemon(p2, TeamSelector.OPPONENT);

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
        mh.addUniquePokemon(p1);
        mh.addUniquePokemon(p2);

        m1.setWin();
        m1.addPokemonByName("Flutter Mane", TeamSelector.USER, mh);
        assertEquals(1, m1.getMyTeam().size());
        assertTrue(m1.getMyTeam().contains(p1));

        m1.addPokemonByName("Iron Hands", TeamSelector.OPPONENT, mh);
        assertEquals(1, m1.getEnemyTeam().size());
        assertTrue(m1.getEnemyTeam().contains(p2));
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

        m1.addPokemon(p1, TeamSelector.USER);
        assertEquals(1, m1.getMyTeam().size());
        assertTrue(m1.getMyTeam().contains(p1));

        assertEquals(1, p1.getAlliedMatches().size());
        assertTrue(p1.getAlliedMatches().contains(m1));
        assertEquals(0, p1.getAlliedWins());

        assertEquals(0, p1.getEnemyMatches().size());
        assertEquals(0, p1.getEnemyWins());

    }

    @Test
    public void testAddOnePokemonEachTeamToLostMatch() {
        m1.setLoss();

        m1.addPokemon(p1, TeamSelector.USER);
        m1.addPokemon(p2, TeamSelector.OPPONENT);

        assertEquals(1, m1.getMyTeam().size());
        assertTrue(m1.getMyTeam().contains(p1));

        assertEquals(1, m1.getEnemyTeam().size());
        assertTrue(m1.getEnemyTeam().contains(p2));

        assertEquals(1, p1.getAlliedMatches().size());
        assertTrue(p1.getAlliedMatches().contains(m1));
        assertEquals(0, p1.getAlliedWins());

        assertEquals(1, p2.getEnemyMatches().size());
        assertTrue(p2.getEnemyMatches().contains(m1));
        assertEquals(0, p2.getEnemyWins());
    }

    @Test
    public void testAddOneAllyPokemonToWonMatch() {
        m1.setWin();

        m1.addPokemon(p1, TeamSelector.USER);
        assertEquals(1, m1.getMyTeam().size());
        assertTrue(m1.getMyTeam().contains(p1));

        assertEquals(1, p1.getAlliedMatches().size());
        assertTrue(p1.getAlliedMatches().contains(m1));
        assertEquals(1, p1.getAlliedWins());

        assertEquals(0, p1.getEnemyMatches().size());
        assertEquals(0, p1.getEnemyWins());
    }

    @Test
    public void testAddOnePokemonEachTeamToWonMatch() {
        m1.setWin();

        m1.addPokemon(p1, TeamSelector.USER);
        m1.addPokemon(p2, TeamSelector.OPPONENT);

        assertEquals(1, m1.getMyTeam().size());
        assertTrue(m1.getMyTeam().contains(p1));

        assertEquals(1, m1.getEnemyTeam().size());
        assertTrue(m1.getEnemyTeam().contains(p2));

        assertEquals(1, p1.getAlliedMatches().size());
        assertTrue(p1.getAlliedMatches().contains(m1));
        assertEquals(1, p1.getAlliedWins());

        assertEquals(1, p2.getEnemyMatches().size());
        assertTrue(p2.getEnemyMatches().contains(m1));
        assertEquals(1, p2.getEnemyWins());
    }

    @Test
    public void testAddMultiplePokemonEachTeamToLostMatch() {
        m1.setLoss();

        m1.addPokemon(p1, TeamSelector.USER);
        m1.addPokemon(p2, TeamSelector.USER);

        assertEquals(2, m1.getMyTeam().size());
        assertTrue(m1.getMyTeam().contains(p1));
        assertTrue(m1.getMyTeam().contains(p2));

        m1.addPokemon(p1, TeamSelector.OPPONENT);
        m1.addPokemon(p2, TeamSelector.OPPONENT);

        assertEquals(2, m1.getMyTeam().size());
        assertTrue(m1.getEnemyTeam().contains(p1));
        assertTrue(m1.getEnemyTeam().contains(p2));

        assertEquals(1, p1.getAlliedMatches().size());
        assertTrue(p1.getAlliedMatches().contains(m1));
        assertEquals(0, p1.getAlliedWins());

        assertEquals(1, p2.getAlliedMatches().size());
        assertTrue(p2.getAlliedMatches().contains(m1));
        assertEquals(0, p2.getAlliedWins());

        assertEquals(1, p1.getEnemyMatches().size());
        assertTrue(p1.getEnemyMatches().contains(m1));
        assertEquals(0, p1.getEnemyWins());

        assertEquals(1, p2.getEnemyMatches().size());
        assertTrue(p2.getEnemyMatches().contains(m1));
        assertEquals(0, p2.getEnemyWins());

    }

    @Test
    public void testAddMultiplePokemonEachTeamToWonMatch() {
        m1.setWin();

        m1.addPokemon(p1, TeamSelector.USER);
        m1.addPokemon(p2, TeamSelector.USER);

        assertEquals(2, m1.getMyTeam().size());
        assertTrue(m1.getMyTeam().contains(p1));
        assertTrue(m1.getMyTeam().contains(p2));

        m1.addPokemon(p1, TeamSelector.OPPONENT);
        m1.addPokemon(p2, TeamSelector.OPPONENT);

        assertEquals(2, m1.getMyTeam().size());
        assertTrue(m1.getEnemyTeam().contains(p1));
        assertTrue(m1.getEnemyTeam().contains(p2));

        assertEquals(1, p1.getAlliedMatches().size());
        assertTrue(p1.getAlliedMatches().contains(m1));
        assertEquals(1, p1.getAlliedWins());

        assertEquals(1, p2.getAlliedMatches().size());
        assertTrue(p2.getAlliedMatches().contains(m1));
        assertEquals(1, p2.getAlliedWins());

        assertEquals(1, p1.getEnemyMatches().size());
        assertTrue(p1.getEnemyMatches().contains(m1));
        assertEquals(1, p1.getEnemyWins());

        assertEquals(1, p2.getEnemyMatches().size());
        assertTrue(p2.getEnemyMatches().contains(m1));
        assertEquals(1, p2.getEnemyWins());

    }

}
