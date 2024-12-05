package Cliente;

import Cliente.board.GameBoard;
import Cliente.boardController.OpponentBoardController;
import Cliente.boardController.PlayerBoardController;
import Cliente.Game.Game;
import Cliente.network.SocketClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.io.IOException;
import java.util.logging.Level;

import static com.spanish_inquisition.battleship.common.AppLogger.DEFAULT_LEVEL;
import static com.spanish_inquisition.battleship.common.AppLogger.logger;

public class MainMenuController {
    @FXML
    VBox centralVBox;
    @FXML
    TextField nameTextField;
    @FXML
    Button featureButton;
    @FXML
    HBox gameHBox;
    @FXML
    VBox playersVBox;
    @FXML
    Label playersLabel;
    @FXML
    GridPane playersGridPane;
    @FXML
    VBox opponentsVBox;
    @FXML
    VBox playerNameVBox;
    @FXML
    Label opponentsLabel;
    @FXML
    GridPane opponentsGridPane;
    @FXML
    Label gameStatusLabel;
    @FXML
    Button sendToServerButton;
    @FXML
    Button fleetSetupButton;
    @FXML
    BorderPane mainBorderPane;
    @FXML
    ProgressIndicator socketProgressIndicator;
    @FXML
    ComboBox<String> colorComboBox;

    SocketClient socketClient;
    Game game;
    private int port = 6666;
    private String hostIP = "localhost";

    @FXML
    public void initialize() {
        showMainElements();
        colorComboBox.getItems().addAll("Azul", "Rojo");
        colorComboBox.setPromptText("Elige un color");
    }

    private void showMainElements() {
        this.centralVBox.setVisible(true);
    }

    @FXML
    public void onFeatureButtonClicked() {
        String playerName = nameTextField.getText();
        String playerColor = colorComboBox.getValue();

        if (playerName.isEmpty()) {
            gameStatusLabel.setText("Por favor, ingresa un nombre");
            return;
        }

        if (playerColor == null) {
            gameStatusLabel.setText("Por favor, selecciona un color");
            return;
        }

        this.acceptANameAndStartNewGame(playerName, playerColor);
    }

    @FXML
    public void onNameTextFieldEntered() {
        String playerName = nameTextField.getText();
        String playerColor = colorComboBox.getValue();

        if (playerName.isEmpty() || playerColor == null) return;

        this.acceptANameAndStartNewGame(playerName, playerColor);
    }

    private void acceptANameAndStartNewGame(String playerName, String playerColor) {
        setUpOnCloseRequest();
        setUpSocketConnection();
        game.acceptPlayersName(playerName);
        // Assuming you'll add this method to Game class
        game.setPlayerColor(playerColor);
    }

    private void setUpSocketConnection() {
        this.game = new Game();
        game.setStatusController(new StatusController(playersLabel));
        socketProgressIndicator.setVisible(true);
        try {
            gameStatusLabel.setText("Trying to connect to the server: " + hostIP);
            featureButton.setText("Connecting...");
            socketClient = SocketClient.createSocketClientWithSocket(hostIP, port);
            game.setSocketClient(socketClient);
            socketProgressIndicator.setVisible(false);
            gameStatusLabel.setText("");
            playerNameVBox.setVisible(false);
            buildPlayerBoard();
        } catch (IOException e) {
            logger.log(Level.WARNING,
                    "The client could not connect to the server", e);
            gameStatusLabel.setText("I couldn't connect to the server");
            socketProgressIndicator.setVisible(false);
            featureButton.setText("Send");
        }
    }

    private void setUpOnCloseRequest() {
        Scene scene = mainBorderPane.getScene();
        if (scene == null) {
            return;
        }
        Window window = scene.getWindow();
        window.setOnCloseRequest(event -> {
            if (game != null) {
                game.stopGameRunning();
                game.closeSocketConnection();
            }
            Platform.exit();
        });
    }

    private void buildPlayerBoard() {
        Platform.runLater(() -> playersLabel.setText("Set up your ships"));
        game.buildPlayersBoard(new PlayerBoardController(
                new GameBoard(this.playersGridPane), game));
        fleetSetupButton.setVisible(true);
    }

    @FXML
    public void onFleetSetupButtonClicked() {
        game.placePlayersShips();
        sendToServerButton.setVisible(true);
    }

    @FXML
    void onSendToServerButtonClicked() {
        fleetSetupButton.setVisible(false);
        sendToServerButton.setVisible(false);
        playersGridPane.setDisable(true);
        opponentsGridPane.setDisable(true);
        Platform.runLater(() -> playersLabel.setText("Wait for opponent's ships placement"));

        game.buildOpponentsBoard(new OpponentBoardController(
                new GameBoard(opponentsGridPane), game));
        game.sendTheFleetToServer();
        new Thread(() -> {
            try {
                game.runTheGame();
            } catch (InterruptedException e) {
                logger.log(DEFAULT_LEVEL, "Game interrupted");
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    void setPort(int port) {
        this.port = port;
    }

    void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }
}
