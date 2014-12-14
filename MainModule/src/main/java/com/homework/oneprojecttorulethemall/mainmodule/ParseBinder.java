package com.homework.oneprojecttorulethemall.mainmodule;

import android.os.Binder;

public class ParseBinder extends Binder {
    private ParseService service;

    public ParseBinder(ParseService service) {
        this.service = service;
    }

    public ParseService getService() {
        return service;
    }
}
