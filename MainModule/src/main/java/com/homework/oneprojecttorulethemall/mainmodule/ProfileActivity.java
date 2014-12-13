package com.homework.oneprojecttorulethemall.mainmodule;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends Activity {

    private ImageView avatar;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        avatar = (ImageView) findViewById(R.id.avatar);
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
        SaveIntoParse(bitmap,hobbies);
        SharedPreferences sp = getSharedPreferences("SharedPref", 0);
        if (!sp.getBoolean("active", false))
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        this.finish();
    }

    public void cancel(View v) {
        SharedPreferences sp = getSharedPreferences("SharedPref", 0);
        if (!sp.getBoolean("active", false))
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        this.finish();
    }

    private List<String> getHobbies() {
        List<String> hobbies = new ArrayList<String>();
        CheckBox[] checkBoxes = {(CheckBox) findViewById(R.id.shopingCheckBox),
                (CheckBox) findViewById(R.id.sportCheckBox),
                (CheckBox) findViewById(R.id.travelingCheckBox),
                (CheckBox) findViewById(R.id.hangoutCheckBox),
                (CheckBox) findViewById(R.id.musicCheckBox),
                (CheckBox) findViewById(R.id.booksCheckBox)};
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {
                hobbies.add(checkBox.getText().toString());
            }
        }
        return hobbies;
    }

    private void SaveIntoParse(Bitmap bitmap, List<String> hobbies) {
        EditText name = (EditText) findViewById(R.id.nameField);
        EditText surname = (EditText) findViewById(R.id.nicknameField);

        ParseObject userInfo = new ParseObject("DATA");
        userInfo.put("ID", ParseObject.createWithoutData("ID",ParseUser.getCurrentUser().getObjectId()));
        userInfo.put("NAME", name.getText().toString());
        userInfo.put("SURNAME", surname.getText().toString());
        userInfo.put("HOBBIES", hobbies);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] data = stream.toByteArray();
        ParseFile imgFile = new ParseFile(surname.getText().toString()+".png",data);
        imgFile.saveInBackground();
        userInfo.put("AVATAR", imgFile);

        userInfo.saveInBackground();

    }
}
