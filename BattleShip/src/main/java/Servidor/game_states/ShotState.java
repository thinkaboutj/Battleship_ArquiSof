package Servidor.game_states;

import Servidor.utilerias.Header;
import Servidor.utilerias.NetworkMessage;
import Servidor.Player;
import Servidor.Players;
import Server.bus.MessageBuilder;
import Server.bus.MessageBus;
import Servidor.fleet.Ship;

import static Servidor.BattleshipServer.SERVER_ID;

/**
 * 
 * @author jesus
 */
public class ShotState extends GameState {
    ShotState(final Players players, final MessageBus requestBus) {
        super(players, requestBus);
    }

    @Override
    public GameState transform() {
        Player currentPlayer = getCurrentPlayer();

        if (!shootIfPlayerSentValidMessage(currentPlayer)) {
            return !didPlayerWon(currentPlayer)
                    ? this
                    : new ResultState(
                    players, requestBus);
        }
        return !didPlayerWon(currentPlayer)
                ? new PlayerActionState(
                players, requestBus)
                : new ResultState(
                players, requestBus);
    }

    private boolean didPlayerWon(final Player player) {
        if (players.getOpponentOf(player).hasNoFleet()) {
            players.setWinner(player);
            return true;
        }
        return false;
    }

    private boolean shootIfPlayerSentValidMessage(final Player player) {
        if (requestBus.haveMessageFromSender(player.getPlayerId())) {
            String shotMessage = getMessageContentFromPlayer(player);
            if (shotMessage.contains(Header.MOVE_REGULAR.name())) {
                if (shotExecution(shotMessage, player)) {
                    return false;
                }
                players.switchCurrentPlayer();
                return true;
            }
        }
        return false;
    }

    private boolean shotExecution(
            final String messageContent, final Player player) {
        Integer targetedPoint = Integer.valueOf(messageContent.substring(
                messageContent.indexOf(
                        NetworkMessage.RESPONSE_HEADER_SPLIT_CHARACTER) + 1,
                messageContent.indexOf(
                        NetworkMessage.RESPONSE_SPLIT_CHARACTER)
        ));

        Player opponent = players.getOpponentOf(player);
        if (opponent.fleetGotHit(targetedPoint)) {
            notifyPlayersAboutHit(player, opponent, targetedPoint);
            if (opponent.gotDestroyedShip()) {
                notifyPlayersAboutDestroyedShip(
                        player,
                        opponent,
                        opponent.pullDestroyedShip()
                );
            }
            return true;
        }
        notifyPlayersAboutMiss(player, opponent, targetedPoint);

        return false;
    }

    private void notifyPlayersAboutDestroyedShip(
            final Player player, final Player opponent, final Ship ship) {
        requestBus.addMessage(
                new MessageBuilder(
                        SERVER_ID,
                        player.getPlayerId()
                ).buildResponseDestroyedShipMessage(ship).getMessage()
        );
        requestBus.addMessage(
                new MessageBuilder(
                        SERVER_ID,
                        opponent.getPlayerId()
                ).buildResponseOpponentDestroyedShipMessage(ship).getMessage()
        );
    }

    private void notifyPlayersAboutHit(
            final Player player,
            final Player opponent,
            final Integer targetedPoint) {
        requestBus.addMessage(
                new MessageBuilder(
                        SERVER_ID,
                        player.getPlayerId()
                ).buildResponseHitMessage(targetedPoint).getMessage()
        );
        requestBus.addMessage(
                new MessageBuilder(
                        SERVER_ID,
                        opponent.getPlayerId()
                ).buildResponseOpponentHitMessage(targetedPoint).getMessage()
        );
    }

    private void notifyPlayersAboutMiss(
            final Player player,
            final Player opponent,
            final Integer targetedPoint) {
        requestBus.addMessage(
                new MessageBuilder(
                        SERVER_ID,
                        player.getPlayerId()
                ).buildResponseMissMessage(targetedPoint).getMessage()
        );
        requestBus.addMessage(
                new MessageBuilder(
                        SERVER_ID,
                        opponent.getPlayerId()
                ).buildResponseOpponentMissMessage(targetedPoint).getMessage()
        );
    }

    private Player getCurrentPlayer() {
        return players.getCurrentPlayer();
    }

    private String getMessageContentFromPlayer(final Player player) {
        return requestBus.getMessageFrom(player.getPlayerId()).getContent();
    }
}
