package com.example.cvt;

import androidx.lifecycle.ViewModelProvider;

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

import com.example.cvt.databinding.FragmentNewModuleBinding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewModuleFragment extends Fragment implements View.OnClickListener{


    public static NewModuleFragment newInstance() {
        return new NewModuleFragment();
    }

    public FragmentNewModuleBinding binding;

    View view;
    Button btnSend;
    static String email_text;
    static String title_text;
    static String description_text;

    public static Connection con = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_module, container, false);

        EditText email = view.findViewById(R.id.new_module_email);
        EditText title = view.findViewById(R.id.new_module_title);
        EditText description = view.findViewById(R.id.new_module_description);

        btnSend = (Button) view.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_text = email.getText().toString();
                title_text = title.getText().toString();
                description_text = description.getText().toString();

                NewModuleFragment.Threads_send threads_send = new NewModuleFragment.Threads_send();
                threads_send.start();
                try {
                    threads_send.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                email.setText("");
                title.setText("");
                description.setText("");
                Toast.makeText(getContext(),"Proposal sent to the developer",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
    }

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
                st.setString(2,"module");
                st.setString(3,title_text);
                st.setString(4,description_text);
                st.setInt(5,SharedPreference.getPrefUserId());
                st.setString(6,email_text);
                st.executeUpdate();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}