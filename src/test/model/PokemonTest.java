package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PokemonTest {

    private Match m1;
    private Match m2;

    private Pokemon p1;

    @BeforeEach
    public void setup() {
        m1 = new Match();
        m2 = new Match();

        p1 = new Pokemon("Incineroar");
    }

    @Test
    public void testConstructor() {
        assertEquals("Incineroar", p1.getName());

        assertTrue(p1.getAlliedMatches().isEmpty());
        assertTrue(p1.getEnemyMatches().isEmpty());

        assertEquals(0, p1.getAlliedWins());
        assertEquals(0, p1.getEnemyWins());

        assertEquals(0, p1.getAlliedWinRate());
        assertEquals(0, p1.getEnemyWinRate());
    }

    @Test
    public void testAddWinUserTeam() {
        p1.addWin(TeamSelector.USER);
        assertEquals(1, p1.getAlliedWins());
    }

    @Test
    public void testAddWinEnemyTeam() {
        p1.addWin(TeamSelector.OPPONENT);
        assertEquals(1, p1.getEnemyWins());
    }

    @Test
    public void testAddMultipleWinsBothTeams() {
        p1.addWin(TeamSelector.USER);
        p1.addWin(TeamSelector.USER);
        p1.addWin(TeamSelector.OPPONENT);
        p1.addWin(TeamSelector.OPPONENT);

        assertEquals(2, p1.getAlliedWins());
        assertEquals(2, p1.getEnemyWins());
    }

    @Test
    public void testAddOneAllyMatch() {
        p1.addMatch(m1, TeamSelector.USER);

        assertEquals(1, p1.getAlliedMatches().size());
        assertTrue(p1.getAlliedMatches().contains(m1));
    }

    @Test
    public void testAddMultipleMatchesBothTeams() {

        p1.addMatch(m1, TeamSelector.USER);
        p1.addMatch(m2, TeamSelector.USER);
        assertEquals(2, p1.getAlliedMatches().size());
        assertTrue(p1.getAlliedMatches().contains(m1));
        assertTrue(p1.getAlliedMatches().contains(m2));

        p1.addMatch(m1, TeamSelector.OPPONENT);
        p1.addMatch(m2, TeamSelector.OPPONENT);
        assertEquals(2, p1.getEnemyMatches().size());
        assertTrue(p1.getEnemyMatches().contains(m1));
        assertTrue(p1.getEnemyMatches().contains(m2));
    }

    @Test
    public void testUpdateWinRateNoWins() {
        p1.addMatch(m1, TeamSelector.USER);
        p1.addMatch(m1, TeamSelector.OPPONENT);
        p1.updateWinRate(TeamSelector.USER);
        p1.updateWinRate(TeamSelector.OPPONENT);
        assertEquals(0, p1.getAlliedWinRate());
        assertEquals(0, p1.getEnemyWinRate());
    }

    @Test
    public void testUpdateAllyWinRateOneWin() {
        p1.addMatch(m1, TeamSelector.USER);
        p1.addWin(TeamSelector.USER);
        p1.updateWinRate(TeamSelector.USER);
        assertEquals(100.0, p1.getAlliedWinRate());
    }

    @Test
    public void testUpdateEnemyWinRateOneWin() {
        p1.addMatch(m1, TeamSelector.OPPONENT);
        p1.addWin(TeamSelector.OPPONENT);
        p1.updateWinRate(TeamSelector.OPPONENT);
        assertEquals(100.0, p1.getEnemyWinRate());
    }

    @Test
    public void testUpdateWinRateOneWinOneLossEachTeam() {
        p1.addMatch(m1, TeamSelector.USER);
        p1.addMatch(m2, TeamSelector.USER);
        p1.addMatch(m1, TeamSelector.OPPONENT);
        p1.addMatch(m2, TeamSelector.OPPONENT);
        p1.addWin(TeamSelector.USER);
        p1.addWin(TeamSelector.OPPONENT);

        p1.updateWinRate(TeamSelector.USER);
        p1.updateWinRate(TeamSelector.OPPONENT);

        assertEquals(50.0, p1.getAlliedWinRate());
        assertEquals(50.0, p1.getEnemyWinRate());
    }

}
