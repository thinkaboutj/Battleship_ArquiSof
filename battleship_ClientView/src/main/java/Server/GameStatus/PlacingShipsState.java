package Server.GameStatus;

import Utilerias.Header;
import Server.Player;
import Server.Players;
import Server.Bus.MessageBus;
import Cliente.Fleet.FleetBuilder;
import Cliente.Fleet.FleetValidator;

import java.util.LinkedList;
import java.util.List;

import static Server.BattleshipServer.SERVER_ID;

public class PlacingShipsState extends GameState {
    public PlacingShipsState(Players players, MessageBus requestBus) {
        super(players, requestBus);
    }

    @Override
    public GameState transform() {
        List<Player> notReadyPlayers = new LinkedList<>(players.getBothPlayers());
        while (!notReadyPlayers.isEmpty()) {
            for (Player player : notReadyPlayers) {
                if (requestBus.haveMessageFromSender(player.getPlayerId())) {
                    resolvePlayerFleetValidation(player, notReadyPlayers);
                }
            }
        }
        return new PlayerActionState(players, requestBus);
    }

    private void resolvePlayerFleetValidation(Player player, List<Player> notReadyPlayers) {
        String fleetMessage = requestBus.getMessageFrom(player.getPlayerId()).getContent();
        boolean isValidFleet = validateFleet(fleetMessage);
        if (isValidFleet) {
            player.setFleet(new FleetBuilder().build(fleetMessage));
            notReadyPlayers.remove(player);
            requestBus.addMessage(SERVER_ID, player.getPlayerId(), Header.FLEET_VALID.name());
        } else {
            requestBus.addMessage(SERVER_ID, player.getPlayerId(), Header.FLEET_INVALID.name());
        }
    }

    private boolean validateFleet(String fleetMessage) {
        return fleetMessage.contains(Header.FLEET_REQUEST.name()) && FleetValidator.validate(fleetMessage);
    }
}