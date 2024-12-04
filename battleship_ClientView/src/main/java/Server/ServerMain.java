package Server;

public class ServerMain {
    public static void main(String[] args) {
        System.out.println("Server is running...");
        BattleshipServer server = new BattleshipServer();
        server.proceed();
    }
}
