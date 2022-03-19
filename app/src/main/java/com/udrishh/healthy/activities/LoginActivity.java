package com.udrishh.healthy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udrishh.healthy.R;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton loginBtn;
    private MaterialButton signupBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //Firestore Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private MaterialAutoCompleteTextView emailInput;
    private MaterialAutoCompleteTextView passwordInput;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialiseComponents();

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void initialiseComponents() {
        loginBtn = findViewById(R.id.login_login_btn);
        signupBtn = findViewById(R.id.create_account_btn);
        emailInput = findViewById(R.id.login_email_input);
        passwordInput = findViewById(R.id.login_password_input);
        loading = findViewById(R.id.login_loading);
    }
}