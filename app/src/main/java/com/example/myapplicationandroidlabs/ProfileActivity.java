package com.example.myapplicationandroidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {

    ImageButton mImageButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String ACTIVITY_NAME = "Profile Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        EditText SecondEmail = findViewById(R.id.EditEmail);

        mImageButton=findViewById(R.id.ButtonCamera);
        mImageButton.setOnClickListener(click -> dispatchTakePictureIntent());

        Intent fromMain = getIntent();

        SecondEmail.setText( fromMain.getStringExtra("email") );
        Log.e(ACTIVITY_NAME, "In Function onCreate");


        Button chat = findViewById(R.id.ButtonToChat);
        chat.setOnClickListener(click -> startActivity(new Intent(ProfileActivity.this, ChatRoomActivity.class)));

        Button weather = findViewById(R.id.ButtonToWeather);
        weather.setOnClickListener(click -> startActivity(new Intent(ProfileActivity.this, WeatherForecast.class)));

        Button btnToolbar = findViewById(R.id.ButtonToToolbar);
        btnToolbar.setOnClickListener(click -> {
            startActivityForResult(new Intent(ProfileActivity.this, TestToolbar.class),500);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In Function onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In Function onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In Function onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In Function onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In Function onDestroy");
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
       if(resultCode==500){
           finish();
        }
    }

}
