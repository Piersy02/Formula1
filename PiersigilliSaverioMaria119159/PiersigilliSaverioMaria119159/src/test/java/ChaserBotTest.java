import VectorRace.Giocatori.BasePlayer;
import VectorRace.Giocatori.ChaserBot;
import VectorRace.Giocatori.IPlayer;
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
 * Test per la classe ChaserBot.
 * Verifichiamo che insegua correttamente il giocatore più vicino
 * e che l'accelerazione rispetti la logica programmata.
 */
public class ChaserBotTest {

    private ChaserBot chaser;
    private ITrack mockTrack;
    private List<IPlayer> allPlayers;
    private IPlayer targetPlayer;  // un giocatore che sarà inseguito

    @BeforeEach
    void setUp() {
        // 1) Creiamo una lista di giocatori
        allPlayers = new ArrayList<>();

        // 2) Creiamo un track 5x5 con qualche cella ostacolo e/o libera
        mockTrack = new MockTrack();

        // 3) Creiamo il chaser alla posizione (0,0)
        chaser = new ChaserBot("Chaser", new Position(0, 0), mockTrack, allPlayers);

        // 4) Creiamo un altro player semplice che stia in (2,2),
        //    così che ci sia qualcuno da inseguire.
        targetPlayer = new BasePlayer("Target", new Position(2, 2)) {
            @Override
            public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> dirs) {
                // Un player "dummy", non ci serve la logica
                return null;
            }
            @Override
            public int chooseAcceleration() {
                return 0;
            }
        };

        // 5) Aggiungiamo entrambi i player alla lista
        allPlayers.add(chaser);
        allPlayers.add(targetPlayer);
    }

    @Test
    void testChaserDirectionTowardsTarget() {
        // Simuliamo un set di direzioni consentite ampie (tutte e 8, ad esempio)
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

        // Chiediamo al chaser di scegliere la direzione
        VectorDirection.CardinalDirection dir = chaser.chooseDirection(allowedDirections);

        // Verifichiamo che la direzione (N, NE, E, SE, S, SW, W, NW) effettivamente
        // avvicini (0,0) a (2,2). La direzione "migliore" sarebbe SE,
        // ma potremmo accettare che riduca la distanza in un passo (per es. E o S).
        // In altre parole, la distanza dopo lo spostamento deve essere < 4 (la distanza attuale).
        // Distanza attuale = manhattan( (0,0), (2,2) ) = 4.

        Position nextPos = getNextPosition(chaser.getCurrentPosition(), dir);
        int newDist = manhattanDistance(nextPos, targetPlayer.getCurrentPosition());
        // Ci aspettiamo che newDist < 4 (cioè < distanza iniziale).
        assertTrue(newDist < 4,
                "La direzione scelta dal chaser dovrebbe avvicinarlo al bersaglio.");
    }

    @Test
    void testChaserIgnoresSelf() {
        // Spostiamo "Target" ancora più vicino per vedere se effettivamente ignora se stesso
        targetPlayer.setCurrentPosition(new Position(1,1));

        // Idem come sopra
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

        VectorDirection.CardinalDirection dir = chaser.chooseDirection(allowedDirections);

        // Ancora, verifichiamo che la distanza diminuisca
        Position nextPos = getNextPosition(chaser.getCurrentPosition(), dir);
        int newDist = manhattanDistance(nextPos, targetPlayer.getCurrentPosition());
        assertTrue(newDist < 2,
                "Con il target a (1,1) e chaser a (0,0), ci aspettiamo di ridurre la distanza < 2.");
    }

    @Test
    void testChaserNoOtherPlayers() {
        // Rimuoviamo tutti i giocatori tranne se stesso
        allPlayers.clear();
        allPlayers.add(chaser);

        // Ora è l'unico giocatore
        List<VectorDirection.CardinalDirection> allowedDirections = Arrays.asList(
                VectorDirection.CardinalDirection.N,
                VectorDirection.CardinalDirection.E,
                VectorDirection.CardinalDirection.S
        );

        // Semplicemente non trova un "closestPlayer",
        // quindi sceglie una direzione casuale "sicura".
        VectorDirection.CardinalDirection dir = chaser.chooseDirection(allowedDirections);
        assertNotNull(dir,
                "Se non ci sono altri giocatori, deve scegliere comunque una direzione sicura (randomSafeDirection).");
        assertTrue(allowedDirections.contains(dir),
                "La direzione scelta deve essere tra le allowed se non insegue nessuno.");
    }

    @Test
    void testChaserAccelerationWhenVelocityLow() {
        // Se la velocità <= 1, deve restituire 1 (accelera)
        chaser.setVelocity(1);
        int accel = chaser.chooseAcceleration();
        assertEquals(1, accel,
                "Con velocità <= 1, il ChaserBot dovrebbe accelerare.");
    }

    @Test
    void testChaserAccelerationWhenVelocityHigh() {
        // Se la velocità >= 2, restituisce 1 o -1 (50%).
        chaser.setVelocity(3);
        int accel = chaser.chooseAcceleration();
        assertTrue(accel == 1 || accel == -1,
                "Con velocità >= 2, deve scegliere 1 o -1 con probabilità 50%.");
    }

    // ---------------------------
    // Metodi di supporto
    // ---------------------------

    /**
     * Distanza Manhattan, usata per verificare la riduzione di distanza.
     */
    private int manhattanDistance(Position p1, Position p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }

    /**
     * Simula lo spostamento di una cella da (x,y) in una delle
     * CardinalDirection.
     */
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
     * MockTrack molto minimale (5x5) che gestisce solo un paio di ostacoli
     * e sempre '.' altrove.
     */
    private static class MockTrack implements ITrack {

        private int width = 5;
        private int height = 5;

        // Griglia 5x5 con un ostacolo a (3,1) e (1,2) come esempio
        private char[][] grid = {
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '#', '.'},
                {'.', '#', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'}
        };

        @Override
        public void loadFromFile(String filename) {
            // non necessario per questo test
        }

        @Override
        public char getCell(Position position) {
            int x = position.getX();
            int y = position.getY();
            if (x < 0 || x >= width || y < 0 || y >= height) {
                return '#'; // consideriamo fuori dai limiti come ostacolo
            }
            return grid[y][x];
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
            return false; // non gestiamo finish in questo mock
        }

        @Override
        public Position getStartPosition() {
            return new Position(0,0);
        }

        @Override
        public Position getFinishPosition() {
            return null; // non gestiamo finish
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
