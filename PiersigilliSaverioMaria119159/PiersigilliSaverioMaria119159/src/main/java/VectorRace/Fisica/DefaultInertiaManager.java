package VectorRace.Fisica;

import VectorRace.Posizione.VectorDirection;

import java.util.*;

/**
 * DefaultInertiaManager è un’implementazione base di IInertiaManager che,
 * in base alla velocità corrente e alla direzione precedente,
 * limita le direzioni disponibili per il turno successivo.
 *
 * La logica si basa su soglie di velocità:
 * - velocità <= 1: si possono scegliere tutte le direzioni.
 * - velocità == 2: si limita la scelta a direzioni entro ±90° rispetto alla direzione precedente.
 * - velocità >= 3: si limita la scelta a direzioni entro ±45° rispetto alla direzione precedente.
 */
public class DefaultInertiaManager implements IInertiaManager {

    /**
     * Restituisce la lista di direzioni cardinale ammesse,
     * calcolate in base alla velocità e alla direzione precedente.
     *
     * @param currentVelocity       Velocità corrente del giocatore.
     * @param previousDirection     Direzione seguita dal giocatore al turno precedente.
     * @return Lista di direzioni consentite.
     */
    @Override
    public List<VectorDirection.CardinalDirection> allowedDirections(int currentVelocity, VectorDirection.CardinalDirection previousDirection) {
        // Lista delle direzioni finali ammesse
        List<VectorDirection.CardinalDirection> directions = new ArrayList<>();

        // Se la velocità è 0 o 1, consentiamo tutte le direzioni possibili
        if (currentVelocity <= 1) {
            directions.addAll(Arrays.asList(VectorDirection.CardinalDirection.values()));
        }
        // Se la velocità è esattamente 2, limitiamo a ±90° dalla direzione precedente
        else if (currentVelocity == 2) {
            directions.addAll(getDirectionsWithinAngle(previousDirection, 90));
        }
        // Se la velocità è 3 o superiore, limitiamo a ±45° dalla direzione precedente
        else if (currentVelocity >= 3) {
            directions.addAll(getDirectionsWithinAngle(previousDirection, 45));
        }
        return directions;
    }

    /**
     * Restituisce tutte le direzioni la cui differenza angolare rispetto a baseDir
     * rientra in un determinato range (angleRange), valutato in gradi.
     *
     * @param baseDir     Direzione di base da cui calcolare l’angolo.
     * @param angleRange  Intervallo di gradi consentito (es. 45° o 90°).
     * @return Lista di direzioni entro l’angolo specificato rispetto a baseDir.
     */
    private List<VectorDirection.CardinalDirection> getDirectionsWithinAngle(VectorDirection.CardinalDirection baseDir, int angleRange) {
        List<VectorDirection.CardinalDirection> nearby = new ArrayList<>();

        // Mappa di direzioni -> angoli (riferiti all’asse orizzontale, in senso orario/antiorario)
        Map<VectorDirection.CardinalDirection, Double> directionAngles = new HashMap<>();
        for (VectorDirection.CardinalDirection dir : VectorDirection.CardinalDirection.values()) {
            directionAngles.put(dir, getAngle(dir));
        }

        // Angolo di base corrispondente alla direzione precedente
        double baseAngle = directionAngles.get(baseDir);

        // Per ciascuna direzione, confronta l’angolo con quello di base e calcola la differenza
        for (VectorDirection.CardinalDirection dir : VectorDirection.CardinalDirection.values()) {
            double angle = directionAngles.get(dir);
            double diff = Math.abs(baseAngle - angle);

            // Minimizziamo la differenza, tenendo conto che l’angolo può "avvolgersi" intorno a 360°
            diff = Math.min(diff, 360 - diff);

            // Se la differenza rientra nel range specificato, aggiungiamo la direzione alla lista
            if (diff <= angleRange) {
                nearby.add(dir);
            }
        }
        return nearby;
    }

    /**
     * Restituisce l’angolo associato a una direzione cardinale, espresso in gradi,
     * con la convenzione:
     *   E = 0°, NE = 45°, N = 90°, NW = 135°, W = 180°,
     *   SW = 225°, S = 270°, SE = 315°.
     *
     * @param dir Direzione cardinale.
     * @return Angolo corrispondente in gradi (tra 0 e 360).
     */
    private double getAngle(VectorDirection.CardinalDirection dir) {
        switch (dir) {
            case N:  return 90;
            case NE: return 45;
            case E:  return 0;
            case SE: return 315;
            case S:  return 270;
            case SW: return 225;
            case W:  return 180;
            case NW: return 135;
            default: return 0;
        }
    }
}
