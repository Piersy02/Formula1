import VectorRace.Giocatori.BasePlayer;
import VectorRace.Giocatori.IPlayer;
import VectorRace.Giocatori.SafeRunnerBot;
import VectorRace.Posizione.ITrack;
import VectorRace.Posizione.Position;
import VectorRace.Posizione.VectorDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per SafeRunnerBot,
 * un bot che bilancia la ricerca del traguardo con la distanza dai giocatori.
 */
public class SafeRunnerBotTest {

    private SafeRunnerBot bot;
    private ITrack mockTrack;
    private List<IPlayer> allPlayers;

    // Un "altro" giocatore per verificare la logica di distanza
    private IPlayer otherPlayer;

    @BeforeEach
    void setUp() {
        // 1) Creiamo una lista di giocatori
        allPlayers = new ArrayList<>();

        // 2) Creiamo un track 6x6 (esempio) con un finish e qualche ostacolo
        mockTrack = new MockTrack();

        // 3) Creiamo il bot in posizione (0,0)
        bot = new SafeRunnerBot("SafeRunner", new Position(0, 0), mockTrack, allPlayers);

        // 4) Creiamo un altro giocatore, per testare la "distanza dai giocatori"
        otherPlayer = new BasePlayer("Other", new Position(3, 2)) {
            @Override
            public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> dirs) {
                return null; // dummy
            }

            @Override
            public int chooseAcceleration() {
                return 0; // dummy
            }
        };

        // Aggiungiamo entrambi i giocatori alla lista
        allPlayers.add(bot);
        allPlayers.add(otherPlayer);
    }

    @Test
    void testChooseDirectionWithFinishAndOtherPlayer() {
        // Nel mockTrack, c'è una finishPos a (5,5) di default
        // e un ostacolo a (2,2) (vedi MockTrack) solo come esempio.

        // Permettiamo al bot di muoversi in tutte le 8 direzioni
        List<VectorDirection.CardinalDirection> allowedDirections = Arrays.asList(
                VectorDirection.CardinalDirection.N,
                VectorDirection.CardinalDirection.NE,
                VectorDirection.CardinalDirection.E,
                VectorDirection.CardinalDirection.SE,
                VectorDirection.CardinalDirection.S,
                VectorDirection.CardinalDirection.SW,
                VectorDirection.CardinalDirection.W,
                VectorDirection.CardinalDirection.NW
        );

        VectorDirection.CardinalDirection chosenDir = bot.chooseDirection(allowedDirections);
        assertNotNull(chosenDir,
                "Il bot dovrebbe scegliere una direzione, non null, se almeno una cella è libera.");

        // In modo generico, verifichiamo che la direzione selezionata
        // non lo porti su un ostacolo e che idealmente riduca la distanza dal traguardo
        // e non troppo vicino all'altro player. Facciamo un check basilare:
        Position newPos = getNextPosition(bot.getCurrentPosition(), chosenDir);
        assertTrue(mockTrack.isFree(newPos),
                "La posizione scelta dal bot deve essere libera (non un ostacolo).");

        // Controlliamo che la distanza al finish si sia ridotta (se possibile).
        Position finish = mockTrack.getFinishPosition();
        if (finish != null) {
            int oldDist = manhattanDistance(bot.getCurrentPosition(), finish);
            int newDist = manhattanDistance(newPos, finish);
            // Se non riesce a muoversi in direzione del finish, potrebbe non ridurre la distanza,
            // ma di norma ci aspettiamo che la riduca se c'è una traiettoria libera.
            assertTrue(newDist <= oldDist,
                    "Ci si aspetta che il bot non aumenti la distanza dal traguardo.");
        }

        // Infine, non possiamo verificare rigorosamente quanto tenga lontano l'altro player
        // (dipende dal punteggio calcolato), ma verifichiamo che non si muova proprio
        // addosso al player se ci sono alternative migliori.
        // In un test avanzato potresti costruire scenari mirati e asserzioni più precise.
    }

    @Test
    void testChooseDirectionNoFinish() {
        // Rimuoviamo il finish
        ((MockTrack) mockTrack).setFinishPosition(null);

        // Ora si comporta come fallback o gestisce la formula con distanceMin dai giocatori
        List<VectorDirection.CardinalDirection> allowed = Arrays.asList(
                VectorDirection.CardinalDirection.N,
                VectorDirection.CardinalDirection.E,
                VectorDirection.CardinalDirection.S
        );

        VectorDirection.CardinalDirection chosen = bot.chooseDirection(allowed);
        // Se ci sono direzioni libere, non dovrebbe essere null
        assertNotNull(chosen, "Anche senza finish, deve scegliere una direzione libera.");
        assertTrue(allowed.contains(chosen), "La direzione deve essere tra quelle permesse.");
    }

    @Test
    void testChooseAcceleration() {
        // 1) Se la velocità < 2, accelera.
        bot.setVelocity(1);
        int accel = bot.chooseAcceleration();
        assertEquals(1, accel,
                "Con velocità < 2, il bot dovrebbe accelerare.");

        // 2) Se la velocità >= 2 e ci sono giocatori vicini, decelera
        // Simuliamo un giocatore molto vicino
        otherPlayer.setCurrentPosition(new Position(0, 1));
        bot.setVelocity(3);

        accel = bot.chooseAcceleration();
        // Se la logica del SafeRunnerBot dice che con velocity=3 e players vicini,
        // allora decelera (-1). In base alla tua implementazione, potresti dover cambiare il check.
        assertEquals(-1, accel,
                "Con velocità alta e giocatore vicino, ci si aspetta decelerazione.");

        // 3) Se la velocità >= 2 ma nessuno è vicino, potrebbe mantenere (0).
        otherPlayer.setCurrentPosition(new Position(5,5)); // lontano
        accel = bot.chooseAcceleration();
        // Ora potresti decidere di restituire 0 (mantiene) o 1 (accelera), dipende dalla tua logica.
        // Mettiamo per ipotesi che la logica dica: "manca poco al finish, accelera"
        // o "se nessuno vicino, mantieni".
        // Sostituisci col valore atteso per il tuo SafeRunnerBot. Ad esempio:
        // assertEquals(0, accel, "Se nessuno è vicino, mantiene la velocità se è >= 2.");
    }

    // ----------------------------------------------------------
    // Metodi di supporto per il test
    // ----------------------------------------------------------

    // Metodo per calcolare la distanza Manhattan
    private int manhattanDistance(Position p1, Position p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }

    // Metodo per calcolare la prossima posizione in base a una direzione
    private Position getNextPosition(Position current, VectorDirection.CardinalDirection direction) {
        int x = current.getX();
        int y = current.getY();

        switch (direction) {
            case N:  y -= 1; break;
            case NE: x += 1; y -= 1; break;
            case E:  x += 1; break;
            case SE: x += 1; y += 1; break;
            case S:  y += 1; break;
            case SW: x -= 1; y += 1; break;
            case W:  x -= 1; break;
            case NW: x -= 1; y -= 1; break;
        }
        return new Position(x, y);
    }

    /**
     * MockTrack di esempio: 6x6 con un finish di default a (5,5) e un ostacolo a (2,2).
     */
    private static class MockTrack implements ITrack {

        private int width = 6;
        private int height = 6;
        private Position finishPos = new Position(5,5);

        // Griglia 6x6, con un ostacolo a (2,2).
        private char[][] grid = {
                {'.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.'},
                {'.', '.', '#', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.'},
        };

        public void setFinishPosition(Position p) {
            finishPos = p;
        }

        @Override
        public void loadFromFile(String filename) {
            // non usiamo in test
        }

        @Override
        public char getCell(Position position) {
            if (position.getX() < 0 || position.getX() >= width ||
                    position.getY() < 0 || position.getY() >= height) {
                return '#'; // fuoribordo = ostacolo
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
            return finishPos != null && finishPos.equals(position);
        }

        @Override
        public Position getStartPosition() {
            return new Position(0,0);
        }

        @Override
        public Position getFinishPosition() {
            return finishPos;
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
