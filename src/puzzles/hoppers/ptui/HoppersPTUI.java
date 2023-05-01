package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;
import puzzles.chess.ptui.ChessPTUI;

import java.io.IOException;
import java.util.Scanner;

/**
 * The HoppersPIUI class used to display a PTUI of the Hoppers game
 * Observes the HopperModel and prints information to the standard output
 * Implements an MVC design
 *
 *  @author Maanav Contractor (mpc9618)
 */

public class HoppersPTUI implements Observer<HoppersModel, String> {
    private HoppersModel model;

    /**
     * Initialization function for the PTUI
     * @param filename file being loaded
     * @throws IOException
     */
    public void init(String filename) throws IOException {
        this.model = new HoppersModel(filename);
        System.out.println("Loaded :" + filename);
        System.out.println(this.model.getCurrentConfig().toString());
        this.model.addObserver(this);
        displayHelp();
    }

    @Override
    public void update(HoppersModel model, String data) {
        if (data.equals("Complete")) {
            System.out.println("You completed the puzzle!");
            System.out.println(model.getCurrentConfig().toString());
        } else if (data.equals("new game")) {
            System.out.println(model.getCurrentConfig().toString());
        } else {
            System.out.println(data);
            System.out.println(model.getCurrentConfig().toString());
        }
    }

    /**
     * Displays help message to user, outlining all legal commands and their functions
     */
    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    /**
     * Run function called by main to start PTUI application
     */
    public void run() {
        Scanner in = new Scanner( System.in );
        while (true) {
            System.out.print( "> " );
            String line = in.nextLine();
                if (line.startsWith( "q" )) {
                    break;
                } else if (line.startsWith( "h" )) {
                    this.model.hint();
                } else if (line.startsWith( "l" )) {
                    String[] loading = line.split(" ");
                    if (loading[1].startsWith("d")) {
                        this.model.load(loading[1]);
                    } else {
                        this.model.load("data/hoppers/" + loading[1]);
                    }
                    System.out.println("Loaded: " + loading[1]);
                } else if (line.startsWith( "s" )) {
                    String[] args = line.split(" ");
                    int row = Integer.parseInt(args[1]);
                    int col = Integer.parseInt(args[2]);
                    this.model.selectPiece(row, col);
                } else if (line.startsWith( "r" )) {
                    this.model.reset();
                } else {
                    System.out.println();
                    displayHelp();
                }
            }
        }

    /**
     * Main function for HoppersPTUI
     * Initializes the PTUI and runs it
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            try {
                HoppersPTUI ptui = new HoppersPTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}
