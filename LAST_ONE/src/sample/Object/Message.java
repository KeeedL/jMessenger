package sample.Object;

import java.io.Serializable;

public class Message implements Serializable {

    private String typeRequest;
    private Object contenu;

    public Message(String typeRequest, Object contenu) {
        this.typeRequest = typeRequest;
        this.contenu = contenu;
    }

    public Message() {
    }

    public String getTypeRequest() {
        return typeRequest;
    }

    public void setTypeRequest(String typeRequest) {
        this.typeRequest = typeRequest;
    }

    public Object getContenu() {
        return contenu;
    }

    public void setContenu(Object contenu) {
        this.contenu = contenu;
    }
}
