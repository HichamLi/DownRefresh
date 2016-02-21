package com.itql.downrefresh.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.itql.downrefresh.R;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private View headerView;
    private View footerView;

    private ImageView iv_rotate;
    private ImageView iv_refresh;

    private String tvString[];
    private MyAdapter myAdapter;

    private RotateAnimation ra_iv_rotate;
    private RotateAnimation ra_iv_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        lv= (ListView) findViewById(R.id.lv);
        headerView=View.inflate(MainActivity.this,R.layout.view_list_head,null);
        footerView=View.inflate(MainActivity.this,R.layout.view_list_foot,null);

        lv.addHeaderView(headerView);
        lv.addFooterView(footerView);

        tvString=new String[30];
        for(int i=0;i<tvString.length;i++){
            tvString[i]="第 "+ i + "个";
        }
        myAdapter = new MyAdapter();
        lv.setAdapter(myAdapter);

        iv_rotate= (ImageView) findViewById(R.id.iv_rotate);
        iv_refresh= (ImageView) findViewById(R.id.iv_refresh);
    }

    private void initAnimation(){
        ra_iv_rotate = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        ra_iv_refresh = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);

        ra_iv_rotate.setDuration(1);

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
