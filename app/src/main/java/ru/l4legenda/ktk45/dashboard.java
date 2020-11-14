package ru.l4legenda.ktk45;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.l4legenda.ktk45.diary;
import ru.l4legenda.ktk45.setting.cloud;

public class dashboard extends Activity {

    TextView Surname;
    TextView Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Surname = (TextView) findViewById(R.id.textViewSurname);
        Name = (TextView) findViewById(R.id.textViewName);

        Surname.setText(cloud.Surname);
        Name.setText(cloud.Name + " " + cloud.Patronymic);

        LinearLayout LLTimetable = (LinearLayout) findViewById(R.id.LinerLayoutTimetable);
        LLTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextScreen = new Intent(dashboard.this, timetable.class);
                startActivity(nextScreen);
            }
        });

        LinearLayout LLDiray = (LinearLayout) findViewById(R.id.LinerLayoutDiary);

        LLDiray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(dashboard.this, diary.class);
                startActivity(nextScreen);
            }
        });

    }
}