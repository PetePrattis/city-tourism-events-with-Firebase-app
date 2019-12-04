package com.unipi.p15120.visageathens;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Users {

    public String username;
    public String password;

    public Users() {}

    public Users(String username, String password)
    {
        this.username = username;
        this.password = password;
    }


    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    String getUsername()
    {
        return username;
    }
    String getPassword()
    {
        return password;
    }


    //EISAGWGH TOU USERNAME, PASSWORD STH DATABASE
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("password", password);
        return result;
    }


}
