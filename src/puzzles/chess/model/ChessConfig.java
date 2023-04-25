package puzzles.chess.model;
import puzzles.common.solver.Configuration;
import java.util.*;
/**
 * A representation of one layout of a chessboard/
 * chess config
 */
public class ChessConfig implements Configuration {
    private String[][] chessBoard;
    private int rowdim;
    private int coldim;
    /**
     * Makes the chessboard and sets the dimensions
     * @param chessBoard a 2D array of strings that represent the pieces
     *                  of a chess board and where they are located
     */
    public ChessConfig(String[][] chessBoard) {
        this.chessBoard = chessBoard;
        rowdim = chessBoard.length;
        coldim = chessBoard[0].length;
    }
    /**
     * Gets and return the chessbaord
     * @return the chessboard
     */
    public String[][] getChessBoard() {
        return chessBoard;
    }
    /**
     * Gets and returns the row dimension number
     * @return the row dimension
     */
    public int getRowdim() {
        return rowdim;
    }
    /**
     * Gets and returns the column dimension number
     * @return the column dimension
     */
    public int getColdim() {
        return coldim;
    }
    /**
     * Checks to see if the current configuration
     * is the solution to the puzzle
     * @return true if the puzzle is finished and false if not
     */
    @Override
    public boolean isSolution() {
        int pieceCount = 0;
        for (int i = 0; i<rowdim; i++) {
            for (int j = 0; j<coldim; j++) {
                if (!(this.chessBoard[i][j].equals("."))){
                    pieceCount+=1;
                }
            }
        }
        if (pieceCount == 1){
            return true;
        }
        return false;
    }
    /**
     * Goes through the chess board and gets configs
     * that happen when a piece captures another piece
     * @return A set of chess configs that could happen
     * when a piece moves and captures
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        Set neighbors = new LinkedHashSet<ChessConfig>();
        for (int i = 0; i<rowdim; i++) {
            for (int j = 0; j<coldim; j++) {
                if (this.chessBoard[i][j].equals("P")){
                    neighbors.addAll(getPawnNeighbors(i,j));
                } else if (this.chessBoard[i][j].equals("N")) {
                    neighbors.addAll(getKnightNeighbors(i,j));
                } else if (this.chessBoard[i][j].equals("K")) {
                    neighbors.addAll(getKingNeighbors(i,j));
                } else if (this.chessBoard[i][j].equals("R")) {
                    neighbors.addAll(getRookNeighbors(i,j));
                } else if (this.chessBoard[i][j].equals("B")) {
                    neighbors.addAll(getBishopNeighbors(i,j));
                } else if (this.chessBoard[i][j].equals("Q")) {
                    neighbors.addAll(getQueenNeighbors(i,j));
                }
            }
        }
        return neighbors;
    }
    /**
     * Sees if a chess config the same as another chess config
     * @param other the other chess config to be compared
     * @return true if the objects are equal and false if not
     */
    @Override
    public boolean equals(Object other){
        if(!(other instanceof ChessConfig)) {
            return false;
        }
        else
        {
            ChessConfig otherChess = (ChessConfig) other;
            for (int i = 0; i<rowdim; i++){
                for(int j = 0; j<coldim; j++){
                    if (!(this.chessBoard[i][j].equals(otherChess.chessBoard[i][j]))){
                        return false;
                    }
                }
            }
            return true;
        }
    }
    /**
     * Coverts the chess config into hash code
     * @return the hash coded chess config
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(chessBoard);
    }
    /**
     * A string representation of a chess config
     * @return the string representation of the chess config
     */
    @Override
    public String toString() {
        List<String> listOfStringRow = new ArrayList<>();
        String chessString = "";
        String stringRowOne = "   ";
        for (int i = 0; i < coldim; i++){
            stringRowOne+=(i + " ");
        }
        String stringRowTwo = "  ";
        for (int i = 0; i < 2*coldim; i++){
            stringRowTwo+="-";
        }
        listOfStringRow.add(stringRowOne);
        listOfStringRow.add(stringRowTwo);
        int counter = 0;
        for (int i = 0; i<rowdim; i++) {
            String stringRow = "";
            for (int j = 0; j<coldim; j++) {
                if (j ==0){
                    stringRow+=(counter +"| ");
                    counter+=1;
                }
                stringRow+=chessBoard[i][j] + " ";
            }
            listOfStringRow.add(stringRow);
        }
        for (String s: listOfStringRow){
            chessString+=(s+"\n");
        }
        return chessString;
    }
    /**
     * Makes a chess board after a piece get cpatures
     * @param startRow - the row of the piece being moved
     * @param startCol - the column of the piece being moved
     * @param endRow - the row of the piece getting captured
     * @param endCol - the column of the piece getting captured
     * @param piece - the piece being moved
     * @return
     */
    public String[][] makeNeighbor(int startRow, int startCol, int endRow, int endCol, String piece){
        String[][] newChessBoard = new String[rowdim][coldim];
        for (int i = 0; i<rowdim; i++) {
            for (int j = 0; j<coldim; j++) {
                if (i == startRow && j == startCol){
                    newChessBoard[i][j] = ".";
                } else if (i == endRow && j == endCol) {
                    newChessBoard[i][j] = piece;
                } else {
                    newChessBoard[i][j] = this.chessBoard[i][j];
                }
            }
        }
        return newChessBoard;
    }
    /**
     * Gets the configs dealing when a pawn captures another piece
     * @param row - the row of where the pawn is located
     * @param col - the column of where the pawn is located
     * @return a list of configs that deal with when a pawn captures a piece
     */
    public Collection<Configuration> getPawnNeighbors(int row, int col){
        Set pawnNeighbors = new LinkedHashSet<ChessConfig>();
        try{
            if (!(this.chessBoard[row-1][col+1].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row-1,col+1, "P"));
                pawnNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}

        try{
            if (!(this.chessBoard[row-1][col-1].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row-1,col-1, "P"));
                pawnNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        return pawnNeighbors;
    }
    /**
     * Gets the configs dealing when a knight captures another piece
     * @param row - the row of where the knight is located
     * @param col - the column of where the knight is located
     * @return a list of configs that deal with when a knight captures a piece
     */
    public Collection<Configuration> getKnightNeighbors(int row, int col){
        Set knightNeighbors = new LinkedHashSet<ChessConfig>();
        try{
            if (!(this.chessBoard[row-2][col-1].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row-2,col-1, "N"));
                knightNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        try{
            if (!(this.chessBoard[row-2][col+1].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row-2,col+1, "N"));
                knightNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        try{
            if (!(this.chessBoard[row-1][col+2].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row-1,col+2, "N"));
                knightNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        try{
            if (!(this.chessBoard[row+1][col+2].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row+1,col+2, "N"));
                knightNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        try{
            if (!(this.chessBoard[row+2][col+1].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row+2,col+1, "N"));
                knightNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        try{
            if (!(this.chessBoard[row+2][col-1].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row+2,col-1, "N"));
                knightNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        try{
            if (!(this.chessBoard[row+1][col-2].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row+1,col-2, "N"));
                knightNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        try{
            if (!(this.chessBoard[row-1][col-2].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row-1,col-2, "N"));
                knightNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        return knightNeighbors;
    }
    /**
     * Gets the configs dealing when a king captures another piece
     * @param row - the row of where the king is located
     * @param col - the column of where the king is located
     * @return a list of configs that deal with when a king captures a piece
     */
    public Collection<Configuration> getKingNeighbors(int row, int col){
        Set kingNeighbors = new LinkedHashSet<ChessConfig>();
        try{
            if (!(this.chessBoard[row-1][col].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row-1,col, "K"));
                kingNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        try{
            if (!(this.chessBoard[row+1][col].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row+1,col, "K"));
                kingNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        try{
            if (!(this.chessBoard[row][col-1].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row,col-1, "K"));
                kingNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        try{
            if (!(this.chessBoard[row][col+1].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row,col+1, "K"));
                kingNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        try{
            if (!(this.chessBoard[row+1][col+1].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row+1,col+1, "K"));
                kingNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        try{
            if (!(this.chessBoard[row-1][col-1].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row-1,col-1, "K"));
                kingNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        try{
            if (!(this.chessBoard[row+1][col-1].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row+1,col-1, "K"));
                kingNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        try{
            if (!(this.chessBoard[row-1][col+1].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row-1,col+1, "K"));
                kingNeighbors.add(neighbor);
            }
        } catch (IndexOutOfBoundsException e){}
        return kingNeighbors;
    }
    /**
     * Gets the configs dealing when a rook captures another piece
     * @param row - the row of where the rook is located
     * @param col - the column of where the rook is located
     * @return a list of configs that deal with when a rook captures a piece
     */
    public Collection<Configuration> getRookNeighbors(int row, int col){
        Set rookNeighbors = new LinkedHashSet<ChessConfig>();
        for(int i = row-1; i>=0; i-=1){
            if(!(this.chessBoard[i][col].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, i, col, "R"));
                rookNeighbors.add(neighbor);
                break;
            }
        }
        for(int i=row+1; i<rowdim; i+=1){
            if(!(this.chessBoard[i][col].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, i, col, "R"));
                rookNeighbors.add(neighbor);
                break;
            }
        }
        for(int i=col-1; i>=0; i-=1){
            if(!(this.chessBoard[row][i].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row, i, "R"));
                rookNeighbors.add(neighbor);
                break;
            }
        }
        for(int i=col+1; i<coldim; i++){
            if(!(this.chessBoard[row][i].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, row, i, "R"));
                rookNeighbors.add(neighbor);
                break;
            }
        }
        return rookNeighbors;
    }
    /**
     * Gets the configs dealing when a bishop captures another piece
     * @param row - the row of where the bishop is located
     * @param col - the column of where the bishop is located
     * @return a list of configs that deal with when a bishop captures a piece
     */
    public Collection<Configuration> getBishopNeighbors(int row, int col){
        Set bishopNeighbors = new LinkedHashSet<ChessConfig>();
        int dummyrow = row+1;
        int dummycol = col+1;
        while (dummyrow < rowdim && dummycol < coldim){
            if(!(this.chessBoard[dummyrow][dummycol].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, dummyrow, dummycol, "B"));
                bishopNeighbors.add(neighbor);
                break;
            }
            dummyrow+=1;
            dummycol+=1;
        }
        dummyrow=row-1;
        dummycol=col-1;
        while (dummyrow >= 0 && dummycol >= 0){
            if(!(this.chessBoard[dummyrow][dummycol].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, dummyrow, dummycol, "B"));
                bishopNeighbors.add(neighbor);
                break;
            }
            dummyrow-=1;
            dummycol-=1;
        }
        dummyrow=row+1;
        dummycol=col-1;
        while (dummyrow < rowdim && dummycol >= 0){
            if(!(this.chessBoard[dummyrow][dummycol].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, dummyrow, dummycol, "B"));
                bishopNeighbors.add(neighbor);
                break;
            }
            dummyrow+=1;
            dummycol-=1;
        }
        dummyrow=row-1;
        dummycol=col+1;
        while(dummyrow >= 0 && dummycol < coldim){
            if(!(this.chessBoard[dummyrow][dummycol].equals("."))){
                ChessConfig neighbor = new ChessConfig(makeNeighbor(row, col, dummyrow, dummycol, "B"));
                bishopNeighbors.add(neighbor);
                break;
            }
            dummyrow-=1;
            dummycol+=1;
        }
        return bishopNeighbors;
    }
    /**
     * Gets the configs dealing when a queen captures another piece
     * @param row - the row of where the queen is located
     * @param col - the column of where the queen is located
     * @return a list of configs that deal with when a queen captures a piece
     */
    public Collection<Configuration> getQueenNeighbors(int row, int col) {
        Set queenNeighbors = new LinkedHashSet<ChessConfig>();
        queenNeighbors.addAll(getRookNeighbors(row, col));
        queenNeighbors.addAll(getBishopNeighbors(row, col));
        return queenNeighbors;
    }
}