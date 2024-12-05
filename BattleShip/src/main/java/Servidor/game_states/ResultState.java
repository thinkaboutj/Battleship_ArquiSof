package Servidor.game_states;

import com.spanish_inquisition.battleship.common.Header;
import com.spanish_inquisition.battleship.common.NetworkMessage;
import Servidor.Player;
import Servidor.Players;
import Server.bus.MessageBus;

import static Servidor.BattleshipServer.SERVER_ID;

/**
 * 
 * @author jesus
 */
public class ResultState extends GameState {
    ResultState(Players players, MessageBus requestBus) {
        super(players, requestBus);
    }

    @Override
    public GameState transform() {
        buildWinnerResponse();
        return new EndState(players, requestBus);
    }

    private void buildWinnerResponse() {
        Player winner = players.getWinner();
        if (winner != null) {
            NetworkMessage winningMessage = new NetworkMessage(Header.GAME_WON, "true");
            NetworkMessage losingMessage = new NetworkMessage(Header.GAME_WON, "false");
            requestBus.addMessage(SERVER_ID, players.getWinner().getPlayerId(),
                    NetworkMessage.Parser.parseMessageToString(winningMessage));
            requestBus.addMessage(SERVER_ID, players.getOpponentOf(players.getWinner()).getPlayerId(),
                    NetworkMessage.Parser.parseMessageToString(losingMessage));
        }
    }
}
