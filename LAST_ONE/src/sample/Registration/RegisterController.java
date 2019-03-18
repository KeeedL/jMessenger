package sample.Registration;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import sample.ClientServeur.Client;
import sample.Object.Message;
import sample.PrincipalWindow.PrincipalWindowController;
import sample.Object.User;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import static sample.PrincipalWindow.PrincipalWindowController.openPrincipalWindow;

public class RegisterController implements Initializable {

    public static Double avatarChoice = 0.0;
    public static Double availableID = 0.0;

    @FXML
    private TextField identifierRegister;

    @FXML
    private PasswordField passwordRegister;

    @FXML
    private TextField name;

    @FXML
    private TextField firstName;

    @FXML
    private Text stateRegistration;

    @FXML
    private Pane pane;

    private RadioButton firstRadioButton = new RadioButton();
    private RadioButton secondRadioButton = new RadioButton();
    private RadioButton thirdRadioButton = new RadioButton();
    private RadioButton fourthRadioButton = new RadioButton();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ToggleGroup toggleGroup = new ToggleGroup();

        firstRadioButton.setSelected(true);
        firstRadioButton.setToggleGroup(toggleGroup);
        secondRadioButton.setToggleGroup(toggleGroup);
        thirdRadioButton.setToggleGroup(toggleGroup);
        fourthRadioButton.setToggleGroup(toggleGroup);

        firstRadioButton.setLayoutX(257.0);
        firstRadioButton.setLayoutY(95.0);
        secondRadioButton.setLayoutX(257.0);
        secondRadioButton.setLayoutY(145.0);
        thirdRadioButton.setLayoutX(257.0);
        thirdRadioButton.setLayoutY(195.0);
        fourthRadioButton.setLayoutX(257.0);
        fourthRadioButton.setLayoutY(245.0);

        firstRadioButton.setId("firstRadioButton");
        secondRadioButton.setId("secondRadioButton");
        thirdRadioButton.setId("thirdRadioButton");
        fourthRadioButton.setId("fourthRadioButton");

        pane.getChildren().addAll(firstRadioButton, secondRadioButton, thirdRadioButton, fourthRadioButton);

    }

    @FXML
    public void tryRegistration() throws IOException {

        Message message = new Message("CHECK_IDENTIFIANT_AVAILABLE", Arrays.asList(identifierRegister.getText(), passwordRegister.getText(), name.getText(), firstName.getText(), getAvatarChoise()));
        Client.sendObject(message);
    }

    public void isIdAvailable(boolean isAvailable) {

        User user = new User(identifierRegister.getText(), passwordRegister.getText(), firstName.getText(), name.getText(), getAvatarChoise());

        if(identifierRegister.getText().isEmpty() || passwordRegister.getText().isEmpty() || firstName.getText().isEmpty() || name.getText().isEmpty()) {
            stateRegistration.setStyle("-fx-text-fill: red;");
            stateRegistration.setText("Champ(s) vide(s)");
        } else if(!isAvailable) {
            stateRegistration.setText("Identifiant deja utilise");
        } else {
            stateRegistration.setText("Création du compte...");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {

                        PrincipalWindowController.userMain = user;
                        openPrincipalWindow();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private Double getAvatarChoise() {
        if(firstRadioButton.isSelected()) {
            return 1.0;
        } else if(secondRadioButton.isSelected()) {
            return 2.0;
        } else if(thirdRadioButton.isSelected()) {
            return 3.0;
        } else {
            return 4.0;
        }
    }
}
