import VectorRace.Giocatori.GreedyBot;
import VectorRace.Posizione.ITrack;
import VectorRace.Posizione.Position;
import VectorRace.Posizione.VectorDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test della classe GreedyBot.
 * Verifichiamo la scelta della direzione con o senza traguardo,
 * e la scelta di accelerazione (sempre 1).
 */
public class GreedyBotTest {

    private GreedyBot bot;
    private MockTrack track;

    @BeforeEach
    void setUp() {
        // Creiamo un tracciato mock di 5x5 con un finish e alcune celle libere/ostacolo
        track = new MockTrack();
        // Bot parte da (0, 0)
        bot = new GreedyBot("GreedyTest", new Position(0, 0), track);
    }

    @Test
    void testChooseDirectionWithoutFinish() {
        // Rimuoviamo la posizione di finish per simulare "finish == null"
        track.setFinishPosition(null);

        // Creiamo una lista di direzioni consentite
        // (per semplificare, supponiamo che siano tutte 8).
        List<VectorDirection.CardinalDirection> allowed = Arrays.asList(
                VectorDirection.CardinalDirection.N,
                VectorDirection.CardinalDirection.NE,
                VectorDirection.CardinalDirection.E,
                VectorDirection.CardinalDirection.SE,
                VectorDirection.CardinalDirection.S,
                VectorDirection.CardinalDirection.SW,
                VectorDirection.CardinalDirection.W,
                VectorDirection.CardinalDirection.NW
        );

        // Chiediamo al bot di scegliere
        VectorDirection.CardinalDirection chosen = bot.chooseDirection(allowed);

        // Controlliamo che non sia null (se c'è almeno una direzione “safe”).
        // Nel nostro mock, molte posizioni saranno libere, quindi ci aspettiamo
        // che ci sia almeno una direzione sicura.
        assertNotNull(chosen,
                "Dovrebbe scegliere una direzione sicura (non null) in assenza di traguardo.");
        assertTrue(allowed.contains(chosen),
                "La direzione scelta deve essere tra quelle permesse.");
    }

    @Test
    void testChooseDirectionWithFinish() {
        // Assicuriamoci che il finish sia definito
        track.setFinishPosition(new Position(4, 4));

        // Consentiamo tutte le 8 direzioni
        List<VectorDirection.CardinalDirection> allowed = Arrays.asList(
                VectorDirection.CardinalDirection.N,
                VectorDirection.CardinalDirection.NE,
                VectorDirection.CardinalDirection.E,
                VectorDirection.CardinalDirection.SE,
                VectorDirection.CardinalDirection.S,
                VectorDirection.CardinalDirection.SW,
                VectorDirection.CardinalDirection.W,
                VectorDirection.CardinalDirection.NW
        );

        // Chiediamo al bot di scegliere
        VectorDirection.CardinalDirection chosen = bot.chooseDirection(allowed);

        // Deve sempre essere una direzione permessa e non null
        assertNotNull(chosen, "Con un finish presente, deve comunque scegliere una direzione.");
        assertTrue(allowed.contains(chosen),
                "La direzione scelta deve essere tra quelle permesse.");
    }

    @Test
    void testChooseAcceleration() {
        // GreedyBot accelera sempre (restituisce 1)
        int accel = bot.chooseAcceleration();
        assertEquals(1, accel,
                "GreedyBot dovrebbe sempre restituire 1 come accelerazione.");
    }

    /**
     * MockTrack è un’implementazione minimale di ITrack
     * per testare il GreedyBot senza caricare file reali.
     * - Dimensioni 5x5
     * - Posizioniamo alcune celle come ostacolo per testare la logica isFree()
     * - Gestiamo una posizione di finish che può essere abilitata o disabilitata.
     */
    private static class MockTrack implements ITrack {

        private int width = 5;
        private int height = 5;
        private Position finishPos = new Position(4, 4);  // di default
        private char[][] grid = {
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '#', '.'},
                {'.', '#', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'}
        };

        public void setFinishPosition(Position p) {
            finishPos = p;
        }

        @Override
        public void loadFromFile(String filename) {
            // non implementato nel mock
        }

        @Override
        public char getCell(Position position) {
            if (position.getX() < 0 || position.getX() >= width ||
                    position.getY() < 0 || position.getY() >= height) {
                // fuori dai limiti consideriamo '#'
                return '#';
            }
            return grid[position.getY()][position.getX()];
        }

        @Override
        public boolean isFree(Position position) {
            return getCell(position) == '.';
        }

        @Override
        public boolean isObstacle(Position position) {
            return getCell(position) == '#';
        }

        @Override
        public boolean isFinish(Position position) {
            // vero se position == finishPos
            return finishPos != null && finishPos.equals(position);
        }

        @Override
        public Position getStartPosition() {
            // non usato in questo test
            return new Position(0, 0);
        }

        @Override
        public Position getFinishPosition() {
            return finishPos; // può essere null se disabilitato
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }
    }
}

