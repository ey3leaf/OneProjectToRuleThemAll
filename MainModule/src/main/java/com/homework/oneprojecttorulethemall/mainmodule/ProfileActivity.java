package com.homework.oneprojecttorulethemall.mainmodule;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;


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

    }
}
