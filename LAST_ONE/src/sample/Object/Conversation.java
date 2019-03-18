package sample.Object;

import java.io.Serializable;
import java.time.Instant;

public class Conversation implements Serializable {

    private Instant date;
    private String envoyeur;
    private Object receveur;
    private String contenu;

    public void setConcateneString(String concateneString) {
        this.contenu = contenu + concateneString;
    }

    public Conversation(Instant date, String envoyeur, String receveur, String contenu) {
        this.date = date;
        this.envoyeur = envoyeur;
        this.receveur = receveur;
        this.contenu = contenu;
    }

    public Conversation(String envoyeur, String contenu) {
        this.envoyeur = envoyeur;
        this.contenu = contenu;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getEnvoyeur() {
        return envoyeur;
    }

    public void setEnvoyeur(String envoyeur) {
        this.envoyeur = envoyeur;
    }

    public Object getReceveur() {
        return receveur;
    }

    public void setReceveur(Object receveur) {
        this.receveur = receveur;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
}
