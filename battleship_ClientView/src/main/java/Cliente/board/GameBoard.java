package Cliente.board;

import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author jesus
 */
public class GameBoard {
    private GridPane gridPane;
    private Map<Integer, BoardTile> indexTiles = new HashMap<>();

    public GameBoard(GridPane gridPane) {
        this.gridPane = gridPane;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    void addBoardTileToHashMap(int boardIndex, BoardTile tile) {
        this.indexTiles.put(boardIndex, tile);
    }

    public Map<Integer, BoardTile> getIndexTiles() {
        return indexTiles;
    }

    public void colorTileByIndex(int index, String newStyle) {
        indexTiles.get(index).setStyle(newStyle);
    }
}