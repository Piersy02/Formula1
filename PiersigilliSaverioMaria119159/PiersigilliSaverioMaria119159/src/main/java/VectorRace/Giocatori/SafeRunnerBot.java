package VectorRace.Giocatori;

import VectorRace.Posizione.ITrack;
import VectorRace.Posizione.Position;
import VectorRace.Posizione.VectorDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * SafeRunnerBot cerca di correre verso il traguardo,
 * ma allo stesso tempo evita di avvicinarsi troppo agli altri giocatori.
 * Score = -(distanceToFinish) + alpha * (distanceToClosestPlayer).
 */
public class SafeRunnerBot extends BasePlayer {

    private ITrack track;
    private List<IPlayer> players;
    private Random random = new Random();

    // Fattore di bilanciamento tra "vicinanza al traguardo" e "lontananza dagli altri"
    private double alpha = 0.5;

    /**
     * Costruttore per SafeRunnerBot.
     *
     * @param name    Nome del bot.
     * @param start   Posizione di partenza.
     * @param track   Riferimento al tracciato di gioco (ITrack).
     * @param players Lista di tutti i giocatori (incluso se stesso).
     */
    public SafeRunnerBot(String name, Position start, ITrack track, List<IPlayer> players) {
        super(name, start);
        this.track = track;
        this.players = players;
    }

    /**
     * Sceglie la direzione considerando sia la distanza dal traguardo
     * che la distanza dagli altri giocatori.
     *
     * @param allowedDirections Insieme di direzioni ammesse in questo turno (tenendo conto dell'inerzia).
     * @return La direzione con score migliore o null se nessuna è sicura.
     */
    @Override
    public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        Position finish = track.getFinishPosition();
        if (finish == null) {
            // Se non esiste traguardo, comportati come un bot casuale "sicuro".
            return randomSafeDirection(allowedDirections);
        }

        double bestScore = Double.NEGATIVE_INFINITY;
        VectorDirection.CardinalDirection bestDir = null;

        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            Position nextPos = getNextPosition(this.currentPosition, dir);

            // Deve essere libera
            if (!track.isFree(nextPos)) {
                continue;
            }

            // Calcoliamo la distanza al traguardo (Manhattan)
            int distFinish = manhattanDistance(nextPos, finish);

            // Distanza minima dagli altri giocatori
            double distPlayers = distanceToClosestPlayer(nextPos);

            // Calcolo dello score
            // => minore distFinish => punteggio alto (quindi lo mettiamo con segno -)
            // => maggiore distPlayers => punteggio alto (moltiplicato per alpha)
            double score = -(distFinish) + alpha * distPlayers;

            if (score > bestScore) {
                bestScore = score;
                bestDir = dir;
            }
        }

        // Se non abbiamo trovato una direzione valida, ripieghiamo su randomSafeDirection o null
        if (bestDir == null) {
            return randomSafeDirection(allowedDirections);
        }
        return bestDir;
    }

    /**
     * Strategia di accelerazione:
     * - Se velocità < 2, accelera di 1.
     * - Se velocità >= 2 e c'è un giocatore troppo vicino (distanza <= 1), decelera di 1.
     * - Altrimenti 0 (mantiene).
     */
    @Override
    public int chooseAcceleration() {
        int v = getVelocity();
        if (v < 2) {
            return 1; // accelera
        } else {
            boolean someoneClose = isSomeoneTooClose(1);
            if (someoneClose) {
                return -1; // decelera per sicurezza
            }
            return 0; // mantieni
        }
    }

    /**
     * Verifica se esiste un giocatore (diverso da se stesso) con distanza Manhattan <= soglia.
     */
    private boolean isSomeoneTooClose(int threshold) {
        for (IPlayer p : players) {
            if (p != this) {
                if (manhattanDistance(this.currentPosition, p.getCurrentPosition()) <= threshold) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Ritorna la direzione libera scelta a caso fra quelle permesse.
     * Restituisce null se non ne esistono.
     */
    private VectorDirection.CardinalDirection randomSafeDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        List<VectorDirection.CardinalDirection> safeDirs = new ArrayList<>();
        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            Position nextPos = getNextPosition(this.currentPosition, dir);
            if (track.isFree(nextPos)) {
                safeDirs.add(dir);
            }
        }
        if (safeDirs.isEmpty()) {
            return null;
        }
        return safeDirs.get(random.nextInt(safeDirs.size()));
    }

    /**
     * Calcola la distanza minima fra la posizione 'pos' e tutti gli altri giocatori.
     * Se non ci sono altri giocatori, restituisce un valore grande (ad es. Double.MAX_VALUE).
     */
    private double distanceToClosestPlayer(Position pos) {
        double minDist = Double.MAX_VALUE;
        for (IPlayer p : players) {
            if (p != this) {
                double dist = manhattanDistance(pos, p.getCurrentPosition());
                if (dist < minDist) {
                    minDist = dist;
                }
            }
        }
        return minDist;
    }

    /**
     * Distanza Manhattan.
     */
    private int manhattanDistance(Position p1, Position p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }

    /**
     * Calcola la prossima posizione spostandosi di una cella in una delle direzioni cardinali.
     */
    private Position getNextPosition(Position current, VectorDirection.CardinalDirection dir) {
        int x = current.getX();
        int y = current.getY();
        switch (dir) {
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

