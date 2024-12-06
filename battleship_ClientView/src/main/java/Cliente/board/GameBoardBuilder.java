package Cliente.board;

import Cliente.boardController.BoardController;
import Cliente.boardController.OpponentBoardController;
import Cliente.boardController.PlayerBoardController;
import Servidor.utilerias.AdjacentTilesCalc;
import Servidor.utilerias.Styles;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jesus
 */
public class GameBoardBuilder {

    public static final int BOARD_SIZE_WITH_LABELS = 11;
    private static final double FIELD_SIZE = 30;
    GridPane gridPane;
    private BoardController boardController;
    private Map<Integer, Label> verticalLabels = new HashMap<>();
    private Map<Integer, Label> horizontalLabels = new HashMap<>();

    /**
     * Constructor que inicializa el generador de tableros para el jugador.
     * Recibe un controlador de tablero del jugador y extrae la estructura del
     * GridPane del tablero.
     *
     * @param boardController controlador del tablero del jugador.
     */
    public GameBoardBuilder(PlayerBoardController boardController) {
        this.boardController = boardController;
        this.gridPane = boardController.getBoardGridPane();
    }

    /**
     * Constructor que inicializa el generador de tableros para el oponente.
     * Recibe un controlador de tablero del oponente y extrae la estructura del
     * GridPane del tablero.
     *
     * @param boardController controlador del tablero del oponente.
     */
    public GameBoardBuilder(OpponentBoardController boardController) {
        this.boardController = boardController;
        this.gridPane = boardController.getBoardGridPane();
    }

    /**
     * Llena el tablero con botones y etiquetas. Se asegura de construir la
     * estructura del GridPane con filas, columnas, etiquetas horizontales y
     * verticales, y los botones interactivos del tablero.
     */
    public void fillTheBoardWithButtonsAndLabels() {
        for (int column = 0; column < BOARD_SIZE_WITH_LABELS; column++) {
            // Añade restricciones de altura para cada fila.
            Platform.runLater(() -> gridPane.getRowConstraints().add(new RowConstraints(FIELD_SIZE)));
            for (int row = 0; row < BOARD_SIZE_WITH_LABELS; row++) {
                if (column == 0 && row == 0) {
                    // Primera celda (esquina superior izquierda), añade restricciones de ancho para la primera columna.
                    Platform.runLater(() -> gridPane.getColumnConstraints().add(new ColumnConstraints(FIELD_SIZE)));
                } else if (column == 0) {
                    // Etiquetas verticales.
                    Platform.runLater(() -> gridPane.getColumnConstraints().add(new ColumnConstraints(FIELD_SIZE)));
                    addVerticalLabelOnIndex(row);
                } else if (row == 0) {
                    // Etiquetas horizontales.
                    addHorizontalLabelOnIndex(column);
                } else {
                    // Botones interactivos del tablero.
                    createNewBoardTileAndAddIt(column, row);
                }
            }
        }
        addCorrespondingLabelsToTiles();
    }

    /**
     * Asocia las etiquetas correspondientes a cada botón del tablero. Las
     * etiquetas indican la posición (fila y columna) de cada celda del tablero.
     */
    private void addCorrespondingLabelsToTiles() {
        for (Map.Entry<Integer, BoardTile> entry : boardController.getBoardsIndexTiles().entrySet()) {
            BoardTile tile = entry.getValue();
            int boardIndex = entry.getKey();
            int[] tileCoordinates = AdjacentTilesCalc.translateIndexToCoordinates(boardIndex);
            tile.setLabels(horizontalLabels.get(tileCoordinates[1] + 1), verticalLabels.get(tileCoordinates[0] + 1));
            setButtonsHoverOverEvents(tile);
        }
    }

    /**
     * Crea una nueva celda interactiva (BoardTile) y la añade al tablero.
     * Asigna un índice único a la celda basado en sus coordenadas y configura
     * eventos de clic.
     *
     * @param absoluteXCoordinate coordenada X absoluta (columna).
     * @param absoluteYCoordinate coordenada Y absoluta (fila).
     */
    private void createNewBoardTileAndAddIt(int absoluteXCoordinate, int absoluteYCoordinate) {
        int boardIndex = AdjacentTilesCalc.translateCoordinatesToIndex(absoluteXCoordinate - 1, absoluteYCoordinate - 1);
        BoardTile tile = new BoardTile(boardIndex);
        tile.setOnMouseClicked(getOnBoardTileClickedEvent(tile, boardController));
        Platform.runLater(() -> this.gridPane.add(tile, absoluteXCoordinate, absoluteYCoordinate));
        boardController.getGameBoard().addBoardTileToHashMap(boardIndex, tile);
        fillGridPaneHeightAndWidthForNode(tile);
    }

    /**
     * Añade una etiqueta horizontal (para las columnas) en el índice
     * especificado.
     *
     * @param rowIndex índice de fila donde se añadirá la etiqueta.
     */
    private void addHorizontalLabelOnIndex(int rowIndex) {
        Label boardBoardTileLabel = buildCenteredLabelWithText(String.valueOf(rowIndex));
        Platform.runLater(() -> this.gridPane.add(boardBoardTileLabel, 0, rowIndex));
        fillGridPaneHeightAndWidthForNode(boardBoardTileLabel);
        horizontalLabels.put(rowIndex, boardBoardTileLabel);
    }

    /**
     * Añade una etiqueta vertical (para las filas) en el índice especificado.
     *
     * @param columnIndex índice de columna donde se añadirá la etiqueta.
     */
    private void addVerticalLabelOnIndex(int columnIndex) {
        Label boardBoardTileLabel = buildCenteredLabelWithText(String.valueOf((char) ('A' + columnIndex - 1)));
        Platform.runLater(() -> this.gridPane.add(boardBoardTileLabel, columnIndex, 0));
        fillGridPaneHeightAndWidthForNode(boardBoardTileLabel);
        verticalLabels.put(columnIndex, boardBoardTileLabel);
    }

    /**
     * Configura las restricciones de tamaño (altura y ancho) para un nodo en el
     * GridPane.
     *
     * @param node el nodo al que se aplicarán las restricciones.
     */
    private void fillGridPaneHeightAndWidthForNode(Node node) {
        GridPane.setFillHeight(node, true);
        GridPane.setFillWidth(node, true);
    }

    /**
     * Construye una etiqueta centrada con texto especificado.
     *
     * @param text el texto que se mostrará en la etiqueta.
     * @return una etiqueta configurada.
     */
    private Label buildCenteredLabelWithText(String text) {
        Label boardBoardTileLabel = new Label(text);
        boardBoardTileLabel.setAlignment(Pos.CENTER);
        boardBoardTileLabel.setMaxSize(200, 200);
        return boardBoardTileLabel;
    }

    /**
     * Obtiene el evento de clic para una celda del tablero. Si el tablero es
     * del jugador, se define un evento personalizado (pendiente de
     * implementación). Si es del oponente, se envía la acción al servidor para
     * realizar un movimiento.
     *
     * @param tile la celda del tablero para la que se genera el evento.
     * @param boardController el controlador del tablero asociado.
     * @return el manejador de eventos de clic.
     */
    private EventHandler<MouseEvent> getOnBoardTileClickedEvent(BoardTile tile, BoardController boardController) {
        if (boardController instanceof PlayerBoardController) {
            return event -> {
                // TODO Implementación específica para el jugador.
            };
        } else {
            return event -> boardController.getGame().makeAMove(tile.getBoardIndex());
        }
    }

    /**
     * Configura eventos de cambio de estilo visual para las celdas del tablero
     * al pasar el mouse.
     *
     * @param tile la celda del tablero a la que se aplican los eventos.
     */
    private void setButtonsHoverOverEvents(BoardTile tile) {
        tile.setOnMouseEntered(event -> tile.setTileStyle(Styles.TEXT_LIME));
        tile.setOnMouseExited(event -> tile.setTileStyle(Styles.TEXT_BLACK));
    }

}
