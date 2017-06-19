package br.com.memorygame.mychat.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by debo_ on 15/06/2017.
 */

@IgnoreExtraProperties
public class Conversa {
    private String uid;
    public List<Contato> contatoArrayList = new ArrayList<>();
    public List<Mensagem> mensagemArrayList = new ArrayList<>();

    public Conversa() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<Contato> getContatoArrayList() {
        return contatoArrayList;
    }

    public void setContatoArrayList(List<Contato> contatoArrayList) {
        this.contatoArrayList = contatoArrayList;
    }

    public List<Mensagem> getMensagemArrayList() {
        return mensagemArrayList;
    }

    public void setMensagemArrayList(List<Mensagem> mensagemArrayList) {
        this.mensagemArrayList = mensagemArrayList;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("contatoArrayList", contatoArrayList);
        result.put("mensagemArrayList", mensagemArrayList);
        return result;
    }
}
