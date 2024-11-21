/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.awt.Color;
import java.awt.Graphics;
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
    
    public Boat(Position gridPosition, Position drawPosition, int segments, boolean isSideways) {
        this.gridPosition = gridPosition;
        this.drawPosition = drawPosition;
        this.segments = segments;
        this.isSideways = isSideways;
        this.destroyedSections = 0;
        this.boatPlacementColour = Boat.BoatPlacementColour.PLACED;
    }
    
    public void paint(Graphics g) {
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
    
    public void paintVertical(Graphics g) {
        int boatWidth = 24;
        int boatLeftX = this.drawPosition.x + 15 - boatWidth / 2;
        g.fillPolygon(new int[]{this.drawPosition.x + 15, boatLeftX, boatLeftX + boatWidth}, new int[]{this.drawPosition.y + 7, this.drawPosition.y + 30, this.drawPosition.y + 30}, 3);
        g.fillRect(boatLeftX, this.drawPosition.y + 30, boatWidth, (int)(30.0 * ((double)this.segments - 1.2)));
    }
    
    public void paintHorizontal(Graphics g) {
        int boatWidth = 24;
        int boatTopY = this.drawPosition.y + 15 - boatWidth / 2;
        g.fillPolygon(new int[]{this.drawPosition.x + 7, this.drawPosition.x + 30, this.drawPosition.x + 30}, new int[]{this.drawPosition.y + 15, boatTopY, boatTopY + boatWidth}, 3);
        g.fillRect(this.drawPosition.x + 30, boatTopY, (int)(30.0 * ((double)this.segments - 1.2)), boatWidth);
    }
    
    public static enum BoatPlacementColour{
        VALID, INVALID, PLACED;
        
        private BoatPlacementColour(){
            
        }
    }
}
