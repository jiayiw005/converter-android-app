package com.example.cvt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.ViewHolder> {

    private static final String TAG = "RecyclerAdapter2";

    List<String> titleList = new ArrayList<>();
    List<String> typeList = new ArrayList<>();
    List<String> timeList = new ArrayList<>();
    List<Integer> idList = new ArrayList<>();

    public RecyclerAdapter2(List<String> titleList, List<String> typeList, List<String> timeList, List<Integer> idList) {
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
    public RecyclerAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.contribution_list_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter2.ViewHolder holder, int position) {
        holder.contributionTitle.setText(titleList.get(position));
        holder.contributionType.setText(typeList.get(position));
        holder.contributionTime.setText(timeList.get(position));
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView contributionTitle,contributionType,contributionTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contributionTitle = itemView.findViewById(R.id.contribution_title);
            contributionType = itemView.findViewById(R.id.contribution_type);
            contributionTime = itemView.findViewById(R.id.contribution_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(),titleList.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();

            if(typeList.get(getAbsoluteAdapterPosition()).equals("discussion")){
                Navigation.findNavController(v).navigate(R.id.action_contribution_to_discussion);
                SharedPreference.setCurrentDiscussion(idList.get(getAbsoluteAdapterPosition()));
            }else if(typeList.get(getAbsoluteAdapterPosition()).equals("resource")){
                Navigation.findNavController(v).navigate(R.id.action_contribution_to_resource);
                SharedPreference.setCurrentResource(idList.get(getAbsoluteAdapterPosition()));
            }
        }
    }
}
