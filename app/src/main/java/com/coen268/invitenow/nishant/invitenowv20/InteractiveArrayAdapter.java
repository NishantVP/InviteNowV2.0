package com.coen268.invitenow.nishant.invitenowv20;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DELL on 20-02-2015.
 */
public class InteractiveArrayAdapter  extends ArrayAdapter<Model>{

    public static List<Model> list = null;
    private final Activity context;

    public InteractiveArrayAdapter(Activity context, List<Model> list){
        super(context,R.layout.rowbuttonlayout,list);
        this.context = context;
        this.list = list;
    }

    static class Viewholder{
        protected TextView text;
        protected CheckBox checkbox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if(convertView == null){
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.rowbuttonlayout,null);
            final Viewholder viewholder = new Viewholder();
            viewholder.text = (TextView)view.findViewById(R.id.label);
            viewholder.text.setText("ABC");

            viewholder.checkbox = (CheckBox)view.findViewById(R.id.check);
            viewholder.checkbox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            Model element = (Model)viewholder.checkbox.getTag();
                            element.setSelected(buttonView.isChecked());
                        }
                    });

            view.setTag(viewholder);
            viewholder.checkbox.setTag(list.get(position));

        } else {

            view = convertView;
            ((Viewholder) view.getTag()).checkbox.setTag(list.get(position));
        }
        Viewholder holder = (Viewholder) view.getTag();
        holder.text.setText(list.get(position).getName());

        holder.checkbox.setChecked(list.get(position).isSelected());


        return view;



    }
}
