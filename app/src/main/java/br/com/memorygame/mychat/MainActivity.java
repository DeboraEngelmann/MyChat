package br.com.memorygame.mychat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import br.com.memorygame.mychat.fragmentos.TabFragment;
import br.com.memorygame.mychat.models.Contato;
import br.com.memorygame.mychat.models.User;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "MainActivity";
    private FragmentManager FM;
    private FragmentTransaction FT;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10;
    private static ArrayList<Contato> contatosArrayList = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    public static User usuario;
    private DatabaseReference mDatabaseUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    //se não tem nenhum usuário logado abre a tela de loguin
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    Log.d(TAG, "onAuthStateChanged:signed_out:");
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
                // ...
            }
        };
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usuario = User.getInstancia();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            mDatabaseUsuario = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }


        FM= getSupportFragmentManager();
        FT= FM.beginTransaction();
        FT.replace(R.id.containerView, new TabFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // O item da barra de ação de controle é clicado aqui. A barra de ação manipulará automaticamente
        // os cliques no botão Home / Up, desde que você especifique uma atividade pai no AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sair) {
            signOut();
            return true;
        }
        if (id == R.id.action_alterar_dados) {
            Intent alterarDados = new Intent(this,AlterarDadosActivity.class);
            startActivity(alterarDados);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //sign out method
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            Log.i(TAG, "Pediu permissão ao usuário");
            return;
        } else {
            //Ler os contatos
            getNameEmailDetails();

            Log.i(TAG, "Já tem permissão ao usuário");
        }
        if (mDatabaseUsuario!=null) {
            mDatabaseUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    usuario = dataSnapshot.getValue(User.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    //pega o retorno da mensagem de permissão e se o usuário não consedeu permissão avisa que não vai conseguir prosseguir
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                        return;
                    }else{
                        //Ler Contatos
                        getNameEmailDetails();
                    }
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        new AlertDialog.Builder(this).
                                setTitle("Permissão para acessar os seus contatos").
                                setMessage("Você precisa conceder permissão para acessar os seus contatos." +
                                        "Concedendo esse acesso você poderá conversar no chat com seus amigos!").show();
                    } else {
                        new AlertDialog.Builder(this).
                                setTitle("Permissão de acesso aos contatos não concedida").
                                setMessage("Infelizmente não será possivel conversar com seus contatos").show();
                    }
                }
                break;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void getNameEmailDetails(){
        final String[] PROJECTION = new String[] {
                ContactsContract.CommonDataKinds.Email.CONTACT_ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Email.DATA
        };
        boolean inserir=true;
        if (contatosArrayList.size()==0) {
            showProgressDialog();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean inserir=true;

                    ContentResolver cr = getContentResolver();
                    Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, null, null, null);
                    if (cursor != null) {
                        try {
                            final int contactIdIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID);
                            final int displayNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                            final int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
                            long contactId;
                            String displayName, address;
                            while (cursor.moveToNext()) {
                                inserir = true;
                                contactId = cursor.getLong(contactIdIndex);
                                displayName = cursor.getString(displayNameIndex);
                                address = cursor.getString(emailIndex);
                                if (address != null) {
                                    Contato contato = new Contato();
                                    contato.setUid((String.valueOf(contactId)));
                                    contato.setNome(displayName);
                                    contato.setEmail(address);
                                    for (Contato item : contatosArrayList) {
                                        if (item.getEmail().equals(contato.getEmail())) {
                                            inserir = false;
                                        }
                                    }
                                    if (inserir) {
                                        contatosArrayList.add(contato);
                                    }
                                }
                            }
                        } finally {
                            cursor.close();
                        }
                    }
                    hideProgressDialog();
                }
            }).start();
        }
    }
    public static ArrayList<Contato> getContatosArrayList(){
        Collections.sort(contatosArrayList);
        return  contatosArrayList;
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
