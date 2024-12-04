package Server.GameStatus;

import Utilerias.Header;
import Utilerias.NetworkMessage;
import Server.Player;
import Server.Players;
import Server.Bus.MessageBus;

import static Server.BattleshipServer.SERVER_ID;

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
