/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package recursos;

import Dominio.Position;
import Tablero.Rectangulo;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 *
 * @author Chris
 */
public class StatusPanel extends Rectangulo {

    private final Font font = new Font("Arial", Font.BOLD, 20);

    private final String placingShipLine1 = "Place your Ships below!";

    private final String placingShipLine2 = "Z to rotate.";

    private final String gameOverLossLine = "Game Over! You Lost :(";

    private final String gameOverWinLine = "You won! Well done!";

    private final String gameOverBottomLine = "Press R to restart.";

    private String topLine;

    private String bottomLine;

    public StatusPanel(Position position, int width, int height) {
        super(position, width, height);
        this.reset();
    }

    public void reset() {
        topLine = placingShipLine1;
        bottomLine = placingShipLine2;
    }

    public void showGameOver(boolean playerWon) {
        topLine = (playerWon) ? gameOverWinLine : gameOverLossLine;
        bottomLine = gameOverBottomLine;
    }

    public void setTopLine(String message) {
        topLine = message;
    }

    public void setBottomLine(String message) {
        bottomLine = message;
    }

    public void paint(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(position.x, position.y, width, height);
        g.setColor(Color.BLACK);
        g.setFont(font);
        int strWidth = g.getFontMetrics().stringWidth(topLine);
        g.drawString(topLine, position.x+width/2-strWidth/2, position.y+20);
        strWidth = g.getFontMetrics().stringWidth(bottomLine);
        g.drawString(bottomLine, position.x+width/2-strWidth/2, position.y+40);
    }
}
