package ru.l4legenda.ktk45;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.l4legenda.ktk45.api.User;
import ru.l4legenda.ktk45.api.UserApi;

public class Login extends Activity {

    private String Login = "";
    private String Password = "";
    private String Token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Кнопка авторизации
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView login = (TextView) findViewById(R.id.editTextTextPersonName);
                TextView password = (TextView) findViewById(R.id.editTextTextPassword);

                Login = login.getText().toString();
                Password = password.getText().toString();

                parseLogin();
            }
        });

        //Кнопка входя без учетной записи
        TextView textNotLogin = (TextView) findViewById(R.id.textView_notAuth);
        textNotLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextScreen();
            }
        });

    }

    private void nextScreen(){
        Intent nextScreen = new Intent(Login.this, dashboard.class);
        startActivity(nextScreen);
    }


    private void parseLogin(){
        //Отправка запроса на сервер
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("Login", Login);
            jsonBody.put("Password", Password);
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, "https://ktk-45.ru/login", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {

                        if(response.get("Error").equals(null)){
                            Log.d("Parse", "Вход разрешён");

                            Token = response.get("Session").toString();

                            SharedPreferences myPreferences
                                    = PreferenceManager.getDefaultSharedPreferences(Login.this);
                            SharedPreferences.Editor myEditor = myPreferences.edit();

                            myEditor.putString("Login", Login);
                            myEditor.putString("Password", Password);
                            myEditor.putString("Token", response.get("Session").toString());
                            myEditor.commit();

                            parseInfoUser();

                            nextScreen();
                        }else{
                            Log.d("Parse", "Вход не разрешён");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.Response", error.toString());
                }
            }
            ){
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody(){
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

            };
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void parseInfoUser(){
        //Отправка запроса на сервер
        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, "https://ktk-45.ru/api/user/select-info", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {

                        if(response.get("Error").equals(null)){
                            Log.d("ParseUser", "Нормальное данные");
                            Log.d("ParseUser", response.toString());

                        }else{
                            Log.d("ParseUser", "Неправильный Token данные");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.Response", error.toString());
                }
            }
            ){
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8; Authorization=" + Token;
                }


            };
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}