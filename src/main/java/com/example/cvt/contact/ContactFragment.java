package com.example.cvt.contact;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cvt.MySQLConnection;
import com.example.cvt.NewModuleFragment;
import com.example.cvt.R;
import com.example.cvt.SharedPreference;
import com.example.cvt.databinding.FragmentContactBinding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ContactFragment extends Fragment implements View.OnClickListener{

    View view;
    Button btnSend;
    EditText userEmail;
    EditText messageForm;

    static String email_text;
    static String message_text;
    public static Connection con = null;

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //inflate frgament layout
        view = inflater.inflate(R.layout.fragment_contact, container, false);

        //inflate widgets
        userEmail = (EditText) view.findViewById(R.id.user_email_contact);
        messageForm = (EditText) view.findViewById(R.id.message);

        btnSend = (Button) view.findViewById(R.id.send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get texts from widgets
                email_text = userEmail.getText().toString();
                message_text = messageForm.getText().toString();

                //send message to the database
                ContactFragment.Threads_send threads_send = new ContactFragment.Threads_send();
                threads_send.start();
                try {
                    threads_send.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //set texts back to empty
                userEmail.setText("");
                messageForm.setText("");
                Toast.makeText(getContext(),"Message sent to the developer",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onClick(View v) {
    }

    //send message to the database
    class Threads_send extends Thread{
        @Override
        public void run() {
            try {
                con = MySQLConnection.getConnection();

                int rowCount = 0;
                String sqlCount = "SELECT COUNT(*) FROM message_item";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlCount);
                while(rst.next()){
                    rowCount = rst.getInt(1);
                }

                String sqlInsert = "INSERT INTO message_item (messageid,messagetype,title,content,userid,email) VALUES (?,?,?,?,?,?)";
                PreparedStatement st = con.prepareStatement(sqlInsert);
                st.setInt(1, rowCount+1);
                st.setString(2,"message");
                st.setString(3,"");
                st.setString(4,message_text);
                st.setInt(5, SharedPreference.getPrefUserId());
                st.setString(6,email_text);
                st.executeUpdate();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}