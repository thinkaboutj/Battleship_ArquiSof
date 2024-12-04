/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PatronState;

import Dominio.Users;
import EventBus.MessageBus;

/**
 *
 * @author Chris
 */
public abstract class GameState {
    protected Users players;
    protected MessageBus requestBus;

    protected GameState(Users players, MessageBus requestBus) {
        this.players = players;
        this.requestBus = requestBus;
    }

    public boolean isGameRunning() {
        return true;
    }

    public abstract GameState transform();
}
