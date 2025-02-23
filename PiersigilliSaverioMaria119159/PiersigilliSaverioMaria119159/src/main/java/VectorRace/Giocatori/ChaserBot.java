package VectorRace.Giocatori;

import VectorRace.Posizione.ITrack;
import VectorRace.Posizione.Position;
import VectorRace.Posizione.VectorDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ChaserBot si concentra sull'inseguimento di un avversario (per impostazione predefinita,
 * quello più vicino) invece che sul raggiungimento del traguardo.
 */
public class ChaserBot extends BasePlayer {

    private ITrack track;
    private List<IPlayer> players; // Riferimento a tutti i giocatori in partita
    private Random random = new Random();

    /**
     * Costruttore di ChaserBot.
     * @param name    Nome del bot.
     * @param start   Posizione di partenza.
     * @param track   Riferimento al tracciato di gioco.
     * @param players Riferimento a tutti i giocatori (incluso se stesso).
     */
    public ChaserBot(String name, Position start, ITrack track, List<IPlayer> players) {
        super(name, start);
        this.track = track;
        this.players = players;
    }

    /**
     * Sceglie la direzione puntando al giocatore più vicino.
     * Se non trova nessun giocatore diverso da sé, si muove come un bot casuale "sicuro".
     */
    @Override
    public VectorDirection.CardinalDirection chooseDirection(Iterable<VectorDirection.CardinalDirection> allowedDirections) {
        // Trova il giocatore bersaglio (il più vicino).
        IPlayer target = findClosestPlayer();
        if (target == null) {
            // Nessun altro giocatore, muoviti casualmente
            return randomSafeDirection(allowedDirections);
        }

        // Posizione del bersaglio
        Position targetPos = target.getCurrentPosition();

        VectorDirection.CardinalDirection bestDir = null;
        double bestDist = Double.MAX_VALUE; // vogliamo minimizzare la distanza

        for (VectorDirection.CardinalDirection dir : allowedDirections) {
            // Calcola la prossima cella in questa direzione
            Position nextPos = getNextPosition(this.currentPosition, dir);

            // Se non è libera, la scartiamo
            if (!track.isFree(nextPos)) {
                continue;
            }

            // Calcoliamo la distanza dal bersaglio
            double dist = manhattanDistance(nextPos, targetPos);

            // Teniamo traccia della direzione che minimizza la distanza
            if (dist < bestDist) {
                bestDist = dist;
                bestDir = dir;
            }
        }

        // Se non esiste nessuna direzione valida (bestDir == null),
        // ritorniamo una direzione casuale tra quelle ammesse o null
        if (bestDir == null) {
            return randomSafeDirection(allowedDirections);
        }
        return bestDir;
    }

    /**
     * Strategia di accelerazione semplice:
     * - Se la velocità è bassa (<= 1), accelera.
     * - Altrimenti, con una probabilità 50% accelera e 50% decelera,
     *   per non rimanere sempre al massimo o al minimo.
     */
    @Override
    public int chooseAcceleration() {
        if (this.getVelocity() <= 1) {
            return 1; // accelera
        } else {
            // 50% accelera, 50% decelera
            return (random.nextBoolean()) ? 1 : -1;
        }
    }

    /**
     * Trova il giocatore più vicino (escludendo se stesso).
     * Se non c'è nessun altro, restituisce null.
     */
    private IPlayer findClosestPlayer() {
        IPlayer closest = null;
        double minDist = Double.MAX_VALUE;
        for (IPlayer p : players) {
            if (p == this) continue; // salta se stesso
            double dist = manhattanDistance(this.currentPosition, p.getCurrentPosition());
            if (dist < minDist) {
                minDist = dist;
                closest = p;
            }
        }
        return closest;
    }

    /**
     * Sceglie casualmente una direzione fra quelle ammesse e sicure.
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
     * Calcolo della distanza Manhattan.
     */
    private int manhattanDistance(Position p1, Position p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }

    /**
     * Calcola la prossima posizione, spostandosi di 1 cella nella direzione specificata.
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
