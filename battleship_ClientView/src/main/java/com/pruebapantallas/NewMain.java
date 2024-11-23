package com.pruebapantallas;

import controladores.GameController;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

/**
 *
 * @author jesus
 */
public class NewMain implements KeyListener {

    public static void main(String[] args) {
        NewMain game = new NewMain();
    }

    private GameController gamePanel;

    /**
     * Creates the JFrame with a GamePanel inside it, attaches a key listener,
     * and makes everything visible.
     */
    public NewMain() {
        // Choose the AI Difficulty
        String[] options = new String[]{"Easy", "Medium", "Hard"};
        String message = "Easy will make moves entirely randomly,\nMedium will focus on areas where it finds ships,"
                + "\nand Hard will make smarter choices over Medium.";
        int difficultyChoice = JOptionPane.showOptionDialog(null, message,
                "Choose an AI Difficulty",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        // Crear el JFrame personalizado con fondo
        JFrame frame = new JFrame("Battleship") {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                // Dibujar el fondo
                ImageIcon fondo = new ImageIcon(getClass().getResource("/images/PantallaInicio.jpg"));
                g.drawImage(fondo.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);

        // Crear panel del juego
        gamePanel = new GameController(difficultyChoice);
        gamePanel.setOpaque(false); 
        frame.getContentPane().add(gamePanel);

        frame.addKeyListener(this);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Called when the key is pressed down. Passes the key press on to the GamePanel.
     *
     * @param e Information about what key was pressed.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        gamePanel.handleInput(e.getKeyCode());
    }

    /**
     * Not used.
     *
     * @param e Not used.
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Not used.
     *
     * @param e Not used.
     */
    @Override
    public void keyReleased(KeyEvent e) {}

}
