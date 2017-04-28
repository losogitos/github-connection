package com.example.lukasz.githubconnection.utils;

import android.content.Context;
import android.text.TextUtils;

import org.w3c.dom.Text;

import okhttp3.Credentials;

/**
 * Created by Łukasz Ciupa on 28.04.17.
 */

public class Session {

    private static Session instance = null;

    private String userName;
    private String password;

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setCredentials(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getCredentials() {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) throw new RuntimeException("User not logged");
        return Credentials.basic(userName, password);
    }

    public boolean isLogged() {
        return (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password));
    }

    public void logout() {
        userName = null;
        password = null;
    }

}
