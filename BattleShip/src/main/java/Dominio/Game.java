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
public class Game {

    private List<User> users;
    private Board board;
    private boolean gameStatus;
    private List<User> currentTurn;
    private List<Boat> boatsToPlace;

    public Game() {
    }

    public Game(List<User> users, Board board, boolean gameStatus, List<User> currentTurn, List<Boat> boatsToPlace) {
        this.users = users;
        this.board = board;
        this.gameStatus = gameStatus;
        this.currentTurn = currentTurn;
        this.boatsToPlace = boatsToPlace;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean isGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(boolean gameStatus) {
        this.gameStatus = gameStatus;
    }

    public List<User> getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(List<User> currentTurn) {
        this.currentTurn = currentTurn;
    }

    public List<Boat> getBoatsToPlace() {
        return boatsToPlace;
    }

    public void setBoatsToPlace(List<Boat> boatsToPlace) {
        this.boatsToPlace = boatsToPlace;
    }
    
    
}
