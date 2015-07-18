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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HotelActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private JSONObject jsonObject;
    public static JSONArray jsonArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("json");
        try {
            jsonObject = new JSONObject(jsonString);
            jsonArray = jsonObject.getJSONArray("rooms");
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
            switch (viewType) {
                case 0:
                    CardView v = (CardView) LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.cardview_hotel_main, parent, false);
                    return new HotelAdapter.HotelViewHolder(v);
                default:
                    CardView view = (CardView) LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.cardview_hotel_room, parent, false);
                    return new HotelAdapter.RoomViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case 0:
                    HotelViewHolder hotelViewHolder = (HotelViewHolder) holder;
                    try {
                        hotelViewHolder.hotelName.setText(jsonObject.getString("name"));
                        hotelViewHolder.ratingBar.setRating(Float.parseFloat(jsonObject.getString("rating")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    RoomViewHolder roomViewHolder = (RoomViewHolder) holder;
                    try {
                        roomViewHolder.roomsAvailable.setText(jsonArray.getJSONObject(position - 1).getString("available"));
                        roomViewHolder.roomType.setText(jsonArray.getJSONObject(position - 1).getString("type_name"));
                        roomViewHolder.roomBeds.setText(jsonArray.getJSONObject(position - 1).getString("beds"));
                        roomViewHolder.roomRate.setText(jsonArray.getJSONObject(position - 1).getString("rate"));
                        roomViewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getApplicationContext(), BookActivity.class);
                                i.putExtra("position", position -1);
                                startActivity(i);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            if (jsonArray != null) {
                return jsonArray.length() + 1;
            }
            return 0;
        }

        public class HotelViewHolder extends RecyclerView.ViewHolder {

            ImageView hotelLogo;
            RatingBar ratingBar;
            TextView hotelName;

            public HotelViewHolder(View itemView) {
                super(itemView);
                hotelLogo = (ImageView) itemView.findViewById(R.id.room_image);
                ratingBar = (RatingBar) itemView.findViewById(R.id.hotel_rating);
                hotelName = (TextView) itemView.findViewById(R.id.hotel_name);
            }
        }

        public class RoomViewHolder extends RecyclerView.ViewHolder {

            TextView roomType;
            TextView roomsAvailable;
            TextView roomBeds;
            TextView roomRate;
            ImageView roomImage;
            View view;

            public RoomViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                roomType = (TextView) itemView.findViewById(R.id.room_type);
                roomsAvailable = (TextView) itemView.findViewById(R.id.room_available);
                roomBeds = (TextView) itemView.findViewById(R.id.room_beds);
                roomRate = (TextView) itemView.findViewById(R.id.room_rate);
                roomImage = (ImageView) itemView.findViewById(R.id.room_image);
            }
        }
    }
}
