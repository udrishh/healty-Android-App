package com.udrishh.healthy.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.udrishh.healthy.R;
import com.udrishh.healthy.classes.User;
import com.udrishh.healthy.enums.Sex;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private User userObject;

    private MaterialButton loginBtn;
    private MaterialButton signupBtn;
    private MaterialCardView loginCard;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //Firestore Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private FirebaseUser firebaseUser;

    private MaterialAutoCompleteTextView emailInput;
    private MaterialAutoCompleteTextView passwordInput;
    private ProgressBar loading;
    private ProgressBar loadingBig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    loadingBig.setVisibility(View.VISIBLE);
                    loginCard.setVisibility(View.INVISIBLE);
                    currentUser = firebaseAuth.getCurrentUser();
                    final String currentUserId = currentUser.getUid();

                    collectionReference
                            .whereEqualTo("userId", currentUserId)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                                    @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        return;
                                    }

                                    userObject = new User();
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                            userObject.setUserId(snapshot.getString("userId"));
                                            userObject.setName(snapshot.getString("name"));
                                            userObject.setBirthdate(snapshot.getString("birthdate"));
                                            userObject.setHeight(Integer.parseInt(Objects.requireNonNull(snapshot.getString("height"))));
                                            userObject.setWeight(Integer.parseInt(Objects.requireNonNull(snapshot.getString("weight"))));
                                            userObject.setCaloriesPlan(Integer.parseInt(Objects.requireNonNull(snapshot.getString("caloriesPlan"))));
                                            if (Objects.equals(snapshot.getString("sex"), "MALE")) {
                                                userObject.setSex(Sex.MALE);
                                            } else {
                                                userObject.setSex(Sex.FEMALE);
                                            }

                                            Intent intent = new Intent(LoginActivity.this,
                                                    MainActivity.class);
                                            intent.putExtra("userObject", userObject);
                                            startActivity(intent);

                                            finish();
                                        }
                                    }

                                }
                            });

                } else {

                }
            }
        };

        setContentView(R.layout.activity_login);

        initialiseComponents();

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmailPasswordUser(emailInput.getText().toString().trim(),
                        passwordInput.getText().toString().trim());
            }
        });
    }

    private void loginEmailPasswordUser(String email, String password) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            loading.setVisibility(View.VISIBLE);

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String currentUserId = firebaseUser.getUid();
                                collectionReference.whereEqualTo("userId", currentUserId)
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                if (error != null) {
                                                }
                                                assert value != null;
                                                if (!value.isEmpty()) {
                                                    //retrieve data
                                                    userObject = new User();
                                                    for (QueryDocumentSnapshot snapshot : value) {
                                                        userObject.setUserId(snapshot.getString("userId"));
                                                        userObject.setName(snapshot.getString("name"));
                                                        userObject.setBirthdate(snapshot.getString("birthdate"));
                                                        userObject.setHeight(Integer.parseInt(Objects.requireNonNull(snapshot.getString("height"))));
                                                        userObject.setWeight(Integer.parseInt(Objects.requireNonNull(snapshot.getString("weight"))));
                                                        userObject.setCaloriesPlan(Integer.parseInt(Objects.requireNonNull(snapshot.getString("caloriesPlan"))));
                                                        if (Objects.equals(snapshot.getString("sex"), "MALE")) {
                                                            userObject.setSex(Sex.MALE);
                                                        } else {
                                                            userObject.setSex(Sex.FEMALE);
                                                        }
                                                    }

                                                    //go to main activity
                                                    Intent intent = new Intent(LoginActivity.this,
                                                            MainActivity.class);
                                                    intent.putExtra("userObject", userObject);
                                                    startActivity(intent);

                                                } else {
                                                    loading.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });


                            } else {
                                Toast.makeText(LoginActivity.this, "Datele de conectare sunt invalide!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loading.setVisibility(View.INVISIBLE);
                        }
                    });
        } else {
            Toast.makeText(LoginActivity.this, "Introduceti datele de conectare!", Toast.LENGTH_SHORT).show();
            loading.setVisibility(View.INVISIBLE);
        }
    }

    private void initialiseComponents() {
        loginBtn = findViewById(R.id.login_login_btn);
        signupBtn = findViewById(R.id.create_account_btn);
        emailInput = findViewById(R.id.login_email_input);
        passwordInput = findViewById(R.id.login_password_input);
        loading = findViewById(R.id.login_loading);
        loadingBig = findViewById(R.id.login_loadingBig);
        loginCard = findViewById(R.id.login_card);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}