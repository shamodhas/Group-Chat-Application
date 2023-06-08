package lk.ijse.groupchatapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 6/8/2023
 * Time : 4:03 PM
 */

public class ChatFormController implements Initializable {
    @FXML
    public AnchorPane ancPane;
    @FXML
    public ScrollPane scPane;
    @FXML
    public TextField txtMessage;
    public static String userName;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.socket = new Socket("localhost", 1235);
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(userName);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (socket.isConnected()){
                        try {
                            String message = dataInputStream.readUTF();
                            if (message.equals("image")){

                            }else {

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void messageSendOnAction(ActionEvent actionEvent) {

    }

    @FXML
    public void imgSendOnAction(ActionEvent actionEvent) {

    }


}
