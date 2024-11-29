/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Network;

import EventBus.MessageBus;
import static Utilities.AppLogger.DEFAULT_LEVEL;
import static Utilities.AppLogger.initializeLogger;
import static Utilities.AppLogger.logger;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author Chris
 */
public class BattleShipServer {

    private static final Integer PORT_NUMBER = 777;
    static final int NUMBER_OF_PLAYERS = 2;
    public static final int SERVER_ID = 0;
    private MessageBus requestBus;
    List<ClientConnectionHandler> clients;

    public BattleShipServer() {
        requestBus = new MessageBus();
    }

    void proceed() {
        initializeLogger();
        connectWithPlayers(createServerSocket(PORT_NUMBER));
        BattleshipGame game = new BattleshipGame(clients, requestBus);
        game.proceed();
    }

    ServerSocket createServerSocket(int portNumber) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
            logger.log(DEFAULT_LEVEL, "IP ADDRESS OF THE SERVER: "
                    + InetAddress.getLocalHost().toString().split("/")[1]);
        } catch (IOException e) {
            logger.log(Level.WARNING, "could't create server socket", e);
        }
        return serverSocket;
    }

    void connectWithPlayers(ServerSocket serverSocket) {
        clients = new ArrayList<>();
        for (int i = 1; i < NUMBER_OF_PLAYERS + 1; i++) {
            ClientConnectionHandler clientConnectionHandler = null;
            try {
                clientConnectionHandler = new ClientConnectionHandler(i, requestBus);
                clientConnectionHandler.initializeSocket(serverSocket);
                clientConnectionHandler.setUpStreams();
                clientConnectionHandler.start();
            } catch (IOException e) {
                logger.log(Level.WARNING, "couldn't accept connection from client", e);
            }
            clients.add(clientConnectionHandler);
        }
    }
}
