package br.com.memorygame.mychat.models;

import java.util.ArrayList;
import java.util.List;

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
}
