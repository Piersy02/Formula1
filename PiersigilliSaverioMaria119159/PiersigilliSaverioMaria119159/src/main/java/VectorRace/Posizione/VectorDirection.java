package VectorRace.Posizione;

/**
 * La classe VectorDirection calcola la direzione (in gradi, radianti
 * o come direzione cardinale) tra due posizioni (current e next).
 */
public class VectorDirection {

    /**
     * Posizione di partenza.
     */
    private Position current;

    /**
     * Posizione di arrivo.
     */
    private Position next;

    /**
     * Enum che definisce le otto direzioni cardinali.
     */
    public enum CardinalDirection {
        N, NE, E, SE, S, SW, W, NW;
    }

    /**
     * Costruttore di VectorDirection.
     *
     * @param current Posizione iniziale.
     * @param next    Posizione finale (o successiva).
     */
    public VectorDirection(Position current, Position next) {
        this.current = current;
        this.next = next;
    }

    /**
     * Restituisce l'angolo della direzione (da current a next) in radianti,
     * utilizzando la funzione {@code Math.atan2()}.
     *
     * @return Valore in radianti dell'angolo compreso tra i punti.
     */
    public double directionRadians() {
        return Math.atan2(next.getY() - current.getY(), next.getX() - current.getX());
    }

    /**
     * Restituisce l'angolo della direzione (da current a next) in gradi,
     * convertendo il risultato di {@link #directionRadians()} tramite
     * {@code Math.toDegrees()}.
     *
     * @return Valore in gradi (da -180 a 180).
     */
    public double directionDegrees() {
        return Math.toDegrees(directionRadians());
    }

    /**
     * Determina la direzione cardinale corrispondente all'angolo
     * calcolato tra current e next. Normalizza l'angolo a [0, 360)
     * e lo confronta con intervalli di 45° per assegnare una delle otto direzioni.
     *
     * @return Una delle otto cardinal directions (N, NE, E, SE, S, SW, W, NW).
     *         Se non rientra in nessun intervallo (improbabile), restituisce null.
     */
    public CardinalDirection getCardinalDirection() {
        double angle = directionDegrees();
        // Porta l'angolo nel range [0, 360)
        if (angle < 0) {
            angle += 360;
        }

        // Determina la direzione cardinale a seconda dell'intervallo angolare
        if (angle >= 337.5 || angle < 22.5) {
            return CardinalDirection.E;
        } else if (angle >= 22.5 && angle < 67.5) {
            return CardinalDirection.NE;
        } else if (angle >= 67.5 && angle < 112.5) {
            return CardinalDirection.N;
        } else if (angle >= 112.5 && angle < 157.5) {
            return CardinalDirection.NW;
        } else if (angle >= 157.5 && angle < 202.5) {
            return CardinalDirection.W;
        } else if (angle >= 202.5 && angle < 247.5) {
            return CardinalDirection.SW;
        } else if (angle >= 247.5 && angle < 292.5) {
            return CardinalDirection.S;
        } else if (angle >= 292.5 && angle < 337.5) {
            return CardinalDirection.SE;
        }

        // In teoria non dovrebbe mai verificarsi, ma come fallback restituisce null
        return null;
    }
}


