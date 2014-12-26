package com.homework.oneprojecttorulethemall.mainmodule;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import com.parse.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ProfileActivity extends Activity {
    private static boolean isFirstStart = true;
    private ImageView avatar;
    private Bitmap bitmap;
    private EditText name, surname;
    private CheckBox[] checkBoxes;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ParseQuery<ParseObject> query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        query = ParseQuery.getQuery("DATA");
        query.whereEqualTo("ID", ParseObject.createWithoutData("ID", UserSingleton.getInstance().getUser().getObjectId()));

        InitializeFields();

        avatar.setImageResource(R.drawable.contact_default);
        avatar.setDrawingCacheEnabled(true);
        avatar.buildDrawingCache();
        FillProfileDataFromSharedPrefs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isFirstStart = false;
    }

    public void camera(View v) {
        Intent takeAvatarIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takeAvatarIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(takeAvatarIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            avatar.setImageBitmap(bitmap);
        }
    }

    public void done(View v) {
        List<String> hobbies = getHobbies();

        SaveIntoParseAndSharedPrefs(hobbies);

        sharedPreferences = getSharedPreferences(UserSingleton.getInstance().getUser().getObjectId(), 0);
        if (!sharedPreferences.getBoolean("active", false))
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));

        this.finish();
    }

    public void cancel(View v) {
        SharedPreferences sp = getSharedPreferences(UserSingleton.getInstance().getUser().getObjectId(), 0);
        if (!sp.getBoolean("active", false))
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        this.finish();
    }

    private List<String> getHobbies() {
        List<String> hobbies = new ArrayList<String>();
        Set<String> sharedHobbies = new HashSet<String>();
        sharedPreferences = getSharedPreferences(UserSingleton.getInstance().getUser().getObjectId(), 0);
        editor = sharedPreferences.edit();

        for (CheckBox checkBox : checkBoxes) {
            editor.putBoolean(checkBox.getText().toString(), false);
            if (checkBox.isChecked()) {
                hobbies.add(checkBox.getText().toString());
                sharedHobbies.add(checkBox.getText().toString());
                editor.putBoolean(checkBox.getText().toString(), true);
            }
        }
        editor.putStringSet("HOBBIES",sharedHobbies);
        editor.apply();
        return hobbies;
    }

    private void SaveIntoParseAndSharedPrefs(final List<String> hobbies) {
        query = ParseQuery.getQuery("DATA");
        query.whereEqualTo("ID", ParseObject.createWithoutData("ID", UserSingleton.getInstance().getUser().getObjectId()));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    saveData(parseObject, hobbies);
                } else {
                    saveData(new ParseObject("DATA"), hobbies);
                }
            }
        });
    }

    private void saveData(ParseObject parseObject, List<String> hobbies) {
        sharedPreferences = getSharedPreferences(UserSingleton.getInstance().getUser().getObjectId(), 0);
        editor = sharedPreferences.edit();

        parseObject.put("ID", ParseObject.createWithoutData("ID", UserSingleton.getInstance().getUser().getObjectId()));
        parseObject.put("NAME", name.getText().toString());
        parseObject.put("SURNAME", surname.getText().toString());
        parseObject.put("HOBBIES", hobbies);

        editor.putString("NAME", name.getText().toString());
        editor.putString("SURNAME", surname.getText().toString());
        editor.apply();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bitmap != null) {

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] data = stream.toByteArray();

            ParseFile imgFile = new ParseFile(surname.getText().toString() + ".png", data);
            imgFile.saveInBackground();
            parseObject.put("AVATAR", imgFile);
        } else {
            Bitmap bm = avatar.getDrawingCache();
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] data = stream.toByteArray();
            ParseFile imgFile = new ParseFile(surname.getText().toString() + ".png", data);
            parseObject.put("AVATAR", imgFile);
        }
        parseObject.saveInBackground();
    }

    private void FillProfileDataFromSharedPrefs() {
        sharedPreferences = getSharedPreferences(UserSingleton.getInstance().getUser().getObjectId(), 0);
        if (!isFirstStart) {
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    try {
                        ParseFile imageFile = parseObject.getParseFile("AVATAR");
                        imageFile.getDataInBackground(new GetDataCallback() {
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    avatar.setImageBitmap(bmp);
                                }
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }

        name.setText(sharedPreferences.getString("NAME", ""));
        surname.setText(sharedPreferences.getString("SURNAME", ""));

        for (CheckBox checkBox : checkBoxes) {
            checkBox.setChecked(sharedPreferences.getBoolean(checkBox.getText().toString(), false));
        }
    }

    private void InitializeFields() {
        avatar = (ImageView) findViewById(R.id.avatar);
        name = (EditText) findViewById(R.id.nameField);
        surname = (EditText) findViewById(R.id.nicknameField);
        checkBoxes = new CheckBox[]{(CheckBox) findViewById(R.id.shopingCheckBox),
                (CheckBox) findViewById(R.id.sportCheckBox),
                (CheckBox) findViewById(R.id.travelingCheckBox),
                (CheckBox) findViewById(R.id.hangoutCheckBox),
                (CheckBox) findViewById(R.id.musicCheckBox),
                (CheckBox) findViewById(R.id.booksCheckBox)};
    }
}
