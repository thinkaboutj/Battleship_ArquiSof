package Server.GameStatus;

import Server.Players;
import Server.Bus.MessageBus;

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
