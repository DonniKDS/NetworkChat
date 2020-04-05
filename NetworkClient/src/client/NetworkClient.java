package client;

import ru.geekbrains.java2.client.controller.ClientController;

import java.io.IOException;

public class NetworkClient {

    public static void main(String[] args) {
        try {
            ClientController clientController = new ClientController("localhost", 10000);
            clientController.runApplication();
        } catch (IOException e) {
            System.err.println("Failed to connect to server! Please, check you network settings");
        }
    }
}