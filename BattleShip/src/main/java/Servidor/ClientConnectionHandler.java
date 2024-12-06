package Servidor;

import Servidor.utilerias.Header;
import Server.bus.MessageBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import static Servidor.utilerias.AppLogger.DEFAULT_LEVEL;
import static Servidor.utilerias.AppLogger.logger;
import static Servidor.BattleshipServer.SERVER_ID;

/**
 * 
 * @author jesus
 */
public class ClientConnectionHandler extends Thread {
    String name;
    private Socket clientSocket;
    private BufferedReader clientInput;
    private PrintWriter clientOutput;
    private final int clientId;
    private MessageBus requestBus;
    private boolean isConnected;

    public ClientConnectionHandler(final int clientId, MessageBus requestBus) {
        this.clientId = clientId;
        this.requestBus = requestBus;
        this.isConnected = true;
    }

    int getClientId() {
        return clientId;
    }

    void disconnect() {
        isConnected = false;
    }

    public void initializeSocket(final ServerSocket serverSocket) throws IOException {
        clientSocket = serverSocket.accept();
    }

    void setUpStreams() throws IOException {
        clientInput = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream(),
                        StandardCharsets.UTF_8));
        clientOutput = new PrintWriter(
                new OutputStreamWriter(clientSocket.getOutputStream(),
                        StandardCharsets.UTF_8), true);
    }

    public void run() {
        name = acceptNameFromClient();

        while (clientSocket.isConnected() && isConnected) {
            sendMessageToUser(clientOutput);
            getMessageFromUser(clientInput);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.log(DEFAULT_LEVEL, name + " la conexion del cliente con el hilo ha sido interrumpido");
            }
        }
    }

    private String acceptNameFromClient() {
        String readName = "";
        try {
            if (clientInput != null) {
                while (!clientInput.ready()) {
                    Thread.sleep(10);
                }
                readName = clientInput.readLine();
            }
        } catch (IOException | InterruptedException e) {
            logger.log(Level.WARNING, "couldn't read name from client", e);
        }
        return readName;
    }

    private void sendMessageToUser(PrintWriter output) {
        if (output != null) {
            if (requestBus.haveMessageForRecipient(clientId)) {
                String messageToSend = requestBus.getMessageFor(clientId).getContent();
                output.println(messageToSend);
            }
        }
    }

    private void getMessageFromUser(BufferedReader input) {
        try {
            if (input != null) {
                if (input.ready()) {
                    String lineFromClient = input.readLine();
                    logger.log(DEFAULT_LEVEL, "Message from client " + clientId + " " + lineFromClient);
                    if (lineFromClient != null && lineFromClient.trim().equals(Header.EXIT.name())) {
                        isConnected = false;
                    } else {
                        requestBus.addMessage(clientId, SERVER_ID, lineFromClient);
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could't read message from user: " + name + " with ID: " + clientId, e);
        }
    }
}