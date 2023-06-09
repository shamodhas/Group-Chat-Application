package lk.ijse.groupchatapplication.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
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
    public TextField txtMessage;
    @FXML
    public VBox vBox;
    public static String userName;
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
                    String message = bufferedReader.readLine();
                    if (message != null) {
                        Platform.runLater(() -> {
                            if (message.startsWith("Image:")) {
                                byte[] imageData = Base64.getDecoder().decode(message.substring(6));
                                ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(imageData)));
                                imageView.setFitWidth(200);
                                imageView.setPreserveRatio(true);
                                vBox.getChildren().add(imageView);
                            } else {
                                Label messageLabel = new Label(message);
                                vBox.getChildren().add(messageLabel);
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

    @FXML
    public void messageSendOnAction(ActionEvent actionEvent) {
        String message = txtMessage.getText();
        if (!message.isEmpty()) {
            try {
                bufferedWriter.write(message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                txtMessage.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void imgSendOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                byte[] imageData = Files.readAllBytes(file.toPath());
                String encodedImage = Base64.getEncoder().encodeToString(imageData);
                String message = "Image:" + encodedImage;
                bufferedWriter.write(message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
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
        txtMessage.appendText(selectedEmoji);
    }
}