/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;

import Dominio.Boat;
import Dominio.GameStatus;
import Dominio.Position;
import Tablero.SelectionGrid;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import recursos.Computadora;
import recursos.ControlaComputadora;
import recursos.StatusPanel;

/**
 *
 * @author Chris
 */
public class GameController extends JPanel implements MouseListener, MouseMotionListener {

    public GameStatus gameStatus;

    private StatusPanel statusPanel;

    private SelectionGrid computer;

    private SelectionGrid player;

    private BoardController boardController;

    private Boat placingBoat;

    private Position trackerPlacePosition;

    private int placeBoatIndex;

    public static boolean debugModeActive;

    public GameController(int compChoice) {
        computer = new SelectionGrid(0, 0);
        player = new SelectionGrid(0, computer.getHeight() + 50);
        this.setBackground(new Color(42, 136, 163));
        this.setPreferredSize(new Dimension(computer.getWidth(), player.getPosition().y + player.getHeight()));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        if (compChoice == 0) {
            boardController = new Computadora(player);
        } else {
            boardController = new ControlaComputadora(player, compChoice == 2, compChoice == 2);
        }
        statusPanel = new StatusPanel(new Position(0, computer.getHeight() + 1), computer.getWidth(), 49);
        this.restart();
    }

    public void paint(Graphics g) {
        super.paint(g);
        computer.paint(g);
        player.paint(g);
        if (gameStatus == GameStatus.placingBoats) {
            placingBoat.paint(g);
        }
        statusPanel.paint(g);
    }

    public void handleInput(int keyCode) {
        if (keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(1);
        } else if (keyCode == KeyEvent.VK_R) {
            restart();
        } else if (gameStatus == GameStatus.placingBoats && keyCode == KeyEvent.VK_Z) {
            placingBoat.toggleSideways();
            updateShipPlacement(trackerPlacePosition);
        } else if (keyCode == KeyEvent.VK_D) {
            debugModeActive = !debugModeActive;
        }
        repaint();
    }

    public void restart() {
        computer.reset();
        player.reset();
        player.setShowBoats(true);
        boardController.reset();
        trackerPlacePosition = new Position(0, 0);
        placingBoat = new Boat(new Position(0, 0),
                new Position(player.getPosition().x, player.getPosition().y),
                SelectionGrid.BOAT_SIZES[0], true);
        placeBoatIndex = 0;
        updateShipPlacement(trackerPlacePosition);
        computer.populateBoats();
        debugModeActive = false;
        statusPanel.reset();
        gameStatus = GameStatus.placingBoats;
    }

    private void tryPlaceShip(Position mousePosition) {
        Position targetPosition = player.getPositionInGrid(mousePosition.x, mousePosition.y);
        updateShipPlacement(targetPosition);
        if (player.canPlaceShipAt(targetPosition.x, targetPosition.y,
                SelectionGrid.BOAT_SIZES[placeBoatIndex], placingBoat.isSideways())) {
            placeShip(targetPosition);
        }
    }

    private void placeShip(Position targetPosition) {
        placingBoat.setBoatPlacementColour(Boat.BoatPlacementColour.PLACED);
        player.placeBoat(placingBoat, trackerPlacePosition.x, trackerPlacePosition.y);
        placeBoatIndex++;
        if (placeBoatIndex < SelectionGrid.BOAT_SIZES.length) {
            placingBoat = new Boat(new Position(targetPosition.x, targetPosition.y),
                    new Position(player.getPosition().x + targetPosition.x * SelectionGrid.CELL_SIZE,
                            player.getPosition().y + targetPosition.y * SelectionGrid.CELL_SIZE),
                    SelectionGrid.BOAT_SIZES[placeBoatIndex], true);
            updateShipPlacement(trackerPlacePosition);
        } else {
            gameStatus = GameStatus.firingShots;
            statusPanel.setTopLine("Attack the Computer!");
            statusPanel.setBottomLine("Destroy all Ships to win!");
        }
    }

    private void tryFireAtComputer(Position mousePosition) {
        Position targetPosition = computer.getPositionInGrid(mousePosition.x, mousePosition.y);
        // Ignore if position was already clicked
        if (!computer.isPositionMarked(targetPosition)) {
            doPlayerTurn(targetPosition);
            // Only do the AI turn if the game didn't end from the player's turn.
            if (!computer.areAllBoatsDestroyed()) {
                doAITurn();
            }
        }
    }

    private void doPlayerTurn(Position targetPosition) {
        boolean hit = computer.markPosition(targetPosition);
        String hitMiss = hit ? "Hit" : "Missed";
        String destroyed = "";
        if (hit && computer.getMarkerAtPosition(targetPosition).getAssociatedShip().isDestroyed()) {
            destroyed = "(Destroyed)";
        }
        statusPanel.setTopLine("Player " + hitMiss + " " + targetPosition + destroyed);
        if (computer.areAllBoatsDestroyed()) {
            // Player wins!
            gameStatus = GameStatus.gameOver;
            statusPanel.showGameOver(true);
        }
    }

    private void doAITurn() {
        Position aiMove = boardController.selectMove();
        boolean hit = player.markPosition(aiMove);
        String hitMiss = hit ? "Hit" : "Missed";
        String destroyed = "";
        if (hit && player.getMarkerAtPosition(aiMove).getAssociatedShip().isDestroyed()) {
            destroyed = "(Destroyed)";
        }
        statusPanel.setBottomLine("Computer " + hitMiss + " " + aiMove + destroyed);
        if (player.areAllBoatsDestroyed()) {
            // Computer wins!
            gameStatus = GameStatus.gameOver;
            statusPanel.showGameOver(false);
        }
    }

    private void tryMovePlacingShip(Position mousePosition) {
        if (player.isPositionInside(mousePosition)) {
            Position targetPos = player.getPositionInGrid(mousePosition.x, mousePosition.y);
            updateShipPlacement(targetPos);
        }
    }

    private void updateShipPlacement(Position targetPos) {
        if (placingBoat.isSideways()) {
            targetPos.x = Math.min(targetPos.x, SelectionGrid.GRID_WIDTH - SelectionGrid.BOAT_SIZES[placeBoatIndex]);
        } else {
            targetPos.y = Math.min(targetPos.y, SelectionGrid.GRID_HEIGHT - SelectionGrid.BOAT_SIZES[placeBoatIndex]);
        }
        placingBoat.setDrawPosition(new Position(targetPos),
                new Position(player.getPosition().x + targetPos.x * SelectionGrid.CELL_SIZE,
                        player.getPosition().y + targetPos.y * SelectionGrid.CELL_SIZE));
        trackerPlacePosition = targetPos;
        if (player.canPlaceShipAt(trackerPlacePosition.x, trackerPlacePosition.y,
                SelectionGrid.BOAT_SIZES[placeBoatIndex], placingBoat.isSideways())) {
            placingBoat.setBoatPlacementColour(Boat.BoatPlacementColour.VALID);
        } else {
            placingBoat.setBoatPlacementColour(Boat.BoatPlacementColour.INVALID);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Position mousePosition = new Position(e.getX(), e.getY());
        if (gameStatus == GameStatus.placingBoats && player.isPositionInside(mousePosition)) {
            tryPlaceShip(mousePosition);
        } else if (gameStatus == GameStatus.firingShots && computer.isPositionInside(mousePosition)) {
            tryFireAtComputer(mousePosition);
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameStatus != GameStatus.placingBoats) {
            return;
        }
        tryMovePlacingShip(new Position(e.getX(), e.getY()));
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

}
