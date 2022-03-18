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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp2 extends AppCompatActivity {

    EditText houseAddress, street, phase, area, city, province;
    Button signUpBtn;
    private FirebaseAuth mAuth;
    DatabaseReference usersDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        usersDB = FirebaseDatabase.getInstance().getReference("Users");

        houseAddress = findViewById(R.id.houseAddress);
        street = findViewById(R.id.streetAddress);
        phase = findViewById(R.id.phase);
        area = findViewById(R.id.area);
        city = findViewById(R.id.city);
        province = findViewById(R.id.province);
        signUpBtn = findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEmpty = false;
                if(houseAddress.getText().toString().isEmpty()){
                    houseAddress.setError("This field can't be empty");
                    isEmpty = true;
                }
                if(street.getText().toString().isEmpty()){
                    street.setError("This field can't be empty");
                    isEmpty = true;
                }
                if(phase.getText().toString().isEmpty()){
                    phase.setError("This field can't be empty");
                    isEmpty = true;
                }
                if(area.getText().toString().isEmpty()){
                    area.setError("This field can't be empty");
                    isEmpty = true;
                }
                if(city.getText().toString().isEmpty()){
                    city.setError("This field can't be empty");
                    isEmpty = true;
                }
                if(province.getText().toString().isEmpty()){
                    province.setError("This field can't be empty");
                    isEmpty = true;
                }
                if(!isEmpty) {
                    UserModel user = (UserModel) getIntent().getSerializableExtra("user");
                    user.setHouse(houseAddress.getText().toString());
                    user.setStreet(street.getText().toString());
                    user.setSector(phase.getText().toString());
                    user.setArea(area.getText().toString());
                    user.setCity(city.getText().toString());
                    user.setProvince(province.getText().toString());
                    registerUser(user);
                }
            }
        });
    }

    void registerUser(final UserModel user){
        (findViewById(R.id.pbar1)).setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser fUser = mAuth.getCurrentUser();
                    String uid = fUser.getUid();
                    usersDB.child(uid).setValue(user);
                    Toast.makeText(getApplicationContext(), "Account created successfully!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Failed to create account!", Toast.LENGTH_LONG).show();
                }
                (findViewById(R.id.pbar1)).setVisibility(View.INVISIBLE);
            }
        });
    }
}
