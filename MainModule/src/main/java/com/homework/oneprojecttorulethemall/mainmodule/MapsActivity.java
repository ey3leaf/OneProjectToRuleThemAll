package com.homework.oneprojecttorulethemall.mainmodule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.parse.ParseUser;

public class MapsActivity extends FragmentActivity {

    private ListView listView;
    private FriendsAdapter adapter;
    SupportMapFragment mapFragment;
    GoogleMap map;

    @Override
    protected void onStart() {
        super.onStart();

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences(UserSingleton.getInstance().getUser().getObjectId(), 0);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", true);
        ed.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        listView = (ListView) findViewById(R.id.left_drawer);
        adapter = new FriendsAdapter(getApplicationContext());
        listView.setAdapter(adapter);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        if (map == null) {
            finish();
            return;
        }
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.logout):
                new AlertDialog.Builder(this).setMessage("Are you sure you want to exit?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences sp = getSharedPreferences(UserSingleton.getInstance().getUser().getObjectId(), 0);
                                SharedPreferences.Editor ed = sp.edit();
                                ed.putBoolean("active", false);
                                ed.apply();

                                ParseUser.logOut();
                                MapsActivity.this.finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
            case (R.id.profile):
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }
}
