import VectorRace.Posizione.Position;
import VectorRace.Posizione.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per la classe Track,
 * che carica un tracciato da file e gestisce start, finish, ostacoli, ecc.
 */
class TestTrack {

    private Track track;

    @BeforeEach
    void setUp() {
        track = new Track();
    }

    @Test
    void testLoadFromFile() throws IOException {
        // Carichiamo il file di test (test_map.txt) presente in src/test/resources
        track.loadFromFile("test_map.txt");

        // Verifichiamo dimensioni: in base al file di esempio .#S / F.. / ..#
        // Larghezza massima = 3 (colonne), Altezza = 3 (righe)
        assertEquals(3, track.getWidth(),
                "La larghezza dovrebbe essere 3 in base al file test_map.txt");
        assertEquals(3, track.getHeight(),
                "L'altezza dovrebbe essere 3 in base al file test_map.txt");

        // Verifichiamo le posizioni di start e finish
        // Nel file di esempio, 'S' è a (2,0) e 'F' è a (0,1).
        Position startPos = track.getStartPosition();
        assertNotNull(startPos, "Dovrebbe esistere almeno una posizione di start");
        assertEquals(new Position(2, 0), startPos,
                "La posizione di start 'S' dovrebbe essere (2,0)");

        Position finishPos = track.getFinishPosition();
        assertNotNull(finishPos, "Dovrebbe esistere almeno una posizione di finish");
        assertEquals(new Position(0, 1), finishPos,
                "La posizione di finish 'F' dovrebbe essere (0,1)");

        // Verifichiamo la presenza di ostacoli
        // Nel file di esempio, c'è '#' a (1,0) e (2,2).
        assertTrue(track.isObstacle(new Position(1, 0)),
                "Dovrebbe esserci un ostacolo a (1,0)");
        assertTrue(track.isObstacle(new Position(2, 2)),
                "Dovrebbe esserci un ostacolo a (2,2)");

        // Verifichiamo che alcune celle siano libere
        // (0,0) = '.' e (1,1) = '.' e (0,2) = '.' nel nostro file di esempio
        assertTrue(track.isFree(new Position(0, 0)), "Cell (0,0) dovrebbe essere libera");
        assertTrue(track.isFree(new Position(1, 1)), "Cell (1,1) dovrebbe essere libera");
        assertTrue(track.isFree(new Position(0, 2)), "Cell (0,2) dovrebbe essere libera");

        // Verifichiamo che isFinish(0,1) = true
        assertTrue(track.isFinish(new Position(0,1)),
                "Position (0,1) dovrebbe essere un finish");
    }

    @Test
    void testLoadFromFileNotFound() {
        // Verifichiamo il comportamento quando il file non esiste
        assertThrows(IOException.class, () -> {
            track.loadFromFile("non_existent_file.txt");
        }, "Caricare un file inesistente dovrebbe generare IOException");
    }

    @Test
    void testOutOfBounds() throws IOException {
        // Carichiamo comunque il file di test
        track.loadFromFile("test_map.txt");

        // (x,y) al di fuori dei limiti: ad es. (10,10)
        // getCell(...) dovrebbe restituire '#' (ostacolo) fuori dai limiti
        assertEquals('#', track.getCell(new Position(10, 10)),
                "Fuori dai limiti, getCell dovrebbe restituire '#' come ostacolo");
        assertTrue(track.isObstacle(new Position(10, 10)),
                "Fuori dai limiti, isObstacle deve essere true");
        assertFalse(track.isFree(new Position(10, 10)),
                "Fuori dai limiti, non dovrebbe risultare libero");
    }

    @Test
    void testMultipleStartFinishPositions() throws IOException {
        // Se vuoi testare che la classe gestisce più start e finish,
        // puoi caricare un file con più 'S' e 'F'.
        // Oppure puoi modificare test_map.txt di conseguenza.
        // Qui mostriamo un esempio ipotetico.

        track.loadFromFile("multi_sf_map.txt");

        // Verifichiamo che ci siano più posizioni di start
        List<Position> starts = track.getAllStartPositions();
        assertTrue(starts.size() > 1, "Ci aspettiamo più di una posizione di start");

        // Verifichiamo che ci siano più posizioni di finish
        List<Position> finishes = track.getAllFinishPositions();
        assertTrue(finishes.size() > 1, "Ci aspettiamo più di una posizione di finish");
    }
}

