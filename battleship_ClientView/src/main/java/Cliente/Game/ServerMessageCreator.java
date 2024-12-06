package Cliente.Game;

import Servidor.utilerias.Header;

import java.util.List;

/**
 * 
 * @author jesus
 */
public class ServerMessageCreator {

    public static String createFleetMessage(List<Integer> sheepPlaces) {
        StringBuilder builder = new StringBuilder();
        sheepPlaces.forEach(integer -> builder.append(integer).append(","));
        builder.deleteCharAt(builder.length() - 1); // remove last ','
        return Header.FLEET_REQUEST.name() + ":[" + builder.toString() + "];";
    }
}