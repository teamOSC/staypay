package com.greplr.staypay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookActivity extends AppCompatActivity {

    private static final String TAG = "BookingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Intent intent = getIntent();
        final int position = intent.getIntExtra("position", 0);
        TextView bookingRoom = (TextView) findViewById(R.id.booking_room);
        final EditText bookingName = (EditText) findViewById(R.id.booking_name);
        DatePicker bookingCheckoutDate = (DatePicker) findViewById(R.id.booking_date_picker);
        Button bookingUserID = (Button) findViewById(R.id.booking_id);
        Button bookingSubmit = (Button) findViewById(R.id.booking_submit);

        try {
            bookingRoom.setText(HotelActivity.jsonArray.getJSONObject(position).getString("type_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bookingUserID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {

                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                }

                Intent chooser = new Intent(Intent.ACTION_CHOOSER);
                chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
                chooser.putExtra(Intent.EXTRA_TITLE, "Upload ID Photo");

                Intent[] intentArray = {cameraIntent};
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooser, 0);
            }
        });

        bookingSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookingName.getText().length() != 0) {
                    try {
                        new GetTask() {
                            @Override
                            protected void onPostExecute(String s) {
                                super.onPostExecute(s);
                                Log.d(TAG, s);
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    if (jsonObject.getString("success").equals("true")) {
                                        Toast.makeText(getApplicationContext(), "Room no " + jsonObject.getString("room_no") + " booked", Toast.LENGTH_SHORT).show();
                                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        sp.edit().putString("room_number", jsonObject.getString("room_no")).commit();
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error in booking", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Room not available", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.execute("http://192.168.43.21:8000/hotel/24/" + HotelActivity.jsonArray.getJSONObject(position).getString("type_id") + "?user_id=42");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    bookingName.setError("Required");
                }
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book, menu);
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
