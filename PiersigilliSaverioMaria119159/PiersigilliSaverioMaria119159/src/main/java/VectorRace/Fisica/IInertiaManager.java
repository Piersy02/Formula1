package VectorRace.Fisica;

import VectorRace.Posizione.VectorDirection;

import java.util.List;

/**
 * Interfaccia che definisce le regole di "inerzia" per i movimenti dei giocatori.
 * <p>
 * In molti giochi, la direzione attuale e la velocità influenzano le direzioni
 * che il giocatore può intraprendere nel turno successivo. IInertiaManager si
 * occupa proprio di determinare quali direzioni siano ammesse date queste condizioni.
 *
 * Implementazioni diverse di IInertiaManager potrebbero introdurre meccaniche
 * personalizzate, ad esempio restrizioni più o meno severe in base alla velocità.
 */
public interface IInertiaManager {

    /**
     * Restituisce l'elenco di direzioni cardinale consentite per il giocatore,
     * data la sua velocità corrente e la direzione che stava seguendo nel turno precedente.
     *
     * @param currentVelocity      Velocità attuale del giocatore.
     * @param previousDirection    Direzione seguita al turno precedente.
     * @return Lista di direzioni ammesse per il turno in corso.
     */
    List<VectorDirection.CardinalDirection> allowedDirections(int currentVelocity,
                                                              VectorDirection.CardinalDirection previousDirection);
}
