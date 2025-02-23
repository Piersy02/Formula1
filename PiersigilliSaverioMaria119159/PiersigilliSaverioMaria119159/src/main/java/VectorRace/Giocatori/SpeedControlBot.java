package VectorRace.Giocatori;

import VectorRace.Posizione.ITrack;
import VectorRace.Posizione.Position;
import VectorRace.Posizione.VectorDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * SpeedControlBot è un bot che cerca una direzione sicura in modo casuale
 * ma controlla la velocità in modo da non superare un certo limite (3).
 * - Se la velocità è sotto 3, accelera.
 * - Se la velocità è 3 o più, decelera.
 */
public class SpeedControlBot extends BasePlayer {

    /**
     * Generatore di numeri casuali per la scelta tra più opzioni sicure.
     */
    private Random random = new Random();

    /**
     * Riferimento al tracciato di gioco (ITrack), utilizzato per controllare
     * se una posizione è libera o meno.
     */
    private ITrack track;

    /**
     * Costruttore di SpeedControlBot.
     *
     * @param name  Nome del bot.
     * @param start Posizione di partenza sul tracciato.
     * @param track Il tracciato di gioco (implementazione di ITrack).
     */
    public SpeedControlBot(String name, Position start, ITrack track) {
        super(name, start);
        this.track = track;
    }

    /**
     * Sceglie una direzione casuale tra quelle consentite che siano "sicure",
     * ovvero che non portino subito a un ostacolo.
     *
     * @param allowedDirections Insieme di direzioni permesse in questo turno.
     * @return Una direzione sicura (random) o null se non ne esistono.
     */
    @Override
    public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        // Raccoglie in una lista tutte le direzioni consentite
        // che portano a una cella libera.
        List<VectorDirection.CardinalDirection> safeDirs = new ArrayList<>();
        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            // Calcola la posizione futura in base alla direzione.
            Position nextPos = getNextPosition(this.currentPosition, dir);

            // Se la posizione è libera sul tracciato, la aggiunge alle direzioni sicure.
            if (track.isFree(nextPos)) {
                safeDirs.add(dir);
            }
        }

        // Se non ci sono direzioni sicure, restituisce null (non si muove).
        // Altrimenti, sceglie a caso fra quelle disponibili.
        return safeDirs.isEmpty() ? null : safeDirs.get(random.nextInt(safeDirs.size()));
    }

    /**
     * Determina l'accelerazione in base alla velocità corrente:
     * se inferiore a 3, accelera di 1;
     * se uguale o superiore a 3, decelera di 1.
     *
     * @return 1 (accelerazione) o -1 (decelerazione).
     */
    @Override
    public int chooseAcceleration() {
        // Confronta la velocità corrente con la soglia 3.
        // Se < 3, accelera (1); altrimenti, decelera (-1).
        if (this.getVelocity() < 3) {
            return 1;  // accelerazione
        } else {
            return -1; // decelerazione
        }
    }

    /**
     * Calcola la nuova posizione spostandosi di una cella
     * nella direzione specificata.
     *
     * @param current   Posizione attuale.
     * @param direction Direzione cardinale (N, NE, E, SE, S, SW, W, NW).
     * @return La prossima posizione dopo essersi spostati di una cella.
     */
    private Position getNextPosition(Position current, VectorDirection.CardinalDirection direction) {
        int x = current.getX();
        int y = current.getY();

        // Aggiorna (x, y) secondo la direzione scelta.
        switch (direction) {
            case N:  y -= 1; break;
            case NE: x += 1; y -= 1; break;
            case E:  x += 1; break;
            case SE: x += 1; y += 1; break;
            case S:  y += 1; break;
            case SW: x -= 1; y += 1; break;
            case W:  x -= 1; break;
            case NW: x -= 1; y -= 1; break;
        }

        // Restituisce la nuova posizione.
        return new Position(x, y);
    }
}
