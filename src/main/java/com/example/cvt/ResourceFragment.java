package com.example.cvt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cvt.databinding.FragmentResourceBinding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ResourceFragment extends Fragment implements View.OnClickListener{

    View fragmentView;
    static int RESOURCE_ID;
    static String resource_title;
    static String resource_content;
    static String resource_url;
    static int like_num;
    static int collect_num;

    public static Connection con = null;

    public static ResourceFragment newInstance() {
        return new ResourceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_resource, container, false);

        //call thread to set up the layout with data fetched
        ResourceFragment.Threads_setup threads_setup = new ResourceFragment.Threads_setup();
        threads_setup.start();
        try {
            threads_setup.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //inflate widgets
        TextView resourceTitle = (TextView) fragmentView.findViewById(R.id.title);
        resourceTitle.setText(resource_title);

        TextView resourceContent = (TextView) fragmentView.findViewById(R.id.content);
        resourceContent.setText(resource_content);

        Button btnView = (Button) fragmentView.findViewById(R.id.btn_view);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Go to the external resource",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(resource_url));
                startActivity(i);
            }
        });

        //undertake collect operation when button is clicked
        ImageButton btnCollect = (ImageButton) fragmentView.findViewById(R.id.btn_collect);
        TextView collectNum = (TextView) fragmentView.findViewById(R.id.collectnum);
        collectNum.setText("(" + collect_num + ")");
        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResourceFragment.Threads_collect threads_collect = new ResourceFragment.Threads_collect();
                threads_collect.start();
                try {
                    threads_collect.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(getContext(),"Resource collected successfully!",Toast.LENGTH_SHORT).show();

                collectNum.setText("(" + collect_num + ")");
            }
        });

        //undertake like operation when button is clicked
        ImageButton btnLike = (ImageButton) fragmentView.findViewById(R.id.btn_like);
        TextView likeNum = (TextView) fragmentView.findViewById(R.id.likenum);
        likeNum.setText("(" + like_num + ")");
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResourceFragment.Threads_like threads_like = new ResourceFragment.Threads_like();
                threads_like.start();

                try {
                    threads_like.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                likeNum.setText("(" + like_num + ")");

            }
        });

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
    }

    //set up the fragment with resource data fetched
    class Threads_setup extends Thread {
        @Override
        public void run() {
            try {
                con = MySQLConnection.getConnection();

                RESOURCE_ID = SharedPreference.getCurrentResource();

                String sqlSelect = "Select * from resource_item where resourceid like (?) ";
                PreparedStatement pst = con.prepareStatement(sqlSelect);
                pst.setInt(1, RESOURCE_ID);
                ResultSet rset = pst.executeQuery();
                while (rset.next()) {
                    resource_title = rset.getString("title");
                    resource_content = rset.getString("content");
                    resource_url = rset.getString("attachment");
                    like_num = rset.getInt("likenum");
                    collect_num = rset.getInt("collectnum");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //record the collect operation to the database
    class Threads_collect extends Thread{
        @Override
        public void run() {
            try {
                con = MySQLConnection.getConnection();

                //update collectionnum in user_details
                int collection_num = 0;
                String sqlSelect2 = "Select * from user_detail where userid like (?) ";
                PreparedStatement pstmt2 = con.prepareStatement(sqlSelect2);
                pstmt2.setInt(1,SharedPreference.getPrefUserId());
                ResultSet rset2 = pstmt2.executeQuery();
                while(rset2.next()) {
                    collection_num = rset2.getInt("collectionnum");
                }

                String updateNum = "update user_detail set collectionnum = (?) where userid = (?)";
                PreparedStatement pst2 = con.prepareStatement(updateNum);
                pst2.setInt(1, collection_num+1);
                pst2.setInt(2,SharedPreference.getPrefUserId());
                pst2.executeUpdate();

                //insert new collection operation in collection_operation table
                int rowCount2 = 0;
                String sqlCount2 = "SELECT COUNT(*) FROM collection_operation";
                Statement stmt2 = con.createStatement();
                ResultSet rst2 = stmt2.executeQuery(sqlCount2);
                while(rst2.next()){
                    rowCount2 = rst2.getInt(1);
                }

                String timeStamp = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(Calendar.getInstance().getTime());
                String sqlInsert2 = "INSERT INTO collection_operation (collectionid,collectiontype,userid,localid,title,moduleid,ifdelete,createtime) VALUES (?,?,?,?,?,?,?,?)";
                PreparedStatement st2 = con.prepareStatement(sqlInsert2);
                st2.setInt(1, rowCount2+1);
                st2.setString(2,"resource");
                st2.setInt(3,SharedPreference.getPrefUserId());
                st2.setInt(4,SharedPreference.getCurrentResource());
                st2.setString(5,resource_title);
                st2.setInt(6,SharedPreference.getCurrModule(getContext()));
                st2.setBoolean(7,false);
                st2.setString(8,timeStamp);
                st2.executeUpdate();

                collect_num++;
                String updateCollect = "update resource_item set collectnum = (?) where resourceid = (?)";
                PreparedStatement pst3 = con.prepareStatement(updateCollect);
                pst3.setInt(1, collect_num);
                pst3.setInt(2,SharedPreference.getCurrentResource());
                pst3.executeUpdate();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    //record the like operation to database
    class Threads_like extends Thread{
        @Override
        public void run() {
            try {
                con = MySQLConnection.getConnection();

                like_num++;
                String query = "update resource_item set likenum = (?) where resourceid = (?)";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, like_num);
                pst.setInt(2,SharedPreference.getCurrentResource());
                pst.executeUpdate();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}