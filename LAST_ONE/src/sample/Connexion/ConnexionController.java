package sample.Connexion;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.ClientServeur.Client;
import sample.Object.Message;
import sample.Object.Salon;
import sample.PrincipalWindow.PrincipalWindowController;
import sample.Registration.RegisterController;
import sample.Object.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static sample.PrincipalWindow.PrincipalWindowController.openPrincipalWindow;

public class ConnexionController {

    protected Stage connexionStage = new Stage();

    public static RegisterController registerController;

    @FXML
    protected TextField identifierId;

    @FXML
    protected PasswordField passwordId;

    @FXML
    protected Text stateConnect;

    @FXML public void tryConnection() {

        Message message = new Message("CHECK_MATCH_ACCOUNT", Arrays.asList(identifierId.getText(), passwordId.getText()));
        Client.sendObject(message);
    }

    public void isMatchAccount(boolean isMatchAccount, User user, List<User> users, List<Salon> salons) {

        if(identifierId.getText().isEmpty() || passwordId.getText().isEmpty()) {
            stateConnect.setText("Champ(s) vide(s)");
        } else if(!isMatchAccount) {
            stateConnect.setText("User n'existe pas");
        } else if(isMatchAccount){
            stateConnect.setText("User exist");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {

                        PrincipalWindowController.userMain = user;
                        PrincipalWindowController.users = users;
                        PrincipalWindowController.salons = salons;
                        openPrincipalWindow();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @FXML
    public void openRegisterPage() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RegisterController.class.getResource("../Registration/Register.fxml"));
            Parent root = fxmlLoader.load();
            connexionStage.setTitle("Inscription");
            connexionStage.setScene(new Scene(root));
            connexionStage.show();
            registerController = fxmlLoader.<RegisterController>getController();
        } catch (Exception e) {
            System.out.println("error to load second window");
        }
    }
}
