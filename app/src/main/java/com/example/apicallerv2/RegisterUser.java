package com.example.apicallerv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URL;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private EditText enterNameField, enterEmailField, enterPasswordField;
    private Button regBtn;

    private FirebaseAuth mAuth;


    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        mAuth = FirebaseAuth.getInstance();

        regBtn = (Button) findViewById(R.id.registerButton);
        regBtn.setOnClickListener(this);
        enterNameField = (EditText) findViewById(R.id.enterName);
        enterEmailField = (EditText) findViewById(R.id.enterEmail);
        enterPasswordField = (EditText) findViewById(R.id.enterPassword);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.registerButton:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        String email = enterEmailField.getText().toString();
        String name = enterNameField.getText().toString();
        String password = enterPasswordField.getText().toString();

        if(name.isEmpty()){
            enterNameField.setError("Name is required");
            enterNameField.requestFocus();
            return;
        }
        if(email.isEmpty()){
            enterEmailField.setError("Email is required");
            enterEmailField.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            enterEmailField.setError("Email not valid");
            enterEmailField.requestFocus();
            return;
        }
        if(password.isEmpty()){
            enterPasswordField.setError("Password is required");
            enterPasswordField.requestFocus();
            return;
        }
        if(password.length() < 6){
            enterPasswordField.setError("minimum length is 6 characters");
            enterPasswordField.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Log.i("testingregistration", "inside onComplete");
                    //Toast.makeText(RegisterUser.this,"inside onComplete", Toast.LENGTH_LONG).show();
                    User user = new User(name,email);
                    String URL = "https://onlinedatabase2-93f0a-default-rtdb.europe-west1.firebasedatabase.app/";
                    FirebaseDatabase.getInstance(URL).getReference("User")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this, "User Registered Successfully", Toast.LENGTH_LONG).show();
                                        //Log.i("testingregistration", "registration successfull");
                                        startActivity(new Intent(RegisterUser.this, MainActivity.class));
                                    }else{
                                        Toast.makeText(RegisterUser.this,"Failed to register",Toast.LENGTH_LONG).show();
                                        //Log.i("testingregistration", "registration failed");
                                    }
                                }
                            });
                }else{
                    Toast.makeText(RegisterUser.this,"Failed to register user",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}