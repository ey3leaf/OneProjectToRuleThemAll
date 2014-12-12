package com.homework.oneprojecttorulethemall.mainmodule;

import com.parse.ParseUser;

public class UserSingleton {
    private static UserSingleton instance;
    private ParseUser user;

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
}
