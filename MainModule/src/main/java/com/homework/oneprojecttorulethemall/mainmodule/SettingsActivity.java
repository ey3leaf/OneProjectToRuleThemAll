package com.homework.oneprojecttorulethemall.mainmodule;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class SettingsActivity extends Activity {

    private Spinner spinner;
    private String[] data = {"Hybrid","Normal","Satellite","Terrain"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinner = (Spinner) findViewById(R.id.map_type_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    public void okButton(View v){
        MapsActivity.setMapType(spinner.getSelectedItem().toString());
        this.finish();
    }

    public void cancel(View v){
        this.finish();
    }

}
