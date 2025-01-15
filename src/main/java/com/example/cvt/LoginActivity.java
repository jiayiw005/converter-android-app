package com.example.cvt;

import static com.example.cvt.SharedPreference.LOGIN_STATUS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btnlogin = (Button) findViewById(R.id.login);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = username.getText().toString().trim();
                String pwd = password.getText().toString().trim();

                //need to get username and password from database with DBHelper❌
                //Verify if the username already exists in the database

//                ArrayList<User> data = mSQLite.getAllDATA();
                boolean flagExist = false;
                boolean flagCorrect = false;

                Intent intent1 = new Intent(LoginActivity.this, Homepage2Activity.class);
                startActivity(intent1);
                finish();

                //Whether the username is empty
//                if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(pwd)) {
//                    //Employ the boolean variable showing whether the username exists
//                    if(flagExist){
//                        if(flagCorrect){
//                            //set the login status and the username logged in
//                            SharedPreference.setLoginStatus(LoginActivity.this, true);
//                            SharedPreference.setUserName(LoginActivity.this, name);
//
//                            //go to the homepage
//                            Intent intent1 = new Intent(LoginActivity.this, Homepage2Activity.class);
//                            startActivity(intent1);
//                            finish();
//
//                            //show a message noticing logged in successfully
//                            Toast.makeText(LoginActivity.this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
//
//                        }else{
//                            //show a message of login failure
//                            username.setText("");
//                            password.setText("");
//                            Toast.makeText(LoginActivity.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else{
////                        try {
////                            //need more precise insert method❌
////                            mSQLite.insert(name, pwd);
////                        } catch (SQLException e) {
////                            throw new RuntimeException(e);
////                        }
////                        //set the login status and the username logged in
////                        SharedPreference.setLoginStatus(LoginActivity.this, true);
////                        SharedPreference.setUserName(LoginActivity.this, name);
////
////                        //go to the homepage
////                        Intent intent1 = new Intent(LoginActivity.this, Homepage2Activity.class);
////                        startActivity(intent1);
////                        finish();
////
////                        //show a message noticing signed up successfully
////                        Toast.makeText(LoginActivity.this, "Signed up successfully!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else{
//                    username.setText("");
//                    password.setText("");
//                    //show a message that the name and password must be entered
//                    Toast.makeText(LoginActivity.this, "Username and password can't be empty.", Toast.LENGTH_SHORT).show();
//                }
            }
        });

    }
}