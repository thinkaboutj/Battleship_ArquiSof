/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PatronState;

import Dominio.User;
import Dominio.Users;
import EventBus.MessageBus;
import static Network.BattleShipServer.SERVER_ID;
import Utilities.Header;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Chris
 */
public class PlacingShipsState extends GameState {

    public PlacingShipsState(Users players, MessageBus requestBus) {
        super(players, requestBus);
    }

    @Override
    public GameState transform() {
        List<User> notReadyPlayers = new LinkedList<>(players.getBothPlayers());
        while (!notReadyPlayers.isEmpty()) {
            for (User player : notReadyPlayers) {
                if (requestBus.haveMessageFromSender(player.getPlayerId())) {
                    resolvePlayerFleetValidation(player, notReadyPlayers);
                }
            }
        }
        return new PlayerActionState(players, requestBus);
    }

    private void resolvePlayerFleetValidation(User player, List<User> notReadyPlayers) {
//        String fleetMessage = requestBus.getMessageFrom(player.getPlayerId()).getContent();
//        boolean isValidFleet = validateFleet(fleetMessage);
//        if (isValidFleet) {
//            player.setFleet(new FleetBuilder().build(fleetMessage));
//            notReadyPlayers.remove(player);
//            requestBus.addMessage(SERVER_ID, player.getPlayerId(), Header.FLEET_VALID.name());
//        } else {
//            requestBus.addMessage(SERVER_ID, player.getPlayerId(), Header.FLEET_INVALID.name());
//        }
    }

    private boolean validateFleet(String fleetMessage) {
//        return fleetMessage.contains(Header.FLEET_REQUEST.name()) && FleetValidator.validate(fleetMessage);
        return false;
    }
}
