package com.example.cvt;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

public class Preference extends PreferenceActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        addPreferencesFromResource(R.xml.preference);

        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("NIGHT");
        checkBoxPreference.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference preference, Object newValue) {
                boolean yes = (boolean) newValue;
                if(yes){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                return false;
            }
        });

    }

}
