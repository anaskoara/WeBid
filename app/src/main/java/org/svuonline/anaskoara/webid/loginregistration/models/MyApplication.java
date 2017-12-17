package org.svuonline.anaskoara.webid.loginregistration.models;

import android.app.Application;
public class MyApplication extends Application {
    private     boolean loggedIn;
    private  User user;

    public User getUser() {
        return user;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setUser(User user) {
        this.user = user;
    }
}