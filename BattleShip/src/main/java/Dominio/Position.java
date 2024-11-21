/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

/**
 *
 * @author Link
 */
public class Position {
    public static final Position DOWN = new Position(0, 1);
    public static final Position UP = new Position(0, -1);
    public static final Position LEFT = new Position(-1, 0);
    public static final Position RIGHT = new Position(1, 0);
    public static final Position ZERO = new Position(0, 0);
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position positionToCopy) {
        this.x = positionToCopy.x;
        this.y = positionToCopy.y;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void add(Position otherPosition) {
        this.x += otherPosition.x;
        this.y += otherPosition.y;
    }

    public double distanceTo(Position otherPosition) {
        return Math.sqrt(Math.pow((double)(this.x - otherPosition.x), 2.0) + Math.pow((double)(this.y - otherPosition.y), 2.0));
    }
    
    public void multiply(int amount) {
        this.x *= amount;
        this.y *= amount;
    }

    public void subtract(Position otherPosition) {
        this.x -= otherPosition.x;
        this.y -= otherPosition.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Position position = (Position)o;
            return this.x == position.x && this.y == position.y;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
