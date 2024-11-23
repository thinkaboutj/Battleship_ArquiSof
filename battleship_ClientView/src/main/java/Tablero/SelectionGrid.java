/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tablero;

import Dominio.Boat;
import Dominio.Position;
import Pantallas.SetUpView;
import java.util.List;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Link
 */
public class SelectionGrid extends Rectangulo{
    public static final int CELL_SIZE = 30;
    public static final int GRID_WIDTH = 10;
    public static final int GRID_HEIGHT = 10;
    public static final int[] BOAT_SIZES = new int[]{5, 4, 3, 3, 2};
    private Marker[][] markers = new Marker[10][10];
    private List<Boat> boats;
    private Random rand;
    private boolean showBoats;
    private boolean allBoatsDestroyed;
    
    public SelectionGrid(int x, int y) {
        super(x, y, 300, 300);
        this.createMarkerGrid();
        this.boats = new ArrayList();
        this.rand = new Random();
        this.showBoats = false;
    }
    
    public void paint(Graphics g) {
        Iterator var2 = this.boats.iterator();

        while(true) {
            Boat boat;
            do {
                if (!var2.hasNext()) {
                    this.drawMarkers(g);
                    this.drawGrid(g);
                    return;
                }

                boat = (Boat)var2.next();
            } while(!this.showBoats && !SetUpView.debugModeActive && !boat.isDestroyed());

            boat.paint(g);
        }
    }
    
    public void setShowBoats(boolean showBoats) {
        this.showBoats = showBoats;
    }

    public void reset() {
        for(int x = 0; x < 10; ++x) {
            for(int y = 0; y < 10; ++y) {
                this.markers[x][y].reset();
            }
        }

        this.boats.clear();
        this.showBoats = false;
        this.allBoatsDestroyed = false;
    }
    
    public boolean markPosition(Position posToMark) {
        this.markers[posToMark.x][posToMark.y].mark();
        this.allBoatsDestroyed = true;
        Iterator var2 = this.boats.iterator();

        while(var2.hasNext()) {
            Boat boat = (Boat)var2.next();
            if (!boat.isDestroyed()) {
                this.allBoatsDestroyed = false;
                break;
            }
        }

        return this.markers[posToMark.x][posToMark.y].isBoat();
    }
    
    public boolean areAllBoatsDestroyed() {
        return this.allBoatsDestroyed;
    }

    public boolean isPositionMarked(Position posToTest) {
        return this.markers[posToTest.x][posToTest.y].isMarked();
    }

    public Marker getMarkerAtPosition(Position posToSelect) {
        return this.markers[posToSelect.x][posToSelect.y];
    }
    
    public boolean canPlaceBoatAt(int gridX, int gridY, int segments, boolean sideways) {
        if (gridX >= 0 && gridY >= 0) {
            int y;
            if (sideways) {
                if (gridY > 10 || gridX + segments > 10) {
                    return false;
                }

                for(y = 0; y < segments; ++y) {
                    if (this.markers[gridX + y][gridY].isBoat()) {
                        return false;
                    }
                }
            } else {
                if (gridY + segments > 10 || gridX > 10) {
                    return false;
                }

                for(y = 0; y < segments; ++y) {
                    if (this.markers[gridX][gridY + y].isBoat()) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }
    
    private void drawGrid(Graphics g) {
        g.setColor(Color.BLACK);
        int y2 = this.position.y;
        int y1 = this.position.y + this.height;

        int x2;
        for(x2 = 0; x2 <= 10; ++x2) {
            g.drawLine(this.position.x + x2 * 30, y1, this.position.x + x2 * 30, y2);
        }

        x2 = this.position.x;
        int x1 = this.position.x + this.width;

        for(int y = 0; y <= 10; ++y) {
            g.drawLine(x1, this.position.y + y * 30, x2, this.position.y + y * 30);
        }
    }
    
    private void drawMarkers(Graphics g) {
        for(int x = 0; x < 10; ++x) {
            for(int y = 0; y < 10; ++y) {
                this.markers[x][y].paint(g);
            }
        }

    }
    
    private void createMarkerGrid() {
        for(int x = 0; x < 10; ++x) {
            for(int y = 0; y < 10; ++y) {
                this.markers[x][y] = new Marker(this.position.x + x * 30, this.position.y + y * 30, 30, 30);
            }
        }
    }
    
    public void populateBoats() {
        this.boats.clear();

        for(int i = 0; i < BOAT_SIZES.length; ++i) {
            boolean sideways = this.rand.nextBoolean();

            int gridX;
            int gridY;
            do {
                gridX = this.rand.nextInt(sideways ? 10 - BOAT_SIZES[i] : 10);
                gridY = this.rand.nextInt(sideways ? 10 : 10 - BOAT_SIZES[i]);
            } while(!this.canPlaceBoatAt(gridX, gridY, BOAT_SIZES[i], sideways));

            this.placeBoat(gridX, gridY, BOAT_SIZES[i], sideways);
        }
    }
    
    public void placeBoat(int gridX, int gridY, int segments, boolean sideways) {
        this.placeBoat(new Boat(new Position(gridX, gridY), new Position(this.position.x + gridX * 30, this.position.y + gridY * 30), segments, sideways), gridX, gridY);
    }
    
    public void placeBoat(Boat boat, int gridX, int gridY) {
        this.boats.add(boat);
        int x;
        if (boat.isSideways()) {
            for(x = 0; x < boat.getSegments(); ++x) {
                this.markers[gridX + x][gridY].setAsShip((Boat)this.boats.get(this.boats.size() - 1));
            }
        } else {
            for(x = 0; x < boat.getSegments(); ++x) {
                this.markers[gridX][gridY + x].setAsShip((Boat)this.boats.get(this.boats.size() - 1));
            }
        }

    }
    
     public Position getPositionInGrid(int mouseX, int mouseY) {
        if(!isPositionInside(new Position(mouseX,mouseY))) return new Position(-1,-1);

        return new Position((mouseX - position.x)/CELL_SIZE, (mouseY - position.y)/CELL_SIZE);
    }
     
     public boolean canPlaceShipAt(int gridX, int gridY, int segments, boolean sideways) {
        if(gridX < 0 || gridY < 0) return false;

        if(sideways) { 
            if(gridY > GRID_HEIGHT || gridX + segments > GRID_WIDTH) return false;
            for(int x = 0; x < segments; x++) {
                if(markers[gridX+x][gridY].isBoat()) return false;
            }
        } else { 
            if(gridY + segments > GRID_HEIGHT || gridX > GRID_WIDTH) return false;
            for(int y = 0; y < segments; y++) {
                if(markers[gridX][gridY+y].isBoat()) return false;
            }
        }
        return true;
    }
}
