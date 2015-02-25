package com.coen268.invitenow.nishant.invitenowv20;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;


public class SelectFromContactsActivity extends ActionBarActivity {

    List<String> name = new ArrayList<String>();
    List<String> phno = new ArrayList<String>();
    MyAdapter myAdapter;
    Button select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_from_contacts);

        getAllContacts(this.getContentResolver());
        ListView list = (ListView)findViewById(R.id.list);
        myAdapter = new MyAdapter();
        list.setAdapter(myAdapter);
        //       list.setOnItemClickListener(this);
        list.setItemsCanFocus(false);
        list.setTextFilterEnabled(true);

        select = (Button)findViewById(R.id.button1);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder checkedcontacts = new StringBuilder();
     /*           for (int i = 0; i < name.size(); i++) {

                    if (myAdapter.myCheckStates.get(i) == true) {
                        checkedcontacts.append(name.get(i).toString());
                        checkedcontacts.append("\n");
                    } else {
                        System.out.println("Not Checked......" + name.get(i).toString());
                    }
                } */


                for(int i = 0; i < phno.size(); i++)
                {
                    if (myAdapter.myCheckStates.get(i) == true) {
                        checkedcontacts.append(phno.get(i).toString());
                        checkedcontacts.append("\n");
                    } else {
                        System.out.println("Not Checked......" + phno.get(i).toString());

                    }
                }

            //    Toast.makeText(SelectFromContactsActivity.this, checkedcontacts, Toast.LENGTH_SHORT).show();

                String dfq = checkedcontacts.toString();

                Toast.makeText(SelectFromContactsActivity.this, dfq, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SelectFromContactsActivity.this,SendInvitesActivity.class);
                intent.putExtra("abc", checkedcontacts.toString());
                startActivity(intent);

            }
        });

    }

    //    @Override
    public void onItemClick(AdapterView<?> arg0,View arg1, int arg2, long arg3){
        myAdapter.toggle(arg2);
    }


    private void getAllContacts(ContentResolver contentResolver) {


        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        while(cursor.moveToNext()){
            String name1 = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phn = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Toast.makeText(SelectFromContactsActivity.this,"Phone number:"+phn,Toast.LENGTH_SHORT);
            if(!phno.contains(phn)){

                name.add(name1);
                phno.add(phn);
            }

        }
        cursor.close();
    }

    class MyAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
        private SparseBooleanArray myCheckStates;
        LayoutInflater mInflater;
        TextView tv1,tv;
        CheckBox cb;
        MyAdapter()
        {
            myCheckStates = new SparseBooleanArray(name.size());
            mInflater = (LayoutInflater)SelectFromContactsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return name.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub

            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View vi=convertView;
            if(convertView==null)
                vi = mInflater.inflate(R.layout.row, null);
            TextView tv= (TextView) vi.findViewById(R.id.textView1);
            tv1= (TextView) vi.findViewById(R.id.textView2);
            cb = (CheckBox) vi.findViewById(R.id.checkBox1);
            tv.setText(""+ name.get(position));
            tv1.setText(""+ phno.get(position));
            cb.setTag(position);
            cb.setChecked(myCheckStates.get(position, false));
            cb.setOnCheckedChangeListener(this);

            return vi;
        }
        public boolean isChecked(int position) {
            return myCheckStates.get(position, false);
        }

        public void setChecked(int position, boolean isChecked) {
            myCheckStates.put(position, isChecked);
            System.out.println("hello...........");
            notifyDataSetChanged();
        }

        public void toggle(int position) {
            setChecked(position, !isChecked(position));
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub

            myCheckStates.put((Integer) buttonView.getTag(), isChecked);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_from_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(SelectFromContactsActivity.this, SelectFromGroupsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
