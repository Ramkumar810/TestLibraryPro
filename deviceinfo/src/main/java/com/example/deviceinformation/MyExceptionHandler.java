package com.example.deviceinformation;

/**
 * Created by kannas on 6/24/2017.
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.json.JSONException;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.example.deviceinformation.Userinformation.packageName;
import static com.example.deviceinformation.Userinformation.userSignParams;

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Activity activity;
    public MyExceptionHandler(Activity a) {
        activity = a;
    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        StringWriter stackTrace = new StringWriter();
        ex.printStackTrace(new PrintWriter(stackTrace));
        System.err.println(stackTrace);
        Intent intent = new Intent(activity, activity.getClass());
        try {
            userSignParams.put("crashdetail",stackTrace);
            userSignParams.put("crashname",ex.getClass().getSimpleName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.putExtra("crash", true);
        intent.putExtra("crashname",ex.getClass().getSimpleName());
        intent.putExtra("crashDetail", String.valueOf(stackTrace));
        intent.putExtra("detailInfo",userSignParams.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(testproject.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) testproject.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        activity.finish();
        System.exit(2);
    }

}
