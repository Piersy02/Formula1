
import VectorRace.Posizione.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la classe Position, che rappresenta una coordinata (x, y).
 */
class TestPosition {

    @Test
    void testConstructorAndGetters() {
        Position p = new Position(3, 5);
        assertEquals(3, p.getX(), "X dovrebbe essere 3");
        assertEquals(5, p.getY(), "Y dovrebbe essere 5");
    }

    @Test
    void testSetters() {
        Position p = new Position(0, 0);
        p.setX(10);
        p.setY(20);
        assertEquals(10, p.getX(), "Dopo setX(10), X dovrebbe essere 10");
        assertEquals(20, p.getY(), "Dopo setY(20), Y dovrebbe essere 20");
    }

    @Test
    void testEqualsSameCoordinates() {
        Position p1 = new Position(2, 2);
        Position p2 = new Position(2, 2);

        // Due posizioni con le stesse coordinate dovrebbero essere uguali
        assertEquals(p1, p2, "Due Position(2,2) dovrebbero essere uguali");
        assertEquals(p1.hashCode(), p2.hashCode(),
                "Due oggetti uguali devono avere lo stesso hashCode");
    }

    @Test
    void testNotEqualsDifferentCoordinates() {
        Position p1 = new Position(1, 2);
        Position p2 = new Position(2, 1);

        // Coordinate differenti -> not equals
        assertNotEquals(p1, p2,
                "Position(1,2) non dovrebbe essere uguale a Position(2,1)");
    }

    @Test
    void testNotEqualsDifferentType() {
        Position p = new Position(0, 0);
        String otherType = "Not a Position";

        // equals deve restituire false se l'oggetto non è di tipo Position
        assertNotEquals(p, otherType,
                "Un Position non dovrebbe essere uguale a un oggetto di tipo diverso");
    }

    @Test
    void testEqualsItself() {
        Position p = new Position(3, 4);

        // Un oggetto deve essere uguale a se stesso
        assertEquals(p, p, "Un oggetto deve essere uguale a se stesso");
    }
}

