/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angel De Jesus Santos Arabia - 205750
 * @author Jose Miguel Rodriguez Reyna - 216743
 * @author Jesus Gabriel Medina Leyva - 247527
 * @author Cristopher Alberto Elizalde Andrade - 240005
 *
 */
public class Board {

    private List<Boat> botes;
    private ArrayList<Position> positions;
    private int gridWith;
    private int gridHeight;
    private List<Shoot> pastShots;
    
    public boolean boatValidation(){
        return true;
    }
    
    public boolean allBoatsSunken(){
        return true;
    }

    public Board() {
    }

    public Board(List<Boat> botes, ArrayList<Position> positions, int gridWith, int gridHeight, List<Shoot> pastShots) {
        this.botes = botes;
        this.positions = positions;
        this.gridWith = gridWith;
        this.gridHeight = gridHeight;
        this.pastShots = pastShots;
    }

    public List<Boat> getBotes() {
        return botes;
    }

    public void setBotes(List<Boat> botes) {
        this.botes = botes;
    }

    public ArrayList<Position> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<Position> positions) {
        this.positions = positions;
    }

    public int getGridWith() {
        return gridWith;
    }

    public void setGridWith(int gridWith) {
        this.gridWith = gridWith;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }

    public List<Shoot> getPastShots() {
        return pastShots;
    }

    public void setPastShots(List<Shoot> pastShots) {
        this.pastShots = pastShots;
    }
    
    

}
