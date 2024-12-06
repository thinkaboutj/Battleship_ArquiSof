package Cliente.Game;

import Cliente.StatusController;
import Cliente.boardController.OpponentBoardController;
import Cliente.boardController.PlayerBoardController;
import Cliente.network.ResponsesBus;
import Cliente.network.SocketClient;
import Servidor.utilerias.Header;
import Servidor.utilerias.NetworkMessage;
import Servidor.utilerias.Styles;

import java.util.Arrays;

import static Servidor.utilerias.AppLogger.DEFAULT_LEVEL;
import static Servidor.utilerias.AppLogger.logger;

/**
 *
 * @author jesus
 */
public class Game {

    private SocketClient socketClient;
    private ResponsesBus responsesBus;
    private static final int GAME_LOOP_SLEEP = 100;
    PlayerBoardController playerBoardController;
    OpponentBoardController opponentBoardController;
    private StatusController statusController;
    private boolean isGameRunning = true;
    private String playerName;
    private String playerColor;

    /**
     * Establece el color del jugador. Este color será utilizado para
     * identificar visualmente los barcos y movimientos del jugador.
     *
     * @param color el color seleccionado para el jugador.
     */
    public void setPlayerColor(String color) {
        this.playerColor = color;
    }

    /**
     * Obtiene el color asignado al jugador.
     *
     * @return el color del jugador.
     */
    public String getPlayerColor() {
        return this.playerColor;
    }

    /**
     * Asigna un controlador de estado al jugador. El StatusController se
     * encarga de actualizar mensajes y la interfaz gráfica según el estado del
     * juego.
     *
     * @param statusController el controlador de estado a asignar.
     */
    public void setStatusController(StatusController statusController) {
        this.statusController = statusController;
    }

    /**
     * Construye el tablero del jugador utilizando el controlador proporcionado.
     * Este tablero es donde el jugador colocará sus barcos.
     *
     * @param boardController el controlador del tablero del jugador.
     */
    public void buildPlayersBoard(PlayerBoardController boardController) {
        logger.log(DEFAULT_LEVEL, "Construyendo el tablero del jugador");
        this.playerBoardController = boardController;
        boardController.buildBoard(); // Llama al método que construye visualmente el tablero.
    }

    /**
     * Construye el tablero del oponente utilizando el controlador
     * proporcionado. Este tablero es donde el jugador realizará sus ataques al
     * oponente.
     *
     * @param boardController el controlador del tablero del oponente.
     */
    public void buildOpponentsBoard(OpponentBoardController boardController) {
        logger.log(DEFAULT_LEVEL, "Construyendo el tablero del oponente");
        this.opponentBoardController = boardController;
        boardController.buildBoard(); // Llama al método que construye visualmente el tablero.
    }

    /**
     * Asigna un cliente de socket para manejar la comunicación con el servidor.
     * También inicializa un "bus" para gestionar las respuestas del servidor.
     *
     * @param socketClient el cliente de socket a asignar.
     */
    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
        this.responsesBus = socketClient.getResponsesBus(); // Vincula el bus de respuestas con el cliente de socket.
    }

    /**
     * Configura los barcos en el tablero del jugador. Utiliza el controlador
     * del tablero para realizar el posicionamiento inicial.
     */
    public void placePlayersShips() {
        logger.log(DEFAULT_LEVEL, "Configurando los barcos de los jugadores");
        playerBoardController.placeShips(); // Llama al método encargado de posicionar los barcos.
    }

    /**
     * Obtiene la configuración actual de los barcos en el tablero del jugador.
     * Devuelve la configuración en un formato adecuado para enviarla al
     * servidor.
     *
     * @return un mensaje en formato cadena con la configuración de la flota.
     */
    public String getShipPlacementForServer() {
        return playerBoardController.getFleetMessageForServer();
    }

    /**
     * Envía al servidor la configuración actual de los barcos del jugador. Esto
     * es necesario para validar el posicionamiento antes de iniciar el juego.
     */
    public void sendTheFleetToServer() {
        if (socketClient != null) {
            socketClient.sendStringToServer(getShipPlacementForServer());
        }
    }

    /**
     * Ejecuta el bucle principal del juego. Procesa mensajes recibidos del
     * servidor y actualiza el estado del juego en consecuencia.
     *
     * @throws InterruptedException si ocurre una interrupción durante el bucle.
     * @throws NullPointerException si hay un acceso inesperado a un valor nulo.
     */
    public void runTheGame() throws InterruptedException, NullPointerException {
        game_loop: // Etiqueta para salir del bucle principal cuando el juego termine.
        while (isGameRunning) {
            Thread.sleep(GAME_LOOP_SLEEP); // Pausa entre iteraciones para controlar la velocidad del bucle.

            // Verifica si hay respuestas del servidor para procesar.
            if (this.responsesBus.hasServerResponses()) {
                NetworkMessage message = this.responsesBus.getAServerResponse();
                if (message == null) {
                    continue; // Si no hay mensajes válidos, continúa al siguiente ciclo.
                }

                // Procesa los mensajes según el encabezado recibido.
                switch (message.getHeader()) {
                    case FLEET_VALID:
                        // El servidor validó correctamente la configuración de la flota.
                        break;
                    case FLEET_INVALID:
                        // La configuración de la flota fue rechazada por el servidor.
                        break;
                    case PLAYER_TURN:
                        // Es el turno del jugador; habilita el tablero del oponente.
                        statusController.setPlayersLabel("Es tu turno!");
                        opponentBoardController.setBoardDisabled(false);
                        break;
                    case RESPONSE_HIT:
                        // El ataque del jugador impactó en el tablero del oponente.
                        int hitIndex = Integer.parseInt(message.getBody());
                        opponentBoardController.colorBoardTile(hitIndex, Styles.RESPONSE_HIT);
                        break;
                    // Más casos según los mensajes del servidor...
                    case GAME_WON:
                        // Finaliza el juego y muestra si el jugador ganó o perdió.
                        opponentBoardController.setBoardDisabled(true);
                        if (message.getBody().equals("true")) {
                            statusController.setPlayersLabel("¡Has ganado la partida!");
                        } else {
                            statusController.setPlayersLabel("Has perdido la partida, mejor suerte la próxima vez.");
                        }
                        closeSocketConnection();
                        break game_loop; // Sale del bucle principal.
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Cierra la conexión del socket con el servidor. Libera los recursos
     * asociados a la comunicación.
     */
    public void closeSocketConnection() {
        if (socketClient != null) {
            socketClient.closeTheSocketClient();
        }
    }

    /**
     * Realiza un movimiento al seleccionar un índice en el tablero del
     * oponente. Envía la acción al servidor para que sea procesada.
     *
     * @param tileIndex el índice del tablero seleccionado para el movimiento.
     */
    public void makeAMove(Integer tileIndex) {
        logger.log(DEFAULT_LEVEL, "Se ha hecho un click en el cuadrado " + tileIndex);
        String regularMoveMessage = Header.MOVE_REGULAR + NetworkMessage.RESPONSE_HEADER_SPLIT_CHARACTER
                + tileIndex + NetworkMessage.RESPONSE_SPLIT_CHARACTER;
        if (socketClient != null) {
            socketClient.sendStringToServer(regularMoveMessage);
        }
    }

    /**
     * Acepta el nombre del jugador y lo envía al servidor.
     *
     * @param name el nombre del jugador.
     */
    public void acceptPlayersName(String name) {
        this.playerName = name;
        if (socketClient != null) {
            socketClient.sendStringToServer(name);
        }
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return el nombre del jugador.
     */
    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * Detiene la ejecución del juego. Cambia el estado del bucle principal para
     * finalizar la partida.
     */
    public void stopGameRunning() {
        isGameRunning = false;
    }

}
