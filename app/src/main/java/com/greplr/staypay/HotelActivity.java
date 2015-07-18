package com.greplr.staypay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

public class HotelActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("json");
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_rooms);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new HotelAdapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hotel, menu);
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

    private class HotelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch(viewType){
                case 0 : CardView v = (CardView) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_hotel_main, parent, false);
                    return new HotelAdapter.HotelViewHolder(v);
                default : CardView view = (CardView) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardview_hotel_room, parent, false);
                    return new HotelAdapter.RoomViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            try {
                return jsonObject.getJSONArray("rooms").length() + 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }

        public class HotelViewHolder extends RecyclerView.ViewHolder {

            public HotelViewHolder(View itemView) {
                super(itemView);
            }
        }

        public class RoomViewHolder extends RecyclerView.ViewHolder {

            public RoomViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
