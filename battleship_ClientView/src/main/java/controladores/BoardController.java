/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;

import Dominio.Position;
import Tablero.SelectionGrid;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Chris
 */
public class BoardController {
    public final SelectionGrid playerGrid;
    
    public List<Position> validMoves;

    public BoardController(SelectionGrid playerGrid) {
        this.playerGrid = playerGrid;
        this.createValidMoveList();
    }
    
     public Position selectMove() {
        return Position.ZERO;
    }

    public void reset() {
        createValidMoveList();
    }
    
    private void createValidMoveList() {
        validMoves = new ArrayList<>();
        for(int x = 0; x < SelectionGrid.GRID_WIDTH; x++) {
            for(int y = 0; y < SelectionGrid.GRID_HEIGHT; y++) {
                validMoves.add(new Position(x,y));
            }
        }
    }
    
}
