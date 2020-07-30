package com.example.hydration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hydration.sync.ReminderTasks;
import com.example.hydration.sync.ReminderUtilities;
import com.example.hydration.sync.WaterReminderIntentService;
import com.example.hydration.utilities.NotificationUtils;
import com.example.hydration.utilities.PreferenceUtilities;

/*
* Android service is a component that is used to perform operations on the background such as playing music,
*  handle network transactions, interacting content providers etc. It doesn't has any UI (user interface).
*  The service runs in the background indefinitely even if application is destroyed.
* A service is a component that runs in the background to perform long-running operations without needing to interact
* with the user and it works even if application is destroyed
* There are four main Android app components: activities , services , content providers , and broadcast receivers*/

public class MainActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener,AdapterView.OnItemSelectedListener{
    private TextView mWaterCountDisplay;
    private TextView mChargingCountDisplay;
    private ImageView mChargingImageView;
    private Toast mToast;
    private SwitchCompat switchCompat;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /** Get the views **/
        spinner=findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> spinneradp=ArrayAdapter.createFromResource(this,R.array.time,android.R.layout.simple_spinner_item);
        spinneradp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinneradp);
        switchCompat=(SwitchCompat) findViewById(R.id.switch1);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
spinner.setVisibility(View.VISIBLE);
                }
            }
        });
        mWaterCountDisplay = (TextView) findViewById(R.id.tv_water_count);
        mChargingCountDisplay = (TextView) findViewById(R.id.tv_charging_reminder_count);
        mChargingImageView = (ImageView) findViewById(R.id.iv_power_increment);
        /** Set the original values in the UI **/
        updateWaterCount();
        updateChargingReminderCount();

        /** Setup the shared preference listener **/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        ReminderUtilities.scheduleChargingReminder(this);
    }
    /**
     * Updates the TextView to display the new water count from SharedPreferences
     */
    private void updateWaterCount() {
        int waterCount = PreferenceUtilities.getWaterCount(this);
        mWaterCountDisplay.setText(waterCount+"");
    }

    /*
     * Updates the TextView to display the new charging reminder count from SharedPreferences
     */
    private void updateChargingReminderCount() {
        int chargingReminders = PreferenceUtilities.getChargingReminderCount(this);
        String formattedChargingReminders = getResources().getQuantityString(

                R.plurals.charge_notification_count, chargingReminders, chargingReminders);
        mChargingCountDisplay.setText(formattedChargingReminders);


    }
    /**
     * Adds one to the water count and shows a toast
     */
    public void incrementWater(View view) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, R.string.water_chug_toast, Toast.LENGTH_SHORT);
        mToast.show();
        Intent intent=new Intent(this, WaterReminderIntentService.class);
        intent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);
        startService(intent);
        // TODO (15) Create an explicit intent for WaterReminderIntentService
        // TODO (16) Set the action of the intent to ACTION_INCREMENT_WATER_COUNT
        // TODO (17) Call startService and pass the explicit intent you just created
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        /** Cleanup the shared preference listener **/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }
    /**
     * This is a listener that will update the UI when the water count or charging reminder counts
     * change
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (PreferenceUtilities.KEY_WATER_COUNT.equals(key)) {
            updateWaterCount();
        } else if (PreferenceUtilities.KEY_CHARGING_REMINDER_COUNT.equals(key)) {
            updateChargingReminderCount();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
String item=adapterView.getItemAtPosition(i).toString();
ReminderUtilities.REMINDER_INTERVAL_SECONDS=ToSeconds(item.substring(0,2));
    }
int ToSeconds(String sec)
{
    int s=Integer.parseInt((sec));
    return  (s*60);
}
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    /* public void testNotification(View view)
    {
        NotificationUtils.remindUserBecauseCharging(this);
    }*/

}
        //lecture 9,16 for theory get through them again

                                                                //pending intent
/*If you give the foreign application an Intent, it will execute your Intent with its own permissions.
 But if you give the foreign application a PendingIntent, that application will execute your Intent using your
  application's permission.*/