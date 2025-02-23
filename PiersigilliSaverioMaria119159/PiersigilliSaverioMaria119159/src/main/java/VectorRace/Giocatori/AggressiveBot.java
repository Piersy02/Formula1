package VectorRace.Giocatori;

import VectorRace.Posizione.Position;
import VectorRace.Posizione.VectorDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * La classe AggressiveBot estende BasePlayer e rappresenta
 * un giocatore "aggressivo" all'interno del gioco VectorRace.
 * - Sceglie una direzione casuale fra quelle disponibili.
 * - Accelera fino a raggiungere una determinata velocità (soglia = 3).
 */
public class AggressiveBot extends BasePlayer {

    // Istanza di Random usata per selezionare le direzioni in modo aleatorio.
    private Random random = new Random();

    /**
     * Costruttore di AggressiveBot.
     * @param name Nome del bot.
     * @param start Posizione di partenza del bot.
     */
    public AggressiveBot(String name, Position start) {
        // Richiama il costruttore della superclasse (BasePlayer).
        super(name, start);
    }

    /**
     * Sceglie una direzione in modo casuale tra quelle permesse.
     * @param allowedDirections Le direzioni consentite in questo turno.
     * @return Una delle direzioni ammesse, selezionata casualmente.
     */
    @Override
    public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        // Copia tutte le direzioni ammesse in una lista.
        List<VectorDirection.CardinalDirection> options = new ArrayList<>();
        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            options.add(dir);
        }
        // Seleziona in modo casuale un indice tra gli elementi della lista.
        return options.get(random.nextInt(options.size()));
    }

    /**
     * Sceglie l'accelerazione con una logica più elaborata:
     * - Se la velocità < 2, accelera di 1.
     * - Se la velocità >= 2, con probabilità del 70% accelera di 1,
     *   altrimenti decelera di 1 (comportamento aggressivo ma non sempre al massimo).
     */
    @Override
    public int chooseAcceleration() {
        int currentVelocity = this.getVelocity();
        if (currentVelocity < 2) {
            // Sotto 2, accelera sempre
            return 1;
        } else {
            // Se >= 2, accelera con probabilità 70%, altrimenti decelera
            double roll = random.nextDouble(); // valore tra 0.0 e 1.0
            if (roll < 0.7) {
                return 1;  // accelera
            } else {
                return -1; // decelera
            }
        }
    }
}
