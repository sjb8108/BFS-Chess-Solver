package puzzles.chess.solver;

import puzzles.chess.model.ChessConfig;
import puzzles.common.solver.Solver;

import java.io.*;
import java.util.Scanner;

public class Chess {
    public static void main(String[] args) throws IOException {
        //NEED TO FIX DIM STUFF CAUSE THEY ARENT SQUARES SOMETIMES!
        if (args.length != 1) {
            System.out.println("Usage: java Chess filename");
        } else {
            BufferedReader chessLoader = new BufferedReader(new FileReader(args[0]));
            String[] dims = chessLoader.readLine().split(" ");
            int dim = Integer.parseInt(dims[0]);
            String[][] chessBoard = new String[dim][dim];
            for(int i = 0; i < dim; i++){
                String l = chessLoader.readLine();
                String[] f = l.split("\\s+");
                for (int j = 0; j < f.length; j++){
                    String chessSpot = f[j];
                    chessBoard[i][j] = chessSpot;
                }
            }
            System.out.println("File: "+args[0]);
            for (int i = 0; i<dim; i++) {
                for (int j = 0; j<dim; j++) {
                    System.out.print(chessBoard[i][j] + " ");
                }
                System.out.println();
            }
            ChessConfig initialConfig = new ChessConfig(chessBoard);
            Solver.solve(initialConfig);
        }
    }
}
