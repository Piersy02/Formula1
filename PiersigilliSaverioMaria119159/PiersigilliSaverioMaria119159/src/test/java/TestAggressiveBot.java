import VectorRace.Giocatori.AggressiveBot;
import VectorRace.Posizione.Position;
import VectorRace.Posizione.VectorDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestAggressiveBot {

    private AggressiveBot bot;

    @BeforeEach
    void setUp() {
        // Inizializza il bot a ogni test
        bot = new AggressiveBot("TestAggressive", new Position(0, 0));
    }

    /**
     * Verifica che la direzione scelta dal bot sia sempre
     * tra quelle "allowedDirections" fornite.
     */
    @Test
    void testChooseDirection() {
        // Simuliamo un insieme di direzioni consentite (tutte le 8 direzioni)
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

        // Invoca chooseDirection
        VectorDirection.CardinalDirection chosen = bot.chooseDirection(allowed);

        // Controlla che la direzione scelta sia effettivamente tra quelle consentite
        assertTrue(allowed.contains(chosen),
                "La direzione scelta non appartiene a quelle consentite");
    }

    /**
     * Verifica che se la velocità è < 2, il bot acceleri sempre (restituisce 1).
     */
    @Test
    void testChooseAccelerationLowVelocity() {
        // Imposta la velocità del bot a 1
        bot.setVelocity(1);

        // Se la velocità è < 2, deve restituire 1 (accelerazione)
        int accel = bot.chooseAcceleration();
        assertEquals(1, accel,
                "Per velocità < 2, AggressiveBot dovrebbe accelerare di 1");
    }

    /**
     * Verifica la logica quando la velocità è >= 2.
     * - Con probabilità 70% restituisce 1, con 30% restituisce -1.
     * Per un singolo test, controlliamo solo che ritorni 1 o -1, non altri valori.
     *
     * Usiamo @RepeatedTest per chiamarlo più volte e avere un campione.
     * (È un test statistico, quindi se vuoi testare in modo deterministico,
     * dovresti "mockare" o fissare un seed per la classe Random.)
     */
    @RepeatedTest(5)
    void testChooseAccelerationHighVelocity() {
        // Impostiamo la velocità del bot a 3
        bot.setVelocity(3);

        int accel = bot.chooseAcceleration();

        // Dovrebbe essere o 1 (accelera) o -1 (decelera)
        assertTrue(accel == 1 || accel == -1,
                "Per velocità >= 2, AggressiveBot deve restituire 1 o -1");
    }

    /**
     * Esempio di test su chooseDirection con un sottoinsieme di direzioni consentite.
     * Qui simuliamo un caso in cui sono ammesse solo 3 direzioni.
     */
    @Test
    void testChooseDirectionSubset() {
        List<VectorDirection.CardinalDirection> allowedSubset = new ArrayList<>();
        allowedSubset.add(VectorDirection.CardinalDirection.E);
        allowedSubset.add(VectorDirection.CardinalDirection.SE);
        allowedSubset.add(VectorDirection.CardinalDirection.S);

        VectorDirection.CardinalDirection chosen = bot.chooseDirection(allowedSubset);

        // Deve essere una di E, SE o S.
        assertTrue(allowedSubset.contains(chosen),
                "La direzione scelta deve appartenere al subset consentito");
    }
}
