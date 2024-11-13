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

}
