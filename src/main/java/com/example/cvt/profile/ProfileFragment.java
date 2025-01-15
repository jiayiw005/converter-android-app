package com.example.cvt.profile;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cvt.LoginActivity;
import com.example.cvt.MySQLConnection;
import com.example.cvt.R;
import com.example.cvt.SharedPreference;
import com.example.cvt.UserProfileActivity;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private ProfileViewModel mViewModel;
    static String community_joined;
    static String username;
    static String signature;
    public static Connection con = null;
    View view;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //inflate fragment layout
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        boolean loginstatus = SharedPreference.getLoginStatus(getContext());
        if(loginstatus == false){
            Snackbar.make(view, "You're not logged in.", Snackbar.LENGTH_LONG).setAction("Log in", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(view).navigate(R.id.action_profile_to_signup);
                }
            }).show();
        }

        //inflate widgets
        EditText username = (EditText) view.findViewById(R.id.username);
        username.setText(SharedPreference.getUserName(getContext()));

        EditText signature = (EditText) view.findViewById(R.id.signature);
        signature.setText(SharedPreference.getSIGNATURE());

        signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SharedPreference.getLoginStatus(getContext())) {

                }else{
                    //remind that the user isn't logged in yet
                    Toast.makeText(getContext(), "You're not logged in.", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.action_profile_to_signup);
                }
            }
        });

        EditText community = (EditText) view.findViewById(R.id.community_joined);

        Button btnLogout = (Button) view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreference.setLoginStatus(getContext(),false);
                SharedPreference.setUserEmail(getContext(), null);
                SharedPreference.setUserName(getContext(),null);
                SharedPreference.setPrefUserId(0);
                SharedPreference.setSIGNATURE(null);

                Toast.makeText(getContext(), "You're logged out successfully!", Toast.LENGTH_SHORT).show();

                Navigation.findNavController(view).navigate(R.id.action_profile_to_home);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View v) {

    }

    class Threads_setup extends Thread{
        @Override
        public void run() {
            try{
                con = MySQLConnection.getConnection();

                String sqlSelect = "Select * from user_detail where userid like (?) ";
                PreparedStatement pst = con.prepareStatement(sqlSelect);
                pst.setInt(1,SharedPreference.getPrefUserId());
                ResultSet rset = pst.executeQuery();
                while(rset.next()) {
                    username = rset.getString("username");
                    community_joined = rset.getString("communities");
                    signature = rset.getString("signature");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    class Threads_changename extends Thread{
        @Override
        public void run() {
            try{
                con = MySQLConnection.getConnection();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    class Threads_signature extends Thread{
        @Override
        public void run() {
            try{
                con = MySQLConnection.getConnection();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}