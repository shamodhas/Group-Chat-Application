package lk.ijse.groupchatapplication.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;
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
    public VBox vBox;
    public static String userName;
    public Label lblClientName;
    public Circle showProPic;
    @FXML
    public TextField txtTextMessage;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Socket socket = new Socket("localhost", 1234);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            System.out.println(userName + " Connected to server : " + socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread messageReceiver = new Thread(()->{
            try {
                while (true) {
                    String[] data = bufferedReader.readLine().split("/#sendingClientName#/");
                    String clientName = data[0];
                    String message = data[1];
                    if (message != null) {
                        Platform.runLater(() -> {
                            if (message.startsWith("Image:")) {
                                byte[] imageData = Base64.getDecoder().decode(message.substring(6));
                                ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(imageData)));
                                imageView.setFitWidth(200);
                                imageView.setPreserveRatio(true);
                                HBox imageHbox = new HBox(imageView);
                                Label lblName = new Label(clientName);
                                lblName.setStyle("-fx-background-color: blue;-fx-background-radius:15;-fx-font-size: 13;-fx-text-fill: white;-fx-alignment: center;-fx-padding: 5px;");
                                imageHbox.setStyle("-fx-background-color: blue;-fx-background-radius:15;-fx-alignment: center;-fx-padding: 20px 5px;");
                                HBox hBox = new HBox(lblName, imageHbox);
                                hBox.setSpacing(10);
                                hBox.setStyle("-fx-alignment: center-left;-fx-pref-width: 584;-fx-padding: 10px 0 0 0");
                                vBox.getChildren().add(hBox);
                            } else {
                                Label messageLabel = new Label(message);
                                messageLabel.setStyle("-fx-background-color: blue;-fx-background-radius:15;-fx-font-size: 16;-fx-text-fill: white;-fx-alignment: center;-fx-padding: 5px;");
                                Label lblName = new Label(clientName);
                                lblName.setStyle("-fx-background-color: blue;-fx-background-radius:15;-fx-font-size: 13;-fx-text-fill: white;-fx-alignment: center;-fx-padding: 5px;");
                                HBox hBox = new HBox(lblName, messageLabel);
                                hBox.setSpacing(10);
                                hBox.setStyle("-fx-alignment: center-lefta;-fx-pref-width: 584;-fx-padding: 10px 0 0 0");
                                vBox.getChildren().add(hBox);
                            }
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        messageReceiver.setDaemon(true);
        messageReceiver.start();
    }

    private void sendMessage(String message) throws IOException {
        message = userName+"/#sendingClientName#/"+message;
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    @FXML
    public void textSendOnAction(ActionEvent actionEvent) {
        String message = txtTextMessage.getText();
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-background-color: purple;-fx-background-radius:15;-fx-font-size: 16;-fx-text-fill: white;-fx-alignment: center;-fx-padding: 5px;");
        HBox hBox = new HBox(messageLabel);
        hBox.setStyle("-fx-alignment: center-right;-fx-pref-width: 584;-fx-padding: 10px 0 0 0");
        vBox.getChildren().add(hBox);
        if (!message.isEmpty()) {
            try {
                sendMessage(message);
                txtTextMessage.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void imageSendOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                byte[] imageData = Files.readAllBytes(file.toPath());
                String encodedImage = Base64.getEncoder().encodeToString(imageData);
                String message = "Image:" + encodedImage;
                ImageView imageView = new ImageView(new Image(file.getPath()));
                imageView.setFitWidth(200);
                imageView.setPreserveRatio(true);
                HBox imageHbox = new HBox(imageView);
                imageHbox.setStyle("-fx-background-color: purple;-fx-background-radius:15;-fx-alignment: center;-fx-padding: 20px 5px;");
                HBox hBox = new HBox(imageHbox);
                hBox.setStyle("-fx-alignment: center-right;-fx-pref-width: 584;-fx-padding: 10px 0 0 0");
                vBox.getChildren().add(hBox);
                sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void emojiSendOnAction(ActionEvent actionEvent) {
        // Replace with your own implementation to handle emoji selection and append it to the message field
        // For example, you can use a dialog or a custom emoji picker component
        String selectedEmoji = "🙂";
        txtTextMessage.appendText(selectedEmoji);
    }
}