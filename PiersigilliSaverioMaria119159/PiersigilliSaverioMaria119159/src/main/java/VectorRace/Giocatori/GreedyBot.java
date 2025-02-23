package VectorRace.Giocatori;

import VectorRace.Posizione.ITrack;
import VectorRace.Posizione.Position;
import VectorRace.Posizione.VectorDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * GreedyBot rappresenta un bot "goloso" (o avido) che sceglie la direzione
 * cercando di avvicinarsi il più possibile al traguardo e allo stesso tempo
 * restando lontano dagli ostacoli.
 *
 * - Se non c'è un traguardo definito, si comporta come un bot casuale "sicuro".
 * - Quando c'è un traguardo, valuta ciascuna direzione sicura
 *   combinando due fattori:
 *       1. Distanza Manhattan dal traguardo (più è piccola, meglio è).
 *       2. Distanza fino al prossimo ostacolo nella direzione scelta (più è grande, meglio è).
 */
public class GreedyBot extends BasePlayer {

    /**
     * Generatore di numeri casuali per scelte aleatorie nelle situazioni di parità.
     */
    private Random random = new Random();

    /**
     * Riferimento al tracciato (ITrack) su cui si muove il bot.
     */
    private ITrack track;

    /**
     * Costruttore di GreedyBot.
     *
     * @param name  Nome del bot.
     * @param start Posizione di partenza.
     * @param track Riferimento al tracciato di gioco.
     */
    public GreedyBot(String name, Position start, ITrack track) {
        super(name, start);
        this.track = track;
    }

    /**
     * Sceglie la direzione in base a un mix di:
     * - Distanza dal traguardo (obiettivo: ridurla il più possibile).
     * - Distanza dal prossimo ostacolo (obiettivo: mantenerla il più ampia possibile).
     *
     * Se non esiste un traguardo definito, sceglie casualmente una direzione "sicura" (senza ostacoli immediati).
     *
     * @param allowedDirections Le direzioni consentite in questo turno.
     * @return La direzione selezionata dal bot, oppure null se nessuna è sicura.
     */
    @Override
    public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        Position finish = track.getFinishPosition();
        // Se non esiste una posizione di traguardo, comportati come bot casuale sicuro.
        if (finish == null) {
            return randomSafeDirection(allowedDirections);
        }

        VectorDirection.CardinalDirection bestDirection = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        // Valuta ogni direzione consentita
        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            Position nextPos = getNextPosition(this.currentPosition, dir);

            // Salta la direzione se la cella non è libera
            if (!track.isFree(nextPos)) {
                continue;
            }

            // Distanza dal traguardo (Manhattan)
            int distanceToFinish = manhattanDistance(nextPos, finish);

            // Distanza fino al prossimo ostacolo in questa direzione
            int distanceToObstacle = distanceToNextObstacle(nextPos, dir);

            // Calcola uno "score" combinando la vicinanza al traguardo (meglio se piccola)
            // e la lontananza dal prossimo ostacolo (meglio se grande).
            // Qui usiamo un peso (alfa) per bilanciare i due fattori.
            double alfa = 1.0;
            // Più il traguardo è vicino, più il valore (distanceToFinish) è piccolo,
            // quindi il punteggio deve essere inversamente proporzionale.
            // Più l'ostacolo è lontano, più distanceToObstacle è grande, quindi positivo.
            double score = (-alfa * distanceToFinish) + distanceToObstacle;

            // Mantiene in memoria la direzione con punteggio migliore
            if (score > bestScore) {
                bestScore = score;
                bestDirection = dir;
            }
        }

        // Se non ha trovato alcuna direzione valida, non si muove (null).
        return (bestDirection != null) ? bestDirection : null;
    }

    /**
     * Strategia di accelerazione semplice: cerca sempre di accelerare se possibile.
     *
     * @return 1, per indicare l'intenzione di incrementare la velocità di un'unità.
     */
    @Override
    public int chooseAcceleration() {
        return 1;
    }

    /**
     * Se non è definito alcun traguardo, sceglie in modo casuale una direzione
     * che non porti immediatamente a un ostacolo.
     *
     * @param allowedDirections Direzioni consentite in questo turno.
     * @return Una direzione "sicura" scelta a caso, o null se non ce ne sono.
     */
    private VectorDirection.CardinalDirection randomSafeDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        List<VectorDirection.CardinalDirection> safeDirs = new ArrayList<>();
        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            Position nextPos = getNextPosition(this.currentPosition, dir);
            // Aggiunge la direzione se porta a una cella libera
            if (track.isFree(nextPos)) {
                safeDirs.add(dir);
            }
        }
        // Se non ci sono direzioni sicure, ritorna null
        return safeDirs.isEmpty() ? null : safeDirs.get(random.nextInt(safeDirs.size()));
    }

    /**
     * Calcola la distanza Manhattan tra due posizioni,
     * cioè la somma della differenza assoluta sulle coordinate x e y.
     *
     * @param p1 Prima posizione.
     * @param p2 Seconda posizione.
     * @return Distanza Manhattan tra p1 e p2.
     */
    private int manhattanDistance(Position p1, Position p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }

    /**
     * Calcola la distanza (in celle) fino al prossimo ostacolo nella direzione specificata,
     * partendo da una posizione iniziale.
     *
     * @param start     Posizione di partenza.
     * @param direction Direzione da seguire.
     * @return Numero di celle "libere" prima di incontrare un ostacolo o uscire dai confini.
     */
    private int distanceToNextObstacle(Position start, VectorDirection.CardinalDirection direction) {
        int distance = 0;
        Position current = start;

        while (true) {
            // Calcola la prossima cella nella direzione scelta
            Position next = getNextPosition(current, direction);

            // Controlla se è fuori dai confini
            boolean outOfBounds = next.getX() < 0 || next.getX() >= track.getWidth()
                    || next.getY() < 0 || next.getY() >= track.getHeight();
            // Se siamo fuori dal tracciato o la cella non è libera, interrompe il conteggio.
            if (outOfBounds || !track.isFree(next)) {
                break;
            }

            distance++;
            current = next;
        }

        return distance;
    }

    /**
     * Restituisce la nuova posizione spostandosi di una cella in
     * una delle direzioni cardinali.
     *
     * @param current   Posizione di partenza.
     * @param direction Direzione (N, NE, E, SE, S, SW, W, NW).
     * @return          Nuova posizione dopo essersi spostati di una cella.
     */
    private Position getNextPosition(Position current, VectorDirection.CardinalDirection direction) {
        int x = current.getX();
        int y = current.getY();

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

        return new Position(x, y);
    }
}
