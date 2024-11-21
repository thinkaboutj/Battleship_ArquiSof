/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tablero;

import Dominio.Boat;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Link
 */
public class Marker extends Rectangulo{
    private final Color HIT_COLOUR = new Color(219, 23, 23, 180);
    private final Color MISS_COLOUR = new Color(26, 26, 97, 180);
    private final int PADDING = 3;
    private boolean showMarker;
    private Boat boatAtMarker;
    
    public Marker(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.reset();
    }

    public void reset() {
        this.boatAtMarker = null;
        this.showMarker = false;
    }

    public void mark() {
        if (!this.showMarker && this.isBoat()) {
            this.boatAtMarker.destroySection();
        }

        this.showMarker = true;
    }
    
    public boolean isMarked() {
        return this.showMarker;
    }

    public void setAsShip(Boat boat) {
        this.boatAtMarker = boat;
    }

    public boolean isBoat() {
        return this.boatAtMarker != null;
    }

    public Boat getAssociatedShip() {
        return this.boatAtMarker;
    }

    public void paint(Graphics g) {
        if (this.showMarker) {
            g.setColor(this.isBoat() ? this.HIT_COLOUR : this.MISS_COLOUR);
            g.fillRect(this.position.x + 3 + 1, this.position.y + 3 + 1, this.width - 6, this.height - 6);
        }
    }
}
