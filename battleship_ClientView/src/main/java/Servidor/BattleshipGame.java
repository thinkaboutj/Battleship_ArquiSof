package Servidor;

import com.spanish_inquisition.battleship.common.AppLogger;
import Server.bus.MessageBus;
import Servidor.game_states.GameState;
import Servidor.game_states.PlacingShipsState;

import java.util.List;
import java.util.logging.Logger;

import static com.spanish_inquisition.battleship.common.AppLogger.DEFAULT_LEVEL;

/**
 * 
 * @author jesus
 */
public class BattleshipGame {
    private List<ClientConnectionHandler> clients;
    private MessageBus requestBus;
    private static final Logger logger = Logger.getLogger(AppLogger.class.getName());

    public BattleshipGame(List<ClientConnectionHandler> clients, MessageBus requestBus) {
        this.clients = clients;
        this.requestBus = requestBus;
    }

    public void proceed() {
        Players players = new Players();
        for (ClientConnectionHandler client : clients) {
            players.addPlayer(new Player(client));
        }
        GameState currentState = new PlacingShipsState(players, requestBus);
        while (currentState.isGameRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.log(DEFAULT_LEVEL, "Server game main thread interrupted");
            }
            currentState = currentState.transform();
        }
    }
}