package br.com.memorygame.mychat.utilitarios;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import br.com.memorygame.mychat.models.Contato;

/**
 * Created by debo_ on 11/06/2017.
 */

public class Funcoes {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    public static boolean validateNotNull(View pView, String pMessage) {
        if (pView instanceof EditText) {
            EditText edText = (EditText) pView;
            Editable text = edText.getText();
            if (text != null) {
                String strText = text.toString();
                if (!TextUtils.isEmpty(strText)) {
                    return true;
                }
            }
            // em qualquer outra condição é gerado um erro
            edText.setError(pMessage);
            edText.setFocusable(true);
            edText.requestFocus();
            return false;
        }
        return false;
    }
}
