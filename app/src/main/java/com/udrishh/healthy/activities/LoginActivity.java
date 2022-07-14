package com.udrishh.healthy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udrishh.healthy.R;
import com.udrishh.healthy.classes.User;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private User userObject;

    private MaterialButton loginBtn;
    private MaterialButton signupBtn;
    private MaterialCardView loginCard;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private ProgressBar loading;
    private ProgressBar loadingBig;
    private ImageView loadingBigText;

    //Firestore Connection
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Users");

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialiseComponents();
        setClickListeners();
        setAuthStateListener();
    }

    private void setClickListeners() {
        signupBtn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));

        loginBtn.setOnClickListener(v -> {
            boolean isValid = true;
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (TextUtils.isEmpty(Objects.requireNonNull(emailInput.getText()).toString().trim())
                    || !emailInput.getText().toString().trim().matches(emailPattern)) {
                emailInput.setError(getString(R.string.invalid_email_text));
                isValid = false;
            }
            if (TextUtils.isEmpty(Objects.requireNonNull(passwordInput.getText()).toString().trim())
                    || passwordInput.getText().toString().trim().length() < 6) {
                passwordInput.setError(getString(R.string.invalid_password_login_text));
                isValid = false;
            }

            if (!isValid) {
                return;
            }
            passwordInput.setError(null);
            emailInput.setError(null);
            loginEmailPasswordUser(emailInput.getText().toString().trim(),
                    passwordInput.getText().toString().trim());
        });
    }

    private void setAuthStateListener() {
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth -> {
            currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                loadingBig.setVisibility(View.VISIBLE);
                loadingBigText.setVisibility(View.VISIBLE);
                loginCard.setVisibility(View.INVISIBLE);
                currentUser = firebaseAuth.getCurrentUser();
                final String currentUserId = currentUser.getUid();

                collectionReference.document(currentUserId).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot != null) {
                                userObject = documentSnapshot.toObject(User.class);
                                moveToMainActivity();
                            }
                        });
            }
        };
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(LoginActivity.this,
                MainActivity.class);
        intent.putExtra("userObject", userObject);
        startActivity(intent);
        finish();
    }

    private void loginEmailPasswordUser(String email, String password) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            loading.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String currentUserId = firebaseUser.getUid();
                            collectionReference.document(currentUserId).get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot != null) {
                                            userObject = documentSnapshot.toObject(User.class);
                                            moveToMainActivity();
                                        } else {
                                            loading.setVisibility(View.INVISIBLE);
                                        }
                                    });
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.login_invalid_data_error_text, Toast.LENGTH_SHORT).show();
                        }

                    })
                    .addOnFailureListener(e -> loading.setVisibility(View.INVISIBLE));
        } else {
            Toast.makeText(LoginActivity.this, R.string.login_no_data_error_text, Toast.LENGTH_SHORT).show();
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
        loadingBigText = findViewById(R.id.login_loadingBigText);
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