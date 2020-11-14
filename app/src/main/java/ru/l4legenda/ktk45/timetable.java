package ru.l4legenda.ktk45;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
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
import ru.l4legenda.ktk45.setting.data;

public class timetable extends Activity {
    LinearLayout layoutContent;
    LinearLayout layoutDay;

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

        layoutContent = (LinearLayout) findViewById(R.id.LayoutContent);

        pushTimetable();
    }


    private void pushTimetable(){

        String[] date = data.getDate();

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
                            Log.d("ParseShedule", "Нормальное данные");
                            Log.d("ParseShedule", response.toString());
                            JSONArray dataArray = response.getJSONArray("Data");
                            Log.d("Length", String.valueOf(dataArray.length()));

                            int dayCounter = 0;
                            //Список пар
                            HashMap<Integer, LinearLayout> llClassList = new HashMap<Integer, LinearLayout>();

                            for(int i = 0; i < dataArray.length(); i++){
                                JSONObject objectArray = (JSONObject) dataArray.get(i);
                                Log.d("Object", objectArray.toString());

                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(objectArray.getString("Date"));
                                Log.d("Date", String.valueOf(date.getDay()));


                                if(dayCounter != date.getDay()){
                                    if(dayCounter > 0){
                                        for(int j : llClassList.keySet())
                                            layoutDay.addView(llClassList.get(j));
                                    }
                                    llClassList = new HashMap<Integer, LinearLayout>();
                                    dayCounter = date.getDay();
                                    createDay(date.getDay());


                                }
                                int id = objectArray.getInt("Pair");

                                if(!llClassList.containsKey(id)){
                                    llClassList.put(id, createllClass(id));
                                }

                                llClassList.get(id).addView(
                                        createClass(
                                                objectArray.getInt("Pair"),
                                                objectArray.getString("DisciplineFull"),
                                                objectArray.getString("Discipline"),
                                                objectArray.getString("LectureHall"),
                                                objectArray.getString("Teacher"),
                                                objectArray.getString("Subgroup")
                                        )
                                );
                            }
                            for(int j : llClassList.keySet())
                                layoutDay.addView(llClassList.get(j));
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



    private void createDay(int day){
        Log.d("createDay", String.valueOf(data.getTitleDay(day)));
        LinearLayout.LayoutParams lp;

        layoutDay = new LinearLayout(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(30,30,30,0);
        layoutDay.setOrientation(LinearLayout.VERTICAL);
        layoutDay.setLayoutParams(lp);

        TextView title = new TextView(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        title.setText( data.getTitleDay(day) );
        title.setBackgroundColor( getResources().getColor(R.color.colorRed) );
        title.setTextColor( getResources().getColor(R.color.colorWhite) );
        title.setTextSize(20);
        title.setPadding(0, 5, 0, 10);
        title.setGravity(Gravity.CENTER);
        title.setLayoutParams(lp);

        layoutDay.addView(title);

        layoutContent.addView(layoutDay);
    }

    private LinearLayout createClass(int n, String DisciplineFull, String Discipline, String numKab, String TeacherFull, String type){

        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        Typeface italicTypeface = Typeface.defaultFromStyle(Typeface.ITALIC);

        LinearLayout.LayoutParams lp;
        float weight;
        String disciplin = DisciplineFull;
        String pedagog = TeacherFull;

        String[] TeacherArr = pedagog.split(" ");
        String Teacher = TeacherArr[0] + " " + TeacherArr[1].substring(0, 1) + ". " + TeacherArr[2].substring(0, 1) + ".";

        if(type.equals("middle")){
            weight = 2f;
        }else{
            disciplin = Discipline;
            weight = 1f;
            pedagog = Teacher;
        }


        LinearLayout llContent = new LinearLayout(this);
        if(type.equals("middle")){
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, weight);
        }else{
            lp = new LinearLayout.LayoutParams(getPx(160), LinearLayout.LayoutParams.WRAP_CONTENT, weight);
        }

        if(type.equals("left") && n == 1){
            lp.setMargins(5,5,0,5);
        }
        else if(n == 1){
            lp.setMargins(5,5,5,5);
        }
        else if(type.equals("left")){
            lp.setMargins(5,0,0,5);
        }else{
            lp.setMargins(5,0,5,5);
        }
        llContent.setLayoutParams(lp);
        llContent.setBackgroundColor( getResources().getColor(R.color.colorGray)  );
        llContent.setOrientation(LinearLayout.VERTICAL);
        llContent.setPadding(20,5,20,5);
        if(type.equals("right")){
            llContent.setGravity(Gravity.RIGHT);
        }else if(type.equals("left")){
            llContent.setGravity(Gravity.LEFT);
        }



        LinearLayout llTitle = new LinearLayout(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llTitle.setOrientation(LinearLayout.HORIZONTAL);
        llTitle.setLayoutParams(lp);

        TextView namePredmeta = new TextView(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        namePredmeta.setLayoutParams(lp);
        namePredmeta.setTypeface(boldTypeface);
        namePredmeta.setText(disciplin);
        namePredmeta.setTextColor( getResources().getColor(R.color.colorBlack) );


        llTitle.addView(namePredmeta);

        TextView numKabineta = new TextView(this);
        lp = new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        numKabineta.setLayoutParams(lp);
        numKabineta.setTypeface(boldTypeface);
        numKabineta.setText(numKab);
        numKabineta.setGravity(Gravity.RIGHT);
        numKabineta.setTextColor( getResources().getColor(R.color.colorBlack) );

        llTitle.addView(numKabineta);


        llContent.addView( llTitle );

        TextView namePedagog = new TextView(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        namePedagog.setLayoutParams(lp);
        namePedagog.setTypeface(italicTypeface);
        namePedagog.setText(pedagog);
        namePedagog.setTextSize(12);
        namePedagog.setTextColor( getResources().getColor(R.color.colorBlack) );


        llContent.addView(namePedagog);


        return llContent;




    }

    private LinearLayout createllClass(int n){
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);

        LinearLayout.LayoutParams lp;
        LinearLayout llClass = new LinearLayout(this);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llClass.setOrientation(LinearLayout.HORIZONTAL);
        llClass.setLayoutParams(lp);
        llClass.setBackgroundColor( getResources().getColor(R.color.colorWhite) );

        TextView num = new TextView(this);
        lp = new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        num.setText(String.valueOf(n));
        num.setGravity(Gravity.CENTER);
        num.setTextColor( getResources().getColor(R.color.colorBlack) );
        num.setTextSize(20);
        num.setTypeface(boldTypeface);
        num.setLayoutParams(lp);
        llClass.addView(num);

        return llClass;
    }



    public int getPx(int dp){
        float scale = getResources().getDisplayMetrics().density;
        return((int) (dp * scale + 0.5f));
    }
}






















