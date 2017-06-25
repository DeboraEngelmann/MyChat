package br.com.memorygame.mychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.memorygame.mychat.models.User;
import br.com.memorygame.mychat.utilitarios.Funcoes;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private EditText inputEmail, inputPassword;
    private Button btnCadastrar, btnLogin, btnReset;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    public static GoogleApiClient mGoogleApiClient;
    private User user;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        // set the view now
        setContentView(R.layout.activity_login);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnCadastrar = (Button) findViewById(R.id.btn_cadastrar);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnCadastrar.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        findViewById(R.id.sign_in_button_google).setOnClickListener(this);

        //google - pega a id e seta para o componente
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButtonGoogle = (SignInButton) findViewById(R.id.sign_in_button_google);
        signInButtonGoogle.setSize(SignInButton.SIZE_STANDARD);

        user = User.getInstancia();
        mAuthListener = getFirebaseAuthResultHandler();

    }

    private FirebaseAuth.AuthStateListener getFirebaseAuthResultHandler() {
        FirebaseAuth.AuthStateListener callback = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser userFirebase = firebaseAuth.getCurrentUser();
                if (userFirebase == null) {
                    hideProgressDialog();
                    return;
                } else {
                    user.setUid(userFirebase.getUid());
                    adicionarUsuario();
                    goMainScreen();
                }
            }
        };
        return (callback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //google
            case R.id.sign_in_button_google:
                signIn();
                break;

            case R.id.btn_cadastrar:
                try {
                    startActivity(new Intent(LoginActivity.this, CadastrarUsuarioActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_reset_password:
                try {
                    startActivity(new Intent(LoginActivity.this, ResetarSenhaActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_login:
                try {
                    showProgressDialog();
                    final String email = inputEmail.getText().toString();
                    final String password = inputPassword.getText().toString();

                    if (!Funcoes.validateNotNull(inputEmail, getString(R.string.val_email_empty))) {
                        hideProgressDialog();
                        return;
                    }
                    if (!Funcoes.validateNotNull(inputPassword, getString(R.string.val_senha_empty))) {
                        hideProgressDialog();
                        return;
                    }

                    showProgressDialog();

                    //Autenticar o  usuario
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Se o login falhar, exiba uma mensagem para o usuário. Se o login for bem-sucedido
                                    // o ouvinte do estado de autenticação será notificado e a lógica para lidar com o
                                    // assinado no usuário pode ser manipulado no ouvinte.
                                    hideProgressDialog();
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (password.length() < 6) {
                                            inputPassword.setError(getString(R.string.minimum_password));
                                        } else {
                                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                        }
                                    } else {//Caso feliz
                                        hideProgressDialog();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;

        }


    }

    @Override //recebe a resposta da tela de loguin do google
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                user.setEmail(account.getEmail());
                user.setNome(account.getGivenName() + " " + account.getFamilyName());
                user.setUid(account.getId());
                user.setUrl_photo(account.getPhotoUrl().toString());
                accessGoogleLoginData(account.getIdToken());
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess();
        }

        //Adiciona o ouvinte do Firebase
        mAuth.addAuthStateListener(mAuthListener);
        //Adiciona o ouvinte do login do google
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    //google
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Conectado com êxito, mostre interface do usuário autenticada.
            GoogleSignInAccount acct = result.getSignInAccount();

            // updateUI(true);
        } else {
        }
    }

    //abre a tela do google para fazer login
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
    //vai para a tela inicial
    public void goMainScreen() {
        hideProgressDialog();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        //implementar
    }

    //faz o login utilizando uma credencial - no caso, google
    private void accessLoginData(String... tokens) {
        if (tokens != null && tokens.length > 0 && tokens[0] != null) {

            AuthCredential credential = GoogleAuthProvider.getCredential(tokens[0], null);

            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                hideProgressDialog();
                                Toast.makeText(LoginActivity.this, "Não foi possível entrar!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseCrash.report(e);
                        }
                    });
        } else {
            mAuth.signOut();
        }
    }

    private void accessGoogleLoginData(String accessToken) {
        accessLoginData(accessToken);
    }


    private void onAuthSuccess() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void adicionarUsuario() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child("users").child(user.getUid()).setValue(user.toMap());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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