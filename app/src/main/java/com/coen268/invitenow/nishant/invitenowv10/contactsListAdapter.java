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
public class contactsListAdapter extends BaseAdapter{

    public LayoutInflater inflater;

    public Context context;
    public String[] contactNames;
    public int[] contactImages;

    public TextView contactName;
    public ImageView contactImage;
    public CheckBox contactCheckox;

    public contactsListAdapter(Context context, String[] contactNames, int[] contactImages) {
        this.context = context;
        this.contactNames = contactNames;
        this.contactImages = contactImages;
    }

    @Override
    public int getCount() {
        return contactNames.length;
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

        View listItemView = inflater.inflate(R.layout.contact_list_view_item, parent, false);

        contactImage = (ImageView) listItemView.findViewById(R.id.contactListViewItemImage);
        contactName = (TextView) listItemView.findViewById(R.id.contactListViewItemName);
        //contactCheckox = (CheckBox) listItemView.findViewById(R.id.contactListViewItemCheckBox);
        contactImage.setImageResource(contactImages[position]);
        contactName.setText(contactNames[position]);
        //contactCheckox.toggle();

        return listItemView;

    }
}
