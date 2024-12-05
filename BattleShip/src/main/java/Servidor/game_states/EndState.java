package Servidor.game_states;

import Servidor.Players;
import Server.bus.MessageBus;

/**
 * 
 * @author jesus
 */
public class EndState extends GameState {
    EndState(Players players, MessageBus requestBus) {
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
