package VectorRace;

import VectorRace.Fisica.DefaultInertiaManager;
import VectorRace.Fisica.IInertiaManager;
import VectorRace.Fisica.IVelocityCalculator;
import VectorRace.Fisica.SimpleVelocityCalculator;
import VectorRace.Giocatori.*;
import VectorRace.Posizione.GameBoard;
import VectorRace.Motore.GameEngine;
import VectorRace.Posizione.ITrack;
import VectorRace.Posizione.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ITrack track = new Track();
        try {
            track.loadFromFile("track.txt");
        } catch (IOException e) {
            System.err.println("Errore nel caricamento del tracciato: " + e.getMessage());
            return;
        }

        GameBoard board = new GameBoard(track);
        IVelocityCalculator velocityCalc = new SimpleVelocityCalculator();
        IInertiaManager inertiaMgr = new DefaultInertiaManager();
        int maxTurns = 20;  // Imposta qui il limite desiderato
        GameEngine engine = new GameEngine(board, velocityCalc, inertiaMgr, maxTurns);

        List<IPlayer> allPlayers = new ArrayList<>();

        //IPlayer human = new HumanPlayer("h", track.getStartPosition());
        //allPlayers.add(human);
        IPlayer greedyBot = new GreedyBot("g", track.getStartPosition(), track);
        allPlayers.add(greedyBot);
        IPlayer chaser = new ChaserBot("c", track.getStartPosition(), track, allPlayers);
        allPlayers.add(chaser);
        IPlayer saf = new SafeRunnerBot("s", track.getStartPosition(), track, allPlayers);
        allPlayers.add(saf);


        //engine.addPlayer(human);
        engine.addPlayer(saf);
        engine.addPlayer(chaser);
        engine.addPlayer(greedyBot);

        engine.startRace();
    }
}