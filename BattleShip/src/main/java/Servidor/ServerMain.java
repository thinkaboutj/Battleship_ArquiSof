package Servidor;

/**
 * 
 * @author jesus
 */
public class ServerMain {
    public static void main(String[] args) {
        System.out.println("EL servidor se encuentra en curso...");
        BattleshipServer server = new BattleshipServer();
        server.proceed();
    }
}
