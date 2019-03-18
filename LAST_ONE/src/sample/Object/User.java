package sample.Object;

import java.io.Serializable;

public class User implements Serializable {

    private Double _id;
    private String identifiant;
    private String password;
    private String firstname;
    private String name;
    private Double avaterNumber;

    private boolean isConnected;

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public User() {
    }

    public User(Double _id, String identifiant, String password, String firstname, String name, Double avaterNumber) {
        this._id = _id;
        this.identifiant = identifiant;
        this.password = password;
        this.firstname = firstname;
        this.name = name;
        this.avaterNumber = avaterNumber;
    }

    public User(String identifiant, String password, String firstname, String name, Double avaterNumber) {
        this.identifiant = identifiant;
        this.password = password;
        this.firstname = firstname;
        this.name = name;
        this.avaterNumber = avaterNumber;
    }

    public User(String firstname, String name, Double avaterNumber) {
        this.firstname = firstname;
        this.name = name;
        this.avaterNumber = avaterNumber;
    }

    public User(String identifiant, String firstname, String name, Double avaterNumber) {
        this.identifiant = identifiant;
        this.firstname = firstname;
        this.name = name;
        this.avaterNumber = avaterNumber;
    }

    public Double get_id() {
        return _id;
    }

    public void set_id(Double _id) {
        this._id = _id;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAvaterNumber() {
        return avaterNumber;
    }

    public void setAvaterNumber(Double avaterNumber) {
        this.avaterNumber = avaterNumber;
    }
}
