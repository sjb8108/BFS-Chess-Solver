package puzzles.chess.model;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * The model for the chess game
 */
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
    /**
     * Makes the model for the PTUI and GUI
     * @param filename the name of the file where
     *                 the initial chess config is stored
     * @throws IOException
     */
    public ChessModel(String filename) throws IOException {
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
        this.currentConfig = new ChessConfig(chessBoard);
        this.initalConfigForRestart = currentConfig;
        this.captureOrNot = 1;
        this.selectedPiece = "";
        this.selectedRow = 0;
        this.selectedCol = 0;
    }
    /**
     * Gets and returns the current chess config
     * @return the current chess config
     */
    public ChessConfig getCurrentConfig() {
        return currentConfig;
    }
    /**
     * Makes a new game by loading in the filename and replacing
     * fields with updated and correct values of the new loaded file
     * @param filename
     */
    public void newGame(String filename) {
        try {
            BufferedReader chessLoader = new BufferedReader(new FileReader(filename));
            String[] dims = chessLoader.readLine().split(" ");
            int rowdim = Integer.parseInt(dims[0]);
            int coldim = Integer.parseInt(dims[1]);
            this.rowdim = rowdim;
            this.coldim = coldim;
            String[][] chessBoard = new String[rowdim][coldim];
            for (int i = 0; i < rowdim; i++) {
                String l = chessLoader.readLine();
                String[] f = l.split("\\s+");
                for (int j = 0; j < f.length; j++) {
                    String chessSpot = f[j];
                    chessBoard[i][j] = chessSpot;
                }
            }
            this.currentConfig = new ChessConfig(chessBoard);
            this.initalConfigForRestart = currentConfig;
            this.captureOrNot = 1;
            this.selectedPiece = "";
            this.selectedRow = 0;
            this.selectedCol = 0;
            String[] fileDirectory = filename.split("/");
            if (filename.contains("project")){
                fileDirectory = filename.split(Pattern.quote(File.separator));
                alertObservers("Loaded: "+fileDirectory[fileDirectory.length-1]);
            } else{
                alertObservers("Loaded: " + fileDirectory[2]);
            }
        }catch (Exception e){
            alertObservers("Failed to load: "+filename);
        }
    }
    /**
     * Gives a hint to the user by providing the next movement to solve the puzzle
     */
    public void useHint(){
        if (this.currentConfig.isSolution() == true){
            alertObservers("You finished the puzzle!");
        } else {
            Map<Configuration, Configuration> predecessor = new HashMap<>();
            Configuration startConfig;
            startConfig = this.currentConfig;
            predecessor.put(startConfig, null);
            Queue<Configuration> toVisit = new LinkedList<>();
            toVisit.offer(startConfig);
            while (!toVisit.isEmpty() && !toVisit.peek().isSolution()) {
                Configuration current = toVisit.remove();
                for (Configuration neighbors : current.getNeighbors()) {
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
                ChessConfig hintConfig = (ChessConfig) path.get(1);
                this.currentConfig = hintConfig;
                if (this.currentConfig.isSolution() == true) {
                    alertObservers("You finished the puzzle!");
                } else {
                    alertObservers("Next Step!");
                }
                this.captureOrNot = 1;
            }
        }
    }
    /**
     * Restarts the puzzle to the initial state
     */
    public void restart(){
        this.currentConfig = this.initalConfigForRestart;
        this.captureOrNot = 1;
        this.selectedPiece = "";
        this.selectedRow = 0;
        this.selectedCol = 0;
        alertObservers("Puzzle Reset!");
    }
    /**
     * Sees if when the user picks out a row and col if the user
     * can progress the puzzle game. Whether selecting an empty space
     * or trying to move a chess piece illegally can halt the progress
     * of the game
     * @param row the row the user selected
     * @param col the column the user selected
     */
    public void selectPieces(int row, int col){
        if (row > rowdim-1 || col > coldim-1){
            alertObservers("You choose a space outside the chess board");
        } else {
            String[][] board = this.currentConfig.getChessBoard();
            if (this.captureOrNot % 2 == 1) {
                if (board[row][col].equals(".")) {
                    alertObservers("You choose an empty space");
                } else {
                    this.selectedRow = row;
                    this.selectedCol = col;
                    this.selectedPiece = board[row][col];
                    this.captureOrNot = 2;
                    alertObservers("Selected (" + row + ", " + col + ")");
                }
            } else {
                if (board[row][col].equals(".")) {
                    alertObservers("Can't capture from (" + selectedRow + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                    this.captureOrNot = 1;
                } else {
                    if (this.selectedPiece.equals("P")) {
                        if (checkPawns(row, col) == true) {
                            this.currentConfig = new ChessConfig(this.currentConfig.makeNeighbor(selectedRow, selectedCol, row, col, selectedPiece));
                            this.captureOrNot = 1;
                            if (this.currentConfig.isSolution() == true) {
                                alertObservers("You finished the puzzle!");
                            } else {
                                alertObservers("Captured from (" + selectedRow + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                            }
                        } else {
                            this.captureOrNot = 1;
                            alertObservers("Can't capture from (" + selectedRow + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                        }
                    } else if (this.selectedPiece.equals("N")) {
                        if (checkKnights(row, col) == true) {
                            this.currentConfig = new ChessConfig(this.currentConfig.makeNeighbor(selectedRow, selectedCol, row, col, selectedPiece));
                            this.captureOrNot = 1;
                            if (this.currentConfig.isSolution() == true) {
                                alertObservers("You finished the puzzle!");
                            } else {
                                alertObservers("Captured from (" + selectedRow + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                            }
                        } else {
                            this.captureOrNot = 1;
                            alertObservers("Can't capture from (" + selectedRow + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                        }
                    } else if (this.selectedPiece.equals("K")) {
                        if (checkKings(row, col) == true) {
                            this.currentConfig = new ChessConfig(this.currentConfig.makeNeighbor(selectedRow, selectedCol, row, col, selectedPiece));
                            this.captureOrNot = 1;
                            if (this.currentConfig.isSolution() == true) {
                                alertObservers("You finished the puzzle!");
                            } else {
                                alertObservers("Captured from (" + selectedRow + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                            }
                        } else {
                            this.captureOrNot = 1;
                            alertObservers("Can't capture from (" + selectedRow + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                        }
                    } else if (this.selectedPiece.equals("R")) {
                        if (checkRooks(row, col) == true) {
                            this.currentConfig = new ChessConfig(this.currentConfig.makeNeighbor(selectedRow, selectedCol, row, col, selectedPiece));
                            this.captureOrNot = 1;
                            if (this.currentConfig.isSolution() == true) {
                                alertObservers("You finished the puzzle!");
                            } else {
                                alertObservers("Captured from (" + selectedRow + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                            }
                        } else {
                            this.captureOrNot = 1;
                            alertObservers("Can't capture from (" + selectedRow + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                        }
                    } else if (this.selectedPiece.equals("B")) {
                        if (checkBishops(row, col) == true) {
                            this.currentConfig = new ChessConfig(this.currentConfig.makeNeighbor(selectedRow, selectedCol, row, col, selectedPiece));
                            this.captureOrNot = 1;
                            if (this.currentConfig.isSolution() == true) {
                                alertObservers("You finished the puzzle!");
                            } else {
                                alertObservers("Captured from (" + selectedRow + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                            }
                        } else {
                            this.captureOrNot = 1;
                            alertObservers("Can't capture from (" + selectedRow + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                        }
                    } else {
                        if (checkQueen(row, col) == true) {
                            this.currentConfig = new ChessConfig(this.currentConfig.makeNeighbor(selectedRow, selectedCol, row, col, selectedPiece));
                            this.captureOrNot = 1;
                            if (this.currentConfig.isSolution() == true) {
                                alertObservers("You finished the puzzle!");
                            } else {
                                alertObservers("Captured from (" + selectedRow + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                            }
                        } else {
                            this.captureOrNot = 1;
                            alertObservers("Can't capture from (" + selectedRow + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                        }
                    }
                }
            }
        }
    }
    /**
     * Sees if the pawns movement is legal
     * @param row the row of the pawn
     * @param col the column of the pawn
     * @return true if the pawn can move the selected
     * row and column false if not
     */
    public boolean checkPawns(int row, int col){
        if (row == selectedRow-1 && col == selectedCol+1){
            return true;
        }
        if (row == selectedRow-1 && col == selectedCol-1){
            return true;
        }
        return false;
    }
    /**
     * Sees if the kings movement is legal
     * @param row the row of the king
     * @param col the column of the king
     * @return true if the king can move the selected
     * row and column false if not
     */
    public boolean checkKings(int row, int col){
        if(row == selectedRow-1 && col == selectedCol){
            return true;
        }
        if (row == selectedRow+1 && col == selectedCol){
            return true;
        }
        if (row == selectedRow && col == selectedCol-1){
            return true;
        }
        if (row == selectedRow && col == selectedCol+1){
            return true;
        }
        if (row == selectedRow+1 && col == selectedCol+1){
            return true;
        }
        if (row == selectedRow-1 && col == selectedCol-1){
            return true;
        }
        if (row == selectedRow+1 && col == selectedCol-1){
            return true;
        }
        try{
            if (!(this.currentConfig.getChessBoard()[row-1][col+1].equals("."))){
                return true;
            }
        } catch (IndexOutOfBoundsException e){}
        if (row == selectedRow-1 && col == selectedCol+1){
            return true;
        }
        return false;
    }
    /**
     * Sees if the knights movement is legal
     * @param row the row of the knight
     * @param col the column of the knight
     * @return true if the knight can move the selected
     * row and column false if not
     */
    public boolean checkKnights(int row, int col){
        if (row == selectedRow-2 && col == selectedCol-1){
            return true;
        }
        if (row == selectedRow-2 && col == selectedCol+1){
            return true;
        }
        if (row == selectedRow-1 && col == selectedCol+2){
            return true;
        }
        if (row == selectedRow+1 && col == selectedCol+2){
            return true;
        }
        if (row == selectedRow+2 && col == selectedCol+1){
            return true;
        }
        if (row == selectedRow+2 && col == selectedCol-1){
            return true;
        }
        if (row == selectedRow+1 && col == selectedCol-2){
            return true;
        }
        if (row == selectedRow-1 && col == selectedCol-2){
            return true;
        }
        return false;
    }
    /**
     * Sees if the rooks movement is legal
     * @param row the row of the rook
     * @param col the column of the rook
     * @return true if the rook can move the selected
     * row and column false if not
     */
    public boolean checkRooks(int row, int col){
        if (selectedRow-row > 0 && col == selectedCol){
            for(int i = selectedRow-1; i>=0; i-=1){
                if (!(this.currentConfig.getChessBoard()[i][col].equals("."))){
                    if (row == i && col == selectedCol){
                        return true;
                    }
                    return false;
                }
            }
        }
        if (selectedRow-row < 0 && col == selectedCol) {
            for (int i = selectedRow + 1; i < rowdim; i += 1) {
                if (!(this.currentConfig.getChessBoard()[i][col].equals("."))) {
                    if (row == i && col == selectedCol) {
                        return true;
                    }
                    return false;
                }
            }
        }
        if (row == selectedRow && selectedCol-col > 0) {
            for (int i = selectedCol - 1; i >= 0; i -= 1) {
                if (!(this.currentConfig.getChessBoard()[row][i].equals("."))) {
                    if (row == selectedRow && col == i) {
                        return true;
                    }
                    return false;
                }
            }
        }
        if (row == selectedRow && selectedCol-col < 0) {
            for (int i = selectedCol + 1; i < coldim; i++) {
                if (!(this.currentConfig.getChessBoard()[row][i].equals("."))) {
                    if (row == selectedRow && col == i) {
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }
    /**
     * Sees if the bishops movement is legal
     * @param row the row of the bishop
     * @param col the column of the bishop
     * @return true if the bishop can move the selected
     * row and column false if not
     */
    public boolean checkBishops(int row, int col){
        int dummyrow;
        int dummycol;
        if (selectedRow-row < 0 && selectedCol-col < 0) {
            dummyrow = selectedRow + 1;
            dummycol = selectedCol + 1;
            while (dummyrow < rowdim && dummycol < coldim) {
                if (!(this.currentConfig.getChessBoard()[dummyrow][dummycol].equals("."))) {
                    if (row == dummyrow && col == dummycol) {
                        return true;
                    }
                    return false;
                }
                dummyrow += 1;
                dummycol += 1;
            }
        }
        if (selectedRow-row > 0 && selectedCol-col > 0) {
            dummyrow=selectedRow-1;
            dummycol=selectedCol-1;
            while (dummyrow >= 0 && dummycol >= 0){
                if (!(this.currentConfig.getChessBoard()[dummyrow][dummycol].equals("."))) {
                    if (row == dummyrow && col == dummycol) {
                        return true;
                    }
                    return false;
                }
                dummyrow-=1;
                dummycol-=1;
            }
        }
        if (selectedRow-row < 0 && selectedCol-col > 0) {
            dummyrow=selectedRow+1;
            dummycol=selectedCol-1;
            while (dummyrow < rowdim && dummycol >= 0){
                if (!(this.currentConfig.getChessBoard()[dummyrow][dummycol].equals("."))) {
                    if (row == dummyrow && col == dummycol) {
                        return true;
                    }
                    return false;
                }
                dummyrow+=1;
                dummycol-=1;
            }
        }
        if (selectedRow-row > 0 && selectedCol-col < 0){
            dummyrow=selectedRow-1;
            dummycol=selectedCol+1;
            while(dummyrow >= 0 && dummycol < coldim){
                if (!(this.currentConfig.getChessBoard()[dummyrow][dummycol].equals("."))) {
                    if (row == dummyrow && col == dummycol) {
                        return true;
                    }
                    return false;
                }
                dummyrow-=1;
                dummycol+=1;
            }
        }
        return false;
    }
    /**
     * Sees if the queens movement is legal
     * @param row the row of the queen
     * @param col the column of the queen
     * @return true if the queen can move the selected
     * row and column false if not
     */
    public boolean checkQueen(int row, int col){
        if (selectedRow-row > 0 && col == selectedCol){
            for(int i = selectedRow-1; i>=0; i-=1){
                if (!(this.currentConfig.getChessBoard()[i][col].equals("."))){
                    if (row == i && col == selectedCol){
                        return true;
                    }
                    return false;
                }
            }
        }
        if (selectedRow-row < 0 && col == selectedCol) {
            for (int i = selectedRow + 1; i < rowdim; i += 1) {
                if (!(this.currentConfig.getChessBoard()[i][col].equals("."))) {
                    if (row == i && col == selectedCol) {
                        return true;
                    }
                    return false;
                }
            }
        }
        if (row == selectedRow && selectedCol-col > 0) {
            for (int i = selectedCol - 1; i >= 0; i -= 1) {
                if (!(this.currentConfig.getChessBoard()[row][i].equals("."))) {
                    if (row == selectedRow && col == i) {
                        return true;
                    }
                    return false;
                }
            }
        }
        if (row == selectedRow && selectedCol-col < 0) {
            for (int i = selectedCol + 1; i < coldim; i++) {
                if (!(this.currentConfig.getChessBoard()[row][i].equals("."))) {
                    if (row == selectedRow && col == i) {
                        return true;
                    }
                    return false;
                }
            }
        }
        int dummyrow;
        int dummycol;
        if (selectedRow-row < 0 && selectedCol-col < 0) {
            dummyrow = selectedRow + 1;
            dummycol = selectedCol + 1;
            while (dummyrow < rowdim && dummycol < coldim) {
                if (!(this.currentConfig.getChessBoard()[dummyrow][dummycol].equals("."))) {
                    if (row == dummyrow && col == dummycol) {
                        return true;
                    }
                    return false;
                }
                dummyrow += 1;
                dummycol += 1;
            }
        }
        if (selectedRow-row > 0 && selectedCol-col > 0) {
            dummyrow=selectedRow-1;
            dummycol=selectedCol-1;
            while (dummyrow >= 0 && dummycol >= 0){
                if (!(this.currentConfig.getChessBoard()[dummyrow][dummycol].equals("."))) {
                    if (row == dummyrow && col == dummycol) {
                        return true;
                    }
                    return false;
                }
                dummyrow-=1;
                dummycol-=1;
            }
        }
        if (selectedRow-row < 0 && selectedCol-col > 0) {
            dummyrow=selectedRow+1;
            dummycol=selectedCol-1;
            while (dummyrow < rowdim && dummycol >= 0){
                if (!(this.currentConfig.getChessBoard()[dummyrow][dummycol].equals("."))) {
                    if (row == dummyrow && col == dummycol) {
                        return true;
                    }
                    return false;
                }
                dummyrow+=1;
                dummycol-=1;
            }
        }
        if (selectedRow-row > 0 && selectedCol-col < 0){
            dummyrow=selectedRow-1;
            dummycol=selectedCol+1;
            while(dummyrow >= 0 && dummycol < coldim){
                if (!(this.currentConfig.getChessBoard()[dummyrow][dummycol].equals("."))) {
                    if (row == dummyrow && col == dummycol) {
                        return true;
                    }
                    return false;
                }
                dummyrow-=1;
                dummycol+=1;
            }
        }
        return false;
    }
}