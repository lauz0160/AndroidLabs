package com.example.myapplicationandroidlabs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(btn -> Toast.makeText(getApplicationContext(), getString(R.string.ToastMessage) , Toast.LENGTH_LONG).show());



        CheckBox checkBox1 = findViewById(R.id.checkBox);
        checkBox1.setOnCheckedChangeListener((cb, isChecked)-> {
            Snackbar sBar = Snackbar.make(checkBox1,"",Snackbar.LENGTH_LONG);
            if (checkBox1.isChecked()){
                sBar.setText( getString(R.string.SnackMessageChecked));
            }
            else {
                sBar.setText( getString(R.string.SnackMessageUnChecked));
            }
            sBar.setAction("Undo", click -> cb.setChecked(!isChecked));
            sBar.show();
        });


        Switch switch1 = findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener((cb, isChecked)-> {
            Snackbar sBar = Snackbar.make(switch1,"",Snackbar.LENGTH_LONG);
            if (switch1.isChecked()){
                sBar.setText( getString(R.string.SnackMessageOn));
            }
            else {
                sBar.setText( getString(R.string.SnackMessageOff));
            }
            sBar.setAction("Undo", click -> cb.setChecked(!isChecked));
            sBar.show();
        });
    }
}
