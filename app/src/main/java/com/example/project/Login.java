package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    CheckBox rememberMe;
    Button button;
    EditText emailTxt, passwordTxt;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);

        TextView t = (TextView) findViewById(R.id.forgot_password);
        rememberMe=findViewById(R.id.rememberMe);

        SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox=preferences.getString("remember","");
        if(checkbox.equals("true")){
            Intent intent = new Intent(Login.this, Drawer.class);
            startActivity(intent);
        }
        else if(checkbox.equals("false"))
        {
            Toast.makeText(this, "LogIn", Toast.LENGTH_SHORT).show();
        }
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        TextView t2 = (TextView) findViewById(R.id.Signup);

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

        button=findViewById(R.id.button);
        emailTxt=findViewById(R.id.emailEdtTxt);
        passwordTxt=findViewById(R.id.password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = emailTxt.getText().toString();
                String passwordStr = passwordTxt.getText().toString();
                boolean isEmpty = false;
                if(emailStr.isEmpty()){
                    emailTxt.setError("Email field can't be empty");
                    isEmpty = true;
                }
                if(passwordStr.isEmpty()){
                    passwordTxt.setError("Password field can't be empty");
                    isEmpty = true;
                }
                if(!isEmpty) {
                    validateUserLogin(emailStr, passwordStr);
                }
            }
        });

        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                    Toast.makeText(Login.this, "Checked", Toast.LENGTH_SHORT).show();
                } else {

                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Toast.makeText(Login.this, "Unchecked", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    void validateUserLogin(String userEmail, String userPassword) {
        (findViewById(R.id.pbar1)).setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    String uid = user.getUid();
                    Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Login.this, Drawer.class);
                    SharedPreferences preferences = getSharedPreferences("UID", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("uid", uid);
                    editor.apply();
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Login.this, "User login failed!", Toast.LENGTH_LONG).show();
                }
                (findViewById(R.id.pbar1)).setVisibility(View.INVISIBLE);
            }
        });
    }
}
