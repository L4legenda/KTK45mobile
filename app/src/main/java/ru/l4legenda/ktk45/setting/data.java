package ru.l4legenda.ktk45.setting;

import android.util.Log;

import java.util.Date;

public class data {

    static public String[] getDate(){
        Date date = new Date();
        int day = date.getDay();
        int firstDate = date.getDate() - (day - 1);
        int lastDate = date.getDate() + (6 - day);

        String Year = String.valueOf(date.getYear() + 1900);
        String Month = String.valueOf(date.getMonth() + 1);
        String firstDayStr = String.valueOf(firstDate);
        String lastDayStr = String.valueOf(lastDate);

        if(firstDate < 10) firstDayStr = "0" + firstDayStr;

        if(lastDate < 10) lastDayStr = "0" + lastDayStr;

        Log.d("FirstDay", firstDayStr );
        Log.d("lastDay",  lastDayStr   );

        Log.d("Date", Year + "-" + Month + "-" + firstDayStr);
        Log.d("Date", Year + "-" + Month + "-" + lastDayStr);

        return new String[] {
            Year + "-" + Month + "-" + firstDayStr,
            Year + "-" + Month + "-" + lastDayStr
        };
    }


    static public String getTitleDay(int day){
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
