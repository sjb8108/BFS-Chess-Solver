package puzzles.chess.model;

import puzzles.common.Observer;
import puzzles.common.solver.Solver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ChessModel {
    /** the collection of observers of this model */
    private final List<Observer<ChessModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private ChessConfig currentConfig;
    private ChessConfig initalConfigForRestart;
    private int rowdim;
    private int coldim;
    private int captureOrNot;
    private String selectedPiece;
    private int selectedRow;
    private int selectedCol;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<ChessModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    public ChessModel(String filename) throws IOException {
        BufferedReader chessLoader = new BufferedReader(new FileReader(filename));
        System.out.println("Loaded: "+filename);
        String[] dims = chessLoader.readLine().split(" ");
        int rowdim = Integer.parseInt(dims[0]);
        int coldim = Integer.parseInt(dims[1]);
        this.rowdim = rowdim;
        this.coldim = coldim;
        String[][] chessBoard = new String[rowdim][coldim];
        for(int i = 0; i < rowdim; i++){
            String l = chessLoader.readLine();
            String[] f = l.split("\\s+");
            for (int j = 0; j < f.length; j++){
                String chessSpot = f[j];
                chessBoard[i][j] = chessSpot;
            }
        }
        for (int i = 0; i<rowdim; i++) {
            for (int j = 0; j<coldim; j++) {
                System.out.print(chessBoard[i][j] + " ");
            }
            System.out.println();
        }
        this.currentConfig = new ChessConfig(chessBoard);
        this.initalConfigForRestart = currentConfig;
        this.captureOrNot = 1;
        this.selectedPiece = "";
        this.selectedRow = 0;
        this.selectedCol = 0;
    }

    public ChessConfig getCurrentConfig() {
        return currentConfig;
    }

    public void newGame(String filename) throws IOException {
        System.out.println("Loaded: "+filename);
        BufferedReader chessLoader = new BufferedReader(new FileReader(filename));
        String[] dims = chessLoader.readLine().split(" ");
        int rowdim = Integer.parseInt(dims[0]);
        int coldim = Integer.parseInt(dims[1]);
        this.rowdim = rowdim;
        this.coldim = coldim;
        String[][] chessBoard = new String[rowdim][coldim];
        for(int i = 0; i < rowdim; i++){
            String l = chessLoader.readLine();
            String[] f = l.split("\\s+");
            for (int j = 0; j < f.length; j++){
                String chessSpot = f[j];
                chessBoard[i][j] = chessSpot;
            }
        }
        for (int i = 0; i<rowdim; i++) {
            for (int j = 0; j<coldim; j++) {
                System.out.print(chessBoard[i][j] + " ");
            }
            System.out.println();
        }
        this.currentConfig = new ChessConfig(chessBoard);
        this.initalConfigForRestart = currentConfig;
        this.captureOrNot = 1;
        this.selectedPiece = "";
        this.selectedRow = 0;
        this.selectedCol = 0;
        alertObservers("new game");
    }
    public void useHint(){

    }
    public void restart(){
        this.currentConfig = this.initalConfigForRestart;
        this.captureOrNot = 1;
        this.selectedPiece = "";
        this.selectedRow = 0;
        this.selectedCol = 0;
        alertObservers("Puzzle Restart!");
    }
    public void selectPieces(int row, int col){
        String[][] board = this.currentConfig.getChessBoard();
        if (this.captureOrNot % 2== 1){
            if (board[row][col].equals(".")){
                alertObservers("You choose an empty space");
            } else {
                this.selectedRow = row;
                this.selectedCol = col;
                this.selectedPiece = board[row][col];
                this.captureOrNot = 2;
                alertObservers("Selected ("+row+", "+col+")");
            }
        } else {
            if (board[row][col].equals(".")){
                alertObservers("You choose an empty space");
            } else{
                //Do checking to see if the move is valid
                this.currentConfig = new ChessConfig(this.currentConfig.makeNeighbor(selectedRow, selectedCol, row, col, selectedPiece));
                this.captureOrNot = 1;
                alertObservers("Taken");
            }
        }
    }
}
