package ru.l4legenda.ktk45;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class timetable_setting extends Activity {

    String[] BranchData = {"Автоматизация и вычислительная техника", "Автосервис", "Технология и дизайн", "Шатровский филиал"};
    String[] GroupData = {"101", "102", "103"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_setting);

        ArrayAdapter<String> BranchAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, BranchData);
        BranchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerBranch = (Spinner) findViewById(R.id.spinnerBranch);
        spinnerBranch.setAdapter(BranchAdapter);
        // заголовок
        spinnerBranch.setPrompt("Отделения");
        // выделяем элемент
        spinnerBranch.setSelection(0);
        // устанавливаем обработчик нажатия
        spinnerBranch.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });




        ArrayAdapter<String> GroupAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GroupData);
        BranchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerGroup = (Spinner) findViewById(R.id.spinnerGroup);
        spinnerGroup.setAdapter(GroupAdapter);
        // заголовок
        spinnerGroup.setPrompt("Группы");
        // выделяем элемент
        spinnerGroup.setSelection(0);
        // устанавливаем обработчик нажатия
        spinnerGroup.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
}