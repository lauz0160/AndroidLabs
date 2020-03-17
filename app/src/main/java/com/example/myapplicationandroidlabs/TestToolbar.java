package com.example.myapplicationandroidlabs;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class TestToolbar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        //Sets the toolbar for the activity with the in-activity icons
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //sets the drawers layout of this activity
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Sets the navigation pane to contain the icons to launch other activities and the help icon
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option1:
                Toast.makeText(this, "You clicked on item 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.option2:
                Toast.makeText(this, "You clicked on item 2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.option3:
                Toast.makeText(this, "You clicked on item 3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.option4:
                Toast.makeText(this, "You clicked on the overflow item", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.goToChat:
                startActivity(new Intent(TestToolbar.this, ChatRoomActivity.class));
                break;
            case R.id.goToWeather:
                startActivity(new Intent(TestToolbar.this, WeatherForecast.class));
                break;
            case R.id.backToLogin:
                setResult(500);
                finish();
                break;
        }

        return false;
    }
}
