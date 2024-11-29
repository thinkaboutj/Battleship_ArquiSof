/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PatronState;

import Dominio.Users;
import EventBus.MessageBus;
import static Network.BattleShipServer.SERVER_ID;
import Utilities.Header;

/**
 *
 * @author Chris
 */
public class PlayerActionState extends GameState {

    PlayerActionState(Users players, MessageBus requestBus) {
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
