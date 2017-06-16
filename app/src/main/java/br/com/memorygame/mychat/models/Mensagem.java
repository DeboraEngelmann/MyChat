package br.com.memorygame.mychat.models;

/**
 * Created by debo_ on 15/06/2017.
 */

public class Mensagem {
    private String uid;
    private String nome;
    private String mensagem;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
