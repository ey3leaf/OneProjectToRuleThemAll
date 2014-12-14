package com.homework.oneprojecttorulethemall.mainmodule;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ParseService extends Service implements Runnable {
    private MapsActivity activity;

    public void setActivity(MapsActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("DATA");
            query.whereNotEqualTo("ID", ParseObject.createWithoutData("ID", UserSingleton.getInstance().getUser().getObjectId()));
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    UserSingleton.getInstance().getFriendList().clear();
                    for (ParseObject parseObject : list) {
                        UserSingleton.getInstance().getFriendList().add(new Friend(parseObject.getParseFile("AVATAR"),
                                parseObject.getString("NAME") + " " + parseObject.getString("SURNAME"), parseObject.getObjectId()));
                    }
                    if (activity != null) {
                        activity.updateView();
                    }

                }
            });

            SystemClock.sleep(5000);

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ParseBinder(this);
    }

}
