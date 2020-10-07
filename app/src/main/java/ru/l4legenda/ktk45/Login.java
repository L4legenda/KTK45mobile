package ru.l4legenda.ktk45;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        View.OnClickListener nextDashboard = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextScreen = new Intent(Login.this, dashboard.class);
                startActivity(nextScreen);

            }
        };
        // Кнопка авторизации
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(nextDashboard);

        //Кнопка входя без учетной записи
        TextView textNotLogin = (TextView) findViewById(R.id.textView_notAuth);
        textNotLogin.setOnClickListener(nextDashboard);


    }
}