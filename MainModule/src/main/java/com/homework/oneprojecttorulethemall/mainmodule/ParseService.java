package com.homework.oneprojecttorulethemall.mainmodule;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;
import com.parse.*;

import java.util.List;

public class ParseService extends Service implements Runnable {

    private MapsActivity activity;
    private SharedPreferences sharedPreferences;

    public void setActivity(MapsActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate() {
        sharedPreferences = getSharedPreferences(UserSingleton.getInstance().getUser().getObjectId(), 0);
        new Thread(this).start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        parseDATA();
        parseMessages();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void run() {

        while (true) {
            parseDATA();
            parseMessages();

            SystemClock.sleep(sharedPreferences.getInt("UPDATE_TIME", 60) * 100);
        }
    }

    private void parseDATA() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("DATA");
        query.whereNotEqualTo("ID", ParseObject.createWithoutData("ID", UserSingleton.getInstance().getUser().getObjectId()));
        switch (sharedPreferences.getInt("SEARCH", R.id.allRadio)) {
            case R.id.hobbiesRadio:
                if (sharedPreferences.getStringSet("HOBBIES", null) != null) {
                    query.whereContainedIn("HOBBIES", sharedPreferences.getStringSet("HOBBIES", null));
                }
                break;
            case R.id.rangeRadio:
                query.whereWithinKilometers("LOCATION", activity.getCurrentGeoPoint(),
                        sharedPreferences.getInt("RANGE", 1000));
                break;
            default:
                break;
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                UserSingleton.getInstance().getFriendList().clear();
                if (e == null) {
                    for (ParseObject parseObject : list) {
                        UserSingleton.getInstance().getFriendList().add(new Friend(parseObject.getParseFile("AVATAR"),
                                parseObject.getString("NAME") + " " + parseObject.getString("SURNAME"),
                                parseObject.getParseGeoPoint("LOCATION"),
                                parseObject.getParseObject("ID").getObjectId()));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT).show();
                }
                if (activity != null) {
                    activity.updateView();
                }
            }
        });
    }

    private void parseMessages() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Messages");
        query.whereEqualTo("IS_READ", false);
        query.whereEqualTo("TO", UserSingleton.getInstance().getUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject parseObject : list) {
                    ParsePush push = new ParsePush();
                    push.setChannel("One" + UserSingleton.getInstance().getUser().getObjectId());
                    push.setMessage(" You have a new message");
                    push.sendInBackground();
                    parseObject.put("IS_READ", true);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ParseBinder(this);
    }

}
