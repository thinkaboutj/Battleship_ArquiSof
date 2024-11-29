/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PatronState;

import Dominio.User;
import Dominio.Users;
import EventBus.MessageBus;
import static Network.BattleShipServer.SERVER_ID;
import Network.NetworkMessage;
import Utilities.Header;

/**
 *
 * @author Chris
 */
public class ResultState extends GameState {

    ResultState(Users players, MessageBus requestBus) {
        super(players, requestBus);
    }

    @Override
    public GameState transform() {
        buildWinnerResponse();
        return new EndState(players, requestBus);
    }

    private void buildWinnerResponse() {
        User winner = players.getWinner();
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
