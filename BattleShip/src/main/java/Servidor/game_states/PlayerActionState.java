package Servidor.game_states;

import Servidor.utilerias.Header;
import Servidor.Players;
import Server.bus.MessageBus;

import static Servidor.BattleshipServer.SERVER_ID;

/**
 * 
 * @author jesus
 */
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