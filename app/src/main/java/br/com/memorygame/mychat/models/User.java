package br.com.memorygame.mychat.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by debo_ on 11/06/2017.
 */

public class User {
    private static User instancia;

    private String uid;
    private String email;
    private String nome;
    private String urlFoto;

    public User() {
    }

    public static User getInstancia(){
        if (instancia==null){
            instancia = new User();
        }
        return instancia;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", nome);
        result.put("email", email);
        result.put("url_photo",urlFoto);
        return result;
    }
}
