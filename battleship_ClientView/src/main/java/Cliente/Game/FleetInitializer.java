package Cliente.Game;

import Cliente.board.BoardTile;
import Cliente.boardController.BoardController;
import Servidor.utilerias.AppLogger;
import Servidor.utilerias.Styles;
import javafx.application.Platform;

import java.util.List;
import java.util.Map;

import static Servidor.utilerias.AppLogger.DEFAULT_LEVEL;

/**
 * 
 * @author jesus
 */
public class FleetInitializer {

    private BoardController boardController;
    private Map<Integer, BoardTile> boardTiles;
    private List<Integer> shipPlaces;

    public List<Integer> getShipPlaces() {
        return shipPlaces;
    }

    public FleetInitializer(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setUpShips() {
        boardTiles = boardController.getBoardsIndexTiles();
        shipPlaces = Fleet.getRandomFleet().getShipPlaces();
        Platform.runLater(() -> shipPlaces.forEach(this::changeTileStyle));
    }

    private void changeTileStyle(int tileId) {
        BoardTile boardTile = boardTiles.get(tileId);
        String style = Styles.SHIP_PLACED_COLOR;
        AppLogger.logger.log(DEFAULT_LEVEL, "Configurar nuevo color: " + tileId + " to " + style);
        boardTile.setStyle(style);
    }
}