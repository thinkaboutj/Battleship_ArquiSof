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
public class EndState extends GameState {
    EndState(Users players, MessageBus requestBus) {
        super(players, requestBus);
    }

    @Override
    public boolean isGameRunning() {
        return false;
    }

    @Override
    public GameState transform() {
        return null;
    }
}
