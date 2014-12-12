package com.homework.oneprojecttorulethemall.mainmodule;

import android.app.Application;
import com.parse.Parse;

public class ApplicationKeys extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "lL6HqomjxsLBDeKUOl3kBnKTGCjHVSQRxnuVR4wv", "OR369qA30iN2cHqYXhl6oRZcua5FungrjagguGN9");
    }
}
