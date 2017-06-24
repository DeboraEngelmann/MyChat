package br.com.memorygame.mychat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.memorygame.mychat.models.User;

public class AlterarDadosActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        mAuth = FirebaseAuth.getInstance();
                setContentView(R.layout.activity_alterar_dados);
    }

            @Override
    protected void onStart() {
                super.onStart();
                if (mAuth.getCurrentUser() != null) {
                        onAuthSuccess(mAuth.getCurrentUser());
                    }
                //Firebase
                        mAuth.addAuthStateListener(mAuthListener);
            }

            private void onAuthSuccess(FirebaseUser user) {
        //        startActivity(new Intent(LoginActivity.this, MainActivity.class));
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

            //EXEMPLO INTERNET - https://firebase.google.com/docs/database/android/save-data?hl=pt-br
        //    private void onStarClicked(DatabaseReference postRef) {
        //        postRef.runTransaction(new Transaction.Handler() {
        //            @Override
        //            public Transaction.Result doTransaction(MutableData mutableData) {
        //                Post p = mutableData.getValue(Post.class);
        //                if (p == null) {
        //                    return Transaction.success(mutableData);
        //                }
        //
        //                if (p.stars.containsKey(getUid())) {
        //                    // Unstar the post and remove self from stars
        //                    p.starCount = p.starCount - 1;
        //                    p.stars.remove(getUid());
        //                } else {
        //                    // Star the post and add self to stars
        //                    p.starCount = p.starCount  1;
        //                    p.stars.put(getUid(), true);
        //                }
        //
        //                // Set value and report transaction success
        //                mutableData.setValue(p);
        //                return Transaction.success(mutableData);
        //            }
        //
        //            @Override
        //            public void onComplete(DatabaseError databaseError, boolean b,
        //                                   DataSnapshot dataSnapshot) {
        //                // Transaction completed
        //                Log.d(TAG, "postTransaction:onComplete:"  databaseError);
        //            }
        //        });
        //    }

}

