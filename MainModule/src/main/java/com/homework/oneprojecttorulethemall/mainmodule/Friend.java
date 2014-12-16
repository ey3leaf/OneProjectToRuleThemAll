package com.homework.oneprojecttorulethemall.mainmodule;

import com.parse.ParseFile;
import com.parse.ParseGeoPoint;

public class Friend {
    private ParseFile friendPhoto;
    private String name, id;
    private ParseGeoPoint location;

    public Friend(ParseFile friendPhoto, String name,String id) {
        this.friendPhoto = friendPhoto;
        this.name = name;
        this.id = id;
    }

    public Friend(ParseFile friendPhoto, String name, ParseGeoPoint location, String id) {
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

    public ParseGeoPoint getLocation() {
        return location;
    }

    public String getId(){return id;}
}
