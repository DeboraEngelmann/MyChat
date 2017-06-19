package br.com.memorygame.mychat.utilitarios;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by debo_ on 11/06/2017.
 */

public class Funcoes {
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

    public static Date getDateTime() {
        return new Date();
    }

    public static String dateToString(Date data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM HH:mm", Locale.getDefault());
        if (data == null) {
            data = getDateTime();
        }
        return dateFormat.format(data);
    }
}
