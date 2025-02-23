package VectorRace.Fisica;

import VectorRace.Posizione.Position;

/**
 * SimpleVelocityCalculator è un’implementazione di IVelocityCalculator
 * che calcola la velocità come la massima differenza tra le coordinate
 * X e Y di due posizioni.
 * <p>
 * In pratica, usa:
 * <pre>
 *   velocity = max(|?x|, |?y|)
 * </pre>
 * Si tratta di una metrica comunemente detta "Chebyshev distance",
 * spesso usata quando il movimento può avvenire sia in diagonale
 * che orizzontalmente/verticalmente in un singolo passo.
 */
public class SimpleVelocityCalculator implements IVelocityCalculator {

    /**
     * Calcola la velocità come la massima differenza in valore assoluto
     * tra le coordinate x e y delle due posizioni.
     *
     * @param pos1 Prima posizione.
     * @param pos2 Seconda posizione.
     * @return Un valore intero che rappresenta la "Chebyshev distance"
     *         tra pos1 e pos2.
     */
    @Override
    public int velocity(Position pos1, Position pos2) {
        int diffX = pos2.getX() - pos1.getX();
        int diffY = pos2.getY() - pos1.getY();

        // Restituisce la distanza massima lungo un asse (X o Y)
        return Math.max(Math.abs(diffX), Math.abs(diffY));
    }
}

