import VectorRace.Giocatori.HumanPlayer;
import VectorRace.Posizione.Position;
import VectorRace.Posizione.VectorDirection;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HumanPlayerTest {

    @Test
    void testChooseDirection() {
        // Simuliamo l'input "N" per la direzione
        String simulatedInput = "N\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Creiamo un HumanPlayer e una lista di direzioni consentite
        HumanPlayer player = new HumanPlayer("Player1", new Position(0, 0));
        List<VectorDirection.CardinalDirection> allowedDirections = new ArrayList<>();
        allowedDirections.add(VectorDirection.CardinalDirection.N);
        allowedDirections.add(VectorDirection.CardinalDirection.E);

        // Testiamo la scelta della direzione
        VectorDirection.CardinalDirection chosenDirection = player.chooseDirection(allowedDirections);

        // Verifichiamo che la direzione scelta sia corretta
        assertEquals(VectorDirection.CardinalDirection.N, chosenDirection, "La direzione scelta dovrebbe essere N.");
    }

    @Test
    void testChooseAcceleration() {
        // Simuliamo l'input "1" per l'accelerazione
        String simulatedInput = "1\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Creiamo un HumanPlayer
        HumanPlayer player = new HumanPlayer("Player1", new Position(0, 0));

        // Testiamo la scelta dell'accelerazione
        int chosenAcceleration = player.chooseAcceleration();

        // Verifichiamo che l'accelerazione scelta sia corretta
        assertEquals(1, chosenAcceleration, "L'accelerazione scelta dovrebbe essere +1.");
    }
}
