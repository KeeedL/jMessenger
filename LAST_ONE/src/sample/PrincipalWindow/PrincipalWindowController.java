package sample.PrincipalWindow;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.ClientServeur.Client;
import sample.Object.Conversation;
import sample.Object.Message;
import sample.PrincipalWindow.AddFriends.AddFriendsWindowController;
import sample.PrincipalWindow.Statistiques.StatistiquesWindows;
import sample.Registration.RegisterController;
import sample.Object.Salon;
import sample.Object.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

//import static sample.Database.Repository.getNbMessageByUser;
//import static sample.Database.Repository.getNbUser;

public class PrincipalWindowController implements Initializable {

    public static List<User> users = new ArrayList<>();
    public static User userMain = new User();
    public static List<Salon> salons =new ArrayList<>();

    public static StatistiquesWindows statistiquesWindows;
    public static PrincipalWindowController principalWindowController;
    public static AddFriendsWindowController addFriendsWindowController;

    private static Stage mainWindowStage;
    private Double sizePane = 11.0;

    //protected static Stage mainWindowStage = new Stage();
    protected static boolean isAlreadyRunning = false;

    public static List<Conversation> messageWithCurrentUser = new ArrayList<>();

    @FXML
    protected ScrollPane scrollBar;

    @FXML
    protected Pane conversationPane;

    @FXML
    protected AnchorPane anchorPane;

    @FXML
    protected TextArea chatBox;

    @FXML
    protected ImageView userAvatar;


    private static String salonIdentifiantCurrent;
    private static String friendsIdentifiantCurrent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for(Salon salon : salons)
        {
            Pane pane = new Pane();
            pane.setStyle("-fx-background-color: #FFFFFF;");
            sizePane += 60;
            pane.setLayoutY(sizePane);
            pane.setPrefHeight(60.0);
            pane.setPrefWidth(180.0);


            String picture = "sample/pictures/salon.png";
            ImageView imageView = new ImageView();
            Image imageFriend = new Image(picture, true);
            imageView.setImage(imageFriend);
            imageView.setFitHeight(40.0);
            imageView.setFitWidth(55.0);
            imageView.setLayoutY(11.0);
            imageView.setLayoutX(11.0);
            imageView.setPickOnBounds(true);
            imageView.setPreserveRatio(true);

            Text text = new Text(salon.getIdentifiantSalon());
            text.setLayoutX(75.0);
            text.setLayoutY(30.0);
            text.setWrappingWidth(90.22);


            Line line = new Line();
            line.setStartX(0.0);
            line.setEndX(180.0);

            text.setOnMouseClicked(event -> {

                salonIdentifiantCurrent= text.getText();
                friendsIdentifiantCurrent = null;
                onClickSalon(text.getText());
            });

            pane.getChildren().addAll(imageView, text, line);

            anchorPane.getChildren().add(pane);
        }

        for (User user : users) {
            Pane pane = new Pane();
            pane.setStyle("-fx-background-color: #FFFFFF;");
            sizePane += 60;
            pane.setLayoutY(sizePane);
            pane.setPrefHeight(60.0);
            pane.setPrefWidth(175.0);

            String picture = getImageFromAvatarNumber(user.getAvaterNumber());
            ImageView imageView = new ImageView();
            Image imageFriend = new Image(picture, true);
            imageView.setImage(imageFriend);
            imageView.setFitHeight(40.0);
            imageView.setFitWidth(55.0);
            imageView.setLayoutX(13.0);
            imageView.setLayoutY(11.0);
            imageView.setPickOnBounds(true);
            imageView.setPreserveRatio(true);

            ImageView imageViewConnect = new ImageView();
            if(user.isConnected()) {
                String pictureOnline = "sample/pictures/connected.png";
                Image imageFriendConnect = new Image(pictureOnline, true);
                imageViewConnect.setImage(imageFriendConnect);
                imageViewConnect.setFitHeight(15.0);
                imageViewConnect.setFitWidth(15.0);
                imageViewConnect.setLayoutX(40.0);
                imageViewConnect.setLayoutY(11.0);
                imageViewConnect.setPickOnBounds(true);
                imageViewConnect.setPreserveRatio(true);
            }

            Text text = new Text(user.getIdentifiant()); //USER.getidentifiant pour avoir le id des amis
            text.setLayoutX(75.0);
            text.setLayoutY(30.0);
            text.setWrappingWidth(90.22);


            Line line = new Line();
            line.setStartX(0.0);
            line.setEndX(175.0);

            text.setOnMouseClicked(event -> {

                friendsIdentifiantCurrent = text.getText();
                salonIdentifiantCurrent = null;
                onClickFriends(userMain.getIdentifiant(), text.getText());
            });

            pane.getChildren().addAll(imageView, text, line, imageViewConnect);

            anchorPane.getChildren().add(pane);
        }

        String picture = getImageFromAvatarNumber(userMain.getAvaterNumber());
        Image imageFriend = new Image(picture, true);
        userAvatar.setImage(imageFriend);

    }
    @FXML
    public void onClickQuitSalon() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RegisterController.class.getResource("../PrincipalWindow/QuitSalon/QuitSalonWindows.fxml"));
        Parent root = fxmlLoader.load();
        Stage mainWindowStage = new Stage();
        mainWindowStage.setTitle("Quitter un salon");
        mainWindowStage.setScene(new Scene(root, 350, 230));
        mainWindowStage.show();
    }
    @FXML
    public void onClickAddUserSalon() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RegisterController.class.getResource("../PrincipalWindow/AddUserToSalon/AddUserToSalonWindow.fxml"));
        Parent root = fxmlLoader.load();
        Stage mainWindowStage = new Stage();
        mainWindowStage.setTitle("Ajouter un utilisateur à un salon");
        mainWindowStage.setScene(new Scene(root, 350, 230));
        mainWindowStage.show();
    }
    @FXML
    public void onClickExportXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RegisterController.class.getResource("../PrincipalWindow/ExportXML/ExportXMLWindow.fxml"));
        Parent root = fxmlLoader.load();
        Stage mainWindowStage = new Stage();
        mainWindowStage.setTitle("Ajouter un utilisateur à un salon");
        mainWindowStage.setScene(new Scene(root, 350, 230));
        mainWindowStage.show();
    }
    public void onClickSalon(String identifiantSalon) {

        //salonIdentifiantCurrent = identifiantSalon;
        Message message = new Message("GET_MESSAGE_SALON", identifiantSalon);
        Client.sendObject(message);
    }

    public void onClickFriends(String identifiantMainUser, String toIdenfitifantUser) {

        //friendsIdentifiantCurrent = toIdenfitifantUser;
        Message message = new Message("GET_MESSAGE_FROM", Arrays.asList(identifiantMainUser, toIdenfitifantUser));
        Client.sendObject(message);
    }

    public void friendsIsConnected(List<String> allUsersConnected) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {

                    allUsersConnected.forEach( userConnected -> {
                        for(User friend : users) {
                            if (friend.getIdentifiant().equals(userConnected)) {
                                friend.setConnected(true);
                            }
                        }
                    });


                    openPrincipalWindow();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void Threadstats(String nbAmi,String nbMsgUser,String nbSalon){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    afficheStats();
                    statistiquesWindows.setdata(nbAmi,nbMsgUser,nbSalon);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void afficheStats() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RegisterController.class.getResource("../PrincipalWindow/Statistiques/StatistiquesWindows.fxml"));
        Parent root = fxmlLoader.load();
        Stage mainWindowStage = new Stage();
        mainWindowStage.setTitle("Vos statistiques");
        mainWindowStage.setScene(new Scene(root, 350, 230));
        mainWindowStage.show();
        statistiquesWindows = fxmlLoader.<StatistiquesWindows>getController();
    }

    @FXML
    public void getStatistiques() {
        Message message=new Message("GET_STATS",Arrays.asList(userMain.get_id(),userMain.getIdentifiant()));
        Client.sendObject(message);
    }

    public void addUserToSalon(){
        Message message= new Message("ADD_USER_TO_SALON",Arrays.asList("IDENTIFIANT A RECUP D'UN CHAMP TEXT","ID DU SALON"));
        Client.sendObject(message);
    }

    public void setConversationToMainWindow(List<Conversation> conversations) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                conversationPane.getChildren().clear();
                //friendsIdentifiantCurrent = toIdenfitifantUser;
                Double layoutXMainUserMessage = 260.0;
                Double layoutXFriendMessage = 10.0;
                Integer nbCharPerLine = 34;
                Double layoutY = 20.0;

                //List<Conversation> conversationMessages = Repository.getMessageSorted(identifiantMainUser, toIdenfitifantUser);

                for(Conversation conver : conversations) {

                    Text text = new Text();
                    String contenuMessage = conver.getContenu();
                    String messageConcat = new String();

                    if(contenuMessage.length() > 34) {
                        Integer nbLineToJump = contenuMessage.length() / 34;
                        for(int i = 0; i <= nbLineToJump; i++ ) {
                            Integer nbChar = i * nbCharPerLine;
                            Integer nbNext;
                            if( i == nbLineToJump) {
                                nbNext = contenuMessage.length();
                            } else {
                                nbNext = (i+1) * nbCharPerLine;
                            }
                            messageConcat = messageConcat + contenuMessage.substring(nbChar, nbNext) + "\r\n";
                        }
                    } else {
                        messageConcat = conver.getContenu();
                    }

                    text.setText("<" + conver.getEnvoyeur() + "> : \r\n" + messageConcat);
                    // Set content for ScrollPane

                    if (conver.getEnvoyeur().equals(userMain.getIdentifiant())) {
                        text.setLayoutX(layoutXMainUserMessage);
                    } else {
                        text.setLayoutX(layoutXFriendMessage);
                    }
                    text.setLayoutY(layoutY);
                    layoutY += 40.0;

                    conversationPane.getChildren().add(text);
                }
            }
        });
    }

    public static void openPrincipalWindow() throws IOException {

        if(isAlreadyRunning) {
            mainWindowStage.close();
            createMainWindow();
        } else {
            createMainWindow();
        }
    }

    public static String getImageFromAvatarNumber(Double avatarNumber) {
        if(avatarNumber.equals(1.0)) {
            return "sample/pictures/avatar_1.png";
        } else if(avatarNumber.equals(2.0)) {
            return "sample/pictures/avatar_2.png";
        } else if(avatarNumber.equals(3.0)) {
            return "sample/pictures/avatar_3.png";
        } else {
            return "sample/pictures/avatar_4.png";
        }
    }

    @FXML
    public void addFriends() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(RegisterController.class.getResource("../PrincipalWindow/AddFriends/AddFriendsWindow.fxml"));
        Parent root = fxmlLoader.load();
        Stage mainWindowStage = new Stage();
        mainWindowStage.setTitle("Ajout d'amis");
        mainWindowStage.setScene(new Scene(root, 300, 110));
        mainWindowStage.show();
        addFriendsWindowController = fxmlLoader.<AddFriendsWindowController>getController();
    }

    @FXML
    public void addSalon() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(RegisterController.class.getResource("../PrincipalWindow/AddSalon/AddSalonWindow.fxml"));
        Parent root = fxmlLoader.load();
        Stage mainWindowStage = new Stage();
        mainWindowStage.setTitle("Création d'un salon");
        mainWindowStage.setScene(new Scene(root, 350, 230));
        mainWindowStage.show();
    }

    @FXML
    public void statistiques() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(RegisterController.class.getResource("../PrincipalWindow/Statistiques/StatistiquesWindows.fxml"));
        Parent root = fxmlLoader.load();
        Stage mainWindowStage = new Stage();
        mainWindowStage.setTitle("Vos statistiques");
        mainWindowStage.setScene(new Scene(root, 350, 230));
        mainWindowStage.show();
    }

    private static void createMainWindow() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(RegisterController.class.getResource("../PrincipalWindow/PrincipalWindow.fxml"));
        Parent root = fxmlLoader.load();
        mainWindowStage = new Stage();
        mainWindowStage.setTitle("jMessenger");
        Scene scene = new Scene(root);
        mainWindowStage.setScene(scene);
        mainWindowStage.show();
        principalWindowController = fxmlLoader.<PrincipalWindowController>getController();
        if(!isAlreadyRunning) {
            Message message = new Message("IM_CONNECTED", userMain.getIdentifiant());
            Client.sendObject(message);
        }

        isAlreadyRunning = true;

    }


    @FXML
    public void sendMessageTo() {

        if(chatBox.getText() != null) {
            if(salonIdentifiantCurrent != null) {
                Message message = new Message("SEND_MESSAGE_SALON", Arrays.asList(salonIdentifiantCurrent, userMain.getIdentifiant(), chatBox.getText()));
                Client.sendObject(message);
                onClickSalon(salonIdentifiantCurrent);
                chatBox.clear();
            } else {
                Message message = new Message("SEND_MESSAGE_TO", Arrays.asList(userMain.getIdentifiant(), friendsIdentifiantCurrent, chatBox.getText()));
                Client.sendObject(message);
                onClickFriends(userMain.getIdentifiant(), friendsIdentifiantCurrent);
                chatBox.clear();
            }
        }
    }
}

