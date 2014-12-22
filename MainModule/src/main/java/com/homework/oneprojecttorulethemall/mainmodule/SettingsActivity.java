package com.homework.oneprojecttorulethemall.mainmodule;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class SettingsActivity extends Activity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Spinner spinner;
    private String[] data = {"Hybrid", "Normal", "Satellite", "Terrain"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinner = (Spinner) findViewById(R.id.map_type_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void okButton(View v) {
        sharedPreferences = getSharedPreferences(UserSingleton.getInstance().getUser().getObjectId(), 0);
        editor = sharedPreferences.edit();
        editor.putString("MAP", spinner.getSelectedItem().toString());
        editor.apply();

        MapsActivity.setMapType(spinner.getSelectedItem().toString());
        this.finish();
    }

    public void cancel(View v) {
        this.finish();
    }

}
