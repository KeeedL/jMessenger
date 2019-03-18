package sample.PrincipalWindow.QuitSalon;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.ClientServeur.Client;
import sample.Object.Message;

import java.io.IOException;
import java.util.Arrays;

import static sample.PrincipalWindow.PrincipalWindowController.userMain;

public class QuitSalonWindows {

    @FXML
    private TextField idSalonQuit;

    @FXML
    public void QuitterSalon() throws IOException {
        System.out.println(userMain.getIdentifiant()+"  "+idSalonQuit.getText());
        Message message=new Message("QUIT_SALON",Arrays.asList(userMain.getIdentifiant(),idSalonQuit.getText()));
        Client.sendObject(message);

    }
}
