package Servidor;

import Server.bus.MessageBus;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static Servidor.utilerias.AppLogger.DEFAULT_LEVEL;
import static Servidor.utilerias.AppLogger.initializeLogger;
import static Servidor.utilerias.AppLogger.logger;

/**
 * 
 * @author jesus
 */
public class BattleshipServer {
    private static final Integer PORT_NUMBER = 6666;
    static final int NUMBER_OF_PLAYERS = 2;
    public static final int SERVER_ID = 0;
    private MessageBus requestBus;
    List<ClientConnectionHandler> clients;

    public BattleshipServer() {
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
            logger.log(DEFAULT_LEVEL, "La direccion IP del servidor: " +
                    InetAddress.getLocalHost().toString().split("/")[1]);
        } catch (IOException e) {
            logger.log(Level.WARNING, "No se pudo crear el socket del servidor", e);
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
                logger.log(Level.WARNING, "No se pudo aceptar la coneccion desde el cliente.", e);
            }
            clients.add(clientConnectionHandler);
        }
    }
}
