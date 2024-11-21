/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tablero;

import Dominio.Position;

/**
 *
 * @author Link
 */
public class Rectangulo {
    protected  Position position;
    protected int width;
    protected int height;

    public Rectangulo(Position position, int width, int height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }
    
    public Rectangulo(int x, int y, int width, int height) {
        this(new Position(x, y), width, height);
    }
    
    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public Position getPosition() {
        return this.position;
    }
    
    public boolean isPositionInside(Position targetPosition) {
        return targetPosition.x >= this.position.x && targetPosition.y >= this.position.y && targetPosition.x < this.position.x + this.width && targetPosition.y < this.position.y + this.height;
    }
}
