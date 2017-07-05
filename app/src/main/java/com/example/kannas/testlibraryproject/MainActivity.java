package com.example.kannas.testlibraryproject;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.deviceinformation.AppMonitorSdk;
import com.example.deviceinformation.Userinformation;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.deviceinformation.Userinformation.deviceDetailType;
import static com.example.deviceinformation.Userinformation.deviceLatitude;
import static com.example.deviceinformation.Userinformation.deviceLongitude;
import static com.example.deviceinformation.Userinformation.packageName;
import static com.example.deviceinformation.Userinformation.userSignParams;

public class MainActivity extends AppCompatActivity {
    TextView mTextview;
    String crashName="";
    String crashFullLog="";
    Button button;
    String provider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AppMonitorSdk.sdkInitialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String apiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY;
        Log.d("apiKey", apiKey + "");
        ApplicationInfo ai = null;
        try {
            ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle=ai.metaData;
            String apikey1=bundle.getString("com.example.kannas.testproject.AppMonitorSdk");
            Log.d("apikey1", apikey1 + "");

        } catch (PackageManager.NameNotFoundException e){

        }

        setSupportActionBar(toolbar);
        mTextview = (TextView) findViewById(R.id.device_txt);
        button=(Button) findViewById(R.id.buttonPanel);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        if (getIntent().getBooleanExtra("crash", false)) {
            String string=getIntent().getStringExtra("detailInfo");
            crashName=getIntent().getStringExtra("crashname");
            crashFullLog=getIntent().getStringExtra("crashDetail");
            try {
                JSONObject jsonObject=new JSONObject(string);
                userSignParams=jsonObject;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Log.d("String",string+"");
            //Toast.makeText(this,string,Toast.LENGTH_LONG).show();
            new Userinformation.PutCredential().execute();
            //Toast.makeText(this, "App restarted after crash", Toast.LENGTH_SHORT).show();
        }
        String applicationVersion = Userinformation.applicationVersion;
        String memory = Userinformation.applicationMemoryUsage;
        String deviceName = Userinformation.deviceName;
        String deviceLanguage = Userinformation.deviceLanguage;
        String deviceCountry = Userinformation.deviceCountry;
        String deviceId = Userinformation.deviceId;
        String deviceModel = Userinformation.deviceModel;
        String deviceOsVersion = Userinformation.deviceOsVersion;
        String deviceVersionRelease = Userinformation.deviceVersionRelease;
        String deviceCity= Userinformation.deviceCity;
        mTextview.setText(" os : " + "Android" + "\n osv : " + deviceVersionRelease + "\n appId : " + packageName + "\n dCmp : " + deviceName + "\n dDetType : " + deviceModel + " \n dLan : " + deviceLanguage
                + " \n dCountry : " + deviceCountry + "\n dCity : " + deviceCity +" \n dID : " + deviceId +" \n lat : " + deviceLatitude + " \n lon : " + deviceLongitude + "\n appVer : " + applicationVersion + "\n dType : " + deviceDetailType
                + "\n gid : " + AppMonitorSdk.applicationId + "\n crashdetail : " + crashFullLog + "\n crashname : " + crashName + " \n status : " + "a");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(provider.equals("")){

                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
