package com.example.apicallerv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class LoggedInActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Button logout, weatherButton, otherAPIBtn;
    private TextView otherAPIbtnTV;

    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        logout = (Button) findViewById(R.id.logOutButton);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LoggedInActivity.this, MainActivity.class));
            }
        });
        String URL = "https://onlinedatabase2-93f0a-default-rtdb.europe-west1.firebasedatabase.app/";
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance(URL).getReference("User");
        userID = user.getUid();

        final TextView lTV = (TextView) findViewById(R.id.loggedInTV);
        final TextView wTV = (TextView) findViewById(R.id.welcomeTV);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userL = snapshot.getValue(User.class);
                Log.i("testingsomeshit", "this code got executeddddd");
                if(userL != null){
                    String email = userL.email;
                    String name = userL.name;

                    lTV.setText("Logged in as: "+ email);
                    wTV.setText("Welcome: " + name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("testingsomeshit", "this code got executed");
            }
        });

        weatherButton = (Button) findViewById(R.id.weatherBtn);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoggedInActivity.this, WeatherActivity.class));
            }
        });

        otherAPIBtn = (Button) findViewById(R.id.otherAPIBtn);
        otherAPIbtnTV = (TextView) findViewById(R.id.workInProgress);
        otherAPIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherAPIbtnTV.setText("More features comming soon, stay tuned");
            }
        });


    }
}