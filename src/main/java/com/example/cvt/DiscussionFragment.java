package com.example.cvt;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DiscussionFragment extends Fragment {

    private DiscussionViewModel mViewModel;

    public static DiscussionFragment newInstance() {
        return new DiscussionFragment();
    }

    static int DISCUSSION_ID;
    static String discussion_title;
    static String discussion_content;
    static int comment_num;
    static int like_num;
    static int collect_num;
    static String discussion_publisher;
    static String new_comment;
    View fragmentView;
    ListView listView;
    public static Connection con = null;
    static String[] comment;
    static int[] comment_id;
    static String[] comment_time;
    static String[] comment_publisher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_discussion, container, false);

        //call thread to fetch information of the discussion from database
        DiscussionFragment.Threads_setup threads_setup = new DiscussionFragment.Threads_setup();
        threads_setup.start();
        try {
            threads_setup.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //inflate widgets and set texts
        TextView discussionTitle = (TextView) fragmentView.findViewById(R.id.title);
        discussionTitle.setText(discussion_title);

        TextView discussionContent = (TextView) fragmentView.findViewById(R.id.content);
        discussionContent.setText(discussion_content);

        TextView discussionPublisher = (TextView) fragmentView.findViewById(R.id.publisher);
        discussionPublisher.setText(discussion_publisher);

        listView = fragmentView.findViewById(R.id.commentList);
        CommentAdapter commentAdapter = new CommentAdapter(getContext(),comment,comment_time,comment_publisher);
        listView.setAdapter(commentAdapter);
        setListViewHeightBasedOnChildren(listView);

        EditText newComment = (EditText) fragmentView.findViewById(R.id.new_comment);
        ImageButton sendComment = (ImageButton) fragmentView.findViewById(R.id.send_comment);

        ImageButton btnCollect = (ImageButton) fragmentView.findViewById(R.id.btn_collect);
        TextView collectNum = (TextView) fragmentView.findViewById(R.id.collectnum) ;
        collectNum.setText("("+collect_num+")");
        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call thread to record the collection operation
                DiscussionFragment.Threads_collect threads_collect = new DiscussionFragment.Threads_collect();
                threads_collect.start();
                try {
                    threads_collect.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //toast message to remind the user
                Toast.makeText(getContext(),"Discussion collected successfully!",Toast.LENGTH_SHORT).show();
                collectNum.setText("("+collect_num+")");
            }
        });

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_comment = newComment.getText().toString();

                //call thread to store the new comment to database
                DiscussionFragment.Threads_comment threads_comment = new DiscussionFragment.Threads_comment();
                threads_comment.start();
                try {
                    threads_comment.join();
                    Toast.makeText(getContext(),"Comment sent successfully!",Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    Toast.makeText(getContext(),"Connection failed",Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }

                //call thread to refresh the listview
                DiscussionFragment.Threads_setup threads_setup = new DiscussionFragment.Threads_setup();
                threads_setup.start();
                try {
                    threads_setup.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                listView = fragmentView.findViewById(R.id.commentList);
                CommentAdapter commentAdapter = new CommentAdapter(getContext(),comment,comment_time,comment_publisher);
                listView.setAdapter(commentAdapter);
                setListViewHeightBasedOnChildren(listView);

                //set the comment field to empty
                newComment.setText("");
            }
        });

        ImageButton btnLike = (ImageButton) fragmentView.findViewById(R.id.btn_like);
        TextView likeNum = (TextView) fragmentView.findViewById(R.id.likenum);
        likeNum.setText("(" + like_num + ")");

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call thread to increment like number in database
                DiscussionFragment.Threads_like threads_like = new DiscussionFragment.Threads_like();
                threads_like.start();
                try {
                    threads_like.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //display incremented like number
                likeNum.setText("(" + like_num + ")");
            }
        });

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    //fetech information about the discussion
    class Threads_setup extends Thread {
        @Override
        public void run() {
            try {
                con = MySQLConnection.getConnection();

                DISCUSSION_ID = SharedPreference.getCurrentDiscussion();

                int publisher_id = 0;
                String sqlSelect = "Select * from discussion_item where discussionid like (?) ";
                PreparedStatement pst = con.prepareStatement(sqlSelect);
                pst.setInt(1, DISCUSSION_ID);
                ResultSet rset = pst.executeQuery();
                while (rset.next()) {
                    comment_num = rset.getInt("commentnum");
                    discussion_title = rset.getString("title");
                    discussion_content = rset.getString("content");
                    publisher_id = rset.getInt("publisherid");
                    like_num = rset.getInt("likenum");
                    collect_num = rset.getInt("collectnum");
                }

                //fetch username
                String sqlSelect2 = "Select * from user_detail where userid like (?) ";
                PreparedStatement pstmt2 = con.prepareStatement(sqlSelect2);
                pstmt2.setInt(1,publisher_id);
                ResultSet rset2 = pstmt2.executeQuery();
                while(rset2.next()) {
                    discussion_publisher = rset2.getString("username");
                }

                int rowCount = 0;
                String sqlCount = "SELECT COUNT(*) FROM comment_item";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlCount);
                while (rst.next()) {
                    rowCount = rst.getInt(1);
                }

                String commentPublisher = null;
                String selectTitle = "Select * from comment_item where commentid like (?) ";
                comment = new String[comment_num];
                comment_time = new String[comment_num];
                comment_publisher = new String[comment_num];
                comment_id = new int[comment_num];
                int comment_idx = 0;
                for (int i = 1; i <= rowCount; i++) {
                    PreparedStatement pstmt = con.prepareStatement(selectTitle);
                    pstmt.setInt(1, i);
                    ResultSet resultSet = pstmt.executeQuery();

                    while (resultSet.next()) {
                        int discussion_fetched = resultSet.getInt("discussionid");
                        String comment_content = resultSet.getString("content");
                        String commentTime = resultSet.getString("createtime");
                        int commentPublisherId = resultSet.getInt("publisherid");


                        String selectName = "Select * from user_detail where userid like (?) ";
                        PreparedStatement stmtName = con.prepareStatement(selectName);
                        stmtName.setInt(1,commentPublisherId);
                        ResultSet rsetName = stmtName.executeQuery();
                        while(rsetName.next()) {
                            commentPublisher = rsetName.getString("username");
                        }

//                        FindUserName findUserName = new FindUserName();
//                        String commentPublisher = findUserName.find(commentPublisherId);

                        if (discussion_fetched == DISCUSSION_ID) {
                            comment[comment_idx] = comment_content;
                            comment_time[comment_idx] = commentTime;
                            comment_publisher[comment_idx] = commentPublisher;
                            comment_id[comment_idx] = i;
                            comment_idx++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //send comment to the database
    class Threads_comment extends Thread{
        @Override
        public void run() {
            try{
                con = MySQLConnection.getConnection();

                int rowCount = 0;
                String sqlCount = "SELECT COUNT(*) FROM comment_item";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlCount);
                while(rst.next()){
                    rowCount = rst.getInt(1);
                }

                SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd HH:mm");
                Date date = new Date();
                String timeStamp = formatter.format(date);

                String sqlInsert = "INSERT INTO comment_item (commentid,discussionid,publisherid,content,likenum,collectnum,createtime) VALUES (?,?,?,?,?,?,?)";
                PreparedStatement st = con.prepareStatement(sqlInsert);
                st.setInt(1, rowCount+1);
                st.setInt(2,SharedPreference.getCurrentDiscussion());
                st.setInt(3,SharedPreference.getPrefUserId());
                st.setString(4,new_comment);
                st.setInt(5,0);
                st.setInt(6,0);
                st.setString(7,timeStamp);
                st.executeUpdate();

                int comment_num = 0;
                String sqlSelect = "Select * from discussion_item where discussionid like (?) ";
                PreparedStatement pstmt = con.prepareStatement(sqlSelect);
                pstmt.setInt(1,SharedPreference.getCurrentDiscussion());
                ResultSet rset = pstmt.executeQuery();
                while(rset.next()) {
                    comment_num = rset.getInt("commentnum");
                }

                String query = "update discussion_item set commentnum = (?) where discussionid = (?)";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, comment_num+1);
                pst.setInt(2,SharedPreference.getCurrentDiscussion());
                pst.executeUpdate();

                //update user's contribution
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
                st2.setString(2,"comment");
                st2.setInt(3,SharedPreference.getPrefUserId());
                st2.setInt(4,SharedPreference.getCurrentDiscussion());
                st2.setString(5,new_comment);
                st2.setInt(6,SharedPreference.getCurrModule(getContext()));
                st2.setString(7,timeStamp);
                st2.executeUpdate();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    //update user's collection
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
                st2.setString(2,"discussion");
                st2.setInt(3,SharedPreference.getPrefUserId());
                st2.setInt(4,SharedPreference.getCurrentDiscussion());
                st2.setString(5,discussion_title);
                st2.setInt(6,SharedPreference.getCurrModule(getContext()));
                st2.setBoolean(7,false);
                st2.setString(8,timeStamp);
                st2.executeUpdate();

                //update collectnum for the discussion item
                collect_num++;
                String updateCollect = "update discussion_item set collectnum = (?) where discussionid = (?)";
                PreparedStatement pst3 = con.prepareStatement(updateCollect);
                pst3.setInt(1, collect_num);
                pst3.setInt(2,SharedPreference.getCurrentDiscussion());
                pst3.executeUpdate();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    //increment the like number in database
    class Threads_like extends Thread{
        @Override
        public void run() {
            try {
                con = MySQLConnection.getConnection();

                String sqlSelect = "Select * from discussion_item where discussionid like (?) ";
                PreparedStatement pstmt = con.prepareStatement(sqlSelect);
                pstmt.setInt(1,SharedPreference.getCurrentDiscussion());
                ResultSet rset = pstmt.executeQuery();
                while(rset.next()) {
                    like_num = rset.getInt("likenum");
                }

                like_num++;

                String query = "update discussion_item set likenum = (?) where discussionid = (?)";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, like_num);
                pst.setInt(2,SharedPreference.getCurrentDiscussion());
                pst.executeUpdate();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}