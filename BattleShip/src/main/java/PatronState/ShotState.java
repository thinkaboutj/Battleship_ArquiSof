/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PatronState;

import Dominio.Ship;
import Dominio.User;
import Dominio.Users;
import EventBus.MessageBus;
import static Network.BattleShipServer.SERVER_ID;
import Network.NetworkMessage;
import PatronBuilder.MessageBuilder;
import Utilities.Header;

/**
 *
 * @author Chris
 */
public class ShotState extends GameState {

    ShotState(final Users players, final MessageBus requestBus) {
        super(players, requestBus);
    }

    @Override
    public GameState transform() {
//        User currentPlayer = getCurrentPlayer();
//
//        if (!shootIfPlayerSentValidMessage(currentPlayer)) {
//            return !didPlayerWon(currentPlayer)
//                    ? this
//                    : new ResultState(
//                            players, requestBus);
//        }
//        return !didPlayerWon(currentPlayer)
//                ? new PlayerActionState(
//                        players, requestBus)
//                : new ResultState(
//                        players, requestBus);
        return null;
    }

//    private boolean didPlayerWon(final User player) {
//        if (players.getOpponentOf(player).hasNoFleet()) {
//            players.setWinner(player);
//            return true;
//        }
//        return false;
//    }
    private boolean shootIfPlayerSentValidMessage(final User player) {
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
            final String messageContent, final User player) {
        Integer targetedPoint = Integer.valueOf(messageContent.substring(
                messageContent.indexOf(
                        NetworkMessage.RESPONSE_HEADER_SPLIT_CHARACTER) + 1,
                messageContent.indexOf(
                        NetworkMessage.RESPONSE_SPLIT_CHARACTER)
        ));

        User opponent = players.getOpponentOf(player);
//        if (opponent.fleetGotHit(targetedPoint)) {
//            notifyPlayersAboutHit(player, opponent, targetedPoint);
//            if (opponent.gotDestroyedShip()) {
//                notifyPlayersAboutDestroyedShip(
//                        player,
//                        opponent,
//                        opponent.pullDestroyedShip()
//                );
//            }
//            return true;
//        }
        notifyPlayersAboutMiss(player, opponent, targetedPoint);

        return false;
    }

    private void notifyPlayersAboutDestroyedShip(
            final User player, final User opponent, final Ship ship) {
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
            final User player,
            final User opponent,
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
            final User player,
            final User opponent,
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

    private User getCurrentPlayer() {
        return players.getCurrentPlayer();
    }

    private String getMessageContentFromPlayer(final User player) {
        return requestBus.getMessageFrom(player.getPlayerId()).getContent();
    }
}
