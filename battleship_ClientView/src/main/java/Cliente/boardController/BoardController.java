package Cliente.boardController;

import Cliente.board.BoardTile;
import Cliente.board.GameBoard;
import Cliente.Game.Game;
import javafx.scene.layout.GridPane;

import java.util.Map;

/**
 * 
 * @author jesus
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
