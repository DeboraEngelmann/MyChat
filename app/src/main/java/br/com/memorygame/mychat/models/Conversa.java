
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
    public String uid;
    public List<Contato> contato_array_list = new ArrayList<>();
    public List<Mensagem> mensagem_array_list = new ArrayList<>();

    public Conversa() {
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("contato_array_list", contato_array_list);
        result.put("mensagem_array_list", mensagem_array_list);
        return result;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public List<Mensagem> getMensagem_array_list() {
        return mensagem_array_list;
    }

    public void setMensagem_array_list(List<Mensagem> mensagem_array_list) {
        this.mensagem_array_list = mensagem_array_list;
    }

    public List<Contato> getContato_array_list() {
        return contato_array_list;
    }

    public void setContato_array_list(List<Contato> contato_array_list) {
        this.contato_array_list = contato_array_list;
    }
}