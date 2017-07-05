package com.example.deviceinformation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


/**
 * Created by kannas on 6/30/2017.
 */

public class AppMonitorSdk extends Activity{
    private static final String APPLICATION_ID_PROPERTY = "com.example.kannas.testproject.AppMonitorSdk.applicationId";
    Context mContext;
    public static  String applicationId;
    public Userinformation userinformation;
    private static final int REQUEST_CAMERA = 0;
    private  Activity mActivity;
  /*  public AppMonitorSdk(Context context){
        mActivity=(Activity)context;
        this.mContext=context;


    }*/
    public AppMonitorSdk(){

    }
    public static void sdkInitialize(Context applicationContext) {
           AppMonitorSdk appMonitorSdk=new AppMonitorSdk();
           appMonitorSdk.getProperty(applicationContext);
    }
    private void getProperty(Context context){
        mActivity=(Activity)context;
        this.mContext=context;
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(mActivity));
        ApplicationInfo ai=null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle=ai.metaData;
            if(bundle==null){
                throw new MonitorLiveException("A valid Facebook app id must be set in the " +
                        "AndroidManifest.xml or set by calling FacebookSdk.setApplicationId " +
                        "before initializing the sdk.");
            }else {
                Log.d("AppMonitorSdk","AppMonitorSdk");
                applicationId=bundle.getString(APPLICATION_ID_PROPERTY);
                userinformation=new Userinformation(context);
               // userinformation.locationSet(context);
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                    userinformation.locationSet(context);
                } else {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED || (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)) {
                        // Camera permission has not been granted.
                        requestPermission();
                    } else {
                        userinformation.locationSet(context);
                    }}

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
    public static boolean isNullOrEmpty(String s) {
        return (s == null) || (s.length() == 0);
    }
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(mActivity,Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CAMERA);
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CAMERA);
        }
        // userinformation.locationSet(this);
        //userinformation.locationSet();
        // END_INCLUDE(camera_permission_request)
    }


}
