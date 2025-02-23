import VectorRace.Fisica.DefaultInertiaManager;
import VectorRace.Fisica.IInertiaManager;
import VectorRace.Posizione.VectorDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per DefaultInertiaManager.
 */
public class TestDefaultInertiaManager {

    private IInertiaManager inertiaManager;

    @BeforeEach
    void setUp() {
        // Inizializza l'istanza di DefaultInertiaManager
        inertiaManager = new DefaultInertiaManager();
    }

    @Test
    void testAllowedDirectionsVelocity1() {
        // Con velocità <= 1, tutte le direzioni sono consentite
        List<VectorDirection.CardinalDirection> directions = inertiaManager.allowedDirections(1, VectorDirection.CardinalDirection.N);

        assertEquals(8, directions.size(), "Tutte le 8 direzioni dovrebbero essere consentite.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.N), "N dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.S), "S dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.E), "E dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.W), "W dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.NE), "NE dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.SE), "SE dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.SW), "SW dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.NW), "NW dovrebbe essere consentita.");
    }

    @Test
    void testAllowedDirectionsVelocity2() {
        // Con velocità == 2, solo le direzioni entro ±90° rispetto alla precedente sono consentite
        List<VectorDirection.CardinalDirection> directions = inertiaManager.allowedDirections(2, VectorDirection.CardinalDirection.N);

        assertEquals(5, directions.size(), "Solo 5 direzioni entro ±90° dovrebbero essere consentite.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.N), "N dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.NE), "NE dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.NW), "NW dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.E), "E dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.W), "W dovrebbe essere consentita.");

        // Direzioni opposte non consentite
        assertFalse(directions.contains(VectorDirection.CardinalDirection.S), "S non dovrebbe essere consentita.");
        assertFalse(directions.contains(VectorDirection.CardinalDirection.SE), "SE non dovrebbe essere consentita.");
        assertFalse(directions.contains(VectorDirection.CardinalDirection.SW), "SW non dovrebbe essere consentita.");
    }

    @Test
    void testAllowedDirectionsVelocity3() {
        // Con velocità >= 3, solo le direzioni entro ±45° rispetto alla precedente sono consentite
        List<VectorDirection.CardinalDirection> directions = inertiaManager.allowedDirections(3, VectorDirection.CardinalDirection.N);

        assertEquals(3, directions.size(), "Solo 3 direzioni entro ±45° dovrebbero essere consentite.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.N), "N dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.NE), "NE dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.NW), "NW dovrebbe essere consentita.");

        // Direzioni fuori dal range di ±45° non consentite
        assertFalse(directions.contains(VectorDirection.CardinalDirection.E), "E non dovrebbe essere consentita.");
        assertFalse(directions.contains(VectorDirection.CardinalDirection.W), "W non dovrebbe essere consentita.");
        assertFalse(directions.contains(VectorDirection.CardinalDirection.S), "S non dovrebbe essere consentita.");
    }

    @Test
    void testAllowedDirectionsVelocity0() {
        // Con velocità == 0, tutte le direzioni sono consentite (come velocità 1)
        List<VectorDirection.CardinalDirection> directions = inertiaManager.allowedDirections(0, VectorDirection.CardinalDirection.S);

        assertEquals(8, directions.size(), "Tutte le 8 direzioni dovrebbero essere consentite.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.N), "N dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.S), "S dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.E), "E dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.W), "W dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.NE), "NE dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.SE), "SE dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.SW), "SW dovrebbe essere consentita.");
        assertTrue(directions.contains(VectorDirection.CardinalDirection.NW), "NW dovrebbe essere consentita.");
    }
}

