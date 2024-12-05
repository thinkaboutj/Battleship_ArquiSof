package Cliente.Game;

import Cliente.StatusController;
import Cliente.boardController.OpponentBoardController;
import Cliente.boardController.PlayerBoardController;
import Cliente.network.ResponsesBus;
import Cliente.network.SocketClient;
import com.spanish_inquisition.battleship.common.Header;
import com.spanish_inquisition.battleship.common.NetworkMessage;
import com.spanish_inquisition.battleship.common.Styles;

import java.util.Arrays;

import static com.spanish_inquisition.battleship.common.AppLogger.DEFAULT_LEVEL;
import static com.spanish_inquisition.battleship.common.AppLogger.logger;

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

   public void setPlayerColor(String color) {
       this.playerColor = color;
   }

   public String getPlayerColor() {
       return this.playerColor;
   }

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
                       break;
                   }
                   case FLEET_INVALID: {
                       break;
                   }
                   case PLAYER_TURN: {
                       statusController.setPlayersLabel("Your turn!");
                       opponentBoardController.setBoardDisabled(false);
                       break;
                   }
                   case DECIDE_ON_MOVE: {
                       break;
                   }
                   case RESPONSE_HIT: {
                       int index = Integer.parseInt(message.getBody());
                       opponentBoardController.colorBoardTile(index, Styles.RESPONSE_HIT);
                       break;
                   }
                   case RESPONSE_MISS: {
                       int index = Integer.parseInt(message.getBody());
                       opponentBoardController.colorBoardTile(index, Styles.RESPONSE_MISS);
                       break;
                   }
                   case RESPONSE_DESTROYED_SHIP: {
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
                       int index = Integer.parseInt(message.getBody());
                       playerBoardController.colorBoardTile(index, Styles.RESPONSE_HIT);
                       break;
                   }
                   case RESPONSE_OPPONENT_MISS: {
                       int index = Integer.parseInt(message.getBody());
                       playerBoardController.colorBoardTile(index, Styles.RESPONSE_MISS);
                       break;
                   }
                   case RESPONSE_OPPONENT_DESTROYED_SHIP: {
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
                       break game_loop;
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
       this.playerName = name;
       if (socketClient != null) {
           socketClient.sendStringToServer(name);
       }
   }

   public String getPlayerName() {
       return this.playerName;
   }

   public void stopGameRunning() {
       isGameRunning = false;
   }
}