/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Network;

import Dominio.User;
import Dominio.Users;
import EventBus.MessageBus;
import PatronState.GameState;
import PatronState.PlacingShipsState;
import Utilities.AppLogger;
import static Utilities.AppLogger.DEFAULT_LEVEL;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Chris
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
        Users players = new Users();
        for (ClientConnectionHandler client : clients) {
            players.addPlayer(new User(client));
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
