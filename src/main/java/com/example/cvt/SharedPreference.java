package com.example.cvt;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreference
{
    static String PREF_USER_NAME="";
    static String PREF_USER_EMAIL;
    static String SIGNATURE;
    static int PREF_USER_ID;
    static boolean LOGIN_STATUS= false;
    static int CURRENT_MODULE;
    static int CURRENT_RESOURCE;
    static int CURRENT_DISCUSSION;

    public static String getSIGNATURE() {
        return SIGNATURE;
    }

    public static void setSIGNATURE(String SIGNATURE) {
        SharedPreference.SIGNATURE = SIGNATURE;
    }


    public static int getPrefUserId() {
        return PREF_USER_ID;
    }
    public static void setPrefUserId(int prefUserId) {
        PREF_USER_ID = prefUserId;
    }

    public static int getCurrentResource() {
        return CURRENT_RESOURCE;
    }

    public static void setCurrentResource(int currentResource) {
        CURRENT_RESOURCE = currentResource;
    }

    public static void setLoginStatus(boolean loginStatus) {
        LOGIN_STATUS = loginStatus;
    }

    public static int getCurrentDiscussion() {
        return CURRENT_DISCUSSION;
    }

    public static void setCurrentDiscussion(int currentDiscussion) {
        CURRENT_DISCUSSION = currentDiscussion;
    }

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        PREF_USER_NAME=userName;
    }

    public static void setLoginStatus(Context ctx, boolean currStatus)
    {
        LOGIN_STATUS=currStatus;
    }

    public static void setUserEmail(Context ctx, String userEmail)
    {
        PREF_USER_EMAIL=userEmail;
    }

    public static void setCurrentModule(Context ctx, int currModule){
        CURRENT_MODULE=currModule;
    }

    public static String getUserName(Context ctx)
    {
        return PREF_USER_NAME;
    }

    public static boolean getLoginStatus(Context ctx)
    {
        return LOGIN_STATUS;
    }

    public static String getUserEmail(Context ctx)
    {
        return PREF_USER_EMAIL;
    }

    public static int getCurrModule(Context ctx){return CURRENT_MODULE;}
}