package ru.l4legenda.ktk45;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.l4legenda.ktk45.setting.cloud;

public class timetable extends Activity {
    LinearLayout layoutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        Button btnSetting = (Button) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextScreen = new Intent(timetable.this, timetable_setting.class);
                startActivity(nextScreen);
            }
        });

        layoutContent = findViewById(R.id.LayoutContent);

        pushTimetable();
    }


    private void pushTimetable(){
        String[] date = getDate();
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("FirstDate", date[0]);
            jsonBody.put("LastDate", date[1]);
            jsonBody.put("Branch", cloud.Branch);
            jsonBody.put("Group", cloud.Group);
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, "https://ktk-45.ru/select/schedule-group", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {

                        if(response.get("Error").equals(null)){
                            Log.d("ParseUser", "Нормальное данные");
                            Log.d("ParseUser", response.toString());
                            JSONArray dataArray = response.getJSONArray("Data");
                            Log.d("Length", String.valueOf(dataArray.length()));

                            int dayCounter = 0;

                            for(int i = 0; i < dataArray.length(); i++){
                                JSONObject objectArray = (JSONObject) dataArray.get(i);
                                Log.d("Object", objectArray.toString());

                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(objectArray.getString("Date"));
                                Log.d("Date", String.valueOf(date.getDay()));


                                if(dayCounter != date.getDay()){
                                    dayCounter = date.getDay();
                                    createDay(date.getDay());

                                }
                            }

                        }else{
                            Log.d("ParseUser", "Неправильный данные");
                        }

                    } catch (Exception e) {
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
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Log.d("Token", cloud.Token);
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("Authorization", cloud.Token);

                    return params;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String[] getDate(){
        Date date = new Date();
        int day = date.getDay();
        int firstDate = date.getDate() - (day - 1);
        int lastDate = date.getDate() + (6 - day);

        Log.d("FirstDay", String.valueOf(firstDate));
        Log.d("lastDay", String.valueOf(lastDate));

        String Year = String.valueOf(date.getYear() + 1900);
        String Month = String.valueOf(date.getMonth() + 1);
        String firstDayStr = String.valueOf(firstDate);
        String lastDayStr = String.valueOf(lastDate);

        Log.d("Date", Year + "-" + Month + "-" + firstDayStr);
        Log.d("Date", Year + "-" + Month + "-" + lastDayStr);

        return new String[] {
                Year + "-" + Month + "-" + firstDayStr,
                Year + "-" + Month + "-" + lastDayStr
        };
    }



    private void createDay(int day){
        Log.d("createDay", String.valueOf(getTitleDay(day)));
        LinearLayout.LayoutParams lp;

        LinearLayout layoutDay = new LinearLayout(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(20,30,20,0);
        layoutDay.setOrientation(LinearLayout.VERTICAL);
        layoutDay.setLayoutParams(lp);

        TextView title = new TextView(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        title.setText( getTitleDay(day) );
        title.setBackgroundColor( getResources().getColor(R.color.colorRed) );
        title.setTextColor( getResources().getColor(R.color.colorWhite) );
        title.setTextSize(20);
        title.setPadding(0, 5, 0, 5);
        title.setGravity(Gravity.CENTER);
        title.setLayoutParams(lp);

        layoutDay.addView(title);

        layoutContent.addView(layoutDay);




    }

    private String getTitleDay(int day){
        switch (day){
            case 1: return "Понедельник";
            case 2: return "Вторник";
            case 3: return "Среда";
            case 4: return "Четверг";
            case 5: return "Пятница";
            case 6: return "Суббота";
            default: return "Ошибка";
        }
    }
}