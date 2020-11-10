package ru.l4legenda.ktk45;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ru.l4legenda.ktk45.setting.cloud;

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
        String Token = myPreferences.getString("Token", "unknown");
        String Branch = myPreferences.getString("Branch", "unknown");
        String Group = myPreferences.getString("Group", "unknown");

        Log.d("FileSaveLogin", Login);
        Log.d("FileSavePassword", Password);
        Log.d("FileSaveToken", Token);
        Log.d("FileSaveBranch", Branch);
        Log.d("FileSaveGroup", Group);

        cloud.Login = Login;
        cloud.Password = Password;
        cloud.Token = Token;
        cloud.Branch = Branch;
        cloud.Group = Group;

        if(!Login.equals("unknown")  && !Password.equals("unknown") && !Token.equals("unknown") && !Branch.equals("unknown") && !Group.equals("unknown")){
            Intent nextScreen = new Intent(MainActivity.this, dashboard.class);
            startActivity(nextScreen);
        }

        btn_main_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextScreen = new Intent(MainActivity.this, Login.class);
                startActivity(nextScreen);
            }
        });

    }

    public int getPx(int dp){
        float scale = getResources().getDisplayMetrics().density;
        return((int) (dp * scale + 0.5f));
    }
}