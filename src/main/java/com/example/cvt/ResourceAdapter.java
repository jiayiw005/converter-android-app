package com.example.cvt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ResourceAdapter extends BaseAdapter {

    Context context;
    String[] resource;
    String[] resource_time;
    String[] resource_publisher;
    int[] collection_num;
    LayoutInflater inflater;

    public ResourceAdapter(Context ctx,String[] resource,String[] resource_time,String[] resource_publisher,int[] collection_num){
        this.context = ctx;
        this.resource = resource;
        this.resource_time = resource_time;
        this.resource_publisher = resource_publisher;
        this.collection_num = collection_num;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return resource.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.activity_resource_list_row,null);
        TextView Resource = (TextView) convertView.findViewById(R.id.resource);
        Resource.setText(resource[position]);
        TextView ResourceTime = (TextView) convertView.findViewById(R.id.resource_time);
        ResourceTime.setText(resource_time[position]);
        TextView ResourcePublisher = (TextView) convertView.findViewById(R.id.resource_publisher);
        ResourcePublisher.setText(resource_publisher[position]);
        TextView CollectNum = (TextView) convertView.findViewById(R.id.collect_num);
        CollectNum.setText("("+String.valueOf(collection_num[position])+")");
        return convertView;
    }

}

