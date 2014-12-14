package com.homework.oneprojecttorulethemall.mainmodule;

import android.location.Location;
import com.parse.ParseFile;

public class Friend {
    private ParseFile friendPhoto;
    private String name, id;
    private Location location;

    public Friend(ParseFile friendPhoto, String name,String id) {
        this.friendPhoto = friendPhoto;
        this.name = name;
        this.id = id;
    }

    public Friend(ParseFile friendPhoto, String name, Location location, String id) {
        this.friendPhoto = friendPhoto;
        this.name = name;
        this.location = location;
        this.id = id;
    }

    public ParseFile getFriendPhoto() {
        return friendPhoto;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public String getId(){return id;}
}
