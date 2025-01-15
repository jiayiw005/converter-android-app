package com.example.cvt.collection;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cvt.MySQLConnection;
import com.example.cvt.R;
import com.example.cvt.RecyclerAdapter;
import com.example.cvt.SharedPreference;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CollectionFragment extends Fragment implements View.OnClickListener {

    List<Integer> collection_id = new ArrayList<>();
    List<String> item_title = new ArrayList<>();
    List<Integer> item_publisher = new ArrayList<>();
    List<String> item_type = new ArrayList<>();
    List<Integer> item_id = new ArrayList<>();
    List<Integer> item_module = new ArrayList<>();
    List<String> collect_time = new ArrayList<>();
    static int collection_num;
    static int delete_position;
    View view;
    SearchView searchView;

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    public static Connection con = null;

    public static CollectionFragment newInstance() {
        return new CollectionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //inflate fragment view
        view = inflater.inflate(R.layout.fragment_collection, container, false);

        boolean loginstatus = SharedPreference.getLoginStatus(getContext());
        if(loginstatus == false){
            Snackbar.make(view, "You're not logged in.", Snackbar.LENGTH_LONG).setAction("Log in", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(view).navigate(R.id.action_collection_to_signup);
                }
            }).show();
        }

        //set up fragment with collection data
        CollectionFragment.Threads_setup threads_setup = new CollectionFragment.Threads_setup();
        threads_setup.start();
        try {
            threads_setup.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Set up recycler view⚠️⚠️
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(item_title, item_type, collect_time, item_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        return view;
    }

    private void filterList(String text) {
        List<Integer> filtered_collection_id = new ArrayList<>();
        List<String> filtered_item_title = new ArrayList<>();
        List<Integer> filtered_item_publisher = new ArrayList<>();
        List<String> filtered_item_type = new ArrayList<>();
        List<Integer> filtered_item_id = new ArrayList<>();
        List<Integer> filtered_item_module = new ArrayList<>();
        List<String> filtered_collect_time = new ArrayList<>();

        for(int i=0;i<item_title.size();i++){
            if(item_title.get(i).toLowerCase().contains(text.toLowerCase())){
                filtered_collection_id.add(collection_id.get(i));
                filtered_item_title.add(item_title.get(i));
                filtered_item_publisher.add(item_publisher.get(i));
                filtered_item_type.add(item_type.get(i));
                filtered_item_id.add(item_id.get(i));
                filtered_item_module.add(item_module.get(i));
                filtered_collect_time.add(collect_time.get(i));
            }
        }

        if(filtered_item_title.isEmpty()){
            recyclerAdapter.setFilteredList(filtered_item_title,filtered_item_type,filtered_item_id,filtered_collect_time);
            Toast.makeText(getContext(),"No content found.",Toast.LENGTH_SHORT).show();
        }else{
            recyclerAdapter.setFilteredList(filtered_item_title,filtered_item_type,filtered_item_id,filtered_collect_time);

        }
    }

    int deletedCol = -1;
    String deletedTitle = null;
    int deletedId = -1;
    int deletedModule = -1;
    String deletedType = null;
    int deletedUser = -1;
    String deletedTime = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    delete_position = position;
                    collection_num--;

                    deletedCol = collection_id.get(position);
                    deletedTitle = item_title.get(position);
                    deletedId = item_id.get(position);
                    deletedModule = item_module.get(position);
                    deletedType = item_type.get(position);
                    deletedUser = item_publisher.get(position);
                    deletedTime = collect_time.get(position);

                    CollectionFragment.Threads_delete threads_delete = new CollectionFragment.Threads_delete();
                    threads_delete.start();
                    try {
                        threads_delete.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    collection_id.remove(position);
                    item_title.remove(position);
                    item_id.remove(position);
                    item_module.remove(position);
                    item_type.remove(position);
                    item_publisher.remove(position);
                    collect_time.remove(position);

                    recyclerAdapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView, deletedTitle + " DELETED", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            collection_num++;

                            collection_id.add(position, deletedCol);
                            item_title.add(position, deletedTitle);
                            item_id.add(position, deletedId);
                            item_module.add(position, deletedModule);
                            item_type.add(position, deletedType);
                            item_publisher.add(position, deletedUser);
                            collect_time.add(position, deletedTime);

                            CollectionFragment.Threads_undo threads_undo = new CollectionFragment.Threads_undo();
                            threads_undo.start();
                            try {
                                threads_undo.join();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            recyclerAdapter.notifyItemInserted(position);
                        }
                    }).show();
                    break;
                case ItemTouchHelper.RIGHT:
//                    String collectionTitle = collectionList.get(position);
//                    archivedCollection.add(collectionTitle);
//
//                    Snackbar.make(recyclerView,collectionTitle + ", Archived",Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            archivedCollection.remove(archivedCollection.lastIndexOf(collectionTitle));
//                        }
//                    });
                    break;
            }
        }

    };



    @Override
    public void onClick(View v) {
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //set up listview items with collection table in database
    class Threads_setup extends Thread{
        @Override
        public void run() {
            try{
                con = MySQLConnection.getConnection();

                if(item_title!=null){
                    collection_id.clear();
                    item_title.clear();
                    item_id.clear();
                    item_type.clear();
                    item_module.clear();
                    item_publisher.clear();
                    collect_time.clear();
                }

                String sqlSelect = "Select * from user_detail where userid like (?) ";
                PreparedStatement pst = con.prepareStatement(sqlSelect);
                pst.setInt(1,SharedPreference.getPrefUserId());
                ResultSet rset = pst.executeQuery();
                while(rset.next()) {
                    collection_num = rset.getInt("collectionnum");
                }

                int rowCount = 0;
                String sqlCount = "SELECT COUNT(*) FROM collection_operation";
                Statement stmt = con.createStatement();
                ResultSet rst = stmt.executeQuery(sqlCount);
                while(rst.next()){
                    rowCount = rst.getInt(1);
                }

                String selectItem = "Select * from collection_operation where collectionid like (?) ";

                for(int i=1;i<=rowCount;i++){
                    PreparedStatement pstmt = con.prepareStatement(selectItem);
                    pstmt.setInt(1,i);
                    ResultSet resultSet = pstmt.executeQuery();

                    while(resultSet.next()) {
                        int id_fetched = i;
                        int item_id_fetched = resultSet.getInt("localid");
                        int user_fetched = resultSet.getInt("userid");
                        String type_fetched = resultSet.getString("collectiontype");
                        String title_fetched = resultSet.getString("title");
                        int module_fetched = resultSet.getInt("moduleid");
                        boolean delete_fetched = resultSet.getBoolean("ifdelete");
                        String time_fetched = resultSet.getString("createtime");

                        if (user_fetched==SharedPreference.getPrefUserId()&&delete_fetched!=true){
                            collection_id.add(id_fetched);
                            item_title.add(title_fetched);
                            item_id.add(item_id_fetched);
                            item_type.add(type_fetched);
                            item_module.add(module_fetched);
                            item_publisher.add(user_fetched);
                            collect_time.add(time_fetched);
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    class Threads_delete extends Thread{
        @Override
        public void run() {
            try{
                con = MySQLConnection.getConnection();

                //update collectionnum in user_details
                String updateNum = "update user_detail set collectionnum = (?) where userid = (?)";
                PreparedStatement pst2 = con.prepareStatement(updateNum);
                pst2.setInt(1, collection_num);
                pst2.setInt(2,SharedPreference.getPrefUserId());
                pst2.executeUpdate();

                //update collectnum for the item
                if (item_type.get(delete_position).equals("discussion")){
                    int collect_num = 0;
                    String sqlSelect3 = "Select * from discussion_item where discussionid like (?) ";
                    PreparedStatement pstmt3 = con.prepareStatement(sqlSelect3);
                    pstmt3.setInt(1,item_id.get(delete_position));
                    ResultSet rset3 = pstmt3.executeQuery();
                    while(rset3.next()) {
                        collect_num = rset3.getInt("collectnum");
                    }

                    String updateCollect = "update discussion_item set collectnum = (?) where discussionid = (?)";
                    PreparedStatement pst3 = con.prepareStatement(updateCollect);
                    pst3.setInt(1, collect_num-1);
                    pst3.setInt(2,item_id.get(delete_position));
                    pst3.executeUpdate();
                }
                else if (item_type.get(delete_position).equals("resource")){
                    int collect_num = 0;
                    String sqlSelect4 = "Select * from resource_item where resourceid like (?) ";
                    PreparedStatement pstmt4 = con.prepareStatement(sqlSelect4);
                    pstmt4.setInt(1,item_id.get(delete_position));
                    ResultSet rset4 = pstmt4.executeQuery();
                    while(rset4.next()) {
                        collect_num = rset4.getInt("collectnum");
                    }

                    String updateCollect = "update resource_item set collectnum = (?) where resourceid = (?)";
                    PreparedStatement pst4 = con.prepareStatement(updateCollect);
                    pst4.setInt(1, collect_num-1);
                    pst4.setInt(2,item_id.get(delete_position));
                    pst4.executeUpdate();
                }

                //update ifdelete status of collection
                String updateStatus = "update collection_operation set ifdelete = (?) where collectionid = (?)";
                PreparedStatement pst = con.prepareStatement(updateStatus);
                pst.setBoolean(1, true);
                pst.setInt(2,collection_id.get(delete_position));
                pst.executeUpdate();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    class Threads_undo extends Thread{
        @Override
        public void run() {
            try{
                con = MySQLConnection.getConnection();

                //update collectionnum in user_details
                String updateNum = "update user_detail set collectionnum = (?) where userid = (?)";
                PreparedStatement pst2 = con.prepareStatement(updateNum);
                pst2.setInt(1, collection_num);
                pst2.setInt(2,SharedPreference.getPrefUserId());
                pst2.executeUpdate();

                //update collectnum for the item
                if (item_type.get(delete_position).equals("discussion")){
                    int collect_num = 0;
                    String sqlSelect3 = "Select * from discussion_item where discussionid like (?) ";
                    PreparedStatement pstmt3 = con.prepareStatement(sqlSelect3);
                    pstmt3.setInt(1,item_id.get(delete_position));
                    ResultSet rset3 = pstmt3.executeQuery();
                    while(rset3.next()) {
                        collect_num = rset3.getInt("collectnum");
                    }

                    String updateCollect = "update discussion_item set collectnum = (?) where discussionid = (?)";
                    PreparedStatement pst3 = con.prepareStatement(updateCollect);
                    pst3.setInt(1, collect_num+1);
                    pst3.setInt(2,item_id.get(delete_position));
                    pst3.executeUpdate();
                }
                else if (item_type.get(delete_position).equals("resource")){
                    int collect_num = 0;
                    String sqlSelect4 = "Select * from resource_item where resourceid like (?) ";
                    PreparedStatement pstmt4 = con.prepareStatement(sqlSelect4);
                    pstmt4.setInt(1,item_id.get(delete_position));
                    ResultSet rset4 = pstmt4.executeQuery();
                    while(rset4.next()) {
                        collect_num = rset4.getInt("collectnum");
                    }

                    String updateCollect = "update resource_item set collectnum = (?) where resourceid = (?)";
                    PreparedStatement pst4 = con.prepareStatement(updateCollect);
                    pst4.setInt(1, collect_num+1);
                    pst4.setInt(2,item_id.get(delete_position));
                    pst4.executeUpdate();
                }

                //update ifdelete status of collection
                String updateStatus = "update collection_operation set ifdelete = (?) where collectionid = (?)";
                PreparedStatement pst = con.prepareStatement(updateStatus);
                pst.setBoolean(1, false);
                pst.setInt(2,collection_id.get(delete_position));
                pst.executeUpdate();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}