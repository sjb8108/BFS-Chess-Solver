package puzzles.hoppers.solver;

import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.*;

/**
 * The full representation of the Hoppers puzzle.
 * Reads initial configuration from command line, and runs
 * Solver to find the shortest solution.
 *
 *  @author Maanav Contractor (mpc9618)
 */

public class Hoppers {
    public static void main(String[] args) throws IOException {
        try {
            if (args.length != 1) {
                System.out.println("Usage: java Hoppers filename");
            } else {
                BufferedReader hopperLoader = new BufferedReader( new FileReader(args[0]));
                String[] dimensions = hopperLoader.readLine().split(" ");
                int rowDim = Integer.parseInt(dimensions[0]);
                int colDim = Integer.parseInt(dimensions[1]);
                String[][] hopperBoard = new String[rowDim][colDim];

                for (int iRow = 0; iRow < rowDim; iRow++) {
                    String[] line = hopperLoader.readLine().split("\\s+");
                    for (int iCol = 0; iCol < colDim; iCol++) {
                        hopperBoard[iRow][iCol] = line[iCol];
                    }
                }

                System.out.println("File: " + args[0]);
                for (int iRow = 0; iRow < rowDim; iRow++) {
                    for (int iCol = 0; iCol < colDim; iCol++) {
                        System.out.print(hopperBoard[iRow][iCol] + " ");
                    }
                    System.out.println();
                }
                HoppersConfig initialConfig = new HoppersConfig(hopperBoard);
                Solver.solve(initialConfig);

            }
        } catch (FileNotFoundException fnfe) {System.out.println("Invalid file exception thrown");}
    }
}
