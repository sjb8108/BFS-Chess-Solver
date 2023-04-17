package puzzles.chess.solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Chess {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 1) {
            System.out.println("Usage: java Chess filename");
        } else {
            Scanner chessLoader = new Scanner(new File(args[0]));

        }
    }
}
