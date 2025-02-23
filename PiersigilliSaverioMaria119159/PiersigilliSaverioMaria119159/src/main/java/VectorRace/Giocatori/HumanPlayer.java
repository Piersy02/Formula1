package VectorRace.Giocatori;

import VectorRace.Posizione.Position;
import VectorRace.Posizione.VectorDirection;

import java.util.Scanner;

/**
 * HumanPlayer consente di gestire l'input da parte di un giocatore umano.
 * - Chiede all'utente di inserire la direzione desiderata tra quelle consentite.
 * - Chiede all'utente di inserire l'accelerazione desiderata (-1, 0, +1).
 */
public class HumanPlayer extends BasePlayer {

    /**
     * Scanner per leggere l'input da console.
     */
    private Scanner scanner = new Scanner(System.in);

    /**
     * Costruttore di HumanPlayer.
     *
     * @param name  Nome del giocatore umano.
     * @param start Posizione di partenza.
     */
    public HumanPlayer(String name, Position start) {
        super(name, start);
    }

    /**
     * Permette di scegliere una direzione digitandone il nome da tastiera.
     * Verifica che la direzione inserita sia effettivamente tra quelle consentite.
     * Se l'input è errato, richiede nuovamente la direzione.
     *
     * @param allowedDirections Insieme delle direzioni ammesse in questo turno.
     * @return La direzione scelta dall’utente, se valida.
     */
    @Override
    public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        System.out.println("Scegli una direzione tra: " + allowedDirections);

        // Legge l'input e lo converte in maiuscolo.
        String input = scanner.nextLine().toUpperCase();

        try {
            // Tenta di convertire l’input in una delle enum cardinali.
            VectorDirection.CardinalDirection chosen = VectorDirection.CardinalDirection.valueOf(input);

            // Verifica se la direzione scelta è effettivamente consentita.
            for (VectorDirection.CardinalDirection dir : allowedDirections) {
                if (dir == chosen) {
                    return chosen;
                }
            }
        } catch (IllegalArgumentException e) {
            // Se l’input non è una direzione valida, ignora e richiede un nuovo inserimento.
        }

        System.out.println("Direzione non valida. Riprova.");
        return chooseDirection(allowedDirections);
    }

    /**
     * Permette di scegliere l'accelerazione inserendo un valore intero (-1, 0 o +1) da tastiera.
     * Se l’input non è valido, ripete la richiesta.
     *
     * @return L'accelerazione desiderata: -1 (decelerazione), 0 (velocità costante) o +1 (accelerazione).
     */
    @Override
    public int chooseAcceleration() {
        System.out.println("Inserisci decisione: -1 per decelerare, 0 per costante, +1 per accelerare");
        int decision = scanner.nextInt();
        scanner.nextLine(); // Consuma l’eventuale newline residuo

        // Controlla validità dell'input.
        if (decision < -1 || decision > 1) {
            System.out.println("Input non valido. Riprova.");
            return chooseAcceleration();
        }

        return decision;
    }
}
