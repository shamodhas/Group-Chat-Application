package lk.ijse.groupchatapplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 6/8/2023
 * Time : 4:33 PM
 */

public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clients = new ArrayList<>();
            System.out.println("Server started on port " + port);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket);
                    ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                    clients.add(clientHandler);
                    clientHandler.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(ClientHandler clientHandler, String message) {
        message = clientHandler.getUserName() +" : "+message;
        for (ClientHandler client : clients) {
            if (client != clientHandler) {
                client.sendMessage(message);
            }
        }
    }

    public void removeClient(ClientHandler client) {
        broadcastMessage(client, "left the chat ..!");
        clients.remove(client);
    }

    public static void main(String[] args) {
        Server server = new Server(1234);
    }
}