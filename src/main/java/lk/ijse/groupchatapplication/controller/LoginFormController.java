package lk.ijse.groupchatapplication.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    @FXML
    public TextField txtUserName;

    @FXML
    protected void onHelloButtonClick() throws IOException {
        ChatFormController.userName = txtUserName.getText();
        Stage stage = (Stage) txtUserName.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/lk/ijse/groupchatapplication/view/ChatForm.fxml"))));
        stage.setOnCloseRequest(event -> {
            System.out.println("end");
            System.exit(0);
        });
        stage.show();
    }
}