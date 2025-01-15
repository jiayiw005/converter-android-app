package com.example.cvt;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.cvt.signup.SignupFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ModuleFragment extends Fragment implements View.OnClickListener {
    static int MODULE_ID;
    static String INST_URL;
    static int discussion_num;
    static int manager_id;

    View fragmentView;
    public static Connection con = null;
    static String[] discussion_title;
    static int[] discussion_id;
    static String[] create_time;
    static int[] publisher_id;
    static String[] publisher_name;
    static int[] collect_num;
    ListView listView;
    private ModuleViewModel mViewModel;

    public static ModuleFragment newInstance() {
        return new ModuleFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate view
        fragmentView=inflater.inflate(R.layout.fragment_module,container,false);

        //call thread to fetch module data from database
        ModuleFragment.Threads_setup threads_setup = new ModuleFragment.Threads_setup();
        threads_setup.start();
        try {
            threads_setup.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //inflate listview
        listView = fragmentView.findViewById(R.id.discussionList);
        ResourceAdapter resourceAdapter = new ResourceAdapter(getContext(),discussion_title,create_time,publisher_name,collect_num);
        listView.setAdapter(resourceAdapter);
        setListViewHeightBasedOnChildren(listView);

        //set onClickListener for listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("start procedure");
                Navigation.findNavController(fragmentView).navigate(R.id.action_module_to_discussion);
                SharedPreference.setCurrentDiscussion(discussion_id[position]);
                System.out.println("should succeed");
            }
        });

        int module = SharedPreference.getCurrModule(getContext());

        //inflate other widgets
        ImageButton instructionBoard = (ImageButton) fragmentView.findViewById(R.id.instruction_board);
        ImageButton resourceBoard = (ImageButton) fragmentView.findViewById(R.id.resource_board);
        if(module == 1){
            instructionBoard.setImageResource(R.drawable.philosophy_instruction);
            resourceBoard.setImageResource(R.drawable.philosophy_resource_center);
        }else if (module ==2){
            instructionBoard.setImageResource(R.drawable.theater_instruction);
            resourceBoard.setImageResource(R.drawable.theater_resource_center);
        }else if (module ==3){
            instructionBoard.setImageResource(R.drawable.cs_instruction);
            resourceBoard.setImageResource(R.drawable.cs_resource_center);
        }
        instructionBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go the external instruction
                Toast.makeText(getContext(),"Go to the external instruction",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(INST_URL));
                startActivity(i);
            }
        });

        resourceBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to the resource center
                Navigation.findNavController(fragmentView).navigate(R.id.action_module_to_resource_nest);
            }
        });

        FloatingActionButton newDiscussion = (FloatingActionButton) fragmentView.findViewById(R.id.new_discussion);
        newDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to the new discussion GUI
                Navigation.findNavController(fragmentView).navigate(R.id.action_module_to_new_discussion);
            }
        });

        return fragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(fragmentView).navigate(R.id.action_module_to_home);
                }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onClick(View v) {

    }

    //ensure the listview is scrollable
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

    class Threads_setup extends Thread{
        @Override
        public void run() {
            try{
                con = MySQLConnection.getConnection();

                MODULE_ID = SharedPreference.getCurrModule(getContext());

                //get module information from the database
                String sqlSelect = "Select * from module_item where moduleid like (?) ";
                PreparedStatement pst = con.prepareStatement(sqlSelect);
                pst.setInt(1,MODULE_ID);
                ResultSet rset = pst.executeQuery();
                while(rset.next()) {
                    INST_URL = rset.getString("insturl");
                    manager_id = rset.getInt("managerid");
                    discussion_num = rset.getInt("discussionnum");
                }

                //count the number of discussion items
                int rowCount = 0;
                String sqlCount = "SELECT COUNT(*) FROM discussion_item";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlCount);
                while(rst.next()){
                    rowCount = rst.getInt(1);
                }

                //traverse through the discussion_item table to fetch ones under this module
                String selectTitle = "Select * from discussion_item where discussionid like (?) ";
                discussion_title = new String[discussion_num];
                discussion_id = new int[discussion_num];
                create_time = new String[discussion_num];
                publisher_id = new int[discussion_num];
                publisher_name = new String[discussion_num];
                collect_num = new int[discussion_num];
                int discussion_idx = 0;
                for(int i=1;i<=rowCount;i++){
                    PreparedStatement pstmt = con.prepareStatement(selectTitle);
                    pstmt.setInt(1,i);
                    ResultSet resultSet = pstmt.executeQuery();

                    while(resultSet.next()) {
                        int module_fetched = resultSet.getInt("moduleid");
                        String title_fetched = resultSet.getString("title");
                        String time_fetched = resultSet.getString("createtime");
                        int user_id_fetched = resultSet.getInt("publisherid");
                        int collect_num_fetched = resultSet.getInt("collectnum");
                        String user_name = null;

                        String selectName = "Select * from user_detail where userid like (?) ";
                        PreparedStatement stmtName = con.prepareStatement(selectName);
                        stmtName.setInt(1,user_id_fetched);
                        ResultSet rsetName = stmtName.executeQuery();
                        while(rsetName.next()) {
                            user_name = rsetName.getString("username");
                        }

                        if (module_fetched==MODULE_ID){
                            discussion_title[discussion_idx] = title_fetched;
                            discussion_id[discussion_idx] = i;
                            create_time[discussion_idx] = time_fetched;
                            publisher_id[discussion_idx] = user_id_fetched;
                            publisher_name[discussion_idx] = user_name;
                            collect_num[discussion_idx] = collect_num_fetched;
                            discussion_idx++;
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }
}