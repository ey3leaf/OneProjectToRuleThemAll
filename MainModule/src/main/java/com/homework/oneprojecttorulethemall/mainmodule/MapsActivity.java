package com.homework.oneprojecttorulethemall.mainmodule;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.parse.ParseUser;

public class MapsActivity extends FragmentActivity {

    SupportMapFragment mapFragment;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

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
                ParseUser.logOut();
                this.finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
