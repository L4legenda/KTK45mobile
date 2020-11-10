package ru.l4legenda.ktk45;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.l4legenda.ktk45.setting.cloud;
import ru.l4legenda.ktk45.setting.data;

public class diary extends Activity {

    LinearLayout layoutDay;
    LinearLayout layoutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        layoutContent = (LinearLayout) findViewById(R.id.layoutContent);
        pushTimetable();
    }



    private void pushTimetable(){

        String[] date = data.getDate();

        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("DateFirst", date[0]);
            jsonBody.put("DateLast", date[1]);
            jsonBody.put("Group", cloud.Group);

            Log.d("ParseDirayDate", date[0]);
            Log.d("ParseDirayDate", date[1]);
            Log.d("ParseDirayGroup", cloud.Group);

            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, "https://ktk-45.ru/api/journal/select-group-by-date", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.d("ParseDiray", response.toString());

                    try {
                        if(response.get("Error").equals(null)){
                            Log.d("ParseDiray", "Нормальное данные");
                            JSONArray dataArray = response.getJSONArray("Data");

                            int dayCounter = 0;

                            for(int i = 0; i < dataArray.length(); i++){
                                JSONObject objectArray = (JSONObject) dataArray.get(i);
                                Log.d("Object", objectArray.toString());

                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(objectArray.getString("Date"));

                                if(dayCounter != date.getDay()){
                                    dayCounter = date.getDay();
                                    createDay(date.getDay());
                                }
                                String predmet = objectArray.getString("DisciplineFull");
                                Integer ocenkaNum = objectArray.getInt("Mark");
                                String ocenka = "";

                                if(( ocenkaNum & 7) != 0 ){
                                    int pn = ocenkaNum & 7;
                                    ocenka = String.valueOf(pn);
                                }else if( ( ocenkaNum & 32760) != 0 ){
                                    int pn = ocenkaNum & 32760;
                                    if(pn == 16){
                                        ocenka = "Н";
                                    }else if(pn == 32) {
                                        ocenka = "О";
                                    }else if(pn == 64){
                                        ocenka = "Б";
                                    }else if(pn == 128){
                                        ocenka = "+";
                                    }
                                }

                                addPredmet(predmet, ocenka);
                                Log.d("Date", String.valueOf(date.getDay()));
                            }
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


    private void createDay(int day){
        Log.d("createDay", String.valueOf(data.getTitleDay(day)));
        LinearLayout.LayoutParams lp;

        layoutDay = new LinearLayout(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(0,40,0,0);
        layoutDay.setBackgroundColor( getResources().getColor(R.color.colorWhite) );
        layoutDay.setOrientation(LinearLayout.VERTICAL);
        layoutDay.setLayoutParams(lp);
        layoutDay.setPadding(0,0,0,10);

        TextView title = new TextView(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(0,0,0,10);
        title.setText( data.getTitleDay(day) );
        title.setBackgroundColor( getResources().getColor(R.color.colorRed) );
        title.setTextColor( getResources().getColor(R.color.colorWhite) );
        title.setTextSize(18);
        title.setPadding(0, getPx(10), 0, getPx(10));
        title.setGravity(Gravity.CENTER);
        title.setLayoutParams(lp);

        layoutDay.addView(title);

        layoutContent.addView(layoutDay);
    }

    private void addPredmet(String predmet, String otmetka){
        LinearLayout.LayoutParams lp;
        LinearLayout llContent = new LinearLayout(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getPx(50));
        lp.setMargins(10, 0,10,5);
        llContent.setLayoutParams(lp);
        llContent.setGravity(Gravity.CENTER);
        llContent.setOrientation(LinearLayout.HORIZONTAL);


        TextView name = new TextView(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        lp.setMargins(5, 5, 5, 0);
        name.setLayoutParams(lp);
        name.setBackgroundColor( getResources().getColor(R.color.colorGray) );
        name.setGravity(Gravity.CENTER_VERTICAL);
        name.setPadding(10, 0, 0, 0);
        name.setText(predmet);
        name.setTextColor( getResources().getColor(R.color.colorBlack) );
        name.setTextSize(18);

        TextView ocenka = new TextView(this);
        lp = new LinearLayout.LayoutParams(getPx(80), LinearLayout.LayoutParams.MATCH_PARENT, 1);
        lp.setMargins(0, 5, 5, 0);
        ocenka.setLayoutParams(lp);
        ocenka.setBackgroundColor( getResources().getColor(R.color.colorDarkGray) );
        ocenka.setGravity(Gravity.CENTER);
        ocenka.setText(otmetka);
        ocenka.setTextColor( getResources().getColor(R.color.colorWhite) );
        ocenka.setTextSize(20);



        llContent.addView(name);
        llContent.addView(ocenka);

        layoutDay.addView(llContent);

    }
    public int getPx(int dp){
        float scale = getResources().getDisplayMetrics().density;
        return((int) (dp * scale + 0.5f));
    }
}