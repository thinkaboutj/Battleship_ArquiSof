/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Network;

/**
 *
 * @author Chris
 */
public class ServerMain {

    public static void main(String[] args) {
        System.out.println("Server is running...");
        BattleShipServer server = new BattleShipServer();
        server.proceed();
    }
}
