package sample.Object;

import java.io.Serializable;
import java.util.List;

public class Salon implements Serializable {

    private String identifiantSalon;
    private String envoyeur;
    private String createur;
    private List<String> membres;
    private String message;

    public Salon() {
    }

    public Salon(String identifiantSalon, String envoyeur, String createur, List<String> membres, String message) {
        this.identifiantSalon = identifiantSalon;
        this.envoyeur = envoyeur;
        this.createur = createur;
        this.membres = membres;
        this.message = message;
    }

    public String getIdentifiantSalon() {
        return identifiantSalon;
    }

    public void setIdentifiantSalon(String identifiantSalon) {
        this.identifiantSalon = identifiantSalon;
    }

    public String getEnvoyeur() {
        return envoyeur;
    }

    public void setEnvoyeur(String envoyeur) {
        this.envoyeur = envoyeur;
    }

    public String getCreateur() {
        return createur;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
    }

    public List<String> getMembres() {
        return membres;
    }

    public void setMembres(List<String> membres) {
        this.membres = membres;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
