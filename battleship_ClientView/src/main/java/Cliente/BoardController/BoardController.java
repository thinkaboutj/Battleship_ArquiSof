package Cliente.BoardController;

import Cliente.Board.BoardTile;
import Cliente.Board.GameBoard;
import Cliente.Game.Game;
import javafx.scene.layout.GridPane;

import java.util.Map;

/**
 * @author Michal_Partacz
 * The method has a GameBoard object and mainly has methods which will operate on it.
 */
public abstract class BoardController {
    protected GameBoard gameBoard;
    private Game game;

    public BoardController(GameBoard gameBoard, Game game) {
        this.gameBoard = gameBoard;
        this.game = game;
    }

    public abstract void buildBoard();

    public GridPane getBoardGridPane() {
        return this.gameBoard.getGridPane();
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public Map<Integer, BoardTile> getBoardsIndexTiles() {
        return gameBoard.getIndexTiles();
    }

    public Game getGame() { return game; }

    public void setGame(Game game) { this.game = game;}

    public void colorBoardTile(int index, String color) {
        gameBoard.colorTileByIndex(index, color);
    }
}
