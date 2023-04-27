package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.util.*;

/**
 * The HoppersConfig class used by Hoppers to represent a single configuration of the Hoppers puzzle.
 * Implements the Configuration interface to allow use through Solver.java BFS implementation.
 *
 *  @author Maanav Contractor (mpc9618)
 */

public class HoppersConfig implements Configuration{
    private String[][] hopperBoard;
    private static int rowDim;
    private static int colDim;

    /**
     * Constructor for HoppersConfig
     * @param hopperBoard 2d nested array representing a Hoppers board configuration
     */
    public HoppersConfig(String[][] hopperBoard) {
        this.hopperBoard = hopperBoard;
        rowDim = hopperBoard.length;
        colDim = hopperBoard[0].length;
    }

    /**
     * Getter for hopperBoard
     * @return hopperBoard
     */
    public String[][] getHopperBoard() {
        return hopperBoard;
    }

    @Override
    public boolean isSolution() {
        int redFrogCount = 0;
        int greenFrogCount = 0;
        for (int iRow = 0; iRow < rowDim; iRow++) {
            for (int iCol = 0; iCol < colDim; iCol++) {
                if (!(this.hopperBoard[iRow][iCol].equals(".") &&
                        (this.hopperBoard[iRow][iCol].equals("*")))) {
                    if (this.hopperBoard[iRow][iCol].equals("R")) {
                        redFrogCount++;
                    } else if (this.hopperBoard[iRow][iCol].equals("G")) {
                        greenFrogCount++;
                    }
                }
            }
        }
        if (greenFrogCount == 0 && redFrogCount == 1) {
            return true;
        }
        return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        Set<Configuration> neighbors = new LinkedHashSet<Configuration>();
        for (int iRow = 0; iRow < rowDim; iRow++) {
            for (int iCol = 0; iCol < colDim; iCol++) {
                if (this.hopperBoard[iRow][iCol].equals("G") ||
                        this.hopperBoard[iRow][iCol].equals("R")) {

                    String frog = this.hopperBoard[iRow][iCol];

                    // Try jumping North
                    try {
                        if (this.hopperBoard[iRow-2][iCol].equals("G") && this.hopperBoard[iRow-4][iCol].equals(".")) {
                            neighbors.add(makeNeighbor(iRow-4,
                                    iCol, iRow-2, iCol, iRow, iCol, frog));
                        }
                    } catch (IndexOutOfBoundsException ie) {}

                    // Try jumping North East
                    try {
                        if (this.hopperBoard[iRow-1][iCol+1].equals("G") && this.hopperBoard[iRow-2][iCol+2].equals(".")) {
                            neighbors.add(makeNeighbor(iRow-2,
                                    iCol+2, iRow-1, iCol+1, iRow, iCol, frog));
                        }
                    } catch (IndexOutOfBoundsException ie) {}

                    // Try jumping East
                    try {
                        if (this.hopperBoard[iRow][iCol+2].equals("G") && this.hopperBoard[iRow][iCol+4].equals(".")) {
                            neighbors.add(makeNeighbor(iRow,
                                    iCol+4, iRow, iCol+2, iRow, iCol, frog));
                        }
                    } catch (IndexOutOfBoundsException ie) {}

                    // Try jumping South East
                    try {
                        if (this.hopperBoard[iRow+1][iCol+1].equals("G") && this.hopperBoard[iRow+2][iCol+2].equals(".")) {
                            neighbors.add(makeNeighbor(iRow+2,
                                    iCol+2, iRow+1, iCol+1, iRow, iCol, frog));
                        }
                    } catch (IndexOutOfBoundsException ie) {}

                    // Try jumping South
                    try {
                        if (this.hopperBoard[iRow+2][iCol].equals("G") && this.hopperBoard[iRow+4][iCol].equals(".")) {
                            neighbors.add(makeNeighbor(iRow+4,
                                    iCol, iRow+2, iCol, iRow, iCol, frog));
                        }
                    } catch (IndexOutOfBoundsException ie) {}

                    // Try jumping South West
                    try {
                        if (this.hopperBoard[iRow+1][iCol-1].equals("G") && this.hopperBoard[iRow+2][iCol-2].equals(".")) {
                            neighbors.add(makeNeighbor(iRow+2,
                                    iCol-2, iRow+1, iCol-1, iRow, iCol, frog));
                        }
                    } catch (IndexOutOfBoundsException ie) {}

                    // Try jumping West
                    try {
                        if (this.hopperBoard[iRow][iCol-2].equals("G") && this.hopperBoard[iRow][iCol-4].equals(".")) {
                            neighbors.add(makeNeighbor(iRow,
                                    iCol-4, iRow, iCol-2, iRow, iCol, frog));
                        }
                    } catch (IndexOutOfBoundsException ie) {}

                    // Try jumping North West
                    try {
                        if (this.hopperBoard[iRow-1][iCol-1].equals("G") && this.hopperBoard[iRow-2][iCol-2].equals(".")) {
                            neighbors.add(makeNeighbor(iRow-2,
                                    iCol-2, iRow-1, iCol-1, iRow, iCol, frog));
                        }
                    } catch (IndexOutOfBoundsException ie) {}
                }
            }
        }
        return neighbors;
    }

    /**
     * Helper function for getNeighbors()
     * Creates a new HoppersConfig neighbor by moving a piece and removing the corresponding piece
     *
     * @param newRow Row value for moving piece
     * @param newCol Column value for moving piece
     * @param removeRow Row value for removed piece
     * @param removeCol Column value for removed piece
     * @param originalRow Row value for original location of moving piece
     * @param originalCol Column value for original location of moving piece
     * @param frog Type of frog being moved, G(reen) or R(ed)
     * @return HoppersConfig representing new configuration
     * @throws IndexOutOfBoundsException to be handled by getNeighbors
     */
    public HoppersConfig makeNeighbor(int newRow, int newCol, int removeRow, int removeCol,
                                      int originalRow, int originalCol, String frog) throws IndexOutOfBoundsException {
        String[][] newHopperBoard = new String[rowDim][colDim];
        for (int iRow = 0; iRow < rowDim; iRow++) {
            for (int iCol = 0; iCol < colDim; iCol++) {
                if (iRow == newRow && iCol == newCol) {
                    newHopperBoard[iRow][iCol] = frog;
                } else if (iRow == removeRow && iCol == removeCol) {
                    newHopperBoard[iRow][iCol] = ".";
                } else if (iRow == originalRow && iCol == originalCol) {
                    newHopperBoard[iRow][iCol] = ".";
                } else {
                    newHopperBoard[iRow][iCol] = this.hopperBoard[iRow][iCol];
                }
            }
        }
        return new HoppersConfig(newHopperBoard);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof HoppersConfig otherHoppers) {
            for (int i = 0; i < rowDim; i++) {
                for (int j = 0; j < colDim; j++) {
                    if (!(this.hopperBoard[i][j].equals(otherHoppers.hopperBoard[i][j]))) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(hopperBoard);
    }

    @Override
    public String toString() {
        String finalString = "";

        String firstRow = "   ";
        for (int i = 0; i < colDim; i++) {
            firstRow += i + " ";
        }
        finalString += firstRow + "\n";

        String secondRow = "  ";
        for (int i = 0; i < 2*colDim; i++) {
            secondRow += "-";
        }
        finalString += secondRow + "\n";

        for (int iRow = 0; iRow < rowDim; iRow++) {
            String rowString = iRow + "| ";
            for (int iCol = 0; iCol < colDim; iCol++) {
                rowString += hopperBoard[iRow][iCol] + " ";
            }
            finalString += rowString + "\n";
        }
        return finalString;
    }

}