package com.homework.oneprojecttorulethemall.mainmodule;

import android.app.AlertDialog;
import android.content.*;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.parse.*;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements ServiceConnection,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private DrawerLayout drawer;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private ParseService service;
    private ListView listView;
    private FriendsAdapter adapter;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private FusedLocationProviderApi fusedAPI = LocationServices.FusedLocationApi;
    private long locationUpdateInterval = 2500;
    private static boolean isFirstStart = true;

    private void InitializeListView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (ListView) findViewById(R.id.left_drawer);
        adapter = new FriendsAdapter(getApplicationContext(), map);
        UserSingleton.getInstance().setFriendList(new ArrayList<Friend>());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawer.closeDrawers();
                animateToLocation(UserSingleton.getInstance().getFriendList().get(i).getLocation());
            }
        });

        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //TODO Open chat
                return false;
            }
        });
    }

    private void InitializeMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        if (map == null) {
            finish();
            return;
        }
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
    }

    private void InitializeGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this, this, this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                build();
    }

    @Override
    protected void onStart() {
        super.onStart();

        googleApiClient.connect();

        SharedPreferences sp = getSharedPreferences(UserSingleton.getInstance().getUser().getObjectId(), 0);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", true);
        ed.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        locationRequest.setInterval(locationUpdateInterval);
        fusedAPI.requestLocationUpdates(googleApiClient, locationRequest, this);
        while (fusedAPI.getLastLocation(googleApiClient) == null) {
            Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_LONG).show();
            SystemClock.sleep(2500);
        }
        Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        InitializeMap();
        InitializeGoogleApiClient();
        InitializeListView();

        Intent intent = new Intent(getApplicationContext(), ParseService.class);
        startService(intent);
        bindService(intent, this, 0);
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
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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

    public void updateView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.invalidateViews();
            }
        });
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        service = ((ParseBinder) iBinder).getService();
        service.setActivity(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
        googleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(final Location location) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("DATA");
        query.whereEqualTo("ID", ParseObject.createWithoutData("ID", UserSingleton.getInstance().getUser().getObjectId()));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                parseObject.put("LOCATION", new ParseGeoPoint(location.getLatitude(), location.getLongitude()));
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        fusedAPI.removeLocationUpdates(googleApiClient, MapsActivity.this);
                        Toast.makeText(getApplicationContext(), "" + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if (isFirstStart) {
            animateToLocation(location);
            isFirstStart = false;
        }
    }

    private void animateToLocation(Location location) {
        CameraPosition newCamPos = new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()),
                15.5f,
                map.getCameraPosition().tilt, //use old tilt
                map.getCameraPosition().bearing); //use old bearing
        map.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 1500, null);
    }

    private void animateToLocation(ParseGeoPoint location) {
        CameraPosition newCamPos = new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()),
                15.5f,
                map.getCameraPosition().tilt, //use old tilt
                map.getCameraPosition().bearing); //use old bearing
        map.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 1500, null);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
