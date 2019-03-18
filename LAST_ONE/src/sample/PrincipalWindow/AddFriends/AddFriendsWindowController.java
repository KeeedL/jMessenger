package sample.PrincipalWindow.AddFriends;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import sample.ClientServeur.Client;
import sample.Object.Message;
import sample.Object.User;
import sample.PrincipalWindow.PrincipalWindowController;

import java.io.IOException;
import java.util.Arrays;

import static sample.PrincipalWindow.PrincipalWindowController.openPrincipalWindow;

public class AddFriendsWindowController {

    @FXML
    private TextField pseudoFriend;

    @FXML
    private Text addFriendState;

    @FXML
    public void addFriendByPseudo() throws IOException {

        Message message = new Message("ADD_FRIEND", Arrays.asList(PrincipalWindowController.userMain.getIdentifiant(), pseudoFriend.getText()));
        Client.sendObject(message);
    }

    public void setDataAddFriendsWindow(String response, User user) throws IOException {

        addFriendState.setFill(Color.WHITE);
        if (pseudoFriend.getText() == null) {
            addFriendState.setText("Champ identifiant vide");
        } else if (PrincipalWindowController.userMain.getIdentifiant().equals(pseudoFriend.getText())) {
            addFriendState.setText("Impossible d'ajouter soi meme");
        } else if (response.equals("ALREADY_FRIENDS")) {
            addFriendState.setText("Vous etes deja ami(e)s");
        } else if (response.equals("OK")) {
            addFriendState.setText("Ami ajoute");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        openPrincipalWindow();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if(response.equals("NOT_FIND")) {
            addFriendState.setText("Utilisateur introuvable");
        }
    }
}
