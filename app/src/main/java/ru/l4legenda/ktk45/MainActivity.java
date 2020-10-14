package ru.l4legenda.ktk45;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_main_login = (Button) findViewById(R.id.btn_mainLogin);

        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String Login = myPreferences.getString("Login", "unknown");
        String Password = myPreferences.getString("Password", "unknown");

//        if(Login != null && Password != null){
//            Intent nextScreen = new Intent(MainActivity.this, dashboard.class);
//            startActivity(nextScreen);
//        }

        btn_main_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextScreen = new Intent(MainActivity.this, Login.class);
                startActivity(nextScreen);
            }
        });

    }
}