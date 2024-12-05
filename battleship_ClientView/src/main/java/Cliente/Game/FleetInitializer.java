package Cliente.Game;

import Cliente.board.BoardTile;
import Cliente.boardController.BoardController;
import com.spanish_inquisition.battleship.common.AppLogger;
import com.spanish_inquisition.battleship.common.Styles;
import javafx.application.Platform;

import java.util.List;
import java.util.Map;

import static com.spanish_inquisition.battleship.common.AppLogger.DEFAULT_LEVEL;
import java.util.ArrayList;

/**
 * 
 * @author jesus
 */
public class FleetInitializer {

     private BoardController boardController;
    private Map<Integer, BoardTile> boardTiles;
    private List<Integer> shipPlaces;
    private List<int[]> fleetOrder; // Lista ordenada de barcos (tamaño y cantidad)
    private int currentShipIndex = 0; // Índice del barco que se está colocando
    private boolean isHorizontal = true; // Orientación predeterminada

    public FleetInitializer(BoardController boardController) {
        this.boardController = boardController;
        this.shipPlaces = new ArrayList<>();
        initializeFleetOrder();
    }

    private void initializeFleetOrder() {
        fleetOrder = new ArrayList<>();
        fleetOrder.add(new int[]{4, 2}); // Portaaviones (4 casillas, 2 unidades)
        fleetOrder.add(new int[]{3, 2}); // Cruceros (3 casillas, 2 unidades)
        fleetOrder.add(new int[]{2, 4}); // Submarinos (2 casillas, 4 unidades)
        fleetOrder.add(new int[]{1, 3}); // Barcos (1 casilla, 3 unidades)
    }


public void setUpShips() {
    boardTiles = boardController.getBoardsIndexTiles();
    boardTiles.forEach((tileId, boardTile) -> {
        boardTile.setOnMouseClicked(event -> handleTileSelection(tileId));
        boardTile.setOnMouseEntered(event -> handleMouseHover(tileId, true));
        boardTile.setOnMouseExited(event -> handleMouseHover(tileId, false));
    });
    addKeyPressListenerForOrientation();
}

private void handleMouseHover(int tileId, boolean isHovering) {
    if (currentShipIndex >= fleetOrder.size()) {
        return; // No hay más barcos que colocar
    }

    int[] currentShip = fleetOrder.get(currentShipIndex);
    int shipSize = currentShip[0];

    for (int i = 0; i < shipSize; i++) {
        int position = isHorizontal ? tileId + i : tileId + (i * 10);

        // Verifica si la posición es válida
        if (boardTiles.containsKey(position)) {
            BoardTile tile = boardTiles.get(position);

            // Establece un estilo de sombreado si es válido
            if (isHovering && canPlaceShip(tileId, shipSize)) {
                // Aplica la clase CSS de hover
                tile.getStyleClass().add("hover-tile");
                tile.getStyleClass().remove("default-tile"); // Asegúrate de quitar cualquier otra clase previa
            } else {
                // Aplica la clase CSS por defecto
                tile.getStyleClass().add("default-tile");
                tile.getStyleClass().remove("hover-tile"); // Asegúrate de quitar la clase de hover
            }
        }
    }
}


   private void addKeyPressListenerForOrientation() {
    // Agrega un listener para cambiar orientación al presionar "Z"
    boardController.getBoardGridPane().setOnKeyPressed(event -> {
        if ("Z".equalsIgnoreCase(event.getCode().toString())) {
            isHorizontal = !isHorizontal;
            AppLogger.logger.log(DEFAULT_LEVEL, "Orientación cambiada a " + (isHorizontal ? "Horizontal" : "Vertical"));
        }
    });
}


    private void handleTileSelection(int tileId) {
        if (currentShipIndex >= fleetOrder.size()) {
            AppLogger.logger.log(DEFAULT_LEVEL, "Todos los barcos han sido colocados.");
            return;
        }

        int[] currentShip = fleetOrder.get(currentShipIndex);
        int shipSize = currentShip[0];
        int shipCount = currentShip[1];

        if (canPlaceShip(tileId, shipSize)) {
            placeShip(tileId, shipSize);
            currentShip[1]--; // Decrementa la cantidad de este barco

            if (currentShip[1] == 0) {
                currentShipIndex++; // Pasa al siguiente barco si no quedan más de este tipo
            }
        } else {
            AppLogger.logger.log(DEFAULT_LEVEL, "No se puede colocar el barco aquí.");
        }
    }

    private void placeShip(int tileId, int shipSize) {
        for (int i = 0; i < shipSize; i++) {
            int position = isHorizontal ? tileId + i : tileId + (i * 10);
            shipPlaces.add(position);
            changeTileStyle(position, Styles.SHIP_PLACED_COLOR);
        }
    }

    private boolean canPlaceShip(int tileId, int shipSize) {
        for (int i = 0; i < shipSize; i++) {
            int position = isHorizontal ? tileId + i : tileId + (i * 10);

            // Validar límites del tablero
            if (!boardTiles.containsKey(position)) {
                return false;
            }

            // Validar superposición
            if (shipPlaces.contains(position)) {
                return false;
            }

            // Validar distancia mínima
            if (!isSafeDistance(position)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSafeDistance(int position) {
        int[] offsets = {-11, -10, -9, -1, 1, 9, 10, 11};
        for (int offset : offsets) {
            int neighbor = position + offset;
            if (shipPlaces.contains(neighbor)) {
                return false;
            }
        }
        return true;
    }

    private void changeTileStyle(int tileId, String style) {
        BoardTile boardTile = boardTiles.get(tileId);
        AppLogger.logger.log(DEFAULT_LEVEL, "Cambiando el color del tile " + tileId + " a " + style);
        boardTile.setStyle(style);
    }
    
    public List<Integer> getShipPlaces() {
    return new ArrayList<>(shipPlaces); // Devuelve una copia de las posiciones
}

}

