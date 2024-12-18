package Servidor.utilerias;

import static Servidor.utilerias.AdjacentTilesCalc.translateCoordinatesToIndex;
import static Servidor.utilerias.AdjacentTilesCalc.translateIndexToCoordinates;

/**
 * 
 * @author jesus
 */
public enum TileDirection {
    LEFT{
        @Override
        public int getAdjacentTileIndex(Integer boardIndex) {
            int[] coordinates = translateIndexToCoordinates(boardIndex);
            return translateCoordinatesToIndex(coordinates[0] - 1, coordinates[1]);
        }
    },
    RIGHT{
        @Override
        public int getAdjacentTileIndex(Integer boardIndex) {
            int[] coordinates = translateIndexToCoordinates(boardIndex);
            return translateCoordinatesToIndex(coordinates[0] + 1, coordinates[1]);
        }
    },
    ABOVE{
        @Override
        public int getAdjacentTileIndex(Integer boardIndex) {
            int[] coordinates = translateIndexToCoordinates(boardIndex);
            return translateCoordinatesToIndex(coordinates[0], coordinates[1] - 1);
        }
    },
    BELOW{
        @Override
        public int getAdjacentTileIndex(Integer boardIndex) {
            int[] coordinates = translateIndexToCoordinates(boardIndex);
            return translateCoordinatesToIndex(coordinates[0], coordinates[1] + 1);
        }
    },
    UPPER_LEFT{
        @Override
        public int getAdjacentTileIndex(Integer boardIndex) {
            int[] coordinates = translateIndexToCoordinates(boardIndex);
            return translateCoordinatesToIndex(coordinates[0] - 1, coordinates[1] - 1);
        }
    },
    UPPER_RIGHT{
        @Override
        public int getAdjacentTileIndex(Integer boardIndex) {
            int[] coordinates = translateIndexToCoordinates(boardIndex);
            return translateCoordinatesToIndex(coordinates[0] + 1, coordinates[1] - 1);
        }
    },
    LOWER_LEFT{
        @Override
        public int getAdjacentTileIndex(Integer boardIndex) {
            int[] coordinates = translateIndexToCoordinates(boardIndex);
            return translateCoordinatesToIndex(coordinates[0] - 1, coordinates[1] + 1);
        }
    },
    LOWER_RIGHT {
        @Override
        public int getAdjacentTileIndex(Integer boardIndex) {
            int[] coordinates = translateIndexToCoordinates(boardIndex);
            return translateCoordinatesToIndex(coordinates[0] + 1, coordinates[1] + 1);
        }
    };

    public abstract int getAdjacentTileIndex(Integer boardIndex);
}
