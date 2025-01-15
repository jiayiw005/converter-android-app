package com.example.cvt;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.cvt.databinding.FragmentNewDiscussionBinding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class NewDiscussionFragment extends Fragment {

    View view;
    static String title;
    static String content;
    public static Connection con = null;

    public NewDiscussionFragment() {
        // Required empty public constructor
    }
    public static NewDiscussionFragment newInstance() { return new NewDiscussionFragment();}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_discussion, container, false);

        //inflate the widgets, get texts or set onClickListeners
        EditText discussionTitle = (EditText) view.findViewById(R.id.discussion_title);
        EditText discussionContent = (EditText) view.findViewById(R.id.discussion_content);

        Button btnCreate = (Button) view.findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = discussionTitle.getText().toString();
                content = discussionContent.getText().toString();

                //call thread to create send the new discussion information to the database
                NewDiscussionFragment.Threads_create threads_create = new NewDiscussionFragment.Threads_create();
                threads_create.start();
                try {
                    threads_create.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Toast.makeText(getContext(),"Discussion created successfully!",Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_new_discussion_to_module);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //send new discussion to database
    class Threads_create extends Thread{
        @Override
        public void run() {
            try{
                con = MySQLConnection.getConnection();

                //get number of discussions in the table
                int rowCount = 0;
                String sqlCount = "SELECT COUNT(*) FROM discussion_item";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlCount);
                while(rst.next()){
                    rowCount = rst.getInt(1);
                }

                //insert the new discussion as the next value
                SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd HH:mm");
                Date date = new Date();
                String timeStamp = formatter.format(date);

                String sqlInsert = "INSERT INTO discussion_item (discussionid,moduleid,publisherid," +
                        "createtime,title,content,commentnum,likenum,collectnum) VALUES (?,?,?,?,?,?,?,?,?)";
                PreparedStatement st = con.prepareStatement(sqlInsert);
                st.setInt(1, rowCount+1);
                st.setInt(2,SharedPreference.getCurrModule(getContext()));
                st.setInt(3,SharedPreference.getPrefUserId());
                st.setString(4,timeStamp);
                st.setString(5,title);
                st.setString(6,content);
                st.setInt(7,0);
                st.setInt(8,0);
                st.setInt(9,0);
                st.executeUpdate();

                //update module table
                int discussion_num = 0;
                String sqlSelect = "Select * from module_item where moduleid like (?) ";
                PreparedStatement pstmt = con.prepareStatement(sqlSelect);
                pstmt.setInt(1,SharedPreference.getCurrModule(getContext()));
                ResultSet rset = pstmt.executeQuery();
                while(rset.next()) {
                    discussion_num = rset.getInt("discussionnum");
                }

                String query = "update module_item set discussionnum = (?) where moduleid = (?)";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, discussion_num+1);
                pst.setInt(2,SharedPreference.getCurrModule(getContext()));
                pst.executeUpdate();

                //update user_detail table
                int contribution_num = 0;
                String sqlSelect2 = "Select * from user_detail where userid like (?) ";
                PreparedStatement pstmt2 = con.prepareStatement(sqlSelect2);
                pstmt2.setInt(1,SharedPreference.getPrefUserId());
                ResultSet rset2 = pstmt2.executeQuery();
                while(rset2.next()) {
                    contribution_num = rset2.getInt("contributionnum");
                }

                String updateNum = "update user_detail set contributionnum = (?) where userid = (?)";
                PreparedStatement pst2 = con.prepareStatement(updateNum);
                pst2.setInt(1, contribution_num+1);
                pst2.setInt(2,SharedPreference.getPrefUserId());
                pst2.executeUpdate();

                //update contribution table
                int rowCount2 = 0;
                String sqlCount2 = "SELECT COUNT(*) FROM contribution_operation";
                Statement stmt2 = con.createStatement();
                ResultSet rst2 = stmt2.executeQuery(sqlCount2);
                while(rst2.next()){
                    rowCount2 = rst2.getInt(1);
                }

                String sqlInsert2 = "INSERT INTO contribution_operation (contributionid,contributiontype,userid,localid,title,moduleid,createtime) VALUES (?,?,?,?,?,?,?)";
                PreparedStatement st2 = con.prepareStatement(sqlInsert2);
                st2.setInt(1, rowCount2+1);
                st2.setString(2,"discussion");
                st2.setInt(3,SharedPreference.getPrefUserId());
                st2.setInt(4,rowCount+1);
                st2.setString(5,title);
                st2.setInt(6,SharedPreference.getCurrModule(getContext()));
                st2.setString(7,timeStamp);
                st2.executeUpdate();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}