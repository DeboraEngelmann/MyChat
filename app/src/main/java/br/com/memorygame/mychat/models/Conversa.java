package br.com.memorygame.mychat.models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by debo_ on 15/06/2017.
 */

public class Conversa {
    private String uid;
    private List<Contato> contatoArrayList = new ArrayList<>();
    private List<Mensagem> mensagemArrayList = new ArrayList<>();

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
