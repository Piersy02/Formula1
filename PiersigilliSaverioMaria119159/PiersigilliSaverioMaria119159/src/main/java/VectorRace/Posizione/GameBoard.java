package VectorRace.Posizione;

import VectorRace.Giocatori.BasePlayer;
import VectorRace.Giocatori.IPlayer;
import VectorRace.Posizione.ITrack;
import VectorRace.Posizione.Position;
import VectorRace.Posizione.Track;
import VectorRace.Posizione.VectorDirection;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * GameBoard rappresenta la "plancia" di gioco, gestendo la posizione dei giocatori
 * e i riferimenti al tracciato. Si occupa di:
 * - Assegnare posizioni di partenza ai giocatori.
 * - Verificare se una cella è libera, contiene un ostacolo o rappresenta il traguardo.
 * - Aggiornare la posizione dei giocatori.
 * - Visualizzare lo stato corrente di gioco.
 */
public class GameBoard {

    /**
     * Riferimento al tracciato (che implementa l'interfaccia ITrack).
     */
    private ITrack track;

    /**
     * Mappa che associa un giocatore (IPlayer) alla sua posizione corrente (Position).
     */
    private Map<IPlayer, Position> playerPositions;

    /**
     * Indice della prossima posizione di partenza disponibile
     * (usato quando il tracciato fornisce più posizioni di start).
     */
    private int nextStartIndex = 0;

    /**
     * Costruttore di GameBoard.
     *
     * @param track Istanza di ITrack che rappresenta il tracciato di gioco.
     */
    public GameBoard(ITrack track) {
        this.track = track;
        this.playerPositions = new HashMap<>();
    }

    /**
     * Aggiunge un nuovo giocatore alla partita, assegnandogli una posizione di partenza.
     * Se track è un'istanza di Track, usa getAllStartPositions() per ottenere più posizioni.
     * Se invece è una generica implementazione di ITrack, usa getStartPosition() come fallback.
     *
     * @param player Il nuovo giocatore da aggiungere.
     */
    public void addPlayer(IPlayer player) {
        Position start;

        // Verifica se track è un'istanza concreta di Track (che potrebbe avere più start)
        if (track instanceof Track) {
            // Ottiene la lista di tutte le posizioni di partenza dal tracciato
            List<Position> starts = ((Track) track).getAllStartPositions();

            // Se c'è ancora una posizione di partenza non assegnata
            if (nextStartIndex < starts.size()) {
                start = starts.get(nextStartIndex++);
            } else {
                // Se esauriamo le posizioni di partenza, usiamo l'ultima disponibile
                start = starts.get(starts.size() - 1);
            }
        } else {
            // Fallback per un ITrack generico
            start = track.getStartPosition();
        }

        // Imposta la posizione iniziale del giocatore e lo aggiunge alla mappa
        player.setCurrentPosition(start);
        playerPositions.put(player, start);
    }

    /**
     * Controlla se la posizione è libera, ossia non occupata da ostacoli
     * e non già occupata da un altro giocatore.
     *
     * @param pos La posizione da verificare.
     * @return true se la posizione è libera, false altrimenti.
     */
    public boolean isFree(Position pos) {
        return track.isFree(pos) && !isOccupied(pos);
    }

    /**
     * Controlla se la posizione rappresenta un ostacolo sul tracciato.
     *
     * @param pos La posizione da verificare.
     * @return true se c'è un ostacolo, false altrimenti.
     */
    public boolean isObstacle(Position pos) {
        return track.isObstacle(pos);
    }

    /**
     * Controlla se la posizione rappresenta il traguardo sul tracciato.
     *
     * @param pos La posizione da verificare.
     * @return true se è il traguardo, false altrimenti.
     */
    public boolean isFinish(Position pos) {
        return track.isFinish(pos);
    }

    /**
     * Verifica se la posizione è già occupata da un giocatore (anche se è libera sul tracciato).
     *
     * @param pos La posizione da verificare.
     * @return true se la posizione è occupata da un giocatore, false altrimenti.
     */
    private boolean isOccupied(Position pos) {
        // Controlla se qualcuno dei giocatori ha la stessa posizione
        return playerPositions.values().stream().anyMatch(p -> p.equals(pos));
    }

    /**
     * Aggiorna la posizione di uno specifico giocatore nella mappa interna.
     *
     * @param player Il giocatore di cui aggiornare la posizione.
     * @param newPos La nuova posizione.
     */
    public void updatePlayerPosition(IPlayer player, Position newPos) {
        player.setCurrentPosition(newPos);

        playerPositions.put(player, newPos);
    }

    /**
     * Restituisce il tracciato associato a questa GameBoard.
     *
     * @return L'istanza di ITrack in uso.
     */
    public ITrack getTrack() {
        return track;
    }

    /**
     * Visualizza la situazione corrente del gioco:
     * - Stampa riga per riga il tracciato, sostituendo i caratteri delle celle
     *   con il simbolo del giocatore se è presente.
     * - Stampa poi uno stato riepilogativo dei giocatori (nome, velocità, posizione, direzione).
     *
     * @param players Lista dei giocatori attivi.
     * @param previousDirections Mappa che collega ogni giocatore
     *                           alla sua direzione al turno precedente.
     */
    public void display(List<IPlayer> players, Map<IPlayer, VectorDirection.CardinalDirection> previousDirections) {
        // Stampa del tracciato, riga per riga
        for (int y = 0; y < track.getHeight(); y++) {
            for (int x = 0; x < track.getWidth(); x++) {
                Position pos = new Position(x, y);

                // Carattere che rappresenta la cella
                char cell;

                // Se track è un'istanza di Track,
                // controlliamo in modo specifico Start (S) e Finish (F)
                if (track instanceof Track) {
                    Track t = (Track) track;
                    if (t.getAllStartPositions().contains(pos)) {
                        cell = 'S';   // Indichiamo posizione di partenza
                    } else if (t.getAllFinishPositions().contains(pos)) {
                        cell = 'F';   // Indichiamo traguardo
                    } else {
                        cell = t.getCell(pos);
                    }
                } else {
                    cell = track.getCell(pos);
                }

                // Verifica se un giocatore è presente nella cella
                String playerSymbol = null;
                for (IPlayer player : players) {
                    if (player.getCurrentPosition().equals(pos)) {
                        // Se il giocatore è un BasePlayer,
                        // usiamo l'iniziale del suo nome come simbolo.
                        // Altrimenti, di default "P".
                        playerSymbol = (player instanceof BasePlayer)
                                ? ((BasePlayer) player).getName().substring(0, 1)
                                : "P";
                        break;
                    }
                }

                // Stampa il simbolo del giocatore o il carattere della cella
                System.out.print(playerSymbol != null ? playerSymbol : cell);
            }
            // Fine di una riga
            System.out.println();
        }

        // Separatore e stato dei giocatori
        System.out.println("\nStato dei giocatori:");
        for (IPlayer player : players) {
            // Mostra informazioni: nome, velocità, posizione e direzione precedente
            String playerName = (player instanceof BasePlayer)
                    ? ((BasePlayer) player).getName()
                    : "Giocatore";
            Position pos = player.getCurrentPosition();
            int velocity = player.getVelocity();
            VectorDirection.CardinalDirection direction = previousDirections.get(player);
            String directionStr = (direction != null) ? direction.toString() : "N/D";

            // Stampa formattata
            System.out.printf("%s - Velocità: %d, Posizione: (%d, %d), Direzione: %s%n",
                    playerName, velocity, pos.getX(), pos.getY(), directionStr);
        }

        System.out.println("--------------------------------------------------");
    }

    public int getPlayerCount() {
        return playerPositions.size();
    }

}
