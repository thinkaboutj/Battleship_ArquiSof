/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Chris
 */
public class Users {

    User player1;
    User player2;
    User currentPlayer;
    User winner;

    public Users() {
    }

    public Users(User player1, User player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
    }

    public void switchCurrentPlayer() {
        currentPlayer = getOpponentOf(currentPlayer);
    }

    public User getOpponentOf(User player) {
        return player1.equals(player) ? player2 : player1;
    }

    public User getCurrentPlayer() {
        return currentPlayer;
    }

    public void addPlayer(User player) {
        if (player1 == null) {
            player1 = player;
            currentPlayer = player1;
        } else if (player2 == null) {
            player2 = player;
        }
    }

    public List<User> getBothPlayers() {
        List<User> list = new LinkedList<>();
        list.add(player1);
        list.add(player2);
        return list;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }
}
