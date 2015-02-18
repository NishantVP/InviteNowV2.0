package com.coen268.invitenow.nishant.invitenowv20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Nishant on 2/2/2015.
 */
public class groupListAdapter extends BaseAdapter {

    public LayoutInflater inflater;

    public Context context;
    public String[] groupNames;
    public int[] groupImages;

    public TextView groupName;
    public ImageView groupImage;


    public groupListAdapter(Context context, String[] groupNames, int[] groupImages) {
        this.context = context;
        this.groupNames = groupNames;
        this.groupImages = groupImages;
    }

    @Override
    public int getCount() {
        return groupNames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listItemView = inflater.inflate(R.layout.group_list_view_item, parent, false);

        groupImage = (ImageView) listItemView.findViewById(R.id.groupListViewItemImage);
        groupName = (TextView) listItemView.findViewById(R.id.groupListViewItemName);
        //contactCheckox = (CheckBox) listItemView.findViewById(R.id.contactListViewItemCheckBox);
        groupImage.setImageResource(groupImages[position]);
        groupName.setText(groupNames[position]);
        //contactCheckox.toggle();

        return listItemView;

    }

}
