package sample.PrincipalWindow.AddUserToSalon;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.ClientServeur.Client;
import sample.Object.Message;

import java.io.IOException;
import java.util.Arrays;

public class AddUserToSalonWindow {
    @FXML
    private TextField idSalon;

    @FXML
    private TextField idUser;

    public void addUserToSalon() throws IOException {
        Message message = new Message("ADD_USER_TO_SALON", Arrays.asList(idUser.getText(),idSalon.getText()));
        Client.sendObject(message);
    }

}
