package com.example.myapplicationandroidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor edit;
    EditText FirstEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab3_main);

        prefs  = getSharedPreferences("file", Context.MODE_PRIVATE);
        edit = prefs.edit();

        FirstEmail = findViewById(R.id.EditEnterEmail);
        FirstEmail.setText(prefs.getString("email",""));

        Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);


        Button login = findViewById(R.id.ButtonLogin);
        login.setOnClickListener(btn -> {
            goToProfile.putExtra("email", getEmailContent());
            startActivity(goToProfile);
        });
    }

    private String getEmailContent(){
        String text = FirstEmail.getText().toString();
        return text;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        edit.putString("email",FirstEmail.getText().toString());
        edit.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
