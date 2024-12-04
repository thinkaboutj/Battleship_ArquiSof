package Server.GameStatus;

import Server.Players;
import Server.Bus.MessageBus;

public abstract class GameState {
    protected Players players;
    protected MessageBus requestBus;

    protected GameState(Players players, MessageBus requestBus) {
        this.players = players;
        this.requestBus = requestBus;
    }

    public boolean isGameRunning() {
        return true;
    }

    public abstract GameState transform();
}