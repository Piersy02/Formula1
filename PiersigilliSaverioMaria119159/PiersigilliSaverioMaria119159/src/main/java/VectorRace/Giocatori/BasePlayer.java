package VectorRace.Giocatori;

import VectorRace.Posizione.Position;

/**
 * BasePlayer fornisce un’implementazione parziale dell’interfaccia IPlayer.
 * È una classe astratta, quindi non può essere istanziata direttamente,
 * ma serve come “base” per le varie tipologie di giocatori (bot o umani).
 */
public abstract class BasePlayer implements IPlayer {

    /**
     * Nome del giocatore (es. "Bot1", "Alice", etc.).
     */
    protected String name;

    /**
     * Posizione attuale del giocatore sulla mappa o tracciato.
     */
    protected Position currentPosition;

    /**
     * Velocità attuale del giocatore, con un limite minimo di 0 e massimo di 3.
     */
    protected int velocity;

    /**
     * Costruttore di BasePlayer.
     * Inizializza il nome, la posizione corrente e imposta la velocità di base a 0.
     *
     * @param name  Nome del giocatore.
     * @param start Posizione di partenza.
     */
    public BasePlayer(String name, Position start) {
        this.name = name;
        this.currentPosition = start;
        this.velocity = 0;
    }

    /**
     * Restituisce il nome del giocatore.
     *
     * @return Nome del giocatore.
     */
    public String getName() {
        return name;
    }

    /**
     * Restituisce la posizione corrente del giocatore (implementazione di IPlayer).
     *
     * @return La posizione attuale del giocatore.
     */
    @Override
    public Position getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Imposta la posizione corrente del giocatore (implementazione di IPlayer).
     *
     * @param position Nuova posizione da assegnare.
     */
    @Override
    public void setCurrentPosition(Position position) {
        this.currentPosition = position;
    }

    /**
     * Restituisce la velocità attuale del giocatore (implementazione di IPlayer).
     *
     * @return Valore di velocità.
     */
    @Override
    public int getVelocity() {
        return velocity;
    }

    /**
     * Imposta la velocità del giocatore (implementazione di IPlayer),
     * applicando un vincolo minimo (0) e massimo (3) per evitare
     * velocità negative o troppo elevate.
     *
     * @param velocity Nuova velocità da assegnare (verrà “clampata” tra 0 e 3).
     */
    @Override
    public void setVelocity(int velocity) {
        // Se la velocità proposta è negativa, viene impostata a 0.
        if (velocity < 0) velocity = 0;
            // Se la velocità proposta supera 3, viene impostata a 3.
        else if (velocity > 3) velocity = 3;

        this.velocity = velocity;
    }
}


