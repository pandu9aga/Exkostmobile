package com.example.exkost;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public Context context;

    public static final String PREF_NAME = "LOGIN";
    public static final String LOGIN_STATUS = "false";
    public static final String EMAIL = "EMAIL";
    public static final String NAMA = "NAMA";
    public static final String ID = "ID";
    public static final String SALDO = "SALDO";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createSession(String email, String nama, String id, String saldo) {
        editor.putBoolean(LOGIN_STATUS, true);
        editor.putString(EMAIL, email);
        editor.putString(NAMA, nama);
        editor.putString(ID, id);
        editor.putString(SALDO, saldo);
        editor.apply();
    }

    public boolean isLogin(){
        return pref.getBoolean(LOGIN_STATUS, false);
    }

    public void logout(){
        editor.clear();
        editor.commit();

        Intent login = new Intent(context, MainActivity.class);
        context.startActivity(login);
        ((HomeActivity)context).finish();
    }

    public String getEmailAkun() {
        return pref.getString(EMAIL, null);
    }

    public String getNamaAkun() {
        return pref.getString(NAMA, null);
    }

    public String getIdAkun() {
        return pref.getString(ID, null);
    }

    public String getSaldoAkun() {
        return  pref.getString(SALDO, null);
    }

}
