package VectorRace.Fisica;

import VectorRace.Posizione.Position;

/**
 * L'interfaccia IVelocityCalculator definisce il metodo per calcolare
 * una velocità in base a due posizioni.
 * <p>
 * Questo può essere utile in vari scenari, ad esempio:
 * - Interpretare la distanza tra due punti come velocità (o viceversa).
 * - Tradurre la differenza di posizioni in un valore di velocità da assegnare a un giocatore.
 * <p>
 * L’implementazione concreta di IVelocityCalculator deciderà come usare
 * le coordinate (e.g. distanza Euclidea, Manhattan, ecc.) per restituire
 * un valore di velocità.
 */
public interface IVelocityCalculator {

    /**
     * Calcola la velocità (un valore intero) a partire da due posizioni.
     * In base all'implementazione, potrebbe trattarsi di:
     * - Distanza Euclidea arrotondata.
     * - Distanza Manhattan.
     * - Differenze di coordinate sommate/pesate.
     *
     * @param pos1 Prima posizione.
     * @param pos2 Seconda posizione.
     * @return Un valore intero di velocità, calcolato in base a pos1 e pos2.
     */
    int velocity(Position pos1, Position pos2);
}
