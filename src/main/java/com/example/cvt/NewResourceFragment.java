package com.example.cvt;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cvt.databinding.FragmentNewResourceBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewResourceFragment extends Fragment implements View.OnClickListener{

    View view;
    static String title;
    static String content;
    static String attachment;
    public static Connection con = null;

    public static NewResourceFragment newInstance() {
        return new NewResourceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //inflate layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_resource, container, false);

        //inflate widgets
        EditText resourceTitle = (EditText) view.findViewById(R.id.resource_title);
        EditText resourceContent = (EditText) view.findViewById(R.id.resource_content);
        EditText resourceLAttachment = (EditText) view.findViewById(R.id.resource_attachment);

        Button btnUpload = (Button) view.findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //transmit to external website for upload
                Toast.makeText(getContext(),"Upload your file externally",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://wetransfer.com/"));
                startActivity(i);
            }
        });

        Button btnCreate = (Button) view.findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = resourceTitle.getText().toString();
                content = resourceContent.getText().toString();
                attachment = resourceLAttachment.getText().toString();

                //send new resource to the database
                NewResourceFragment.Threads_create threads_create = new NewResourceFragment.Threads_create();
                threads_create.start();
                try {
                    threads_create.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Toast.makeText(getContext(),"Resource created successfully!",Toast.LENGTH_SHORT).show();
                //go back to resource center
                Navigation.findNavController(view).navigate(R.id.action_new_resource_to_resource_center);
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

    //send new resource to database
    class Threads_create extends Thread{
        @Override
        public void run() {
            try{
                con = MySQLConnection.getConnection();

                int rowCount = 0;
                String sqlCount = "SELECT COUNT(*) FROM resource_item";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlCount);
                while(rst.next()){
                    rowCount = rst.getInt(1);
                }

                SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd HH:mm");
                Date date = new Date();
                String timeStamp = formatter.format(date);

                String sqlInsert = "INSERT INTO resource_item (resourceid,moduleid,publisherid,title,content,attachment,likenum,collectnum,createtime) VALUES (?,?,?,?,?,?,?,?,?)";
                PreparedStatement st = con.prepareStatement(sqlInsert);
                st.setInt(1, rowCount+1);
                st.setInt(2,SharedPreference.getCurrModule(getContext()));
                st.setInt(3,SharedPreference.getPrefUserId());
                st.setString(4,title);
                st.setString(5,content);
                st.setString(6,attachment);
                st.setInt(7,0);
                st.setInt(8,0);
                st.setString(9,timeStamp);
                st.executeUpdate();

                int resource_num = 0;
                String sqlSelect = "Select * from module_item where moduleid like (?) ";
                PreparedStatement pstmt = con.prepareStatement(sqlSelect);
                pstmt.setInt(1,SharedPreference.getCurrModule(getContext()));
                ResultSet rset = pstmt.executeQuery();
                while(rset.next()) {
                    resource_num = rset.getInt("resourcenum");
                }

                String query = "update module_item set resourcenum = (?) where moduleid = (?)";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, resource_num+1);
                pst.setInt(2,SharedPreference.getCurrModule(getContext()));
                pst.executeUpdate();

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
                st2.setString(2,"resource");
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