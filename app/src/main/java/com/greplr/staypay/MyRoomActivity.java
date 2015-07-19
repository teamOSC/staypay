package com.greplr.staypay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MyRoomActivity extends AppCompatActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "MyRoomActivity";
    private Switch light;
    private Switch fan;
    private Switch nightLight;
    private Switch ac;
    private TextView roomNumber;
    private String BASE_URL;
    Button cab;
    Button food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_room);
        roomNumber = (TextView) findViewById(R.id.myroom_no);
        Button roomCleaning = (Button) findViewById(R.id.myroom_clean);
        Button roomService = (Button) findViewById(R.id.myroom_service);
        food = (Button) findViewById(R.id.myroom_food);
        light = (Switch) findViewById(R.id.switch_light);
        fan = (Switch) findViewById(R.id.switch_fan);
        nightLight = (Switch) findViewById(R.id.switch_night_light);
        ac = (Switch) findViewById(R.id.switch_ac);

        roomCleaning.setOnClickListener(this);
        roomService.setOnClickListener(this);
        //cab.setOnClickListener(this);
        food.setOnClickListener(this);

        light.setOnCheckedChangeListener(this);
        fan.setOnCheckedChangeListener(this);
        nightLight.setOnCheckedChangeListener(this);
        ac.setOnCheckedChangeListener(this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        roomNumber.setText("Room : " + sp.getString("room_number", "Unknown"));

        BASE_URL = "http://192.168.43.21:8000/hotel/24/room/" + sp.getString("room_number", "Unknown") + "?cmd=control&user_id="+MainActivity.USER_ID;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_room, menu);
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

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.myroom_food){
            startActivity(new Intent(this, FoodActivity.class));
        }else{
            Toast.makeText(getApplicationContext(), "Request sent", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.equals(light) && b) {
            Log.d(TAG, "light");
            new GetTask() {
            }.execute(BASE_URL + "&device=1&status=1");
        } else if (compoundButton.equals(light) && !b) {
            new GetTask() {
            }.execute(BASE_URL + "&device=1&status=0");
        }

        if (compoundButton.equals(fan) && b) {
            Log.d(TAG, "light");
            new GetTask() {
            }.execute(BASE_URL + "&device=2&status=1");
        } else if (compoundButton.equals(fan) && !b) {
            new GetTask() {
            }.execute(BASE_URL + "&device=2&status=0");
        }

        if (compoundButton.equals(ac) && b) {
            Log.d(TAG, "light");
            new GetTask() {
            }.execute(BASE_URL + "&device=3&status=1");
        } else if (compoundButton.equals(ac) && !b) {
            new GetTask() {
            }.execute(BASE_URL + "&device=3&status=0");
        }

        if (compoundButton.equals(nightLight) && b) {
            Log.d(TAG, "light");
            new GetTask() {
            }.execute(BASE_URL + "&device=4&status=1");
        } else if (compoundButton.equals(nightLight) && !b) {
            new GetTask() {
            }.execute(BASE_URL + "&device=4&status=0");
        }
    }
}
