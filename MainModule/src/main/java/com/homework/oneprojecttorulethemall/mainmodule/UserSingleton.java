package com.homework.oneprojecttorulethemall.mainmodule;

import com.parse.ParseUser;

import java.util.List;

public class UserSingleton {
    private static UserSingleton instance;
    private ParseUser user;
    private List<Friend> friendList;

    private UserSingleton() {
    }

    public static UserSingleton getInstance() {
        if (instance == null)
            instance = new UserSingleton();
        return instance;
    }

    public ParseUser getUser() {
        return user;
    }

    public void setUser(ParseUser user) {
        this.user = user;
    }

    public List<Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
    }
}
