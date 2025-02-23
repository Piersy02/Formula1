import VectorRace.Giocatori.BasePlayer;
import VectorRace.Giocatori.IPlayer;
import VectorRace.Posizione.GameBoard;
import VectorRace.Posizione.ITrack;
import VectorRace.Posizione.Position;
import VectorRace.Posizione.VectorDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per GameBoard.
 * Verifica le funzionalità principali:
 * check posizioni libere/ostacoli, aggiornamento posizioni, display, ecc.
 */
class TestGameBoard {

    private GameBoard gameBoard;
    private ITrack mockTrack;
    private List<IPlayer> players;
    private Map<IPlayer, VectorDirection.CardinalDirection> previousDirections;

    @BeforeEach
    void setUp() {
        // Creiamo la track fittizia
        mockTrack = new MockTrack();

        // Creiamo la GameBoard con la track
        gameBoard = new GameBoard(mockTrack);

        // Lista di giocatori e direzioni precedenti
        players = new ArrayList<>();
        previousDirections = new HashMap<>();
    }


    @Test
    void testIsFreeIsObstacleIsFinish() {
        // In MockTrack:
        //  - (0,0) e (1,0) sono start; (2,2) è finish;
        //  - (1,1) e (3,3) sono ostacoli (#) come esempio
        //  - Tutto il resto è '.'

        // isFree
        assertTrue(gameBoard.isFree(new Position(0, 1)),
                "Cell (0,1) dovrebbe essere libera.");
        // isObstacle
        assertTrue(gameBoard.isObstacle(new Position(1, 1)),
                "(1,1) dovrebbe essere un ostacolo.");
        // isFinish
        assertTrue(gameBoard.isFinish(new Position(2, 2)),
                "(2,2) dovrebbe essere un finish.");
    }

    @Test
    void testUpdatePlayerPosition() {
        // Creiamo un giocatore e lo aggiungiamo
        IPlayer player = new FakePlayer("Mover");
        gameBoard.addPlayer(player);
        // Posizione iniziale = (0,0)

        // Aggiorniamo la posizione a (2,3)
        Position newPos = new Position(2, 3);
        gameBoard.updatePlayerPosition(player, newPos);
        // Verifichiamo
        assertEquals(newPos, player.getCurrentPosition(),
                "La posizione del giocatore dovrebbe essere aggiornata a (2,3).");
        // isOccupied(2,3) == true
        // isOccupied(0,0) == false
    }

    @Test
    void testDisplay() {
        // Aggiungiamo un paio di giocatori
        IPlayer p1 = new FakePlayer("P1");
        IPlayer p2 = new FakePlayer("P2");
        gameBoard.addPlayer(p1);
        gameBoard.addPlayer(p2);

        // Assegniamo direzioni precedenti fittizie
        previousDirections.put(p1, VectorDirection.CardinalDirection.E);
        previousDirections.put(p2, VectorDirection.CardinalDirection.N);

        // Chiamiamo display
        assertDoesNotThrow(() -> {
            gameBoard.display(players, previousDirections);
        }, "display() non dovrebbe sollevare eccezioni.");
    }

    // -----------------------------------------------------
    // Classe interna fittizia per simulare la Track
    // -----------------------------------------------------
    private static class MockTrack implements ITrack {

        private int width = 5;
        private int height = 5;

        private char[][] grid = {
                // 0: start(0,0), start(1,0)
                // 1: ostacolo(1,1)
                // 2: finish(2,2)
                // 3: ostacolo(3,3)
                // Tutto il resto '.'
                { 'S', 'S', '.', '.', '.' },  // y=0
                { '.', '#', '.', '.', '.' },  // y=1
                { '.', '.', 'F', '.', '.' },  // y=2
                { '.', '.', '.', '#', '.' },  // y=3
                { '.', '.', '.', '.', '.' }   // y=4
        };

        @Override
        public void loadFromFile(String filename) {
            // non necessario in un mock
        }

        @Override
        public char getCell(Position position) {
            int x = position.getX();
            int y = position.getY();
            if (x < 0 || x >= width || y < 0 || y >= height) {
                return '#'; // Fuori dai limiti = ostacolo
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
            return getCell(position) == 'F';
        }

        @Override
        public Position getStartPosition() {
            // Ritorna la prima start trovata
            // (0,0) nel nostro grid
            return new Position(0, 0);
        }

        @Override
        public Position getFinishPosition() {
            // Ritorna la prima finish: (2,2)
            return new Position(2, 2);
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        // Metodi specifici di Track da cui otteniamo "all starts" e "all finishes"
        public List<Position> getAllStartPositions() {
            // (0,0) e (1,0)
            List<Position> starts = new ArrayList<>();
            starts.add(new Position(0,0));
            starts.add(new Position(1,0));
            return starts;
        }

        public List<Position> getAllFinishPositions() {
            // (2,2)
            List<Position> finishes = new ArrayList<>();
            finishes.add(new Position(2,2));
            return finishes;
        }
    }

    // -----------------------------------------------------
    // Classe interna fittizia per simulare un IPlayer
    // -----------------------------------------------------
    private static class FakePlayer extends BasePlayer {
        public FakePlayer(String name) {
            super(name, new Position(0,0));
        }
        @Override
        public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> dirs) {
            return null;
        }
        @Override
        public int chooseAcceleration() {
            return 0;
        }
    }
}
