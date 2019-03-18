package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.ClientServeur.Client;
import sample.Connexion.ConnexionController;

public class Main extends Application {

    public static ConnexionController connexionController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        //MultiThreadChatClient.createThread();
        Client client = new Client();
        Thread threadClient = new Thread(client);
        threadClient.start();

        FXMLLoader fxmlLoader = new FXMLLoader(ConnexionController.class.getResource("../Connexion/Connexion.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("jMessanger");
        primaryStage.setScene(new Scene(root, 415, 362));
        primaryStage.show();
        connexionController = fxmlLoader.<ConnexionController>getController();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
