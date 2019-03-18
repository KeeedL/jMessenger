package sample.PrincipalWindow.Statistiques;

import javafx.fxml.FXML;
import javafx.scene.text.Text;


public class StatistiquesWindows {
    @FXML
    private Text NbMsgUser;
    @FXML
    private Text NbSalon;
    @FXML
    private Text NbMsgSalon;
    @FXML
    private Text NbAmi;

    public void setdata(String nbAmi,String nbMsgUser,String nbSalon){
        NbAmi.setText(nbAmi);
        NbMsgUser.setText(nbMsgUser);
        NbSalon.setText(nbSalon);
    }
}
