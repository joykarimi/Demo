package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    Button button;
    EditText name, pno, email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView t2 = (TextView) findViewById(R.id.textView5);

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });


        button = findViewById(R.id.continueBtn);
        name = findViewById(R.id.name);
        pno = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);
        password =  findViewById(R.id.password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEmpty = false;
                if(name.getText().toString().isEmpty()){
                    name.setError("This field can't be empty");
                    isEmpty = true;
                }
                if(pno.getText().toString().isEmpty() && pno.getText().toString().length() != 11){
                    pno.setError("This field can't be empty");
                    isEmpty = true;
                }
                if(email.getText().toString().isEmpty()){
                    email.setError("This field can't be empty");
                    isEmpty = true;
                }
                if(password.getText().toString().isEmpty() && password.getText().toString().length() <= 6){
                    password.setError("This field can't be empty");
                    isEmpty = true;
                }
                if(!isEmpty) {
                    UserModel user = new UserModel(name.getText().toString(), pno.getText().toString(), email.getText().toString(), password.getText().toString());
                    Intent intent = new Intent(SignUp.this, SignUp2.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }
        });
    }
}
