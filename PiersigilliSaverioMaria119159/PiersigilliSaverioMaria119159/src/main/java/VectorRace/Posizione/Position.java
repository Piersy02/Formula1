package VectorRace.Posizione;

import java.util.Objects;

/**
 * La classe Position rappresenta una coordinata (x, y) sulla griglia
 * o tracciato di gioco di VectorRace.
 * <p>
 * Viene utilizzata per identificare la posizione di un giocatore,
 * di un ostacolo o di qualsiasi altro elemento sulla mappa.
 */
public class Position {

    /**
     * Coordinata X della posizione.
     */
    private int x;

    /**
     * Coordinata Y della posizione.
     */
    private int y;

    /**
     * Costruttore che inizializza le coordinate (x, y).
     *
     * @param x Valore intero per l'asse orizzontale.
     * @param y Valore intero per l'asse verticale.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Restituisce la coordinata X.
     *
     * @return Il valore di x.
     */
    public int getX() {
        return x;
    }

    /**
     * Restituisce la coordinata Y.
     *
     * @return Il valore di y.
     */
    public int getY() {
        return y;
    }

    /**
     * Imposta la coordinata X.
     *
     * @param x Il nuovo valore di x.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Imposta la coordinata Y.
     *
     * @param y Il nuovo valore di y.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Confronta l'oggetto corrente con un altro per stabilire se
     * rappresentano la stessa posizione.
     *
     * @param obj Oggetto da confrontare.
     * @return true se l'altro oggetto è un Position con le stesse coordinate,
     *         false altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.x == other.x && this.y == other.y;
    }

    /**
     * Genera un hash univoco basato sulle coordinate (x, y).
     * Viene utilizzato in strutture dati come HashMap o HashSet.
     *
     * @return Valore hash che rappresenta la coppia (x, y).
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

