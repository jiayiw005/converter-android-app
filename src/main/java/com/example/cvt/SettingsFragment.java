package com.example.cvt;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
//import android.support.v14.preference.SwitchPreference;
//import android.support.v7.preference.Preference;
//import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//public class SettingsFragment extends PreferenceFragmentCompat {
//
//
//    public static SettingsFragment newInstance() {
//        return new SettingsFragment();
//    }
//
//    @Override
//    public void onCreatePreferences(Bundle bundle, String s) {
//        setPreferencesFromResource(R.xml.preference,s);
//
//        SwitchPreference switchPreference = (SwitchPreference) findPreference("NIGHT");
//        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object o) {
//                boolean yes = (boolean) o;
//                if(yes){
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                }else{
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                }
//                return true;
//            }
//        });
//
//        Preference aboutUs = (Preference) findPreference("about");
//        aboutUs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                return true;
//            }
//        });
//    }
//
//}