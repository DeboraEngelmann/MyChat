package br.com.memorygame.mychat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.memorygame.mychat.models.User;
import br.com.memorygame.mychat.utilitarios.Funcoes;

public class AlterarDadosActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edt2Nome;
    private Button btnAlterar;
    private User usuario;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private User user;

    //    onCreate(): executado logo antes de a Activity ser exibida na tela.
    //    Aqui você pode adicionar configurações que devem acontecer antes de
    //    o usuário ter uma impressão visual da Activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        setContentView(R.layout.activity_alterar_dados);
        edt2Nome = (EditText) findViewById(R.id.edt2Nome);
        btnAlterar=(Button) findViewById(R.id.btn_alterar_dados);
        btnAlterar.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //listener que recupera o usuario (é executado só uma vez)
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(User.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override //ouvinte de cliques da view
    public void onClick(View v) {
        if (!Funcoes.validateNotNull(edt2Nome, getString(R.string.informe_o_nome))){
            return;
        }
        if (usuario!=null){
            usuario.setNome(edt2Nome.getText().toString());
            mDatabase.updateChildren(usuario.toMap());
            Toast.makeText(this, "Dados Alterados com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

