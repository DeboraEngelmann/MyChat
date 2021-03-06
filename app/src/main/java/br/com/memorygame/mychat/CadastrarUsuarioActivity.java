package br.com.memorygame.mychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.memorygame.mychat.models.User;
import br.com.memorygame.mychat.utilitarios.Funcoes;

public class CadastrarUsuarioActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword, inputPasswordConfirm, edtNome;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    User mUsuario;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }else{
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        }
        mUsuario = User.getInstancia();
        auth = FirebaseAuth.getInstance();
        recuperacomponentes();
    }

    private void recuperacomponentes() {
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputPasswordConfirm = (EditText) findViewById(R.id.confirm_password);
        edtNome = (EditText) findViewById(R.id.edtNome);
    }

    public void clickFinish(View v) {
        finish();
    }

    public void clickEntrar(View v){
        showProgressDialog();

        if (!validaForm()){
            hideProgressDialog();
            return;
        }

        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        mUsuario.setNome(edtNome.getText().toString());
        mUsuario.setEmail(email);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            //create user
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(CadastrarUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            showProgressDialog();
                            if (!task.isSuccessful()) {
                                hideProgressDialog();
                                if (task.getException().getMessage().toString().equalsIgnoreCase("The email address is badly formatted.")){
                                    Toast.makeText(CadastrarUsuarioActivity.this, "Endereço de e-mail invalido",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CadastrarUsuarioActivity.this, "Já existe um usuário cadastrado com esse e-mail",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mUsuario.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                adicionarUsuario();
                                startActivity(new Intent(CadastrarUsuarioActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
        }else{
            atualizarUsuario();
            finish();
        }
    }

    private boolean validaForm() {
        boolean ret = true;
        if (!Funcoes.validateNotNull(edtNome, getString(R.string.informe_o_nome))){
            ret =  false;
        }else if (!Funcoes.validateNotNull(inputEmail, getString(R.string.val_email_empty))){
            ret = false;
        }else if (!Funcoes.validateNotNull(inputPassword, getString(R.string.val_senha_empty))){
            ret =  false;
        }else if (!Funcoes.validateNotNull(inputPasswordConfirm, getString(R.string.val_confirm_senha_empty))){
            ret =  false;
        }else if (inputPassword.getText().length() < 6) {
            inputPassword.setError("A senha deve ter no minimo 6 Caracteres");
            inputPassword.setFocusable(true);
            inputPassword.requestFocus();
            ret = false;
        }else if (!inputPassword.getText().toString().equals(inputPasswordConfirm.getText().toString())){
            inputPasswordConfirm.setError("As senhas não correspondem!");
            inputPasswordConfirm.setFocusable(true);
            inputPasswordConfirm.requestFocus();
            ret =false;
        }
        return ret;
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgressDialog();
    }

    private void adicionarUsuario() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child(mUsuario.getUid()).setValue(mUsuario.toMap());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    private void atualizarUsuario(){
        mDatabase.updateChildren(mUsuario.toMap());
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(getString(R.string.carregando));
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
