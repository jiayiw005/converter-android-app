package com.example.cvt;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "RecyclerAdapter";

    List<String> itemSearched = new ArrayList<>();
    List<String> titleList = new ArrayList<>();
    List<String> typeList = new ArrayList<>();
    List<String> timeList = new ArrayList<>();
    List<Integer> idList = new ArrayList<>();

    public RecyclerAdapter(List<String> titleList, List<String> typeList, List<String> timeList, List<Integer> idList) {
        this.titleList = titleList;
        this.typeList = typeList;
        this.timeList = timeList;
        this.idList = idList;
    }

    public void setFilteredList(List<String> filtered_item_title, List<String> filtered_item_type,
                                List<Integer> filtered_item_id,List<String> filtered_collect_time){
        this.titleList = filtered_item_title;
        this.typeList = filtered_item_type;
        this.idList = filtered_item_id;
        this.timeList = filtered_collect_time;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.collection_list_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.collectionTitle.setText(titleList.get(position));
        holder.collectionType.setText(typeList.get(position));
        holder.collectionTime.setText(timeList.get(position));
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(titleList);
            }else{
                for(String title:titleList){
                    if(title.toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(title);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemSearched.clear();
            itemSearched.addAll((Collection<? extends String>) results.values);
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView collectionTitle,collectionType,collectionTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            collectionTitle = itemView.findViewById(R.id.collection_title);
            collectionType = itemView.findViewById(R.id.collection_type);
            collectionTime = itemView.findViewById(R.id.collection_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(),titleList.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();

            if(typeList.get(getAbsoluteAdapterPosition()).equals("discussion")){
                    Navigation.findNavController(v).navigate(R.id.action_collection_to_discussion);
                    SharedPreference.setCurrentDiscussion(idList.get(getAbsoluteAdapterPosition()));
                }else if(typeList.get(getAbsoluteAdapterPosition()).equals("resource")){
                    Navigation.findNavController(v).navigate(R.id.action_collection_to_resource);
                    SharedPreference.setCurrentResource(idList.get(getAbsoluteAdapterPosition()));
                }
        }
    }
}
