package sample.ClientServeur;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import sample.Connexion.ConnexionController;
import sample.Object.Conversation;
import sample.Main;
import sample.Object.Message;
import sample.Object.Salon;
import sample.PrincipalWindow.PrincipalWindowController;
import sample.Object.User;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class Client implements Runnable {

    // Socket client
    private static Socket clientSocket = null;
    // Flux sortant
    private static ObjectOutputStream os = null;
    // Flux entrant
    private static ObjectInputStream is = null;
    private static boolean closed = false;
    public static TextField identifiant;
    public static PasswordField password;
    public static TextField name;
    public static TextField firstname;
    public static Text stateRegistration;

    public Client() {
    }

    public void run() {
        /*
         * Ouverture du socket et des flux
         */
        try {
            clientSocket = new Socket("localhost", 2222);
            os = new ObjectOutputStream(clientSocket.getOutputStream());
            is = new ObjectInputStream(clientSocket.getInputStream());

        } catch (UnknownHostException e) {
            System.err.println("Probleme de connexion avec le serveur");
        } catch (IOException e) {
            System.err.println("Probleme de connexion avec le serveur");
        }

        /* Verification si tout est bien initialisé */

        if (clientSocket != null && os != null && is != null) {
            try {

                /* Thread pour lire les messages provenant du serveur */
                //new Thread(new Client()).start();
                while (!closed) {
                    //os.println(inputLine.readLine());
                    handleObjectReader(is.readObject());
                }
                /*
                 * Fermeture du socket et des flux
                 */
                os.close();
                is.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendObject(Object objectToSend) {
        try {
            System.out.println("Envoie objet : ");
            if(objectToSend instanceof Message) {
                System.out.println(((Message) objectToSend).getTypeRequest());
                System.out.println(((Message) objectToSend).getContenu());
            }
            os.writeObject(objectToSend);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleObjectReader(Object objectReaded) throws IOException {
        System.out.println(objectReaded.toString());

        if (objectReaded instanceof Message) {
            Message message = (Message) objectReaded;
            System.out.println("client a recu du serveur : " + message.getTypeRequest());
            System.out.println("client a recu du serveur : " + message.getContenu());
            if (message.getTypeRequest().equals("CHECK_IDENTIFIANT_AVAILABLE")) {
                boolean identifiantIsAvailable = Boolean.parseBoolean(String.valueOf(message.getContenu()));
                ConnexionController.registerController.isIdAvailable(identifiantIsAvailable);
            } else if (message.getTypeRequest().equals("CHECK_MATCH_ACCOUNT")) {
                //boolean identifiantIsAvailable = Boolean.parseBoolean(String.valueOf(message.getContenu()));
                if(message.getContenu() instanceof Boolean) {
                    Main.connexionController.isMatchAccount(false, null, null, null);
                } else {
                    if(message.getContenu() instanceof List) {

                        List<Object> response = (List<Object>) message.getContenu();
                        User user = (User) response.get(0);
                        List<User> users = (List<User>) response.get(1);
                        List<Salon> salons = (List<Salon>) response.get(2);
                        Main.connexionController.isMatchAccount(true, user, users, salons);
                    }
                }
            } else if(message.getTypeRequest().equals("ADD_FRIEND")) {
                List response = (List) message.getContenu();
                if(response.get(0).equals("NOT_FIND") || response.get(0).equals("ALREADY_FRIENDS")) {
                    PrincipalWindowController.addFriendsWindowController.setDataAddFriendsWindow(String.valueOf(response.get(0)), null);
                } else {
                    PrincipalWindowController.addFriendsWindowController.setDataAddFriendsWindow(String.valueOf(response.get(0)), (User) response.get(1));
                    PrincipalWindowController.users.add((User) response.get(1));

                }
            } else if(message.getTypeRequest().equals("GET_MESSAGE_SALON")) {
                if(message.getContenu() instanceof List) {
                    PrincipalWindowController.principalWindowController.setConversationToMainWindow((List<Conversation>) message.getContenu());
                }
            } else if(message.getTypeRequest().equals("GET_MESSAGE_FROM")) {
                if(message.getContenu() instanceof List) {
                    PrincipalWindowController.principalWindowController.setConversationToMainWindow((List<Conversation>) message.getContenu());
                }

            } else if (message.getTypeRequest().equals("GET_STATS")){
                if (message.getContenu() instanceof List){
                    List<String> reponse=(List) message.getContenu();
                    //PrincipalWindowController.principalWindowController.afficheStats(reponse.get(0),reponse.get(1),reponse.get(2));
                    PrincipalWindowController.principalWindowController.Threadstats(reponse.get(0),reponse.get(1),reponse.get(2));
                }
            } else if(message.getTypeRequest().equals("SO_CONNECTED")) {
                if(message.getContenu() instanceof List) {
                    List<String> allUsersConnected = (List<String>) message.getContenu();
                    PrincipalWindowController.principalWindowController.friendsIsConnected(allUsersConnected);
                }
            }
        }
    }
}