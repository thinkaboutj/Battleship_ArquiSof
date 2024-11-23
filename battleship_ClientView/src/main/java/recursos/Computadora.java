/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package recursos;

import Dominio.Position;
import Tablero.SelectionGrid;
import controladores.BoardController;
import java.util.Collections;

/**
 *
 * @author Chris
 */
public class Computadora extends BoardController {
    
    public Computadora(SelectionGrid playerGrid) {
        super(playerGrid);
        Collections.shuffle(validMoves);
    }

    
    @Override
    public void reset() {
        super.reset();
        Collections.shuffle(validMoves);
    }

    
    @Override
    public Position selectMove() {
        Position nextMove = validMoves.get(0);
        validMoves.remove(0);
        return nextMove;
    }
}
