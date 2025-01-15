package com.example.cvt;

import static com.example.cvt.SharedPreference.LOGIN_STATUS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

//❌need to use ssd to store pictures to database
public class UserProfileActivity extends AppCompatActivity {

    ImageButton mProfilePic;
    EditText mUserName;
    EditText mSignature;
    EditText mCommunity;

    String username = SharedPreference.getUserName(UserProfileActivity.this);
    String signature;//need to get from database with DBHelper❌

    String[] communities; //need to fetch from database with DBHelper❌

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //initiate all components at respective widgets
        mProfilePic = findViewById(R.id.profilePic);

        mUserName = findViewById(R.id.username);
        mUserName.setText(username);

        mSignature = findViewById(R.id.signature);
        mSignature.setText(signature);
//
//        mCommunity = findViewById(R.id.community_joined);
//        mCommunity.setText(communities.toString());
//
//        //check if the username is changed
//        mUserName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (username.equals(String.valueOf(charSequence))){
//
//                }else{
//                    username = String.valueOf(charSequence);
//                    //if changed, update the new name in database❌
//
//                    Toast.makeText(UserProfileActivity.this,"Username changed successfully.",Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        mSignature.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (signature.equals(String.valueOf(charSequence))){
//
//                }else{
//                    signature = String.valueOf(charSequence);
//                    //if changed, update the new signature in database❌
//
//                    Toast.makeText(UserProfileActivity.this,"Username changed successfully.",Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

    }




}