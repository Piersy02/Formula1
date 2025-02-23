import VectorRace.Fisica.IInertiaManager;
import VectorRace.Fisica.IVelocityCalculator;
import VectorRace.Giocatori.BasePlayer;
import VectorRace.Giocatori.IPlayer;
import VectorRace.Posizione.GameBoard;
import VectorRace.Motore.GameEngine;
import VectorRace.Posizione.ITrack;
import VectorRace.Posizione.Position;
import VectorRace.Posizione.VectorDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestGameEngine {

    private GameEngine gameEngine;
    private MockGameBoard mockBoard;
    private IVelocityCalculator mockVelocityCalculator;
    private IInertiaManager mockInertiaManager;

    @BeforeEach
    void setUp() {
        // Mock GameBoard
        mockBoard = new MockGameBoard();

        // Mock VelocityCalculator
        mockVelocityCalculator = (pos1, pos2) -> 1; // Velocità fittizia costante

        // Mock InertiaManager
        mockInertiaManager = (velocity, previousDirection) -> {
            List<VectorDirection.CardinalDirection> directions = new ArrayList<>();
            Collections.addAll(directions, VectorDirection.CardinalDirection.values());
            return directions;
        };

        // Crea GameEngine con 5 turni massimi
        gameEngine = new GameEngine(mockBoard, mockVelocityCalculator, mockInertiaManager, 5);
    }

    @Test
    void testAddPlayer() {
        IPlayer player = new MockPlayer("Player1");

        gameEngine.addPlayer(player);

        // Verifica che il giocatore sia stato aggiunto correttamente
        assertEquals(1, mockBoard.getPlayerCount(), "Il giocatore dovrebbe essere aggiunto alla board.");
        assertEquals(new Position(0, 0), player.getCurrentPosition(), "Il giocatore dovrebbe partire da (0,0).");
    }

    @Test
    void testStartRace() {
        IPlayer player = new MockPlayer("Player1");
        gameEngine.addPlayer(player);

        assertDoesNotThrow(() -> gameEngine.startRace(), "La corsa non dovrebbe generare eccezioni.");
    }

    @Test
    void testProcessTurn() {
        IPlayer player = new MockPlayer("Player1");
        gameEngine.addPlayer(player);

        gameEngine.startRace();

        // Verifica che il giocatore sia ancora presente dopo un turno
        assertEquals(1, mockBoard.getPlayerCount(), "Il giocatore dovrebbe essere ancora presente.");
    }

    @Test
    void testHandleCollisionWithOtherPlayer() {
        IPlayer player1 = new MockPlayer("Player1");
        IPlayer player2 = new MockPlayer("Player2");

        gameEngine.addPlayer(player1);
        gameEngine.addPlayer(player2);

        // Entrambi i giocatori nello stesso punto
        player1.setCurrentPosition(new Position(1, 1));
        player2.setCurrentPosition(new Position(1, 1));

        gameEngine.startRace();

        // Verifica che entrambi i giocatori siano ancora presenti, ma abbiano "saltato" il turno
        assertEquals(2, mockBoard.getPlayerCount(), "Entrambi i giocatori dovrebbero essere presenti.");
    }

    @Test
    void testCalculateNewPosition() {
        Position current = new Position(2, 2);
        VectorDirection.CardinalDirection direction = VectorDirection.CardinalDirection.NE;
        int velocity = 2;

        Position expected = new Position(4, 0);
        Position result = gameEngine.calculateNewPosition(current, direction, velocity);

        assertEquals(expected, result, "La posizione calcolata non è corretta.");
    }

    // Mock classes for testing
    private static class MockGameBoard extends GameBoard {
        private int playerCount = 0;
        private Set<Position> obstacles = new HashSet<>();

        public MockGameBoard() {
            super(new MockTrack());
        }

        public void addObstacle(Position pos) {
            obstacles.add(pos);
        }

        @Override
        public boolean isObstacle(Position pos) {
            return obstacles.contains(pos);
        }

        @Override
        public void addPlayer(IPlayer player) {
            super.addPlayer(player);
            playerCount++;
        }

        public int getPlayerCount() {
            return playerCount;
        }

        @Override
        public void updatePlayerPosition(IPlayer player, Position newPos) {
            super.updatePlayerPosition(player, newPos);
        }
    }

    private static class MockTrack implements ITrack {
        private int width = 5;
        private int height = 5;

        @Override
        public void loadFromFile(String filename) {}

        @Override
        public char getCell(Position position) {
            return '.';
        }

        @Override
        public boolean isFree(Position position) {
            return true;
        }

        @Override
        public boolean isObstacle(Position position) {
            return false;
        }

        @Override
        public boolean isFinish(Position position) {
            return position.equals(new Position(4, 4));
        }

        @Override
        public Position getStartPosition() {
            return new Position(0, 0);
        }

        @Override
        public Position getFinishPosition() {
            return new Position(4, 4);
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

    private static class MockPlayer extends BasePlayer {
        public MockPlayer(String name) {
            super(name, new Position(0, 0));
        }

        @Override
        public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> dirs) {
            return VectorDirection.CardinalDirection.E;
        }

        @Override
        public int chooseAcceleration() {
            return 1;
        }
    }
}

