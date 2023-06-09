package lk.ijse.groupchatapplication.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 5/21/2023
 * Time :10:51 PM
 */

public class LoginFormController {
    @FXML
    public TextField txtUserName;

    @FXML
    protected void onHelloButtonClick() throws IOException {
        Stage stage = (Stage) txtUserName.getScene().getWindow();
        ChatFormController.userName = txtUserName.getText();
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/lk/ijse/groupchatapplication/view/ChatForm.fxml"))));
        stage.show();
    }
}
