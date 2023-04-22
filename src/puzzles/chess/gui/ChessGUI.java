package puzzles.chess.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.chess.model.ChessModel;
import puzzles.hoppers.model.HoppersModel;

import java.io.IOException;

public class ChessGUI extends Application implements Observer<ChessModel, String> {
    private ChessModel model;
    private Button[][] arrayOfButtons;

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** the font size for labels and buttons */
    private final static int FONT_SIZE = 12;
    private String filename;

    private Stage stage;

    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private Image bishop = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"bishop.png"));
    private Image pawn = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"pawn.png"));
    private Image knight = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"knight.png"));
    private Image rook = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"rook.png"));
    private Image king = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"king.png"));
    private Image queen = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"queen.png"));

    /** a definition of light and dark and for the button backgrounds */
    private static final Background LIGHT =
            new Background( new BackgroundFill(Color.WHITE, null, null));
    private static final Background DARK =
            new Background( new BackgroundFill(Color.MIDNIGHTBLUE, null, null));

    @Override
    public void init() throws IOException {
        // get the file name from the command line
        this.filename = getParameters().getRaw().get(0);
        this.model = new ChessModel(this.filename);
        model.addObserver(this);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.arrayOfButtons = new Button[this.model.getCurrentConfig().getRowdim()][this.model.getCurrentConfig().getColdim()];
        BorderPane mainInterface = new BorderPane();
        HBox gameStatus = new HBox();
        HBox LoadResetHint = new HBox();
        GridPane chessBoard = new GridPane();
        Label statusLabel = new Label();
        statusLabel.setText("Loaded: "+filename);
        statusLabel.setFont(new Font(FONT_SIZE));
        gameStatus.getChildren().add(statusLabel);
        gameStatus.setAlignment(Pos.CENTER);
        Button loadButton = new Button("Load");
        loadButton.setFont(new Font(FONT_SIZE));
        LoadResetHint.getChildren().add(loadButton);
        Button resetButton = new Button("Reset");
        resetButton.setFont(new Font(FONT_SIZE));
        LoadResetHint.getChildren().add(resetButton);
        Button hintButton = new Button("Hint");
        hintButton.setFont(new Font(FONT_SIZE));
        LoadResetHint.getChildren().add(hintButton);
        LoadResetHint.setAlignment(Pos.CENTER);
        int row1;
        int col1;
        int lightOrDark = 0;
        for ( row1 = 1; row1 <= this.model.getCurrentConfig().getRowdim(); ++row1 ) {
            for (col1 = 1; col1 <= this.model.getCurrentConfig().getColdim(); col1++) {
                Button b = new Button();
                String piece = this.model.getCurrentConfig().getChessBoard()[row1-1][col1-1];
                if (piece.equals("P")){
                    b.setGraphic(new ImageView(pawn));
                } else if (piece.equals("N")) {
                    b.setGraphic(new ImageView(knight));
                } else if (piece.equals("K")) {
                    b.setGraphic(new ImageView(king));
                } else if (piece.equals("R")) {
                    b.setGraphic(new ImageView(rook));
                } else if (piece.equals("B")) {
                    b.setGraphic(new ImageView(bishop));
                } else if (piece.equals("Q")) {
                    b.setGraphic(new ImageView(queen));
                }
                if(lightOrDark % 2 == 0){
                    b.setBackground(LIGHT);
                    lightOrDark+=1;
                } else {
                    b.setBackground(DARK);
                    lightOrDark+=1;
                }
                b.setMinSize(ICON_SIZE, ICON_SIZE);
                b.setMaxSize(ICON_SIZE, ICON_SIZE);
                chessBoard.add(b,col1, row1);
                arrayOfButtons[row1-1][col1-1] = b;
            }
            if (this.model.getCurrentConfig().getColdim() % 2 == 0) {
                lightOrDark += 1;
            }
        }
        mainInterface.setTop(gameStatus);
        mainInterface.setBottom(LoadResetHint);
        mainInterface.setCenter(chessBoard);
        Scene scene = new Scene(mainInterface);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void update(ChessModel chessModel, String msg) {

        this.stage.sizeToScene();  // when a different sized puzzle is loaded
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
