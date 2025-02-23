import VectorRace.Fisica.SimpleVelocityCalculator;
import VectorRace.Posizione.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestSimpleVelocityCalculator {

    @Test
    void testVelocity() {
        SimpleVelocityCalculator calculator = new SimpleVelocityCalculator();

        // Caso base: distanza lungo l'asse X
        assertEquals(2, calculator.velocity(new Position(0, 0), new Position(2, 0)),
                "La distanza lungo X dovrebbe essere 2.");

        // Caso base: distanza lungo l'asse Y
        assertEquals(3, calculator.velocity(new Position(0, 0), new Position(0, 3)),
                "La distanza lungo Y dovrebbe essere 3.");

        // Distanza diagonale con valori entro il massimo
        assertEquals(3, calculator.velocity(new Position(0, 0), new Position(3, 3)),
                "La distanza diagonale dovrebbe essere 3.");

        // Stesso punto
        assertEquals(0, calculator.velocity(new Position(1, 1), new Position(1, 1)),
                "La distanza tra lo stesso punto dovrebbe essere 0.");

        // Coordinate negative lungo X e Y
        assertEquals(3, calculator.velocity(new Position(-3, -2), new Position(0, -2)),
                "La distanza lungo X con coordinate negative dovrebbe essere 3.");

        assertEquals(3, calculator.velocity(new Position(-1, -3), new Position(-1, 0)),
                "La distanza lungo Y con coordinate negative dovrebbe essere 3.");

        // Distanza diagonale con valori misti (positivi e negativi)
        assertEquals(3, calculator.velocity(new Position(-2, -2), new Position(1, 1)),
                "La distanza diagonale con valori misti dovrebbe essere 3.");
    }
}

