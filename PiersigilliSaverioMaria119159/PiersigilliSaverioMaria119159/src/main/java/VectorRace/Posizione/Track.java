package VectorRace.Posizione;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Track implementa l'interfaccia ITrack e rappresenta il tracciato di gioco.
 * Può caricare una mappa da un file di testo, dove ogni riga rappresenta
 * una "riga" del tracciato, interpretando i caratteri speciali:
 * <ul>
 *     <li><strong>S</strong>: Posizione di partenza (start)</li>
 *     <li><strong>F</strong>: Posizione di arrivo (finish)</li>
 *     <li><strong>#</strong>: Ostacolo</li>
 *     <li>Altri caratteri interpretati come '.' (cella libera)</li>
 * </ul>
 */
public class Track implements ITrack {

    /**
     * Griglia di caratteri (altezza x larghezza) che rappresenta il tracciato.
     */
    private char[][] grid;

    /**
     * Lista di posizioni di partenza caricate dal file.
     */
    private List<Position> startPositions = new ArrayList<>();

    /**
     * Lista di posizioni di arrivo (finish) caricate dal file.
     */
    private List<Position> finishPositions = new ArrayList<>();

    /**
     * Larghezza effettiva del tracciato (numero di colonne).
     */
    private int width;

    /**
     * Altezza effettiva del tracciato (numero di righe).
     */
    private int height;

    /**
     * Carica i dati del tracciato da un file (resource) specificato.
     * <p>
     * Il file viene letto riga per riga e, per ogni carattere nella riga,
     * si interpretano:
     * <ul>
     *     <li>S: aggiunge la {@link Position} alle startPositions e nella grid salva '.'</li>
     *     <li>F: aggiunge la {@link Position} alle finishPositions e nella grid salva '.'</li>
     *     <li>#: segna la cella come ostacolo nella grid</li>
     *     <li>Altrimenti: segna la cella come '.' (libera)</li>
     * </ul>
     *
     * @param filename Nome del file (o path relativo) da cui caricare il tracciato.
     * @throws IOException Se il file non viene trovato o si verifica un errore di lettura.
     */
    @Override
    public void loadFromFile(String filename) throws IOException {
        // Carichiamo il file come resource dal classpath.
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new IOException("File non trovato: " + filename);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            // Accumula tutte le righe del file in una lista di stringhe
            List<String> lines = new ArrayList<>();
            String line;

            // Legge ogni riga e aggiorna la larghezza massima e l'altezza
            while ((line = br.readLine()) != null) {
                lines.add(line);
                width = Math.max(width, line.length());
                height++;
            }

            // Inizializza la griglia con le dimensioni calcolate
            grid = new char[height][width];

            // Popola la grid interpretando i caratteri speciali
            for (int y = 0; y < height; y++) {
                String currentLine = lines.get(y);

                for (int x = 0; x < width; x++) {
                    // Se x oltre la lunghezza della riga, consideriamo '.' per evitare IndexOutOfBounds
                    char currentChar = (x < currentLine.length()) ? currentLine.charAt(x) : '.';

                    switch (currentChar) {
                        case 'S':
                            // Segna una posizione di partenza
                            startPositions.add(new Position(x, y));
                            grid[y][x] = '.'; // Consideriamo comunque la cella libera in grid
                            break;
                        case 'F':
                            // Segna una posizione di arrivo
                            finishPositions.add(new Position(x, y));
                            grid[y][x] = '.'; // Anche questa cella è libera ma registrata come arrivo
                            break;
                        case '#':
                            // Cella con ostacolo
                            grid[y][x] = '#';
                            break;
                        default:
                            // Di default, consideriamo la cella libera
                            grid[y][x] = '.';
                            break;
                    }
                }
            }
        }
    }

    /**
     * Restituisce il carattere della griglia alla posizione specificata,
     * o '#' se la posizione è fuori dai limiti (considerata ostacolo).
     *
     * @param position Coordinate {@link Position} da cui estrarre il carattere.
     * @return Il carattere corrispondente ('.', '#', ecc.).
     */
    @Override
    public char getCell(Position position) {
        if (isWithinBounds(position)) {
            return grid[position.getY()][position.getX()];
        }
        // Se fuori dalla griglia, consideriamo la cella come ostacolo
        return '#';
    }

    /**
     * Indica se una certa posizione è libera, verificando se la cella
     * corrisponde al carattere '.'.
     *
     * @param position Posizione da controllare.
     * @return true se la cella è '.', false altrimenti.
     */
    @Override
    public boolean isFree(Position position) {
        return getCell(position) == '.';
    }

    /**
     * Indica se una certa posizione è un ostacolo,
     * verificando se la cella corrisponde al carattere '#'.
     *
     * @param position Posizione da controllare.
     * @return true se la cella è '#', false altrimenti.
     */
    @Override
    public boolean isObstacle(Position position) {
        return getCell(position) == '#';
    }

    /**
     * Verifica se la posizione è definita come posizione di arrivo (finish).
     *
     * @param position Posizione da controllare.
     * @return true se la posizione è presente nella lista finishPositions, false altrimenti.
     */
    @Override
    public boolean isFinish(Position position) {
        return finishPositions.contains(position);
    }

    /**
     * Restituisce una posizione di partenza se disponibile.
     * Se esistono più posizioni di partenza, ne viene restituita la prima.
     *
     * @return {@link Position} di start o null se la lista è vuota.
     */
    @Override
    public Position getStartPosition() {
        return startPositions.isEmpty() ? null : startPositions.get(0);
    }

    /**
     * Restituisce una posizione di arrivo se disponibile.
     * Se esistono più posizioni di arrivo, ne viene restituita la prima.
     *
     * @return {@link Position} di finish o null se la lista è vuota.
     */
    @Override
    public Position getFinishPosition() {
        return finishPositions.isEmpty() ? null : finishPositions.get(0);
    }

    /**
     * Restituisce la larghezza del tracciato (numero di colonne).
     *
     * @return Valore intero che rappresenta la larghezza.
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Restituisce l'altezza del tracciato (numero di righe).
     *
     * @return Valore intero che rappresenta l'altezza.
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Fornisce l'intera lista di posizioni di partenza caricate (se ce ne sono più di una).
     *
     * @return Lista di {@link Position} che rappresentano le startPositions.
     */
    public List<Position> getAllStartPositions() {
        return startPositions;
    }

    /**
     * Fornisce l'intera lista di posizioni di arrivo caricate (se ce ne sono più di una).
     *
     * @return Lista di {@link Position} che rappresentano le finishPositions.
     */
    public List<Position> getAllFinishPositions() {
        return finishPositions;
    }

    /**
     * Metodo di utilità per controllare se la posizione ricade
     * all'interno dei limiti del tracciato.
     *
     * @param position Posizione da controllare.
     * @return true se 0 <= x < width e 0 <= y < height, false altrimenti.
     */
    private boolean isWithinBounds(Position position) {
        return position.getX() >= 0 && position.getX() < width &&
                position.getY() >= 0 && position.getY() < height;
    }
}
