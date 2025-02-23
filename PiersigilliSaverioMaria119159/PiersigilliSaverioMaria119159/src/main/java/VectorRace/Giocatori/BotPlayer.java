package VectorRace.Giocatori;

import VectorRace.Posizione.Position;
import VectorRace.Posizione.VectorDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * BotPlayer è una semplice implementazione di un bot che sceglie
 * una direzione e un’accelerazione in modo completamente casuale.
 */
public class BotPlayer extends BasePlayer {

    /**
     * Istanza di Random utilizzata per effettuare scelte pseudo-casuali.
     */
    private Random random = new Random();

    /**
     * Costruttore di BotPlayer.
     *
     * @param name  Nome del bot.
     * @param start Posizione di partenza sul tracciato.
     */
    public BotPlayer(String name, Position start) {
        // Richiama il costruttore della superclasse (BasePlayer)
        // per assegnare nome e posizione iniziale.
        super(name, start);
    }

    /**
     * Sceglie casualmente una direzione fra quelle consentite.
     *
     * @param allowedDirections Lista (Iterable) di direzioni ammesse in questo turno.
     * @return Una direzione selezionata a caso.
     */
    @Override
    public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        // Trasforma l'Iterable in una List per semplificare la selezione casuale.
        List<VectorDirection.CardinalDirection> options = new ArrayList<>();
        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            options.add(dir);
        }

        // Sceglie e restituisce una direzione casuale dalla lista.
        return options.get(random.nextInt(options.size()));
    }

    /**
     * Sceglie in modo casuale l'accelerazione, che può essere -1, 0 o +1.
     *
     * @return Intero rappresentante la variazione di velocità (da -1 a +1).
     */
    @Override
    public int chooseAcceleration() {
        // Genera un valore intero a caso fra -1 e +1.
        return random.nextInt(3) - 1;
    }
}

