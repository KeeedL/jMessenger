package sample.ClientServeur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class RunServer {

    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private static Server[] threads = new Server[100];
    public static List<String> usersConnected = new ArrayList<>();


    public static void main(String args[]) {

        try {
            serverSocket = new ServerSocket(2222);
        } catch (IOException e) {
            System.out.println(e);
        }

        /*
         * Création d'un socket pour chaque connexion d'un client
         */
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                int i;
                for (i = 0; i < threads.length; i++) {
                    if (threads[i] == null) {
                        (threads[i] = new Server(clientSocket, threads)).start();
                        break;
                    }
                }

            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}