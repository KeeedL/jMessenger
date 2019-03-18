package sample.PrincipalWindow.AddSalon;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import sample.ClientServeur.Client;
import sample.Database.Repository;
import sample.Object.Message;
import sample.PrincipalWindow.PrincipalWindowController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static sample.Database.Repository.addSalon;

public class AddSalonWindowController {

    @FXML
    private TextField idSalon;

    @FXML
    private TextField AddSalon2;

    @FXML
    private TextField AddSalon3;

    @FXML
    private TextField AddSalon4;

    @FXML
    private Button boutonAddSalon;

    @FXML
    private Text TextSalon;

    private List<TextField> textFields = new ArrayList<>();
    private List<String> membres = new ArrayList<>();

    @FXML
    public void createSalon() throws IOException {
        int i;
        String test;
        textFields= Arrays.asList(AddSalon2,AddSalon3,AddSalon4);
        int j=0;
        for(TextField textField : textFields){
            i=0;
            if(!textField.getText().isEmpty()) {
                i=1;
                if (Repository.isUserIdentifiantExist(textField.getText())) {
                    i=2;
                    membres.add(textField.getText());
                    if (textField.getText().equals(PrincipalWindowController.userMain.getIdentifiant())) {
                        i=1;
                    }
                }
            }

            if (i==1)
            {
                TextSalon.setText("Erreur : Un ou plusieurs utilisateurs inconnus");
            }
            if(i==2){
                TextSalon.setText("Salon crée");
                j=1;
            }


        }
        if (j==1){
            membres.add(PrincipalWindowController.userMain.getIdentifiant());
            //addSalon(idSalon.getText(),membres,PrincipalWindowController.userMain.getIdentifiant());
            Message message=new Message("ADD_SALON",Arrays.asList(idSalon.getText(),membres,PrincipalWindowController.userMain.getIdentifiant()));
            Client.sendObject(message);
        }
    }

}
