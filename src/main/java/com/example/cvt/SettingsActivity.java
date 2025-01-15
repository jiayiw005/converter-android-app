package com.example.cvt;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;

import com.example.cvt.R;


public class SettingsActivity extends PreferenceActivity {

    private Preference mNotification;
    private Preference mNightmode;
    private Preference mFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        mNotification = findPreference("notifications");
        mNotification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                //TODO:❌change the notification
                return false;
            }
        });

        mNightmode = findPreference("NIGHT");
        mNightmode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //TODO:❌change the mode
                return false;
            }
        });

        mFeedback = findPreference("feedback");
//        mFeedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//
//            }
//        });

    }


}
