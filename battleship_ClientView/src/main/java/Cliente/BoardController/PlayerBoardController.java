package Cliente.BoardController;

import Cliente.Board.BoardTile;
import Cliente.Board.GameBoard;
import Cliente.Board.GameBoardBuilder;
import Cliente.Game.FleetInitializer;
import Cliente.Game.Game;
import Cliente.Game.ServerMessageCreator;
import Utilerias.Styles;
import javafx.application.Platform;

import java.util.Map;

public class PlayerBoardController extends BoardController {
    private FleetInitializer fleetInitializer;

    public PlayerBoardController(GameBoard gameBoard, Game game) {
        super(gameBoard, game);
    }

    @Override
    public void buildBoard() {
        GameBoardBuilder gameBoardBuilder = new GameBoardBuilder(this);
        fleetInitializer = new FleetInitializer(this);
        gameBoardBuilder.fillTheBoardWithButtonsAndLabels();
    }

    public void placeShips() {
        Map<Integer, BoardTile> indexTiles = gameBoard.getIndexTiles();
        Platform.runLater(() -> indexTiles.forEach((integer, boardTile) -> boardTile.setTileStyle(Styles.DEFAULT_TILE_COLOR, Styles.TEXT_BLACK)));
        fleetInitializer.setUpShips();
    }

    public String getFleetMessageForServer() {
        return ServerMessageCreator.createFleetMessage(fleetInitializer.getShipPlaces());
    }
}
