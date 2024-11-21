/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.List;

/**
 * @author Angel De Jesus Santos Arabia - 205750
 * @author Jose Miguel Rodriguez Reyna - 216743
 * @author Jesus Gabriel Medina Leyva - 247527
 * @author Cristopher Alberto Elizalde Andrade - 240005
 */
public class Shoot {

    private boolean hit;
    private List<Position> positionShooted;

    public boolean ShootValidation() {
        return true;
    }

    public Shoot() {
    }
    
    public Shoot(boolean hit, List<Position> positionShooted) {
        this.hit = hit;
        this.positionShooted = positionShooted;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public List<Position> getPositionShooted() {
        return positionShooted;
    }

    public void setPositionShooted(List<Position> positionShooted) {
        this.positionShooted = positionShooted;
    }
    
    
}
