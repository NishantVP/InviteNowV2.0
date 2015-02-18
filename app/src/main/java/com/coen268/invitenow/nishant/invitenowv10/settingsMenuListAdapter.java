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
public class settingsMenuListAdapter extends BaseAdapter {

    public LayoutInflater inflater;

    public Context context;
    public String[] Menus;


    public TextView Menu;

    public settingsMenuListAdapter(Context context, String[] Menus) {
        this.context = context;
        this.Menus = Menus;

    }

    @Override
    public int getCount() {
        return Menus.length;
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

        View listItemView = inflater.inflate(R.layout.menu_list_view_item, parent, false);


        Menu = (TextView) listItemView.findViewById(R.id.menuListViewItemName);
        //contactCheckox = (CheckBox) listItemView.findViewById(R.id.contactListViewItemCheckBox);

        Menu.setText(Menus[position]);
        //contactCheckox.toggle();

        return listItemView;

    }

}
