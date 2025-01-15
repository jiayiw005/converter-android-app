package com.example.cvt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CommentAdapter extends BaseAdapter {

    Context context;
    String[] comment;
    String[] comment_time;
    String[] comment_publisher;
    LayoutInflater inflater;

    public CommentAdapter(Context ctx,String[] comment,String[] comment_time,String[] comment_publisher){
        this.context = ctx;
        this.comment = comment;
        this.comment_time = comment_time;
        this.comment_publisher = comment_publisher;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return comment.length;
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
        convertView = inflater.inflate(R.layout.activity_comment_list_row,null);
        TextView Comment = (TextView) convertView.findViewById(R.id.comment);
        Comment.setText(comment[position]);
        TextView CommentTime = (TextView) convertView.findViewById(R.id.comment_time);
        CommentTime.setText(comment_time[position]);
        TextView CommentPublisher = (TextView) convertView.findViewById(R.id.comment_publisher);
        CommentPublisher.setText(comment_publisher[position]);
        return convertView;
    }
}
