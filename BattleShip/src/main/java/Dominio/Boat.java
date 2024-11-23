package Dominio;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

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
    private Image boatImage;
    
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
        String imagePath = "";
        switch(segments) {
            case 4:
                imagePath = "/images/PortaAviones.png";
                break;
            case 3:
                imagePath = "/images/Crucero.png";
                break;
            case 2:
                imagePath = "/images/Submarino.png";
                break;
            case 1:
                imagePath = "/images/Barco.png";
                break;
        }
        try {
            this.boatImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        } catch (Exception e) {
            System.err.println("Error loading boat image: " + imagePath);
            e.printStackTrace();
        }
    }
    
    public void paint(Graphics g) {
        if (boatImage != null) {
            int width = isSideways ? segments * 30 : 30;
            int height = isSideways ? 30 : segments * 30;
            
            if (this.boatPlacementColour == Boat.BoatPlacementColour.PLACED) {
                if (this.destroyedSections >= this.segments) {
                    g.setColor(new Color(255, 0, 0, 128));  // Semi-transparent red for destroyed
                    g.fillRect(drawPosition.x, drawPosition.y, width, height);
                }
                g.drawImage(boatImage, drawPosition.x, drawPosition.y, width, height, null);
            } else {
                // For placement preview
                Color overlayColor = this.boatPlacementColour == Boat.BoatPlacementColour.VALID ? 
                    new Color(0, 255, 0, 128) : new Color(255, 0, 0, 128);
                g.setColor(overlayColor);
                g.fillRect(drawPosition.x, drawPosition.y, width, height);
                g.drawImage(boatImage, drawPosition.x, drawPosition.y, width, height, null);
            }
        } else {
            // Fallback to original geometric drawing if image loading fails
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

    // [Mantener todos los getters y setters existentes sin cambios]
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

    // Los métodos paintVertical y paintHorizontal se mantienen como respaldo
    private void paintVertical(Graphics g) {
        int boatWidth = 24;
        int boatLeftX = this.drawPosition.x + 15 - boatWidth / 2;
        g.fillPolygon(new int[]{this.drawPosition.x + 15, boatLeftX, boatLeftX + boatWidth}, 
                      new int[]{this.drawPosition.y + 7, this.drawPosition.y + 30, this.drawPosition.y + 30}, 3);
        g.fillRect(boatLeftX, this.drawPosition.y + 30, boatWidth, (int)(30.0 * ((double)this.segments - 1.2)));
    }
    
    private void paintHorizontal(Graphics g) {
        int boatWidth = 24;
        int boatTopY = this.drawPosition.y + 15 - boatWidth / 2;
        g.fillPolygon(new int[]{this.drawPosition.x + 7, this.drawPosition.x + 30, this.drawPosition.x + 30}, 
                      new int[]{this.drawPosition.y + 15, boatTopY, boatTopY + boatWidth}, 3);
        g.fillRect(this.drawPosition.x + 30, boatTopY, (int)(30.0 * ((double)this.segments - 1.2)), boatWidth);
    }
    
    public static enum BoatPlacementColour {
        VALID, INVALID, PLACED;
        
        private BoatPlacementColour() {
        }
    }
}