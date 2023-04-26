package puzzles.chess.ptui;
import puzzles.common.Observer;
import puzzles.chess.model.ChessModel;
import java.io.IOException;
import java.util.Scanner;
/**
 * The chess puzzle game in view of a plain text user interface
 */
public class ChessPTUI implements Observer<ChessModel, String> {
    private ChessModel model;
    /**
     * Makes the model,prints the config,adds an observer,and displays the commands
     * @param filename the name of the file where the initial chess config is
     * @throws IOException
     */
    public void init(String filename) throws IOException {
        this.model = new ChessModel(filename);
        System.out.println("Loaded: "+filename);
        System.out.println(this.model.getCurrentConfig().toString());
        this.model.addObserver(this);
        displayHelp();
    }
    /**
     * Updates the PTUI by displaying messages depending on what the user does
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param data optional data the server.model can send to the observer
     */
    @Override
    public void update(ChessModel model, String data) {
        if (data.equals("Complete")){
            System.out.println("You completed this puzzle!");
            System.out.println(model.getCurrentConfig().toString());
        } else if (data.contains("Loaded:")) {
            System.out.println(data);
            System.out.println(model.getCurrentConfig().toString());
        } else {
            System.out.println(data);
            System.out.println(model.getCurrentConfig().toString());
        }

    }
    /**
     * Prints out the list of commands to help the user play the game
     */
    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }
    /**
     * Runs the game and is the controller for the user
     * @throws IOException
     */
    public void run() throws IOException {
        Scanner in = new Scanner( System.in );
        while(true) {
            System.out.print( "> " );
            String line = in.nextLine();
            if (line.startsWith("q")){
                break;
            } else if (line.startsWith("l")) {
                String[] args = line.split(" ");
                this.model.newGame(args[1]);
            } else if (line.startsWith("r")) {
                this.model.restart();
            } else if (line.startsWith("h")) {
                this.model.useHint();
            } else if (line.startsWith("s")) {
                String[] args = line.split(" ");
                int row = Integer.parseInt(args[1]);
                int col = Integer.parseInt(args[2]);
                this.model.selectPieces(row, col);
            }

        }
    }

    /**
     * Makes the PTUI and calls init and calls the run fuction
     * @param args - has the filename at position 0
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ChessPTUI filename");
        } else {
            try {
                ChessPTUI ptui = new ChessPTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}