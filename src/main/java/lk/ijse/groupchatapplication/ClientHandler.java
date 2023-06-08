package lk.ijse.groupchatapplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 5/21/2023
 * Time :10:51 PM
 */

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private String clientUserName;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    public ClientHandler(Socket socket) {

    }


    @Override
    public void run() {
        incomingMessage();
    }

    private void incomingMessage() {
        String messageFromClient;
        while (socket.isConnected()){
            try {
                messageFromClient = dataInputStream.readUTF();
                broadCastMessage(this, messageFromClient);
            }catch (IOException e){
                // closed
                e.printStackTrace();
                closeEverything(socket, dataOutputStream, dataInputStream);
                break;
            }
        }
    }

    private void broadCastMessage(ClientHandler client, String messageToSent) {
        for (ClientHandler clientHandler: clientHandlers){
            try {
                if (client != clientHandler){
                    clientHandler.dataOutputStream.writeUTF(messageToSent);
                    clientHandler.dataOutputStream.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, dataOutputStream, dataInputStream);
            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        System.out.println("close");
        broadCastMessage(this, "Server : "+clientUserName+" has left the chat");
    }

    private void closeEverything(Socket socket, DataOutputStream dataOutputStream, DataInputStream dataInputStream) {
        removeClientHandler();
        try {
            if (socket != null){
                socket.close();
            }
            if (dataOutputStream != null){
                dataOutputStream.close();
            }
            if (dataInputStream != null){
                dataInputStream.close();
            }
        } catch (IOException e) {e.printStackTrace();
            e.printStackTrace();
        }
    }
}
