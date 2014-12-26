package com.homework.oneprojecttorulethemall.mainmodule;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;


public class SettingsActivity extends Activity {

    private SharedPreferences sharedPreferences;
    private Spinner spinner;
    private EditText updateTimeField, rangeField;
    private String[] data = {"Hybrid", "Normal", "Satellite", "Terrain"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences(UserSingleton.getInstance().getUser().getObjectId(), 0);

        updateTimeField = (EditText) findViewById(R.id.service_update_field);
        updateTimeField.setText("" + sharedPreferences.getInt("UPDATE_TIME", 60));

        rangeField = (EditText) findViewById(R.id.rangeField);
        rangeField.setText("" + sharedPreferences.getInt("RANGE", 1000));

        spinner = (Spinner) findViewById(R.id.map_type_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void okButton(View v) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.settingsRadioGroup);

        editor.putString("MAP", spinner.getSelectedItem().toString());
        editor.putInt("SEARCH", radioGroup.getCheckedRadioButtonId());
        editor.putInt("UPDATE_TIME", Integer.parseInt(updateTimeField.getText().toString()));
        editor.putInt("RANGE", Integer.parseInt(rangeField.getText().toString()));

        editor.apply();

        MapsActivity.setMapType(spinner.getSelectedItem().toString());
        this.finish();
    }

    public void cancel(View v) {
        this.finish();
    }

}
