import VectorRace.Giocatori.BasePlayer;
import VectorRace.Posizione.Position;
import VectorRace.Posizione.VectorDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test per la classe BasePlayer, che � astratta.
 * Usiamo una piccola sottoclasse 'FakePlayer' per poterla istanziare.
 */
class TestBasePlayer {

    private FakePlayer player;
    private Position startPosition;

    @BeforeEach
    void setUp() {
        startPosition = new Position(2, 3);
        player = new FakePlayer("TestBot", startPosition);
    }

    @Test
    void testConstructor() {
        // Verifichiamo che il nome sia corretto
        assertEquals("TestBot", player.getName());

        // Verifichiamo che la posizione iniziale sia quella passata
        assertEquals(startPosition, player.getCurrentPosition());

        // Verifichiamo che la velocit� iniziale sia 0
        assertEquals(0, player.getVelocity());
    }

    @Test
    void testSetCurrentPosition() {
        Position newPos = new Position(5, 10);
        player.setCurrentPosition(newPos);
        assertEquals(newPos, player.getCurrentPosition(),
                "La posizione dovrebbe essere aggiornata correttamente.");
    }

    @Test
    void testSetVelocityWithinBounds() {
        // Impostiamo una velocit� valida (es. 2)
        player.setVelocity(2);
        assertEquals(2, player.getVelocity(),
                "Velocit� dovrebbe essere impostata a 2 quando rientra tra 0 e 3.");

        // Impostiamo la velocit� a 3, � al limite massimo
        player.setVelocity(3);
        assertEquals(3, player.getVelocity(),
                "Velocit� dovrebbe essere 3 (limite massimo).");

        // Impostiamo la velocit� a 0, limite minimo
        player.setVelocity(0);
        assertEquals(0, player.getVelocity(),
                "Velocit� dovrebbe essere 0 (limite minimo).");
    }

    @Test
    void testSetVelocityOverMax() {
        // Se imposti un valore > 3, dovrebbe clampare a 3
        player.setVelocity(5);
        assertEquals(3, player.getVelocity(),
                "La velocit� deve essere clampata a 3 se si supera il limite massimo.");
    }

    @Test
    void testSetVelocityBelowMin() {
        // Se imposti un valore < 0, dovrebbe clampare a 0
        player.setVelocity(-2);
        assertEquals(0, player.getVelocity(),
                "La velocit� deve essere clampata a 0 se � negativa.");
    }

    // Classe fittizia per poter testare l'astratta BasePlayer
    private static class FakePlayer extends BasePlayer {
        public FakePlayer(String name, Position start) {
            super(name, start);
        }

        // Implementazioni minime per completare IPlayer
        @Override
        public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
            return null; // non rilevante per il test
        }

        @Override
        public int chooseAcceleration() {
            return 0; // non rilevante per il test
        }
    }
}

