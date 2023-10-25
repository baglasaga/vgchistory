package persistence;

import model.MatchHistory;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// NOTE: code influenced by the JsonSerializationDemo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            MatchHistory mh = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyMatchHistory() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyMatchHistory.json");
        try {
            MatchHistory mh = reader.read();
            assertTrue(mh.getMatches().isEmpty());
            assertTrue(mh.getPokemonList().isEmpty());
            assertEquals(1000, mh.getElo());
            assertEquals(0, mh.getWinRate());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralMatchHistory() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralMatchHistory.json");
        try {
            MatchHistory mh = reader.read();
            assertEquals(2, mh.getMatches().size());
            assertEquals(50, mh.getWinRate());
            assertEquals(1001, mh.getElo());
            assertEquals(9, mh.getPokemonList().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
