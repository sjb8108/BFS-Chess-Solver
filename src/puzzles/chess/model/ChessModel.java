package puzzles.chess.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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

    public void newGame(String filename) {
        try {
            BufferedReader chessLoader = new BufferedReader(new FileReader(filename));
            System.out.println("Loaded: " + filename);
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
            for (int i = 0; i < rowdim; i++) {
                for (int j = 0; j < coldim; j++) {
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
        }catch (IOException e){
            alertObservers("Failed to load: "+filename);
        }
    }
    public void useHint(){
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
        if ( toVisit.isEmpty() ) {
            alertObservers("Puzzle is not solvable");
        }
        else {
            Configuration finishConfig = toVisit.remove();
            List<Configuration> path = new LinkedList<>();
            path.add( 0, finishConfig );
            Configuration node = predecessor.get( finishConfig );
            while ( node != null ) {
                path.add( 0, node );
                node = predecessor.get( node );
            }
            ChessConfig hintConfig = (ChessConfig) path.get(1);
            this.currentConfig = hintConfig;
            alertObservers("Next Step!");
            this.captureOrNot = 1;
        }
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
                alertObservers("Can't capture from ("+selectedRow+", "+selectedCol+") to ("+row+", "+col+")");
            } else{
                if (this.selectedPiece.equals("P")){
                    if(checkPawns(row, col) == true){
                        this.currentConfig = new ChessConfig(this.currentConfig.makeNeighbor(selectedRow, selectedCol, row, col, selectedPiece));
                        this.captureOrNot = 1;
                        alertObservers("Captured from ("+selectedRow+", "+selectedCol+") to ("+row+", "+col+")");
                    } else{
                        alertObservers("Can't capture from ("+selectedRow+", "+selectedCol+") to ("+row+", "+col+")");
                    }
                } else if (this.selectedPiece.equals("N")) {
                    if(checkKnights(row, col) == true){
                        this.currentConfig = new ChessConfig(this.currentConfig.makeNeighbor(selectedRow, selectedCol, row, col, selectedPiece));
                        this.captureOrNot = 1;
                        alertObservers("Captured from ("+selectedRow+", "+selectedCol+") to ("+row+", "+col+")");
                    } else{
                        alertObservers("Can't capture from ("+selectedRow+", "+selectedCol+") to ("+row+", "+col+")");
                    }
                } else if (this.selectedPiece.equals("K")) {
                    if(checkKings(row, col) == true){
                        this.currentConfig = new ChessConfig(this.currentConfig.makeNeighbor(selectedRow, selectedCol, row, col, selectedPiece));
                        this.captureOrNot = 1;
                        alertObservers("Captured from ("+selectedRow+", "+selectedCol+") to ("+row+", "+col+")");
                    } else{
                        alertObservers("Can't capture from ("+selectedRow+", "+selectedCol+") to ("+row+", "+col+")");
                    }
                } else if (this.selectedPiece.equals("R")) {
                    if(checkRooks(row, col) == true){
                        this.currentConfig = new ChessConfig(this.currentConfig.makeNeighbor(selectedRow, selectedCol, row, col, selectedPiece));
                        this.captureOrNot = 1;
                        alertObservers("Captured from ("+selectedRow+", "+selectedCol+") to ("+row+", "+col+")");
                    } else{
                        alertObservers("Can't capture from ("+selectedRow+", "+selectedCol+") to ("+row+", "+col+")");
                    }
                }
            }
        }
    }
    public boolean checkPawns(int row, int col){
        if (row == selectedRow-1 && col == selectedCol+1){
            return true;
        }
        if (row == selectedRow-1 && col == selectedCol-1){
            return true;
        }
        return false;
    }
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
    public boolean checkBishops(int row, int col){
        return false;
    }
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
        return false;
    }
}
