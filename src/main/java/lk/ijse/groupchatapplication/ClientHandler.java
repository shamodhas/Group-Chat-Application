package lk.ijse.groupchatapplication;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 5/21/2023
 * Time :10:51 PM
 */

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String userName;
    private Server server;

    public ClientHandler(Socket clientSocket, Server server) {
        try {
            this.clientSocket = clientSocket;
            this.server = server;
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            this.userName = reader.readLine();
            server.broadcastMessage(this, userName+" join to chat ..!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = reader.readLine();
                if (message != null) {
                    server.broadcastMessage(this, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
                clientSocket.close();
                server.removeClient(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//    private static final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
//    private Socket socket;
//    private String clientUserName;
//    DataInputStream dataInputStream;
//    DataOutputStream dataOutputStream;
//    public ClientHandler(Socket socket) {
//        try {
//            this.socket = socket;
//            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
//            this.dataInputStream = new DataInputStream(socket.getInputStream());
//
//
//            this.clientUserName = dataInputStream.readUTF();
//            clientHandlers.add(this);
//            broadCastTextMessage(this, "server : "+clientUserName+" has entered tha chat!");
//        } catch (IOException e) {
//            e.printStackTrace();
//            closeEverything(socket, dataOutputStream, dataInputStream);
//        }
//    }
//
//
//    @Override
//    public void run() {
//        incomingMessage();
//    }
//
//    private void incomingMessage() {
//        while (socket.isConnected()){
//            try {
////                String message = dataInputStream.readUTF();
//                if (dataInputStream.readBoolean()){
//                    broadCastImage(this);
//                }else {
//                    broadCastTextMessage(this, dataInputStream.readUTF());
//                }
//            }catch (IOException e){
//                // closed
//                e.printStackTrace();
//                closeEverything(socket, dataOutputStream, dataInputStream);
//                break;
//            }
//        }
//    }
//
//    private void broadCastImage(ClientHandler client) {
//        for (ClientHandler clientHandler: clientHandlers){
//            try {
//                if (client != clientHandler){
//                    String utf = clientHandler.dataInputStream.readUTF();
//                    int size = clientHandler.dataInputStream.readInt();
//                    byte[] bytes = new byte[size];
//                    clientHandler.dataInputStream.readFully(bytes);
//
//                    clientHandler.dataOutputStream.writeBoolean(true);
////                    clientHandler.dataOutputStream.writeUTF(utf);
//                    clientHandler.dataOutputStream.writeInt(bytes.length);
//                    clientHandler.dataOutputStream.write(bytes);
//                    clientHandler.dataOutputStream.flush();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                closeEverything(socket, dataOutputStream, dataInputStream);
//            }
//        }
//    }
//
//    private void broadCastTextMessage(ClientHandler client, String messageToSent) {
//        for (ClientHandler clientHandler: clientHandlers){
//            try {
//                if (client != clientHandler){
//                    clientHandler.dataOutputStream.writeBoolean(false);
//                    clientHandler.dataOutputStream.writeUTF(messageToSent);
//                    clientHandler.dataOutputStream.flush();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                closeEverything(socket, dataOutputStream, dataInputStream);
//            }
//        }
//    }
//
//    public void removeClientHandler(){
//        clientHandlers.remove(this);
//        System.out.println("close");
//    }
//
//    private void closeEverything(Socket socket, DataOutputStream dataOutputStream, DataInputStream dataInputStream) {
//        removeClientHandler();
//        try {
//            if (socket != null){
//                socket.close();
//            }
//            if (dataOutputStream != null){
//                dataOutputStream.close();
//            }
//            if (dataInputStream != null){
//                dataInputStream.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

