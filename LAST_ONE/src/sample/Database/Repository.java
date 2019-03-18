package sample.Database;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import sample.Object.Conversation;
import sample.Object.Salon;
import sample.Object.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Repository {

    private static MongoCollection<Document> userCollection = Repository.connectionToCollections("User");
    private static MongoCollection<Document> messageCollection = Repository.connectionToCollections("Message");
    private static MongoCollection<Document> friendsCollection = Repository.connectionToCollections("Friends");
    private static MongoCollection<Document> salonCollection = Repository.connectionToCollections("Salon");

    public static MongoCollection<Document> connectionToCollections(String database) {
        MongoCollection<Document> dbCollection;

        try {
            MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
            MongoDatabase db = mongoClient.getDatabase("jMessanger");
            dbCollection = db.getCollection(database);
            System.out.println("Connected to bdd");

        } catch (MongoException e) {
            System.out.println("Error connection bdd");
            dbCollection = null;
        }
        return dbCollection;
    }

    public static Double getNextIDUserAvailable() {
        Double test = (double) (userCollection.countDocuments() + 1);
        return test;

    }

    public static void createAccount(User user) {

        Document document = new Document();
        document.append("_id", user.get_id());
        document.append("identifiant", user.getIdentifiant());
        document.append("password", user.getPassword());
        document.append("nom", user.getName());
        document.append("prenom", user.getFirstname());
        document.append("avatar_number", user.getAvaterNumber());

        userCollection.insertOne(document);
    }

    public static String getCreateurFromIdentifiantSalon(String identifiantSalon) {

        Document document = new Document();
        document.append("identifiantSalon", identifiantSalon);

        MongoCursor<Document> results = salonCollection.find(document).iterator();

        if(results.hasNext()) {
            Document result = results.next();
            return (String) result.get("createur");
        } else {
            return new String();
        }
    }

    public static List<String> getMembresFromIdentifiantSalon(String identifiantSalon) {

        Document document = new Document();
        document.append("identifiantSalon", identifiantSalon);

        MongoCursor<Document> results = salonCollection.find(document).iterator();

        if(results.hasNext()) {
            Document result = results.next();
            return (List<String>) result.get("membres");
        } else {
            return new ArrayList<>();
        }
    }

    public static void quitSalon(String identifiant,String idSalon){
        Bson query = new org.bson.Document().append("identifiantSalon",idSalon);
        Bson fields = new org.bson.Document().append("membres", identifiant);
        Bson update = new org.bson.Document("$pull",fields);
        salonCollection.updateMany(query, update);
    }

    public static void sendMessage(String identifiantMainUser, String identifiantFriendUser, String message) {

        Document document = new Document();
        document.append("envoyeur", identifiantMainUser);
        document.append("receveur", identifiantFriendUser);
        document.append("message", message);
        document.append("date", new Date().toInstant());


        messageCollection.insertOne(document);
    }

    public static List<Conversation> getMessageSalonSorted(String identifiantSalon) {

        List<Conversation> messages = new ArrayList<>();

        Document document = new Document();
        document.append("identifiantSalon", identifiantSalon);

        MongoCursor<Document> results = messageCollection.find(document).sort(Sorts.ascending("date")).iterator();
        while(results.hasNext()) {
            Document result = results.next();
            String envoyeur = String.valueOf(result.get("envoyeur"));
            String message = String.valueOf(result.get("message"));
            Conversation objectTest = new Conversation(envoyeur, message);
            messages.add(objectTest);
        }

        return messages;
    }

    public static List<Conversation> getMessageSorted(String fromIdentifiant, String toIdentifiant) {

        List<Conversation> dates = new ArrayList<>();

        Document query = new Document(
                "$or", Arrays.asList(
                new Document("envoyeur", fromIdentifiant).append("receveur", toIdentifiant),
                new Document("envoyeur", toIdentifiant).append("receveur", fromIdentifiant)
        ));

        MongoCursor<Document> results = messageCollection.find(query).sort(Sorts.ascending("date")).iterator();

        while(results.hasNext()) {
            Document result = results.next();
            String envoyeur = String.valueOf(result.get("envoyeur"));
            String message = String.valueOf(result.get("message"));
            Conversation objectTest = new Conversation(envoyeur, message);
            dates.add(objectTest);
        }

        return dates;
    }

    public static void SendMessageTo(String identifiant,String message,String receveur) {

        Document document = new Document();
        document.append("envoyeur", identifiant);
        document.append("message", message);
        document.append("receveur", receveur);
        messageCollection.insertOne(document);
    }
    public static void sendMessageToSalon(String identifiantSalon, String envoyeur, List<String> membres, String message) {

        Document document = new Document();
        document.append("identifiantSalon",identifiantSalon);
        document.append("envoyeur",envoyeur);
        //document.append("createur","toto");
        document.append("membres", membres);
        document.append("message", message);
        document.append("date", new Date().toInstant());
        messageCollection.insertOne(document);
    }
    public static List<String> getMessageFromSalon(String sender, List<String> membres){
        List<String> messages = new ArrayList<>();

        Document dataUser = new Document();
        dataUser.append("envoyeur", sender);
        dataUser.append("membres", membres);

        MongoCursor<Document> resultsIterable = salonCollection.find(dataUser).iterator();

        if(resultsIterable.hasNext()) {
            while(resultsIterable.hasNext()) {
                Document firstLine = resultsIterable.next();
                String message = (String) firstLine.get("message");
                messages.add(message);
            }
        }
        dataUser.append("envoyeur", membres);
        dataUser.append("membres", sender);

        resultsIterable = friendsCollection.find(dataUser).iterator();

        if(resultsIterable.hasNext()) {
            while(resultsIterable.hasNext()) {
                Document firstLine = resultsIterable.next();
                String message = (String) firstLine.get("message");
                messages.add(message);
            }
        }
        return messages;
    }

    public static List<String> getMessageFromTwoIdentifiants(String sender, String receipter) {

        List<String> messages = new ArrayList<>();

        Document dataUser = new Document();
        dataUser.append("envoyeur", sender);
        dataUser.append("receveur", receipter);

        MongoCursor<Document> resultsIterable = messageCollection.find(dataUser).iterator();

        if(resultsIterable.hasNext()) {
            while(resultsIterable.hasNext()) {
                Document firstLine = resultsIterable.next();
                String message = (String) firstLine.get("message");
                messages.add(message);
            }
        }
        dataUser.append("envoyeur", receipter);
        dataUser.append("receveur", sender);

        resultsIterable = friendsCollection.find(dataUser).iterator();

        if(resultsIterable.hasNext()) {
            while(resultsIterable.hasNext()) {
                Document firstLine = resultsIterable.next();
                String message = (String) firstLine.get("message");
                messages.add(message);
            }
        }
        return messages;


    }

    public static boolean areAlreadyFriends(Double mainID, Double friendID) {

        Document dataUser = new Document();
        dataUser.append("_id", mainID);

        MongoCursor<Document> resultsIterable = friendsCollection.find(dataUser).iterator();

        if(resultsIterable.hasNext()) {
            Document firstLine = resultsIterable.next();
            List<Double> friendsID = (List<Double>) firstLine.get("friends");

            for(Double id : friendsID) {
                if(id.equals(friendID)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
    public static void addSalon(String idSalon,List<String> identifiants,String createur) {

        Document document = new Document();

        document.append("identifiantSalon", idSalon);
        document.append("envoyeur", createur);
        document.append("createur", createur);
        document.append("membres", identifiants);
        //document.append("message", "Premier message");

        salonCollection.insertOne(document);
    }



    public static void addUserToSalon(String identifiant,String idSalon){
        Bson query = new org.bson.Document().append("identifiantSalon",idSalon);
        Bson fields = new org.bson.Document().append("membres", identifiant);
        Bson update = new org.bson.Document("$push",fields);
        salonCollection.updateMany(query, update);
    }

    public static void addFriend(Double mainUserID, Double friendUserID) {

        // MAIN USER TO FRIEND
        Document mainUser = new Document();
        mainUser.append("_id", mainUserID);

        MongoCursor<Document> resultsIterable = friendsCollection.find(mainUser).iterator();
        List<Double> mainUserfriends = new ArrayList<>();
        if(resultsIterable.hasNext()) {
            mainUserfriends = (List<Double>) resultsIterable.next().get("friends");
        }
        addIDUserInFriends(mainUserID, friendUserID, mainUserfriends);

        Document friendUser = new Document();
        friendUser.append("_id", friendUserID);

        MongoCursor<Document> results = friendsCollection.find(friendUser).iterator();
        List<Double> friends = new ArrayList<>();
        if(results.hasNext()) {
            friends = (List<Double>) results.next().get("friends");
        }

        addIDUserInFriends(friendUserID, mainUserID, friends);


    }


    public static void addIDUserInFriends(Double mainUserID, Double friendUserID, List<Double> currentlyFriends) {

        Document addfriends = new Document();
        addfriends.append("_id", mainUserID);
        friendsCollection.deleteOne(addfriends);

        currentlyFriends.add(friendUserID);
        addfriends.append("friends", currentlyFriends);

        friendsCollection.insertOne(addfriends);
    }

    public static Double getAvatarNumberByIdentifiant(String identifiant) {


        Document dataUser = new Document();
        dataUser.append("identifiant", identifiant);

        MongoCursor<Document> resultsIterable = userCollection.find(dataUser).iterator();

        Document firstLine = resultsIterable.next();

        Double userID = Double.parseDouble(String.valueOf(firstLine.get("avatar_number")));

        return userID;
    }

    public static Double getIDUserByIdentifiant(String identifiant) {

        Document dataUser = new Document();
        dataUser.append("identifiant", identifiant);

        MongoCursor<Document> resultsIterable = userCollection.find(dataUser).iterator();
        Document firstLine = new Document();
        if(resultsIterable.hasNext()) {
            firstLine = resultsIterable.next();
            Double userID = Double.parseDouble(String.valueOf(firstLine.get("_id")));
            return userID;
        } else {
            return 0.0;
        }
    }

    public static User getUserWithIdentifiant(String identifiant) {

        Document dataUser = new Document();
        dataUser.append("identifiant", identifiant);

        MongoCursor<Document> resultsIterable = userCollection.find(dataUser).iterator();

        Document firstLine = resultsIterable.next();

        //String identifiant = String.valueOf(firstLine.get("identifiant"));
        //String password = String.valueOf(firstLine.get("password"));
        String name = String.valueOf(firstLine.get("nom"));
        String firstName = String.valueOf(firstLine.get("prenom"));
        Double avatarNumber = (Double) firstLine.get("avatar_number");
        //Double userID = (Double) firstLine.get("_id");

        return new User(identifiant, firstName, name, avatarNumber);// UNFINISHED
    }

    public static boolean isIdentifiantAvailable(String identifiant) {
        Document dataUser = new Document();
        dataUser.append("identifiant", identifiant);

        MongoCursor<Document> resultsIterable = userCollection.find(dataUser).iterator();

        if(resultsIterable.hasNext()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isUserIdentifiantExist(String identifiant) {

        Document dataUser = new Document();
        dataUser.append("identifiant", identifiant);

        MongoCursor<Document> resultsIterable = userCollection.find(dataUser).iterator();

        if(resultsIterable.hasNext()) {
            return true;
        } else {
            return false;
        }
    }

    public static User getUserWithID(Double id) {

        Document dataUser = new Document();
        dataUser.append("_id", id);

        MongoCursor<Document> resultsIterable = userCollection.find(dataUser).iterator();

        Document firstLine = resultsIterable.next();

        String identifiant = String.valueOf(firstLine.get("identifiant"));
        String password = String.valueOf(firstLine.get("password"));
        String name = String.valueOf(firstLine.get("nom"));
        String firstName = String.valueOf(firstLine.get("prenom"));
        Double avatarNumber = (Double) firstLine.get("avatar_number");
        Double userID = (Double) firstLine.get("_id");

        return new User(userID, identifiant, password, firstName, name, avatarNumber);
    }

    public static List<User> getFriendsFromUserIdentifiant(String identifiant) {

        MongoCollection<Document> dbCollection = connectionToCollections("Friends");

        Document dataUser = new Document();
        dataUser.append("identifiant", identifiant);

        MongoCursor<Document> resultsIterable = dbCollection.find(dataUser).iterator();
        List<User> users = new ArrayList<>();

        if(resultsIterable.hasNext()) {

            List<Double> friendsID = (List<Double>) resultsIterable.next().get("friends");

            friendsID.forEach(friendID ->
                    users.add(getUserWithID(friendID)));
        }


        return users;
    }

    public static List<User> getFriendsFromUser(Double id) {

        MongoCollection<Document> dbCollection = connectionToCollections("Friends");

        Document dataUser = new Document();
        dataUser.append("_id", id);

        MongoCursor<Document> resultsIterable = dbCollection.find(dataUser).iterator();
        List<User> users = new ArrayList<>();

        if(resultsIterable.hasNext()) {

            List<Double> friendsID = (List<Double>) resultsIterable.next().get("friends");

            friendsID.forEach(friendID ->
                    users.add(getUserWithID(friendID)));
        }


        return users;
    }

    public static List<Salon> getSalonsFromIdentifiant(String identifiant) {

        List<Salon> salons = new ArrayList<>();

        MongoClient m1 = new MongoClient("localhost:27017");
        DB db = m1.getDB("jMessanger");
        DBCollection col = db.getCollection("Salon");

        DBObject query = new BasicDBObject("membres", identifiant);
        DBCursor c1 = col.find(query);

        for(DBObject object : c1) {

            String identifiantSalon = String.valueOf(object.get("identifiantSalon"));
            String envoyeur = String.valueOf(object.get("envoyeur"));
            String createur = String.valueOf(object.get("createur"));
            List<String> membres = (List<String>) object.get("membres");
            String message = String.valueOf(object.get("message"));

            Salon salon = new Salon(identifiantSalon, envoyeur, createur, membres, message);
            salons.add(salon);
        }
        return salons;
    }



    public static int getNbSalon(String identifiant) {

        int compteur = 0;
        MongoClient m1 = new MongoClient("localhost");
        DB db = m1.getDB("jMessanger");
        DBCollection col = db.getCollection("Salon");

        DBObject query = new BasicDBObject("membres", identifiant);
        BasicDBObject fields = new BasicDBObject("_id", 1);
        DBCursor c1 = col.find(query,fields);
        compteur=c1.count();
        return compteur;
    }

    public static long getNbMessageByUser(String identifiant){
        Document document = new Document();
        document.append("envoyeur", identifiant);
        long compteur= salonCollection.countDocuments(document)+messageCollection.countDocuments(document);
        return compteur;
    }
    public static long getNbMessageBySalon(String idsalon){
        Document document=new Document();
        document.append("_id",idsalon);
        long compteur= salonCollection.countDocuments(document)-1;
        return compteur;
    }
    public static long getNbUser(){
        long compteur=userCollection.countDocuments();
        return compteur;
    }
    public static long getNbFriends(){
        Document document=new Document();
        document.append("friends","");
        long compteur=friendsCollection.countDocuments();
        return compteur;
    }


    public static Double getIDFromLoginAndPwd(String identifiant, String password) {

        MongoCollection<Document> dbCollection = Repository.connectionToCollections("User");

        Document dataUser = new Document();
        dataUser.append("identifiant", identifiant);
        dataUser.append("password", password);

        MongoCursor<Document> resultsIterable = dbCollection.find(dataUser).iterator();

        if(resultsIterable.hasNext()) {
            return (Double) resultsIterable.next().get("_id");
        } else {
            System.out.println("Error Repository.getIDFromLoginAndPwd");
            return 0.0;
        }
    }

    public static boolean checkMatchAccount(String identifiant, String password) {


        Document dataUser = new Document();
        dataUser.append("identifiant", identifiant);
        dataUser.append("password", password);

        FindIterable<Document> dbCursor = userCollection.find(dataUser);

        if(dbCursor.first() != null) {
            return true;
        } else {
            return false;
        }
    }
}
