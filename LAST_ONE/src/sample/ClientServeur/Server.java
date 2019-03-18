package sample.ClientServeur;

import sample.Object.Conversation;
import sample.Database.Repository;
import sample.Object.Message;
import sample.Object.Salon;
import sample.Object.User;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Server extends Thread {

    private ObjectOutputStream os = null;
    // Flux entrant
    private ObjectInputStream is = null;
    private Socket clientSocket = null;
    private String identifiant;

    private static List<String> getUsersConnected = new ArrayList<>();

    private Server[] threads = new Server[100];
    private List<String> identifiants;

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public List<String> getIdentifiants() {
        return identifiants;
    }

    public void setIdentifiants(List<String> identifiants) {
        this.identifiants = identifiants;
    }

    public Server(Socket clientSocket, Server[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
    }

    public void run() {
        Server[] threads = this.threads;

        try {
            /*
             * Création des flux.
             */
            is = new ObjectInputStream(clientSocket.getInputStream());
            os = new ObjectOutputStream(clientSocket.getOutputStream());

            //os.println("Entrer un pseudo");
            //String name = is;
            //os.println("Bonjour " + name + " pour quitter le messenger : /quit");

            /*for (int i = 0; i < threads.length; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.println("Arrivé de " + name + " qui est un sacré tchatcheur");
                }
            }*/

            while (clientSocket.isConnected()) {
                Object line = is.readObject();
                handleObjectReaded(line);

               /* for (int i = 0; i < threads.length; i++) {
                    if (threads[i] != null) {
                        threads[i].os.println("<" + name + ">" + line);
                    }
                }*/
            }

            /*
            for (int i = 0; i < threads.length; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.println("Le super tchatcheur  " + name + " vient de se deconnecter");
                }
            }
            os.println("A+ " + name);*/

            /*
             * Nettoie la place pour les autres clients
             *
             */

            for (int i = 0; i < threads.length; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                }
            }
            /*
             * Fermeture des sockets et flux.
             */
            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object objectToSend) {
        try {
            System.out.println("Envoie objet");
            os.writeObject(objectToSend);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleObjectReaded(Object objectReaded) throws IOException {


        if (objectReaded instanceof Message) {
            Message message = (Message) objectReaded;
            System.out.println("serveur a recu du client : " + message.getTypeRequest());
            System.out.println("serveur a recu du client : " + message.getContenu());
            message = (Message) objectReaded;
            if (message.getTypeRequest().equals("CHECK_IDENTIFIANT_AVAILABLE")) {
                List object = (List) message.getContenu();
                boolean identifiantIsAvailable = Repository.isIdentifiantAvailable(String.valueOf(object.get(0)));
                if (identifiantIsAvailable && String.valueOf(object.get(0)).length() != 0 && String.valueOf(object.get(1)).length() != 0 && String.valueOf(object.get(2)).length() != 0 && String.valueOf(object.get(3)).length() != 0 && String.valueOf(object.get(4)).length() != 0) {
                    Double _id = Repository.getNextIDUserAvailable();
                    User user = new User(_id, String.valueOf(object.get(0)), String.valueOf(object.get(1)), String.valueOf(object.get(2)), String.valueOf(object.get(3)), Double.valueOf(String.valueOf(object.get(4))));
                    Repository.createAccount(user);
                    identifiant = user.getIdentifiant();
                }
                System.out.println("serveur à recu CHECK ID AVAILABLE");
                System.out.println("serveur renvoie : " + identifiantIsAvailable);

                Message messageSend = new Message("CHECK_IDENTIFIANT_AVAILABLE", identifiantIsAvailable);
                sendObject(messageSend);
            } else if (message.getTypeRequest().equals("ADD_FRIEND")) {
                List datas = (List) message.getContenu();
                Double addingUserID = Repository.getIDUserByIdentifiant(String.valueOf(datas.get(1)));
                Double mainUserID = Repository.getIDUserByIdentifiant(String.valueOf(datas.get(0)));
                boolean areAlreadyFriends = Repository.areAlreadyFriends(mainUserID, addingUserID);

                Message message1 = new Message();
                message1.setTypeRequest("ADD_FRIEND");
                User user = new User();
                if (!addingUserID.equals(0.0)) {
                    user = Repository.getUserWithIdentifiant(String.valueOf(datas.get(1)));
                }

                if (areAlreadyFriends) {
                    message1.setContenu(Arrays.asList("ALREADY_FRIENDS"));
                } else if (!areAlreadyFriends && addingUserID.equals(0.0)) {
                    message1.setContenu(Arrays.asList("NOT_FIND"));
                } else {
                    Repository.addFriend(mainUserID, addingUserID);
                    message1.setContenu(Arrays.asList("OK", user));
                }
                sendObject(message1);

            } else if (message.getTypeRequest().equals("CHECK_MATCH_ACCOUNT")) {
                message = (Message) objectReaded;
                if (message.getContenu() instanceof List) {

                    List<String> datasToConnect = (List<String>) message.getContenu();
                    boolean isUserExists = Repository.checkMatchAccount(datasToConnect.get(0), datasToConnect.get(1));

                    Message messageSend = new Message();
                    messageSend.setTypeRequest("CHECK_MATCH_ACCOUNT");

                    if (isUserExists) {
                        Double idUser = Repository.getIDFromLoginAndPwd(datasToConnect.get(0), datasToConnect.get(1));
                        User user = Repository.getUserWithID(idUser);
                        List<User> users = Repository.getFriendsFromUser(idUser);
                        List<Salon> salons = Repository.getSalonsFromIdentifiant(datasToConnect.get(0));
                        messageSend.setContenu(Arrays.asList(user, users, salons));
                        identifiant = datasToConnect.get(0);
                    } else {
                        messageSend.setContenu(false);
                    }

                    sendObject(messageSend);
                }
            } else if (message.getTypeRequest().equals("HAS_SEND_MESSAGE")) {
                if (message.getContenu() instanceof List) {
                    List datasMessage = (List) message.getContenu();
                    Repository.sendMessage(String.valueOf(datasMessage.get(0)), String.valueOf(datasMessage.get(1)), String.valueOf(datasMessage.get(2)));
                    List<Conversation> messagesToMainUser = Repository.getMessageSorted(String.valueOf(datasMessage.get(0)), String.valueOf(datasMessage.get(1)));

                    Message messages = new Message("HAS_SEND_MESSAGE", messagesToMainUser);
                    sendObject(messages);
                }
            } else if (message.getTypeRequest().equals("GET_MESSAGE_SALON")) {
                String identifiantSalon = (String) message.getContenu();
                List<Conversation> conversations = Repository.getMessageSalonSorted(identifiantSalon);
                Message message1 = new Message("GET_MESSAGE_SALON", conversations);
                sendObject(message1);

            } else if (message.getTypeRequest().equals("GET_MESSAGE_FROM")) {
                if (message.getContenu() instanceof List) {
                    List identifiants = (List) message.getContenu();
                    List<Conversation> conversations = Repository.getMessageSorted(String.valueOf(identifiants.get(0)), String.valueOf(identifiants.get(1)));
                    Message message1 = new Message("GET_MESSAGE_FROM", conversations);
                    sendObject(message1);
                }
            } else if (message.getTypeRequest().equals("SEND_MESSAGE_TO")) {
                if (message.getContenu() instanceof List) {
                    List contenu = (List) message.getContenu();
                    Repository.sendMessage(String.valueOf(contenu.get(0)), String.valueOf(contenu.get(1)), String.valueOf(contenu.get(2)));

                    for (Server server : threads) {
                        if (server != null) {
                            if (server.getIdentifiant().equals(contenu.get(1))) {
                                List<Conversation> conversations = Repository.getMessageSorted(String.valueOf(contenu.get(0)), String.valueOf(contenu.get(1)));
                                Message message1 = new Message("GET_MESSAGE_FROM", conversations);
                                server.os.writeObject(message1);
                                server.os.flush();
                            }
                        }
                    }
                }
            } else if (message.getTypeRequest().equals("SEND_MESSAGE_SALON")) {
                    if (message.getContenu() instanceof List) {
                        List contenu = (List) message.getContenu();
                        List<String> membres = Repository.getMembresFromIdentifiantSalon(String.valueOf(contenu.get(0)));
                        Repository.sendMessageToSalon(String.valueOf(contenu.get(0)), String.valueOf(contenu.get(1)), membres, String.valueOf(contenu.get(2)));
                        List<Conversation> conversations = Repository.getMessageSalonSorted(String.valueOf(contenu.get(0)));
                        Message message1 = new Message("GET_MESSAGE_SALON", conversations);
                        sendObject(message1);

                        for (Server server : threads) {
                            if (server != null) {
                                for(String membre : membres) {
                                    if (server.getIdentifiant().equals(membre)) {

                                        server.os.writeObject(message1);
                                        server.os.flush();
                                    }
                                }
                            }
                        }
                    }
                } else if (message.getTypeRequest().equals("GET_STATS")) {
                    if (message.getContenu() instanceof List) {
                        List identifiants = (List) message.getContenu();
                        List<String> statistiques = new ArrayList<>();
                        statistiques.add(String.valueOf(Repository.getFriendsFromUser((Double) identifiants.get(0)).size()));
                        statistiques.add(String.valueOf(Repository.getNbMessageByUser(String.valueOf(identifiants.get(1)))));
                        statistiques.add(String.valueOf(Repository.getNbSalon(String.valueOf(identifiants.get(1)))));
                        Message message1 = new Message("GET_STATS", statistiques);
                        sendObject(message1);
                    }
                }else if(message.getTypeRequest().equals("QUIT_SALON")){
                if(message.getContenu() instanceof List){
                    List identifiants = (List) message.getContenu();
                    Repository.quitSalon(String.valueOf(identifiants.get(0)),String.valueOf(identifiants.get(1)));
                    System.out.println("Le client "+identifiants.get(0)+" vient de se desincrire du salon : "+identifiants.get(1));
                }
            }

            else if (message.getTypeRequest().equals("ADD_USER_TO_SALON")){
                if(message.getContenu() instanceof List){
                    List identifiants = (List) message.getContenu();
                    Repository.addUserToSalon(String.valueOf(identifiants.get(0)),String.valueOf(identifiants.get(1)));
                    System.out.println("Le client "+identifiants.get(0)+" vient d'etre ajoute dans le salon : "+identifiants.get(1));
                }
            }
            else if (message.getTypeRequest().equals("ADD_SALON")){
                if(message.getContenu() instanceof List){
                    List identifiants = (List) message.getContenu();
                    Repository.addSalon(String.valueOf(identifiants.get(0)), (List<String>) identifiants.get(1),String.valueOf(identifiants.get(2)));
                }
            } else if(message.getTypeRequest().equals("IM_CONNECTED")) {
                if(message.getContenu() instanceof String) {
                    RunServer.usersConnected.add((String) message.getContenu());
                    Message message1 = new Message("SO_CONNECTED", RunServer.usersConnected);

                    for (Server server : threads) {
                        if (server != null) {
                            server.os.writeObject(message1);
                            server.os.flush();
                        }
                    }
                }

            }


            }
        }
    }
