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

import static Servidor.utilerias.AppLogger.DEFAULT_LEVEL;
import static Servidor.utilerias.AppLogger.logger;

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

    /**
     * Método llamado automáticamente al inicializar el controlador. Configura
     * los elementos visuales principales y llena el ComboBox con opciones de
     * color.
     */
    @FXML
    public void initialize() {
        showMainElements(); // Muestra los elementos principales de la interfaz.
        colorComboBox.getItems().addAll("Azul", "Rojo"); // Agrega opciones al ComboBox de colores.
        colorComboBox.setPromptText("Elige un color"); // Establece el texto por defecto del ComboBox.
    }

    /**
     * Hace visibles los elementos principales de la interfaz gráfica.
     */
    private void showMainElements() {
        this.centralVBox.setVisible(true);
    }

    /**
     * Maneja el evento del botón principal cuando es clicado. Verifica que el
     * usuario haya ingresado un nombre y seleccionado un color, y comienza un
     * nuevo juego.
     */
    @FXML
    public void onFeatureButtonClicked() {
        String playerName = nameTextField.getText(); // Obtiene el nombre del jugador.
        String playerColor = colorComboBox.getValue(); // Obtiene el color seleccionado.

        if (playerName.isEmpty()) { // Verifica si el nombre está vacío.
            gameStatusLabel.setText("Por favor, ingresa un nombre");
            return;
        }

        if (playerColor == null) { // Verifica si no se seleccionó un color.
            gameStatusLabel.setText("Por favor, selecciona un color");
            return;
        }

        this.acceptANameAndStartNewGame(playerName, playerColor); // Inicia un nuevo juego con el nombre y color ingresados.
    }

    /**
     * Maneja el evento cuando se presiona la tecla Enter en el campo de texto.
     * Valida los datos y comienza un nuevo juego si ambos son válidos.
     */
    @FXML
    public void onNameTextFieldEntered() {
        String playerName = nameTextField.getText();
        String playerColor = colorComboBox.getValue();

        if (playerName.isEmpty() || playerColor == null) {
            return; // No hace nada si faltan datos.
        }

        this.acceptANameAndStartNewGame(playerName, playerColor); // Inicia el juego.
    }

    /**
     * Configura los elementos necesarios para iniciar un nuevo juego. Incluye
     * cerrar conexiones, configurar el socket y establecer datos del jugador.
     *
     * @param playerName Nombre del jugador.
     * @param playerColor Color elegido por el jugador.
     */
    private void acceptANameAndStartNewGame(String playerName, String playerColor) {
        setUpOnCloseRequest(); // Configura qué hacer al cerrar la ventana.
        setUpSocketConnection(); // Establece conexión con el servidor.
        game.acceptPlayersName(playerName); // Almacena el nombre del jugador.
        game.setPlayerColor(playerColor); // Establece el color del jugador.
    }

    /**
     * Configura la conexión del socket al servidor. Muestra mensajes de estado
     * en caso de éxito o error.
     */
    private void setUpSocketConnection() {
        this.game = new Game(); // Crea una nueva instancia del juego.
        game.setStatusController(new StatusController(playersLabel)); // Vincula el controlador de estado.
        socketProgressIndicator.setVisible(true); // Muestra un indicador de progreso.

        try {
            gameStatusLabel.setText("Tratando de conectar con el servidor: " + hostIP);
            featureButton.setText("Conectando...");
            socketClient = SocketClient.createSocketClientWithSocket(hostIP, port); // Intenta conectarse al servidor.
            game.setSocketClient(socketClient); // Establece el cliente del socket en el juego.
            socketProgressIndicator.setVisible(false); // Oculta el indicador de progreso.
            gameStatusLabel.setText("");
            playerNameVBox.setVisible(false); // Oculta la sección de nombre del jugador.
            buildPlayerBoard(); // Construye el tablero del jugador.
        } catch (IOException e) {
            logger.log(Level.WARNING, "El cliente no pudo conectarse al servidor", e); // Registra el error.
            gameStatusLabel.setText("No pude conectarme al servidor :C, Debes iniciar el servidor antes de jugar!!");
            socketProgressIndicator.setVisible(false); // Oculta el indicador de progreso.
            featureButton.setText("Confirmar");
        }
    }

    /**
     * Configura el comportamiento al cerrar la ventana de la aplicación.
     * Detiene el juego y cierra las conexiones activas.
     */
    private void setUpOnCloseRequest() {
        Scene scene = mainBorderPane.getScene();
        if (scene == null) {
            return; // No hace nada si no hay escena asociada.
        }
        Window window = scene.getWindow();
        window.setOnCloseRequest(event -> {
            if (game != null) {
                game.stopGameRunning(); // Detiene el juego.
                game.closeSocketConnection(); // Cierra la conexión del socket.
            }
            Platform.exit(); // Sale de la aplicación.
        });
    }

    /**
     * Construye el tablero del jugador en la interfaz. Actualiza las etiquetas
     * y configura el tablero del juego.
     */
    private void buildPlayerBoard() {
        Platform.runLater(() -> playersLabel.setText("Posiciona tus barcos!")); // Muestra un mensaje en la interfaz.
        game.buildPlayersBoard(new PlayerBoardController(
                new GameBoard(this.playersGridPane), game)); // Construye el tablero del jugador.
        fleetSetupButton.setVisible(true); // Muestra el botón para configurar la flota.
    }

    /**
     * Maneja el evento del botón para configurar la flota. Configura los barcos
     * del jugador en el tablero.
     */
    @FXML
    public void onFleetSetupButtonClicked() {
        game.placePlayersShips(); // Posiciona los barcos del jugador.
        sendToServerButton.setVisible(true); // Muestra el botón para enviar los datos al servidor.
    }

    /**
     * Maneja el evento del botón para enviar la configuración de la flota al
     * servidor. Desactiva los tableros, actualiza el mensaje de estado y
     * comienza el juego.
     */
    @FXML
    void onSendToServerButtonClicked() {
        fleetSetupButton.setVisible(false);
        sendToServerButton.setVisible(false);
        playersGridPane.setDisable(true); // Desactiva el tablero del jugador.
        opponentsGridPane.setDisable(true); // Desactiva el tablero del oponente.
        Platform.runLater(() -> playersLabel.setText("Esperando por el posicionamiento de barcos del oponente"));

        game.buildOpponentsBoard(new OpponentBoardController(
                new GameBoard(opponentsGridPane), game)); // Construye el tablero del oponente.
        game.sendTheFleetToServer(); // Envía la configuración al servidor.

        // Inicia un nuevo hilo para ejecutar el juego.
        new Thread(() -> {
            try {
                game.runTheGame(); // Ejecuta el ciclo principal del juego.
            } catch (InterruptedException e) {
                logger.log(DEFAULT_LEVEL, "Juego interrumpido");
                Thread.currentThread().interrupt(); // Marca el hilo como interrumpido.
            }
        }).start();
    }

    /**
     * Establece el puerto para la conexión del socket.
     *
     * @param port Puerto del servidor.
     */
    void setPort(int port) {
        this.port = port;
    }

    /**
     * Establece la dirección IP del host del servidor.
     *
     * @param hostIP Dirección IP del servidor.
     */
    void setHostIP(String hostIP) {
        this.hostIP = hostIP;
    }

}
