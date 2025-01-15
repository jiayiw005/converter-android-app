package com.example.cvt;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.example.cvt.databinding.FragmentResourceBinding;
import com.example.cvt.databinding.FragmentResourceCenterBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ResourceCenterFragment extends Fragment {
    View fragmentView;
    ListView listView;
    static int MODULE_ID;
    static int resource_num;
    static String[] resource_title;
    static int[] resource_id;
    static String[] create_time;
    static int[] publisher_id;
    static String[] publisher_name;
    static int[] collect_num;

    public static Connection con = null;

    public static ResourceCenterFragment newInstance() {
        return new ResourceCenterFragment();
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        //inflate fragment view
        fragmentView=inflater.inflate(R.layout.fragment_resource_center,container,false);

        //call thread to set up the layout with data fetched
        ResourceCenterFragment.Threads_setup threads_setup = new ResourceCenterFragment.Threads_setup();
        threads_setup.start();
        try {
            threads_setup.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //inflate listview and set onClickListener
        listView = fragmentView.findViewById(R.id.resourceList);
        ResourceAdapter resourceAdapter = new ResourceAdapter(getContext(),resource_title,create_time,publisher_name,collect_num);
        listView.setAdapter(resourceAdapter);
        setListViewHeightBasedOnChildren(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NavHostFragment.findNavController(ResourceCenterFragment.this).
                        navigate(R.id.action_resource_center_to_resource);
                SharedPreference.setCurrentResource(resource_id[i]);
            }
        });

        //inflate button widget
        FloatingActionButton btnNewResource = (FloatingActionButton) fragmentView.findViewById(R.id.new_resource);
        btnNewResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ResourceCenterFragment.this).
                        navigate(R.id.action_resource_center_to_new_resource);
            }
        });

        return fragmentView;
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
    public void onDestroy() {
        super.onDestroy();
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

    //set up the view by fetching from database
    class Threads_setup extends Thread{
        @Override
        public void run() {
            try{
                con = MySQLConnection.getConnection();

                MODULE_ID = SharedPreference.getCurrModule(getContext());
                String sqlSelect = "Select * from module_item where moduleid like (?) ";
                PreparedStatement pst = con.prepareStatement(sqlSelect);
                pst.setInt(1,MODULE_ID);
                ResultSet rset = pst.executeQuery();
                while(rset.next()) {
                    resource_num = rset.getInt("resourcenum");
                }

                int rowCount = 0;
                String sqlCount = "SELECT COUNT(*) FROM resource_item";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlCount);
                while(rst.next()){
                    rowCount = rst.getInt(1);
                }

                String selectTitle = "Select * from resource_item where resourceid like (?) ";
                resource_title = new String[resource_num];
                resource_id = new int[resource_num];
                create_time = new String[resource_num];
                publisher_id = new int[resource_num];
                publisher_name = new String[resource_num];
                collect_num = new int[resource_num];
                int resource_idx = 0;
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
                            resource_title[resource_idx] = title_fetched;
                            resource_id[resource_idx] = i;
                            create_time[resource_idx] = time_fetched;
                            publisher_id[resource_idx] = user_id_fetched;
                            publisher_name[resource_idx] = user_name;
                            collect_num[resource_idx] = collect_num_fetched;
                            resource_idx++;
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}