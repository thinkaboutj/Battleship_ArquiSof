/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Angel De Jesus Santos Sarabia - 205750
 * @author Jose Miguel Rodriguez Reyna - 216743
 * @author Jesus Gabriel Medina Leyva - 247527
 * @author Cristopher Alberto Elizalde Andrade - 240005
 */
public class Boat {

    private int size;
    private List<Position> position;
    private ColorManager color;
    private boolean hitStatus;
    private Position gridPosition;
    private Position drawPosition;
    private int segments;
    private boolean isSideways;
    private int destroyedSections;
    private BoatPlacementColour boatPlacementColour;
    private BufferedImage boatImage;
    
    public Boat(Position gridPosition, Position drawPosition, int segments, boolean isSideways) {
        this.gridPosition = gridPosition;
        this.drawPosition = drawPosition;
        this.segments = segments;
        this.isSideways = isSideways;
        this.destroyedSections = 0;
        this.boatPlacementColour = Boat.BoatPlacementColour.PLACED;
        loadBoatImage();
    }
    
    private void loadBoatImage() {
        try {
            boatImage = ImageIO.read(new File("images/Barco.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen del barco: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void paint(Graphics g) {
        if (boatImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            int width = 30 * segments; // Ancho basado en el número de segmentos
            int height = 30; // Altura fija
            
            if (this.boatPlacementColour == Boat.BoatPlacementColour.PLACED) {
                if (this.destroyedSections >= this.segments) {
                    // Si está destruido, dibujamos en rojo
                    g2d.setColor(new Color(255, 0, 0, 128));
                }
            } else {
                // Color para validación de colocación
                Color overlayColor = this.boatPlacementColour == Boat.BoatPlacementColour.VALID ? 
                    new Color(0, 255, 0, 128) : new Color(255, 0, 0, 128);
                g2d.setColor(overlayColor);
            }

            if (isSideways) {
                // Dibujar horizontal
                g2d.drawImage(boatImage, 
                    drawPosition.x, 
                    drawPosition.y,
                    width,
                    height,
                    null);
            } else {
                // Dibujar vertical
                g2d.drawImage(boatImage, 
                    drawPosition.x,
                    drawPosition.y,
                    height, // Intercambiamos width y height para la orientación vertical
                    width,
                    null);
            }
            
            // Si hay un color de overlay (destruido o validación), dibujamos un rectángulo semitransparente
            if (g2d.getColor().getAlpha() != 255) {
                g2d.fillRect(drawPosition.x, drawPosition.y, 
                    isSideways ? width : height,
                    isSideways ? height : width);
            }
        } else {
            // Fallback al método original si la imagen no se pudo cargar
            if (this.boatPlacementColour == Boat.BoatPlacementColour.PLACED) {
                g.setColor(this.destroyedSections >= this.segments ? Color.RED : Color.DARK_GRAY);
            } else {
                g.setColor(this.boatPlacementColour == Boat.BoatPlacementColour.VALID ? Color.GREEN : Color.RED);
            }

            if (this.isSideways) {
                this.paintHorizontal(g);
            } else {
                this.paintVertical(g);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Position> getPosition() {
        return position;
    }

    public void setPosition(List<Position> position) {
        this.position = position;
    }

    public ColorManager getColor() {
        return color;
    }

    public void setColor(ColorManager color) {
        this.color = color;
    }

    public boolean isHitStatus() {
        return hitStatus;
    }

    public void setHitStatus(boolean hitStatus) {
        this.hitStatus = hitStatus;
    }
    
    public void setBoatPlacementColour(BoatPlacementColour boatPlacementColour) {
        this.boatPlacementColour = boatPlacementColour;
    }
    
    public void toggleSideways() {
        this.isSideways = !this.isSideways;
    }

    public void destroySection() {
        ++this.destroyedSections;
    }

    public boolean isDestroyed() {
        return this.destroyedSections >= this.segments;
    }
    
    public void setDrawPosition(Position gridPosition, Position drawPosition) {
        this.drawPosition = drawPosition;
        this.gridPosition = gridPosition;
    }
    
    public boolean isSideways() {
        return this.isSideways;
    }

    public int getSegments() {
        return this.segments;
    }
    
    public List<Position> getOccupiedCoordinates() {
        List<Position> result = new ArrayList();
        int x;
        if (this.isSideways) {
            for(x = 0; x < this.segments; ++x) {
                result.add(new Position(this.gridPosition.x + x, this.gridPosition.y));
            }
        } else {
            for(x = 0; x < this.segments; ++x) {
                result.add(new Position(this.gridPosition.x, this.gridPosition.y + x));
            }
        }

        return result;
    }
    
    private void paintVertical(Graphics g) {
        int boatWidth = 24;
        int boatLeftX = this.drawPosition.x + 15 - boatWidth / 2;
        g.fillPolygon(new int[]{this.drawPosition.x + 15, boatLeftX, boatLeftX + boatWidth},
                     new int[]{this.drawPosition.y + 7, this.drawPosition.y + 30, this.drawPosition.y + 30},
                     3);
        g.fillRect(boatLeftX, this.drawPosition.y + 30, boatWidth, (int)(30.0 * ((double)this.segments - 1.2)));
    }
    
    private void paintHorizontal(Graphics g) {
        int boatWidth = 24;
        int boatTopY = this.drawPosition.y + 15 - boatWidth / 2;
        g.fillPolygon(new int[]{this.drawPosition.x + 7, this.drawPosition.x + 30, this.drawPosition.x + 30},
                     new int[]{this.drawPosition.y + 15, boatTopY, boatTopY + boatWidth},
                     3);
        g.fillRect(this.drawPosition.x + 30, boatTopY, (int)(30.0 * ((double)this.segments - 1.2)), boatWidth);
    }
    
    public static enum BoatPlacementColour {
        VALID, INVALID, PLACED;
        
        private BoatPlacementColour() {
        }
    }
}