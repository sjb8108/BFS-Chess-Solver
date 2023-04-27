package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The HoppersModel class used to implement both a PTUI and GUI for the Hoppers game
 * Implements an MVC design
 *
 *  @author Maanav Contractor (mpc9618)
 */

public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;

    /** starting configuration used for restart */
    private HoppersConfig startingConfig;
    private int rowDim;
    private int colDim;

    /** selectedRow and selectedCol are used when user is making a selection
        holds information for the first selection made */
    private int selectedRow;
    private int selectedCol;

    /** Represents if the current move is selecting a point or moving a frog
        If negative, selecting a point, if positive, moving a frog. */
    private int selectOrJump;
    private String selectedFrog;

    /** Possible legal jumping directions. For use in select(). First index is row, second index is column */
    private final int[][] jumpingDirections = {{-4,0},{-2,2},{0,4},{2,2},{4,0},{2,-2},{0,-4},{-2,-2}};


    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    /**
     * Getter for CurrentConfig
     * @return currentConfig
     */
    public HoppersConfig getCurrentConfig() {
        return currentConfig;
    }


    /**
     * Constructor for HoppersModel
     * Used for PTUI and GUI for Hoppers game
     * @param filename file being loaded into game
     * @throws IOException standard exception for IO operations
     */
    public HoppersModel(String filename) throws IOException {
        BufferedReader hopperLoader = new BufferedReader( new FileReader(filename));
        String[] dimensions = hopperLoader.readLine().split(" ");
        this.rowDim = Integer.parseInt(dimensions[0]);
        this.colDim = Integer.parseInt(dimensions[1]);
        String[][] hopperBoard = new String[rowDim][colDim];

        for (int iRow = 0; iRow < rowDim; iRow++) {
            String[] line = hopperLoader.readLine().split("\\s+");
            for (int iCol = 0; iCol < colDim; iCol++) {
                hopperBoard[iRow][iCol] = line[iCol];
            }
        }

        this.currentConfig = new HoppersConfig(hopperBoard);
        this.startingConfig = new HoppersConfig(hopperBoard);
        this.selectOrJump = -1;
        this.selectedRow = 0;
        this.selectedCol = 0;
    }

    /**
     * Creates a new game by loading the passed filename
     * @param baseFilename file being loaded
     */
    public void load(String baseFilename) {
        try {
            String filename = "data/hoppers/" + baseFilename;
            BufferedReader hopperLoader = new BufferedReader(new FileReader(filename));
            System.out.println("Loaded :" + filename);
            String[] dimensions = hopperLoader.readLine().split(" ");
            this.rowDim = Integer.parseInt(dimensions[0]);
            this.colDim = Integer.parseInt(dimensions[1]);
            String[][] hopperBoard = new String[rowDim][colDim];

            for (int iRow = 0; iRow < rowDim; iRow++) {
                String[] line = hopperLoader.readLine().split("\\s+");
                for (int iCol = 0; iCol < colDim; iCol++) {
                    hopperBoard[iRow][iCol] = line[iCol];
                }
            }

            this.currentConfig = new HoppersConfig(hopperBoard);
            this.startingConfig = new HoppersConfig(hopperBoard);
            this.selectOrJump = -1;
            this.selectedRow = 0;
            this.selectedCol = 0;
            alertObservers("new game");

        } catch (IOException ioe) {
            alertObservers("Failed to load: " + baseFilename);
        }
    }

    /**
     * Resets the puzzle to the original configuration
     */
    public void reset() {
        this.currentConfig = this.startingConfig;
        this.selectOrJump = -1;
        selectedRow = 0;
        selectedCol = 0;
        alertObservers("Puzzle Reset!");
    }

    /**
     * Generates the hint for the next move by implementing a BFS to find the
     * optimal solution. Sets current configuration to the hint found
     */
    public void hint() {
        Map<Configuration, Configuration> predecessor = new HashMap<>();
        Configuration startConfig = this.currentConfig;
        predecessor.put(startConfig, null);

        Queue<Configuration> toVisit = new LinkedList<>();
        toVisit.offer(startConfig);
        int totalConfigs = 1;

        while (!toVisit.isEmpty() && !toVisit.peek().isSolution()) {
            Configuration current = toVisit.remove();
            for (Configuration neighbors : current.getNeighbors()) {
                totalConfigs += 1;
                if (!(predecessor.containsKey(neighbors))) {
                    predecessor.put(neighbors, current);
                    toVisit.offer(neighbors);
                }
            }
        }

        if (toVisit.isEmpty()) {
            alertObservers("Puzzle is not solvable");
        } else {
            Configuration finishConfig = toVisit.remove();
            List<Configuration> path = new LinkedList<>();
            path.add(0, finishConfig);
            Configuration node = predecessor.get(finishConfig);
            while (node != null) {
                path.add(0, node);
                node = predecessor.get(node);
            }

            this.currentConfig = (HoppersConfig) path.get(1);
            if (this.currentConfig.isSolution()) {
                alertObservers("You finished the puzzle!");
            } else {
                alertObservers("Next Step!");
            }
        }
        this.selectOrJump = -1;
    }

    /**
     *
     * @param row user selected row
     * @param col user selected column
     */
    public void selectPiece(int row, int col) {
        if (row > rowDim-1 || row < 0 || col > colDim-1 || col < 0) {
            alertObservers("Selection outside board range: (" + row + "," + col + ")");
        } else {
            String[][] hopperBoard = this.currentConfig.getHopperBoard();
            // Selecting a point
            if (selectOrJump < 0) {
                if (hopperBoard[row][col].equals(".")) {
                    alertObservers("No frog at (" + row + "," + col + ")");
                } else {
                    this.selectedRow = row;
                    this.selectedCol = col;
                    this.selectOrJump = 1;
                    this.selectedFrog = hopperBoard[row][col];
                    alertObservers("Selected (" + row + ", " + col + ")");
                }
                // Selecting to take
            } else {
                if (hopperBoard[row][col].equals("G") || hopperBoard[row][col].equals("R")) {
                    alertObservers("Can't jump from (" + selectedRow
                            + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                    this.selectOrJump = -1;
                    // Selecting another piece to take
                } else {
                    boolean completedJump = false;
                    for (int[] coordinate : this.jumpingDirections) {
                        try {
                            int rowDiv2 = coordinate[0] / 2;
                            int colDiv2 = coordinate[1] / 2;
                            if (row == selectedRow + coordinate[0] && col == selectedCol + coordinate[1]
                                    && hopperBoard[selectedRow + rowDiv2][selectedCol + colDiv2].equals("G")) {
                                this.currentConfig = this.currentConfig.makeNeighbor(selectedRow + coordinate[0], selectedCol + coordinate[1],
                                        selectedRow + rowDiv2, selectedCol + colDiv2, selectedRow, selectedCol, this.selectedFrog);
                                this.selectOrJump = -1;
                                completedJump = true;

                                if (this.currentConfig.isSolution()) {
                                    alertObservers("Complete");
                                } else {
                                    alertObservers("Jumped from (" + selectedRow + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                                }
                            }
                        } catch (IndexOutOfBoundsException iobe) {
                        }
                    }
                    if (!completedJump) {
                        alertObservers("Can't jump from (" + selectedRow
                                + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                    }
                    this.selectOrJump = -1;
                }
            }
        }
    }
}
