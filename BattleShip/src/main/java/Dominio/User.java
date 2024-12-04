/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import Network.ClientConnectionHandler;
import java.util.List;

/**
 * @author Angel De Jesus Santos Arabia - 205750
 * @author Jose Miguel Rodriguez Reyna - 216743
 * @author Jesus Gabriel Medina Leyva - 247527
 * @author Cristopher Alberto Elizalde Andrade - 240005
 */
public class User {

    private String username;
    private String avatarPath;
    private List<Boat> boats;
    private int score;
    private int shotsFired;
    private ClientConnectionHandler client;

    public User() {
    }

    public User(ClientConnectionHandler client) {
        this.client = client;
    }

    public User(String username, String avatarPath, List<Boat> boats, int score, int shotsFired) {
        this.username = username;
        this.avatarPath = avatarPath;
        this.boats = boats;
        this.score = score;
        this.shotsFired = shotsFired;
    }
    
    public int getPlayerId() {
        return client.getClientId();
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public List<Boat> getBoats() {
        return boats;
    }

    public void setBoats(List<Boat> boats) {
        this.boats = boats;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getShotsFired() {
        return shotsFired;
    }

    public void setShotsFired(int shotsFired) {
        this.shotsFired = shotsFired;
    }

}
