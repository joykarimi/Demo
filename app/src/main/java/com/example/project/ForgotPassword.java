package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {


    Button resetPasswordBtn;
    EditText emailEdtTxt;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_forgot_password);
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);
        emailEdtTxt = findViewById(R.id.emailEdtTxt);
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = emailEdtTxt.getText().toString();
                if(emailStr.isEmpty()){
                    emailEdtTxt.setError("Email field can't be empty");
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(emailEdtTxt.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Password reset link is sent", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
