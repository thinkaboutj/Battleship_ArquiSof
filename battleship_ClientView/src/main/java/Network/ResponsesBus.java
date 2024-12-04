/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Network;

import static Utilities.AppLogger.DEFAULT_LEVEL;
import static Utilities.AppLogger.logger;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Chris
 */
public class ResponsesBus {

    private Queue<NetworkMessage> serverResponses;

    ResponsesBus() {
        this.serverResponses = new ConcurrentLinkedQueue<>();
    }

    public boolean hasServerResponses() {
        return !this.serverResponses.isEmpty();
    }

    public NetworkMessage getAServerResponse() {
        return this.serverResponses.poll();
    }

    void addAServerResponse(String response) {
        logger.log(DEFAULT_LEVEL, "Response from server : " + response);
        if (response == null || response.isEmpty()) {
            logger.info("Response empty");
            return;
        }
        List<NetworkMessage> clientServerMessages = NetworkMessage.Parser.parseServerResponse(response);
        this.serverResponses.addAll(clientServerMessages);
    }
}
