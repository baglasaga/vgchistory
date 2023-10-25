package persistence;

import model.Match;
import model.MatchHistory;
import model.TeamSelector;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

// NOTE: code influenced by the JsonSerializationDemo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            MatchHistory mh = new MatchHistory();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyMatchHistory() {
        try {
            MatchHistory mh = new MatchHistory();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyMatchHistory.json");
            writer.open();
            writer.write(mh);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyMatchHistory.json");
            mh = reader.read();
            assertTrue(mh.getMatches().isEmpty());
            assertTrue(mh.getPokemonList().isEmpty());
            assertEquals(1000, mh.getElo());
            assertEquals(0, mh.getWinRate());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralMatchHistory() {
        try {
            MatchHistory mh = new MatchHistory();
            setupTeams(mh);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralMatchHistory.json");
            writer.open();
            writer.write(mh);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralMatchHistory.json");
            mh = reader.read();
            assertEquals(2, mh.getMatches().size());
            assertEquals(50, mh.getWinRate());
            assertEquals(1001, mh.getElo());
            assertEquals(9, mh.getPokemonList().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    void setupTeams(MatchHistory mh) {
        Match m1 = new Match();
        m1.setWin();
        m1.setEloChange(13);
        m1.addPokemonByName("Ogerpon", TeamSelector.USER, mh);
        m1.addPokemonByName("Tornadus", TeamSelector.USER, mh);
        m1.addPokemonByName("Flutter Mane", TeamSelector.USER, mh);
        m1.addPokemonByName("Munkidori", TeamSelector.USER, mh);

        m1.addPokemonByName("Iron Bundle", TeamSelector.OPPONENT, mh);
        m1.addPokemonByName("Ninetales-Alola", TeamSelector.OPPONENT, mh);
        m1.addPokemonByName("Flutter Mane", TeamSelector.OPPONENT, mh);
        m1.addPokemonByName("Iron Hands", TeamSelector.OPPONENT, mh);
        mh.addMatch(m1);

        Match m2 = new Match();
        m2.setLoss();
        m2.setEloChange(-12);
        m1.addPokemonByName("Rillaboom", TeamSelector.USER, mh);
        m1.addPokemonByName("Milotic", TeamSelector.USER, mh);
        m1.addPokemonByName("Flutter Mane", TeamSelector.USER, mh);
        m1.addPokemonByName("Munkidori", TeamSelector.USER, mh);

        m1.addPokemonByName("Iron Bundle", TeamSelector.OPPONENT, mh);
        m1.addPokemonByName("Ninetales-Alola", TeamSelector.OPPONENT, mh);
        m1.addPokemonByName("Flutter Mane", TeamSelector.OPPONENT, mh);
        m1.addPokemonByName("Iron Hands", TeamSelector.OPPONENT, mh);
        mh.addMatch(m2);
    }
}
