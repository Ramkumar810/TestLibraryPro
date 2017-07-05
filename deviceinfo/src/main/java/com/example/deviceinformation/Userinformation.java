package com.example.deviceinformation;


import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;



public  class Userinformation extends Activity implements LocationListener {
    public static String applicationMemoryUsage;
    public static String applicationVersion;
    public static String deviceName;
    public static String deviceLanguage;
    public static String deviceCountry;
    public static String deviceCity;
    public static String deviceId;
    public static String deviceModel;
    public static String deviceOsVersion;
    public static String deviceVersionRelease;
    public static String packageName;
    public static String deviceDetailType;
    public static String deviceLatitude;
    public static String deviceLongitude;
    public static String crashReport="";
    public static String finalResult;
    public static boolean isCalled=false;
    String provider;
    Context mContext;
    public static JSONObject userSignParams;
    public static JSONObject jsonObject;
    LocationManager locationManager;
    Geocoder geoCoder;
    public Userinformation(Context context) {
        this.mContext = context;


    }
    public Userinformation() {

    }

    public void userIp() {

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            double percentAvail = mi.availMem / (double) mi.totalMem;
            applicationMemoryUsage = String.valueOf(percentAvail);
        }
        PackageManager manager = mContext.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(mContext.getPackageName(), 0);
            packageName = info.packageName;
            applicationVersion = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String androidDeviceId = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        deviceId = androidDeviceId;
        String manufacturer = android.os.Build.MANUFACTURER;

        deviceName = manufacturer;
        String model = Build.MODEL;
        deviceModel = model;
        int version = Build.VERSION.SDK_INT;
        deviceOsVersion = String.valueOf(version);
        String versionRelease = Build.VERSION.RELEASE;
        deviceVersionRelease = versionRelease;
        deviceLanguage = Locale.getDefault().getDisplayLanguage();

        try {
            userSignParams = new JSONObject();
            userSignParams.put("os","Android");
            userSignParams.put("osv", deviceVersionRelease);
            userSignParams.put("appId", packageName);
            userSignParams.put("dCmp", deviceName);
            userSignParams.put("dDetType", deviceModel);
            userSignParams.put("dLan",deviceLanguage);
            userSignParams.put("dCountry",Locale.getDefault().getCountry());
            userSignParams.put("dCity",deviceCity);
            userSignParams.put("dID",deviceId);
            userSignParams.put("appVer",applicationVersion);
            userSignParams.put("dType",deviceDetailType);
            userSignParams.put("dLat",deviceLatitude);
            userSignParams.put("dLong",deviceLongitude);
            userSignParams.put("gid",AppMonitorSdk.applicationId);
            userSignParams.put("crashdetail",crashReport);
            new PutCredential().execute();
        }catch (JSONException ex){
            ex.printStackTrace();
        }

    }
   /* public void deviceBattery() {
        int level = 0;
        int scale = 0;
        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = mContext.registerReceiver(null, intentfilter);
        if (batteryStatus != null) {
            deviceStatus = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            if (deviceStatus == BatteryManager.BATTERY_STATUS_CHARGING) {
                batteryState = "Charging";
            }
            if (deviceStatus == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                batteryState = " Discharging";
            }
            if (deviceStatus == BatteryManager.BATTERY_STATUS_FULL) {
                batteryState = "Battery Full";
            }
            if (deviceStatus == BatteryManager.BATTERY_STATUS_UNKNOWN) {
                batteryState = "Charging";
            }
            if (deviceStatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
                batteryState = "Not Charging";
            }

        }
        if (batteryStatus != null) {
            level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        }
        if (batteryStatus != null) {
            scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }
        int batteryLevel = (int) (((float) level / (float) scale) * 100.0f);
        batteryPercentage = String.valueOf(batteryLevel + " %");
        userIp();

    }*/

  /*  public String getDeviceInformation(String deviceId) {
        deviceId = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return deviceId;
    }*/
   /* public void getCrashReport(final Context context){
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                crashReport=paramThrowable.getLocalizedMessage();
                Intent i = context.getPackageManager().
                        getLaunchIntentForPackage(context.getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                //new PutCredential().execute();

            }
        });
    }*/
    public static class PutCredential extends AsyncTask<String, String, String> {
        String line;
        StringBuilder result1;

      @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            try {
                if(userSignParams !=null){
                    Log.d("usersignIn",userSignParams.toString()+"");
                }
                URL urlToRequest = new URL("http://45.32.172.37/mobileappmonitoring");
                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setFixedLengthStreamingMode(userSignParams.toString().getBytes().length);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(userSignParams);
                out.close();
                int responseCode=urlConnection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                  Log.d("true",responseCode+"");
                }
              /*  InputStream inStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(inStream));
                result1 = new StringBuilder();
                while ((line = reader1.readLine()) != null) {
                    result1.append(line);

                }*/
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
          return line;
        }


        @Override
        protected void onPostExecute(String s) {
            try {

                JSONObject object = new JSONObject(String.valueOf(result1));


                //Toast.makeText(mContext,String.valueOf(result1),Toast.LENGTH_SHORT).show();
               // handleLinkedInAccessToken(object.getString("id_token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public  void locationSet(Context context) {

        // Getting LocationManager object
        locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals("")) {

            // Get the location from the given provider
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location  location=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (mContext, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 1, this);

            if (location != null) {
                onLocationChanged(location);

            }
            userIp();
            isTabletOrPhone();
        } else {

        }

    }
    @Override
    public void onLocationChanged(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        deviceLatitude=  String.valueOf(location.getLongitude());
        deviceLongitude=String.valueOf(location.getLatitude());
        geoCoder = new Geocoder(mContext, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address =geoCoder.getFromLocation(latitude, longitude, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i=0; i<maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                String countryname=address.get(0).getCountryCode();
                deviceCountry=countryname;
                String cityName = address.get(0).getLocality();
                deviceCity=cityName;
                Log.d("cityName",cityName+"");
                builder.append(addressStr);
                builder.append(" ");
            }
            //userIp();

            String fnialAddress = builder.toString(); //This is the complete address.
        } catch (IOException e) {
            e.fillInStackTrace();

        }
        catch (NullPointerException e) {
            e.fillInStackTrace();

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    public  void isTabletOrPhone(){

        boolean tabletSize = mContext.getApplicationContext().getResources().getBoolean(R.bool.isTablet);
        if(tabletSize){
            deviceDetailType="tablet";
        }else {
            deviceDetailType="mobile";
        }
        getMethod();
    }
    public void getClassName(Context context){
        String className=context.getClass().getSimpleName();
        Log.d("className",className+"");
    }
public void getMethod(){
    Timer timer = new Timer ();
    TimerTask hourlyTask = new TimerTask () {
        @Override
        public void run () {
          jsonObject=new JSONObject();
            try {
                jsonObject.put("gid",AppMonitorSdk.applicationId);
                jsonObject.put("dCountry",Locale.getDefault().getCountry());
                jsonObject.put("dID",deviceId);
                 new PutJson().execute();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // your code here...
        }
    };

// schedule the task to run starting now and then every hour...
    timer.schedule (hourlyTask, 0, 3*60*1000);
}
    public static class PutJson extends AsyncTask<String, String, String> {
        String line;
        StringBuilder result1;

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            try {
                URL urlToRequest = new URL("http://45.32.172.37/liveappmonitoring");
                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setFixedLengthStreamingMode(jsonObject.toString().getBytes().length);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(jsonObject);
                out.close();
                int responseCode=urlConnection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.d("true",responseCode+"");
                }
                InputStream inStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(inStream));
                result1 = new StringBuilder();
                while ((line = reader1.readLine()) != null) {
                    result1.append(line);

                }
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return line;
        }


        @Override
        protected void onPostExecute(String s) {
            try {
               // isCalled=true;
                //finalResult=String.valueOf(result1);
                JSONObject object = new JSONObject(String.valueOf(result1));


                //Toast.makeText(mContext,String.valueOf(result1),Toast.LENGTH_SHORT).show();
                // handleLinkedInAccessToken(object.getString("id_token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}

