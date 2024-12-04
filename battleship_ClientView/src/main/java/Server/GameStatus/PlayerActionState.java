package Server.GameStatus;

import Utilerias.Header;
import Server.Players;
import Server.Bus.MessageBus;

import static Server.BattleshipServer.SERVER_ID;

public class PlayerActionState extends GameState {

    PlayerActionState(Players players, MessageBus requestBus) {
        super(players, requestBus);
    }

    @Override
    public GameState transform() {
        requestBus.addMessage(SERVER_ID, players.getCurrentPlayer().getPlayerId(), Header.PLAYER_TURN.name());
        requestBus.addMessage(SERVER_ID, players.getOpponentOf(players.getCurrentPlayer()).getPlayerId(),
                Header.OPPONENT_TURN.name());
        return new ShotState(players, requestBus);
    }
}