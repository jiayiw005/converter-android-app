package com.example.cvt.signup;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cvt.MySQLConnection;
import com.example.cvt.R;
import com.example.cvt.SharedPreference;
import com.example.cvt.ui.home.HomeFragment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SignupFragment extends Fragment implements View.OnClickListener{

    View view;
    static String USEREMAIL = null;
    static String email_fetched = null;
    static String PASSWORD = null;
    static String pwd_fetched = null;
    static int userid_fetched;
    static String signature_fetched;

    static String username_fetched = null;

    static int USERID = 0;

    static boolean flagExist=false;

    static boolean flagCorrect=false;

    public static Connection con = null;

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //inflate fragment layout
        view = inflater.inflate(R.layout.fragment_signup, container, false);

        //inflate widgets
        EditText username = (EditText) view.findViewById(R.id.username);
        EditText password = (EditText) view.findViewById(R.id.password);

        Button btnSignup = (Button) view.findViewById(R.id.signup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                USEREMAIL = username.getText().toString();
                PASSWORD = password.getText().toString();

                //search for the email inputted
                SignupFragment.Threads_search threads_search = new SignupFragment.Threads_search();
                threads_search.start();
                try {
                    threads_search.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (!TextUtils.isEmpty(USEREMAIL) && !TextUtils.isEmpty(PASSWORD)) {
                        //Employ the boolean variable showing whether the username exists
                        if (flagExist) {
                            SignupFragment.Threads_check threads_check = new SignupFragment.Threads_check();
                            threads_check.start();
                            try {
                                threads_check.join();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            if (flagCorrect) {
                                //set the login status and the username logged in
                                SharedPreference.setLoginStatus(getContext(), true);SharedPreference.setUserEmail(getContext(), USEREMAIL);SharedPreference.setUserName(getContext(),username_fetched);SharedPreference.setPrefUserId(userid_fetched);SharedPreference.setSIGNATURE(signature_fetched);
                                //go to the homepage
                                if(username_fetched.isEmpty()){
                                    Toast.makeText(getContext(), "Logged in successfully!", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getContext(), username_fetched + " logged in successfully!", Toast.LENGTH_SHORT).show();
                                }
                                Navigation.findNavController(view).navigate(R.id.action_signup_to_homepage);
                            } else {
                                //show a message of login failure
                                username.setText("");
                                password.setText("");
                                Toast.makeText(getContext(), "Incorrect password.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            SignupFragment.Threads_signup threads_signup = new SignupFragment.Threads_signup();
                            threads_signup.start();
                            try {
                                threads_signup.join();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            //set the login status and the username logged in
                            SharedPreference.setLoginStatus(getContext(), true);
                            SharedPreference.setUserEmail(getContext(), USEREMAIL);
                            SharedPreference.setPrefUserId(userid_fetched);
                            //go to the homepage
                            Toast.makeText(getContext(), "Signed up successfully!", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).navigate(R.id.action_signup_to_homepage);
                        }
                    } else {
                        username.setText("");
                        password.setText("");
                        //show a message that the name and password must be entered
                        Toast.makeText(getContext(), "Username and password can't be empty.", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_signup_to_homepage);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onClick(View v) {

    }

    //search for email inputted in database
    class Threads_search extends Thread {
        @Override
        public void run() {
            try {
                con = MySQLConnection.getConnection();

                int rowCount = 0;
                String sqlCount = "SELECT COUNT(*) FROM user_detail";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlCount);
                while(rst.next()){
                    rowCount = rst.getInt(1);
                }

                String sqlSelect = "Select * from user_detail where userid like (?) ";
                for(int i=1;i<=rowCount;i++){
                    PreparedStatement pst = con.prepareStatement(sqlSelect);
                    pst.setInt(1,i);
                    ResultSet rset = pst.executeQuery();
                    while(rset.next()) {
                        email_fetched = rset.getString("email");
                        if (email_fetched.equals(USEREMAIL)){
                            USERID = i;
                            flagExist = true;
                        }
                    }
                    if(flagExist==true){
                        break;
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //check password in database
    class Threads_check extends Thread{
        @Override
        public void run() {
            try{
                con = MySQLConnection.getConnection();

                String sqlSelect = "Select * from user_detail where userid like (?) ";
                PreparedStatement pst = con.prepareStatement(sqlSelect);
                pst.setInt(1,USERID);
                ResultSet rset = pst.executeQuery();

                while(rset.next()) {
                    pwd_fetched = rset.getString("password");
                    username_fetched = rset.getString("username");
                    userid_fetched = rset.getInt("userid");
                    signature_fetched = rset.getString("signature");

                    if (pwd_fetched.equals(PASSWORD)){
                        flagCorrect = true;
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    //create new account in database
    class Threads_signup extends Thread{
        @Override
        public void run() {
            try{
                con = MySQLConnection.getConnection();

                int rowCount = 0;
                String sqlCount = "SELECT COUNT(*) FROM user_detail";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlCount);
                while(rst.next()){
                    rowCount = rst.getInt(1);
                }

                String sqlInsert = "INSERT INTO user_detail (userid,email,password,username) VALUES (?,?,?,?)";
                PreparedStatement st = con.prepareStatement(sqlInsert);
                st.setInt(1, rowCount+1);
                st.setString(2,USEREMAIL);
                st.setString(3,PASSWORD);
                st.setString(4,"");
                st.executeUpdate();

                SharedPreference.setPrefUserId(rowCount+1);

            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }
}