package puzzles.hoppers.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Paths;

public class HoppersGUI extends Application implements Observer<HoppersModel, String> {
    private HoppersModel model;
    private Button[][] buttonArray;
    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** the font size for labels and buttons */
    private final static int FONT_SIZE = 12;
    private String filename;
    private Stage stage;
    private Label statusLabel;

    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    private final Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));
    private final Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green_frog.png"));
    private final Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"lily_pad.png"));
    private final Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"water.png"));

    /** Background */
    private static final Background waterBackground = new Background(
            new BackgroundFill(Color.LIGHTBLUE, null, null));

    public void init() throws IOException {
        this.filename = getParameters().getRaw().get(0);
        this.model = new HoppersModel(this.filename);
        model.addObserver(this);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.buttonArray = new Button[this.model.getCurrentConfig().getRowDim()][this.model.getCurrentConfig().getColDim()];
        BorderPane mainInterface = new BorderPane();
        mainInterface.setBackground(waterBackground);

        HBox gameStatus = new HBox();
        HBox LoadResetHint = new HBox();
        GridPane hopperBoard = new GridPane();

        this.statusLabel = new Label();
        this.statusLabel.setText("Loaded: " + filename);
        this.statusLabel.setFont(new Font(FONT_SIZE));

        gameStatus.getChildren().add(statusLabel);
        gameStatus.setAlignment(Pos.CENTER);

        Button loadButton = new Button( "Load");
        loadButton.setFont(new Font(FONT_SIZE));
        loadButton.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
            currentPath += File.separator + "data" + File.separator + "hoppers";
            chooser.setInitialDirectory(new File(currentPath));
            File filechoosen = chooser.showOpenDialog(stage);
            this.model.load(filechoosen.toString());
        });
        LoadResetHint.getChildren().add(loadButton);

        Button resetButton = new Button("Reset");
        resetButton.setFont(new Font(FONT_SIZE));
        resetButton.setOnAction(event -> {
            this.model.reset();
        });
        LoadResetHint.getChildren().add(resetButton);

        Button hintButton = new Button("Hint");
        hintButton.setFont(new Font(FONT_SIZE));
        hintButton.setOnAction(event -> {
            this.model.hint();
        });
        LoadResetHint.getChildren().add(hintButton);
        LoadResetHint.setAlignment(Pos.CENTER);

        for (int iRow = 0; iRow < this.model.getCurrentConfig().getRowDim(); iRow++) {
            for (int iCol = 0; iCol < this.model.getCurrentConfig().getColDim(); iCol++) {
                Button button = new Button();
                String piece = this.model.getCurrentConfig().getHopperBoard()[iRow][iCol];
                if (piece.equals("G")) {
                    button.setGraphic(new ImageView(greenFrog));
                } else if (piece.equals("R")) {
                    button.setGraphic(new ImageView(redFrog));
                } else if (piece.equals("*")) {
                    button.setGraphic(new ImageView(water));
                } else if (piece.equals(".")) {
                    button.setGraphic(new ImageView(lilyPad));
                }

                button.setMinSize(ICON_SIZE,ICON_SIZE);
                button.setMaxSize(ICON_SIZE,ICON_SIZE);
                int finalIRow = iRow;
                int finalICol = iCol;
                button.setOnAction(event -> {
                    this.model.selectPiece(finalIRow, finalICol);
                });
                hopperBoard.add(button, iRow, iCol);
                buttonArray[iRow][iCol] = button;
            }
        }

        mainInterface.setTop(gameStatus);
        mainInterface.setBottom(LoadResetHint);
        mainInterface.setCenter(hopperBoard);
        Scene scene = new Scene(mainInterface);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void update(HoppersModel hoppersModel, String msg) {
        this.buttonArray = new Button[this.model.getCurrentConfig().getRowDim()][this.model.getCurrentConfig().getColDim()];
        BorderPane mainInterface = new BorderPane();
        mainInterface.setBackground(waterBackground);

        HBox gameStatus = new HBox();
        HBox LoadResetHint = new HBox();
        GridPane hopperBoard = new GridPane();

        this.statusLabel = new Label();
        this.statusLabel.setText(msg);
        this.statusLabel.setFont(new Font(FONT_SIZE));

        gameStatus.getChildren().add(statusLabel);
        gameStatus.setAlignment(Pos.CENTER);

        Button loadButton = new Button( "Load");
        loadButton.setFont(new Font(FONT_SIZE));
        loadButton.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
            currentPath += File.separator + "data" + File.separator + "hoppers";
            chooser.setInitialDirectory(new File(currentPath));
            File filechoosen = chooser.showOpenDialog(stage);
            this.model.load(filechoosen.toString());
        });
        LoadResetHint.getChildren().add(loadButton);

        Button resetButton = new Button("Reset");
        resetButton.setFont(new Font(FONT_SIZE));
        resetButton.setOnAction(event -> {
            this.model.reset();
        });
        LoadResetHint.getChildren().add(resetButton);

        Button hintButton = new Button("Hint");
        hintButton.setFont(new Font(FONT_SIZE));
        hintButton.setOnAction(event -> {
            this.model.hint();
        });
        LoadResetHint.getChildren().add(hintButton);
        LoadResetHint.setAlignment(Pos.CENTER);

        for (int iRow = 0; iRow < this.model.getCurrentConfig().getRowDim(); iRow++) {
            for (int iCol = 0; iCol < this.model.getCurrentConfig().getColDim(); iCol++) {
                Button button = new Button();
                String piece = this.model.getCurrentConfig().getHopperBoard()[iRow][iCol];
                if (piece.equals("G")) {
                    button.setGraphic(new ImageView(greenFrog));
                } else if (piece.equals("R")) {
                    button.setGraphic(new ImageView(redFrog));
                } else if (piece.equals("*")) {
                    button.setGraphic(new ImageView(water));
                } else if (piece.equals(".")) {
                    button.setGraphic(new ImageView(lilyPad));
                }

                button.setMinSize(ICON_SIZE,ICON_SIZE);
                button.setMaxSize(ICON_SIZE,ICON_SIZE);
                int finalIRow = iRow;
                int finalICol = iCol;
                button.setOnAction(event -> {
                    this.model.selectPiece(finalIRow, finalICol);
                });
                hopperBoard.add(button, iRow, iCol);
                buttonArray[iRow][iCol] = button;
            }
        }

        mainInterface.setTop(gameStatus);
        mainInterface.setBottom(LoadResetHint);
        mainInterface.setCenter(hopperBoard);
        Scene scene = new Scene(mainInterface);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
