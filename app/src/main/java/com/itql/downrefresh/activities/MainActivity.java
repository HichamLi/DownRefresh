package com.itql.downrefresh.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itql.downrefresh.R;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private View headerView;

    private String tvString[];
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        lv= (ListView) findViewById(R.id.lv);
        headerView=View.inflate(MainActivity.this,R.layout.view_list_head,null);

        lv.addHeaderView(headerView,null,true);

        tvString=new String[30];
        for(int i=0;i<tvString.length;i++){
            tvString[i]="第 "+ i + "个";
        }
        myAdapter = new MyAdapter();
        lv.setAdapter(myAdapter);
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return tvString.length;
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
            TextView textView=new TextView(MainActivity.this);
            textView.setText(tvString[position]);
            textView.setTextSize(20);
            return textView;
        }
    }

}
