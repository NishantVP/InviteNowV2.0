package com.coen268.invitenow.nishant.invitenowv20;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomListAdapter extends ArrayAdapter<String>{
 private final Activity context;
 private final String[] newlist;
 private final Integer[] imageID;


    public CustomListAdapter(Activity context, String[] newlist, Integer[] imageID) {
        super(context,R.layout.dialogsingle,newlist );

        this.context =context;
        this.newlist = newlist;
        this.imageID = imageID;
    }

    public View getView(int position,View view ,ViewGroup parent){

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView =  inflater.inflate(R.layout.dialogsingle,null,true);
        TextView txtTitle = (TextView)rowView.findViewById(R.id.listname);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.imageView);
        txtTitle.setText(newlist[position]);
        imageView.setImageResource(imageID[position]);
        return  rowView;
    }
}
