package Cliente.Game;

import Cliente.StatusController;
import Cliente.BoardController.OpponentBoardController;
import Cliente.BoardController.PlayerBoardController;
import Cliente.Network.ResponsesBus;
import Cliente.Network.SocketClient;
import Utilerias.Header;
import Utilerias.NetworkMessage;
import Utilerias.Styles;

import java.util.Arrays;

import static Utilerias.AppLogger.DEFAULT_LEVEL;
import static Utilerias.AppLogger.logger;

/**
 * @author Michal_Partacz
 */
public class Game {
    private SocketClient socketClient;
    private ResponsesBus responsesBus;
    private static final int GAME_LOOP_SLEEP = 100;
    PlayerBoardController playerBoardController;
    OpponentBoardController opponentBoardController;
    private StatusController statusController;
    private boolean isGameRunning = true;

    public void setStatusController(StatusController statusController) {
        this.statusController = statusController;
    }

    public void buildPlayersBoard(PlayerBoardController boardController) {
        logger.log(DEFAULT_LEVEL, "Building player's board");
        this.playerBoardController = boardController;
        boardController.buildBoard();
    }

    public void buildOpponentsBoard(OpponentBoardController boardController) {
        logger.log(DEFAULT_LEVEL, "Building opponent's board");
        this.opponentBoardController = boardController;
        boardController.buildBoard();
    }

    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
        this.responsesBus = socketClient.getResponsesBus();
    }

    public void placePlayersShips() {
        //TODO: consider lazy initialization in loggers since they all accept Suppliers
        logger.log(DEFAULT_LEVEL, "Setting fleet for player");
        playerBoardController.placeShips();
    }

    public String getShipPlacementForServer() {
        return playerBoardController.getFleetMessageForServer();
    }

    public void sendTheFleetToServer() {
        if (socketClient != null) {
            socketClient.sendStringToServer(getShipPlacementForServer());
        }
    }

    public void runTheGame() throws InterruptedException, NullPointerException {
        game_loop:
        while (isGameRunning) {
            Thread.sleep(GAME_LOOP_SLEEP);
            if (this.responsesBus.hasServerResponses()) {
                NetworkMessage message = this.responsesBus.getAServerResponse();
                if (message == null) {
                    continue;
                }
                switch (message.getHeader()) {
                    case FLEET_VALID: {
                        // inform that fleet is valid and proceed
                        break;
                    }
                    case FLEET_INVALID: {
                        // inform that fleet is invalid and retry
                        break;
                    }
                    case PLAYER_TURN: {
                        statusController.setPlayersLabel("Your turn!");
                        opponentBoardController.setBoardDisabled(false);
                        break;
                    }
                    case DECIDE_ON_MOVE: {
                        // wait for target point from player
                        break;
                    }
                    case RESPONSE_HIT: {
                        // recolor targeted point on opponent's board
                        int index = Integer.parseInt(message.getBody());
                        opponentBoardController.colorBoardTile(index, Styles.RESPONSE_HIT);
                        break;
                    }
                    case RESPONSE_MISS: {
                        // recolor targeted point on opponent's board
                        int index = Integer.parseInt(message.getBody());
                        opponentBoardController.colorBoardTile(index, Styles.RESPONSE_MISS);
                        break;
                    }
                    case RESPONSE_DESTROYED_SHIP: {
                        // recolor destroyed ship points on opponent's board
                        String msg = message.getBody();
                        String destroyedFieldsString = msg.substring(msg.indexOf('[') + 1, msg.indexOf(']'));
                        String[] destroyedFieldsToParse = destroyedFieldsString.split(",");
                        Arrays.stream(destroyedFieldsToParse)
                                .map(field -> Integer.parseInt(field.trim()))
                                .forEach(index -> opponentBoardController.colorBoardTile(index, Styles.RESPONSE_DESTROYED));
                        break;
                    }
                    case OPPONENT_TURN: {
                        statusController.setPlayersLabel("Wait for opponent move...");
                        opponentBoardController.setBoardDisabled(true);
                        break;
                    }
                    case RESPONSE_OPPONENT_HIT: {
                        // recolor targeted point on player's board
                        int index = Integer.parseInt(message.getBody());
                        playerBoardController.colorBoardTile(index, Styles.RESPONSE_HIT);
                        break;
                    }
                    case RESPONSE_OPPONENT_MISS: {
                        // recolor targeted point on player's board
                        int index = Integer.parseInt(message.getBody());
                        playerBoardController.colorBoardTile(index, Styles.RESPONSE_MISS);
                        break;
                    }
                    case RESPONSE_OPPONENT_DESTROYED_SHIP: {
                        // recolor destroyed ship points on player's board
                        String msg = message.getBody();
                        String destroyedFieldsString = msg.substring(msg.indexOf('[') + 1, msg.indexOf(']'));
                        String[] destroyedFieldsToParse = destroyedFieldsString.split(",");
                        Arrays.stream(destroyedFieldsToParse)
                                .map(field -> Integer.parseInt(field.trim()))
                                .forEach(index -> playerBoardController.colorBoardTile(index, Styles.RESPONSE_DESTROYED));
                        break;
                    }
                    case GAME_WON: {
                        opponentBoardController.setBoardDisabled(true);
                        if (message.getBody().equals("true")) {
                            statusController.setPlayersLabel("You have won!");
                        } else {
                            statusController.setPlayersLabel("You lost!");
                        }
                        closeSocketConnection();
                        break game_loop;/*notify the player he won or lost */
                    }
                    default: {
                        break;

                    }
                }
            }
        }
    }

    public void closeSocketConnection() {
        if (socketClient != null) {
            socketClient.closeTheSocketClient();
        }
    }

    public void makeAMove(Integer tileIndex) {
        logger.log(DEFAULT_LEVEL, "The tile index that was clicked " + tileIndex);
        String regularMoveMessage = Header.MOVE_REGULAR + NetworkMessage.RESPONSE_HEADER_SPLIT_CHARACTER
                + tileIndex + NetworkMessage.RESPONSE_SPLIT_CHARACTER;
        if (socketClient != null) {
            socketClient.sendStringToServer(regularMoveMessage);
        }
    }

    public void acceptPlayersName(String name) {
        if (socketClient != null) {
            socketClient.sendStringToServer(name);
        }
    }

    public void stopGameRunning() {
        isGameRunning = false;
    }
}
